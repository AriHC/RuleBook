package ruleBook

class Game (val name:String) {
  var players : Seq[Int] = Seq.empty[Int]
  var board : GameBoard = GameBoard.empty
  def Players (numPlayers:Int) = {
    players = List.range(1, numPlayers + 1);
    this
  }
  def Board (spaces:SpaceList) = {
	board = GameBoard(spaces)
	this
  }
}

object Game {
	def apply(name:String) = {
	  new Game(name)
	} 
}