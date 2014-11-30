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
        place_piece
      }
      else if ((number_of spaces chosen) is 2) {
        move_piece
      }
    }

    Win_Condition {
      three_in_a_row
    }
    
    Tie_Condition {
      all_spaces_full
    }
        
    Definitions
      /* A player should never need to read this section */
      def place_piece = {
        Legal provided { (chosen space 1) is empty }
        Take pieces (1) from current_player
        board at (chosen space 1) gets taken_pieces
      }
      def move_piece = {
        Legal provided { (board at (chosen space 1) is_not empty) and
          (board at (chosen space 2) is empty) }
        Take pieces (1) from (board at (chosen space 1))
        board at (chosen space 2) gets taken_pieces
      }
      def all_spaces_full = {
        var full_so_far = true
        board foreach_coordinate { coord =>
          full_so_far = full_so_far && (board at coord is_not empty) 
        }
        full_so_far
      }
      def three_in_a_row = {
        x_in_a_row(3)
      }
      def x_in_a_row(x:Int) = {
        var found = false
        board foreach_coordinate { coord =>
          found = found || x_nw_se_diagonal(x, coord) ||
          	x_ne_sw_diagonal(x, coord) || x_vertical(x, coord) ||
          	x_horizontal(x, coord)
        }
        found
      }
      def x_nw_se_diagonal(x:Int, coord:Coordinate) : Boolean = {
        try {
          x == 0 or (coord is in_bounds and
          controlled_by_current_player(board at coord) and
          (x_nw_se_diagonal(x-1, Coordinate north_west_of coord)))
        } catch {
          case e:OutOfBoundsException => false 
        }
      }
      def x_ne_sw_diagonal(x:Int, coord:Coordinate) : Boolean = {
        try {
          x == 0 or (coord is in_bounds and
          controlled_by_current_player(board at coord) and
          (x_ne_sw_diagonal(x-1, Coordinate north_east_of coord)))
        } catch {
          case e:OutOfBoundsException => false
        }
      }
      def x_vertical(x:Int, coord:Coordinate) : Boolean = {
        try {
          x == 0 or (coord is in_bounds and
          controlled_by_current_player(board at coord) and
          (x_vertical(x-1, Coordinate north_of coord)))
        } catch {
          case e:OutOfBoundsException => false
        }
      }
      def x_horizontal(x:Int, coord:Coordinate) : Boolean = {
        try {
          x == 0 or (coord is in_bounds and
          controlled_by_current_player(board at coord) and
          (x_horizontal(x-1, Coordinate east_of coord)))
        } catch {
          case e:OutOfBoundsException => false
        }
      }
      def controlled_by_current_player(space:GameSpace) : Boolean = {
        if (space is empty) {
          false 
        } else {
          space.pieces.toList(0) match {
            case a:tic_tac_toe_piece => a owned_by current_player_number
            case _ => false
          }
        }
      }
	  class tic_tac_toe_piece(display:Char, val owner:Int) extends GamePiece(display) {
        def owned_by(person:Int) = person is owner
        override def copy = new tic_tac_toe_piece(display, owner)
      }
      case class xPiece() extends tic_tac_toe_piece('x',1)
      case class oPiece() extends tic_tac_toe_piece('o',2)
  } 
}