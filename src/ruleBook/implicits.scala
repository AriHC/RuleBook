package object ruleBook {  
   import scala.language.implicitConversions
      
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
   def Move (actions: =>Unit) = {
     Game.move = ()=>actions
   }
   def Legal_if (action: =>Boolean) = {
     if (!action) {
       throw new IllegalMoveException
     }
   }
   def Set_up (actions:Unit) = {}
   def Definitions = {}
   def Current_Player = Game.players(Game.current_player)
}