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
      Player number 1 gets (5 of new xPiece)
      Player number 2 gets (5 of new oPiece)
    }
    
    Move {
      // These rules describe tic-tac-toe where instead of placing
      // a piece you can move someone else's instead. Just because.
      Choose up_to 2 board spaces
      if ((number_of spaces chosen) is 1) {
        Legal provided { (chosen space 1) is empty }
        Take pieces (1) from current_player
        board at (chosen space 1) gets taken_pieces
      }
      else if ((number_of spaces chosen) is 2) {
        Legal provided { (board at (chosen space 1) is_not empty) and
          (board at (chosen space 2) is empty) }
        Take pieces (1) from (board at (chosen space 1))
        board at (chosen space 2) gets taken_pieces
      }
    }
    /* TODO
    Win_Condition {
    }
    
    Tie_Condition {
    }
    */    
    Definitions
	  class tic_tac_toe_piece(display:Char, val owner:InputIndexFrom1) extends GamePiece(display) {
        def owned_by(person:InputIndexFrom1) = person is owner
      }
      case class xPiece() extends tic_tac_toe_piece('x',1)
      case class oPiece() extends tic_tac_toe_piece('o',2)
  } 
}