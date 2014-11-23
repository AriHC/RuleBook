package object ruleBook {  
   import scala.language.implicitConversions
   import scala.reflect.runtime.universe._   
   
   implicit class BoardDimension(val m:Int) {
    def x(n:Int) = SpaceList(Seq.fill(m,n){None})
   }
   
   class IllegalMoveException extends Exception
     
   def Players (numPlayers:Int) = {
    Game.players = Seq.fill(numPlayers){GamePlayer()};
    this
   }
   def Player(n:Int) = {
     Game.players(n - 1)
   }
   def Board (spaces:SpaceList) = {
	Game.board = GameBoard(spaces)
	this
   }
   
   var current_move : GameMove = GameMove.empty 
   def Move (actions: =>Unit) = {
     Game.move = ()=> {
       // reset the current move
       current_move = GameMove.empty
       // do whatever the developer defined as a move
       actions
     }
   }
   def Legal_if (action: =>Boolean) = {
     if (!action) {
       throw new IllegalMoveException
     }
   }
   def Choose = {
     new ChooseCommands
   }
   class ChooseCommands {
     def space : Unit = {
       current_move.spaces += Coordinate(1,1))
     }
   }
   def chosen = current_move
   def Set_up (actions:Unit) = {}
   def Definitions = {}
   def Current_Player = Game.players(Game.current_player)
}