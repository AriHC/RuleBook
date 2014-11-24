package ruleBook

object Game {
  var players : Seq[GamePlayer] = Seq.empty[GamePlayer]
  var board : GameBoard = GameBoard.empty
  var current_player : Int = 0
  var move : ()=>Unit = {() => {}}
  var win_check : ()=>Boolean = {() => false}
  var tie_check : ()=>Boolean = {() => false}
  var loss_check : ()=>Boolean = {() => false}
  var current_outcome : Option[GameOutcome] = None
  def Play = {
    val numPlayers = players.size
    while(current_outcome == None) {
      var moved = false
      while(!moved) {
        board.display
        println(players(current_player).toString + "'s turn.")
        try {
          move()
          moved = true
        } catch {
          // NOTE: as currently written, if the move breaks after already doing some things,
          // they won't be reversed. So coders must be sure to not make any changes until they're
          // sure the move is legal.
	      case e:IllegalMoveException => {
	    	println("Sorry, " + e.move + " is not a legal move. Try another.")
	      }
	      case e:OutOfBoundsException => {
	        println(e.coord + " is out of bounds.")
	      }
	      case e:CoordinateFormatException => {
	        println(e.coord + " is not a valid coordinate. Valid format is " + e.requiredFormat)
	      }
	      case e:MoveFormatException => {
	        println(e.move + " is not a valid move (" + e.info + ").")
	      }
	      case e:NoPiecesException => {
	        println(e.loc + " has no pieces.")
	      } 
        }
      }
      current_player = (current_player + 1) % numPlayers
    }
    current_outcome match {
      case Some(Win(player)) => player.name + " wins!"
      case Some(Loss(player)) => player.name + " loses!"
      case Some(_) => "Stalemate!"
    }
    // TODO: Do something with current outcome
    // TODO: Something different with losses vs. wins? Like losses eliminate players in 
    // multiplayer games maybe.
  }
}