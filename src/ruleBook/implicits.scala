package object ruleBook {  
  import scala.language.implicitConversions
  import scala.reflect.runtime.universe._   
  import scala.collection.mutable.Set
  
  implicit class BoardDimension(val m:Int) {
    def x(n:Int) = SpaceList(Seq.tabulate(m,n){(row,col) => GameSpace("(" + row.toString + "," + col.toString + ")")})
  }
  
 // case class InputIndexFrom1(val playerIndex:Int) {
 //   override def toString = playerIndex.toString
 // }
  // These will help convert from player indecies to game indecies, though not vice-versa.
//  implicit def inputIndexFrom1(i:Int) = InputIndexFrom1(i)
//  implicit def indexFromIndexFrom1(index:InputIndexFrom1) = index.playerIndex - 1
   
  implicit class PieceGenerator(val num:Int) {
    def of(piece:GamePiece) : Set[GamePiece] = {
      val newPieces = Set.empty[GamePiece]
      var i = 0
      for (i <- 1 to num) {
        newPieces.add(piece.copy)
      }
      newPieces
    }
  }
  
  implicit def stringFromInt(n:Int) = n.toString
  
  implicit class isComparable[A](val left:A) {
    def is(right:A) = left == right
    def is_not(right:A) = left != right
  }
  implicit class logicComparable(val left:Boolean) {
    def and(right : =>Boolean) = left && right
    def or(right : =>Boolean) = left || right
  }
  def empty = new Empty
  class Empty
  implicit class pieceHolderEmptyComparable(val left:GamePieceHolder) {
    def is(unused:Empty) = left.isEmpty
    def is_not(unused:Empty) = !left.isEmpty
  }
  def in_bounds = new InBounds
  class InBounds
  implicit class coordinateEmptyBoundsComparable(val left:Coordinate) {
    def is(unused:Empty) = left.isEmpty
    def is_not(unused:Empty) = !this.is(unused)
    def is(unused:InBounds) = { 
      try {
        board at left
        true
      } catch {
        case e:OutOfBoundsException => {
          false
        }
      }
    }
    def is_not(unused:InBounds) = {
      !this.is(unused)
    }
  }
  
  class IllegalMoveException(val move:String) extends Exception
  class NoPiecesException(val loc:String) extends Exception
  class MoveFormatException(val move:String, val info:String = "no additional info") extends Exception
     
  def Players (numPlayers:Int) = {
    Game.players = Seq.tabulate(numPlayers){ i:Int => GamePlayer("Player " + (i + 1).toString) }
  }
  def Player = {
    new PlayerAction
  }
  class PlayerAction {
    def number(n:Int) = Game.players(n - 1) // n indexes from 1
  }
  def Board (spaces:SpaceList) = {
    Game.board = GameBoard(spaces)
    this
  }
  def board = Game.board
  implicit def coordToSpace(coord:Coordinate) : GameSpace = board at coord
   
  var current_move : GameMove = GameMove.empty 
  def chosen = current_move
  def Move (actions: =>Unit) = {
    Game.move = ()=> {
      // reset the current move
      current_move = GameMove.empty
      // do whatever the developer defined as a move
      actions
    }
  }
  def Win_Condition (actions: =>Boolean) = {
    Game.win_check = ()=> {
      actions
    }
  }
  def Tie_Condition (actions: =>Boolean) = {
    Game.tie_check = ()=> {
      actions
    }
  } 
  def Lose_Condition (actions: =>Boolean) = {
    Game.loss_check = ()=> {
      actions
    }
  }
  def Legal = { new LegalChecker }
  class LegalChecker {
    def provided (action: =>Boolean) = {
      if (!action) {
        throw new IllegalMoveException(current_move.toString)
      }
    }
  }
  def Set_up (actions:Unit) = {}
  def Definitions = {}
  def current_player = Game.players(Game.current_player)
  def current_player_number = Game.current_player + 1 // Player index
  
  
  /* Choose Command */
  def Choose = {
    new MoveChoice
  }
  class MoveChoice {
    def readCoordinates(invalid : (Int,Int)=>Boolean, num:Int, description:String) = {
      println("Choose " + description + " " + num + " space-separated coordinates")
      val input = readLine()
      val tokens = input.split(" ")
      val result = tokens map {Coordinate(_)}
      if (invalid(result.size, num)) {
        throw new MoveFormatException(input, "expected " + description + " " + num + 
        							  " space-separated coordinates")        
      }
      current_move.spaces ++= result
      new PartialChoose
    }
    def exactly(num:Int) = { 
      readCoordinates({_ != _}, num, "exactly")
    }
    def up_to(num:Int) = {
      readCoordinates({_ > _}, num, "up to")
    }
    def at_least(num:Int) = {
      readCoordinates({_ < _}, num, "at least") 
    }
    class PartialChoose {
      def board(end:ChooseEnding) = {}
    } 
  }
  class ChooseEnding
  def space = { new ChooseEnding }
  def spaces = { new ChooseEnding }
  def number_of = new Count
  class Count {
    def spaces(move:GameMove) = { move.spaces.size }
  }
  /* End Choose Command */
  
  /* Take Command */
  var taken_pieces = Set.empty[GamePiece]
  def from(loc:GamePieceHolder) = {
    loc
  }
  def Take = {
    // Empty the last take first
    taken_pieces = Set.empty[GamePiece]
    new TakeChoice
  }
  class TakeChoice {
    def pieces(num:Int) = {
      new TakeContinuation(num)
    }
    class TakeContinuation(val num:Int) {
      def from(loc:GamePieceHolder) = {
        taken_pieces ++= loc.take(num)
      }
    }
  }
  /* End Take Command */
}