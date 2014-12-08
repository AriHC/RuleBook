/*
 * This file offers a number of globally accessible keywords
 * and their implementations
 * 
 * Author: Ari Hausman-Cohen
 * For HMC CS111 Fall 2014
 */  
import scala.language.implicitConversions
import scala.collection.mutable.Set

package object ruleBook {
  
  // Divided by sections of the RuleBook/related topics. 
  // Unfortunately you can only have one package object per package,
  // and I don't want users to have to import multiple packages.
  
  /*******
   PLAYERS
   *******/
  def Players (numPlayers:Int) = {
    Game.players = Seq.tabulate(numPlayers){ i:Int => GamePlayer("Player " + (i + 1).toString) }
  }  
  
  // Get players by number ("Player number 1")
  def Player = {
    new PlayerAction
  }
  class PlayerAction {
    def number(n:Int) = Game.players(n - 1) // n indexes from 1
  }
  
  def current_player = Game.players(Game.current_player)
  def current_player_number = Game.current_player + 1 // Player index
  /***********
   END PLAYERS
   ***********/
  
  /*****
   BOARD
   *****/
  def Board (spaces:SpaceList) = {
    Game.board = GameBoard(spaces)
    this
  }
  
  // Implicit helper that lets you use "m x n" notation (e.g. 8 x 8)
  implicit class BoardDimension(val m:Int) {
    def x(n:Int) = {
      SpaceList(Seq.tabulate(m,n){(row,col) =>
        GameSpace("(" + row.toString + "," + col.toString + ")")
      })
    }
  }
  
  // Access the board
  def board = Game.board
  
  // Implicit helper that lets you access the board by coordinates
  implicit def coordToSpace(coord:Coordinate) : GameSpace = board at coord
  /*********
   END BOARD
   *********/
  
  /*****
   SETUP
   *****/
  def Set_up (actions:Unit) = {}
  
  // Lets you say [#] of [piece] to create duplicate pieces.
  // Piece must be copyable.
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
  /*********
   END SETUP
   *********/
  
  /****
   MOVE
   ****/
  def Move (actions: =>Unit) = {
    Game.move = ()=> {
      // reset the current move
      current_move = GameMove.empty
      // do whatever the developer defined as a move
      actions
    }
  }
  
  // Check if a move is legal
  def Legal = { new LegalChecker }
  class LegalChecker {
    def provided (action: =>Boolean) = {
      if (!action) {
        throw new IllegalMoveException(current_move.toString)
      }
    }
  }
  
  // Access the current move
  var current_move : GameMove = GameMove.empty 

  // Move-related exceptions
  class MoveFormatException(val move:String, val info:String = "no additional info") extends Exception
  class IllegalMoveException(val move:String) extends Exception
  
  // Choose Command - getting player input. Choose exactly/up_to/at_least x board spaces
  def Choose = {
    new GameMoveChoice
  }
  class GameMoveChoice {
    def readCoordinates(invalid : (Int,Int)=>Boolean, num:Int, description:String) = {
      println("Choose " + description + " " + num + " space-separated coordinates")
      val input = readLine() // Deprecated depending on version of Scala; but older versions don't seem to recognize scala.io.StdIn
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
  
  // Access the result of the choice
  def chosen = current_move
  
  // Get the number_of spaces chosen
  def number_of = new Count
  class Count {
    def spaces(move:GameMove) = { move.spaces.size }
  }
  
  // Take Command - Take pieces from somewhere
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
  
  // Access the taken pieces (presumably to give them to somewhere else)
  var taken_pieces = Set.empty[GamePiece]
  /********
   END MOVE
   ********/
  
  // Win, tie, lose conditions
  /***********
   GAME ENDING
   ***********/ 
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
  /***************
   END GAME ENDING
   ***************/
  
  // Definitions
  /***********
   DEFINITIONS
   ************/
  def Definitions = {}
  /***************
   END DEFINITIONS
   ***************/
  
  // Miscellaneous other helpers (primarily to help with natural language feel)
  /****
   MISC
   ****/
  // Implicitly convert Ints to strings when necessary
  implicit def stringFromInt(n:Int) = n.toString
   
  // Natural language helpers:
  // Compare by saying a is b instead of a == b
  implicit class isComparable[A](val left:A) {
    def is(right:A) = left == right
    def is_not(right:A) = left != right
  }  
  // Do logic via and and or
  implicit class logicComparable(val left:Boolean) {
    def and(right : =>Boolean) = left && right
    def or(right : =>Boolean) = left || right
  }
  // Ask if a piece holder is or is_not empty
  def empty = new Empty
  class Empty
  implicit class pieceHolderEmptyComparable(val left:GamePieceHolder) {
    def is(unused:Empty) = left.isEmpty
    def is_not(unused:Empty) = !this.is(unused)
  }  
  // Ask if a coordinate is/is_not empty/in_bounds
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
  /********
   END MISC
   ********/
}