package samples

import ruleBook._

object tic_tac_toe extends App {
  Rules {
    Players {
      2
    }
    
    Board {
      3 x 3
    }
    
    Set_up {
      Player(1).has(5)(xPiece)
      Player(2).has(5)(oPiece)
    }
    
    Move {
      println(Game.current_player)
      val line = readLine()
      Choose space
      println(chosen space 1)
      Legal_if {line == Game.current_player.toString()}
    }
    
    Definitions
	  class tic_tac_toe_piece(display:Char, val owner:Int) extends GamePiece(display)
	  case object xPiece extends tic_tac_toe_piece('x',0)
	  case object oPiece extends tic_tac_toe_piece('o',1)
  } 
}