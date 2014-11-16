package ruleBook

object Game {
  var players : Seq[GamePlayer] = Seq.empty[GamePlayer]
  var board : GameBoard = GameBoard.empty
  var current_player : Int = 0
  var move : ()=>Unit = {() => {}}
  def Play = {
    val numPlayers = players.size
    var i = 0
    while(i < 5) {
      var moved = false
      while(!moved) {
        board.display
        println("Player " + (current_player + 1) + "'s turn.")
        moved = true
        try move() catch {
	      case e:IllegalMoveException => {
	    	println("Sorry, that's not a legal move. Try another.")
	        moved = false
	      };
        }
      }
      current_player = (current_player + 1) % numPlayers
      i += 1
    }
  }
}