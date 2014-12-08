package ruleBook

import org.scalatest._

class RuleBookTests extends FunSpec {
  describe("Players"){
    it("should be creatable") {
      Players { 5 }
      assert(Game.players.size === 5)
    }
    it("should have default names") {
    	Players { 2 }
    	assert(Game.players(0).name === "Player 1")
    	assert(Game.players(1).name === "Player 2")
    }
    it("should be accessible by number") {
    	Players { 2 }
    	assert((Player number 1) === Game.players(0))
    	assert((Player number 2) === Game.players(1))
    }
    it("current player should be accessible as \"current_player\"" + 
    	" and their number accessible as \"current_player_number\"") {
    	Players { 2 }
    	assert(current_player === Game.players(0))
    	assert(current_player !== Game.players(1))
    	assert(current_player_number === 1)
    }
  }
  describe("Pieces") {
  	it("should be generatable (via copying - \"[n] of [copyablePiece]\"") {
  		val basePiece = new GamePiece('s')
  		val pieces = 2 of basePiece
  		assert(pieces.size === 2)
  		pieces foreach { piece =>
  			assert(piece.display == basePiece.display)
			assert(piece != basePiece)
  		}
  	}
  }
  describe("Piece holders") {
  	class GenericHolder(override val name: String) extends GamePieceHolder
  	it("should start empty") {
  		val p = new GenericHolder("p")
  		assert(p.pieces.size === 0)
  	}
  	it("should be able to receive pieces via \"gets\" (without overriding)") {
  		val p = new GenericHolder("p")
  		p gets (2 of new GamePiece('x'))
  		assert(p.pieces.size === 2)
  		p.pieces foreach { piece =>
  			assert(piece.display === 'x')
  		}
  		p gets (3 of new GamePiece('y'))
  		assert((p.pieces filter (_.display == 'y')).size == 3)
  		assert((p.pieces filter (_.display == 'x')).size == 2)
  	}
  	it("should be able to \"take\" pieces," +
  	   " and throw an exception if you try to take" +
  	   " pieces away that it doesn't have") {
  		val p = new GenericHolder("p")
  		p gets (3 of new GamePiece('t'))
  		Take pieces (2) from p
  		assert(p.pieces.size === 1)
  		assert(taken_pieces.size === 2)
  		taken_pieces foreach { piece =>
  		  assert(piece.display === 't')
  		}
  		var excepted = false
  		try {
    		Take pieces (2) from p
    	} catch {
    		case e : OutOfPiecesException => excepted = true
    	}
    	assert(excepted)
  	}
  }
  describe("Board") {
  	it("should be accessible via \"board\"") {
  		assert(board === Game.board)
  	}
  	it("should be creatable from m x n notation") {
  		Board { 2 x 3 }
  		assert(board.spaceList.spaces.size === 2)
  		board.spaceList.spaces foreach { row =>
  			assert(row.size === 3)
  		}
  	}
  	it("should be empty when generated") {
  		Board { 2 x 3 }
  		board.spaceList.spaces foreach { row =>
  			row foreach { space =>
  				assert(space.pieces === Set.empty[GamePiece])
  			}
  		}
  	}
  	it("can display (check manually at top of test output)") {
  		Board { 2 x 3 }
  		board.spaceList.spaces(0)(0) gets scala.collection.mutable.Set(new GamePiece('a'))
  		board.spaceList.spaces(1)(2) gets scala.collection.mutable.Set(new GamePiece('b'), new GamePiece('c'))
  		println("\nA 2 row, 3 column board should display below," +
  			    " with an 'a' in the top left space," +
  			    " 'b' and 'c' together in the bottom right," +
  			    " and the rest empty.\n")
  		board.display
  		println("")
  		pending
  	}
  }
  describe ("Coordinates") {
  	it("should be creatable") {
  		val c = Coordinate(3,5)
  		assert(c.row === 3)
  		assert(c.col === 5)
  	}
  	it("can be parsed from a string") {
  		val c = Coordinate("(4,6)") // Users index from 1
  		assert(c.row === 3)
  		assert(c.col === 5)
  		val c2 = Coordinate("(1,1)") // Users index from 1
  		assert(c2.row === 0)
  		assert(c2.col === 0)
  		var excepted = false
  		try {
  			val c3 = Coordinate("(a,b)")
  		} catch {
  			case e : CoordinateFormatException => excepted = true
  		}
  		assert(excepted)
  	}
  	it("should be able to tell you the 8 spaces relative to a coordinate") {
  		assert((Coordinate north_west_of Coordinate(1,1)) === Coordinate(0,0))
  		assert((Coordinate north_of Coordinate(1,1)) === Coordinate(0,1))
  		assert((Coordinate north_east_of Coordinate(1,1)) === Coordinate(0,2))
  		assert((Coordinate east_of Coordinate(1,1)) === Coordinate(1,2))
  		assert((Coordinate south_east_of Coordinate(1,1)) === Coordinate(2,2))
  		assert((Coordinate south_of Coordinate(1,1)) === Coordinate(2,1))
  		assert((Coordinate south_west_of Coordinate(1,1)) === Coordinate(2,0))
  		assert((Coordinate west_of Coordinate(1,1)) === Coordinate(1,0))
  	}
  }
  describe("Board + Coordinates") {
  	it("should be used to access board spaces using \"at\"") {
  		Board { 2 x 3 }
  		board at Coordinate(1,2) gets (3 of new GamePiece('x'))
  		assert(board.spaceList.spaces(1)(2).pieces.size === 3)	
  	}
  	it("should implicitly convert to a space") {
  		Board{3 x 3}
  		Coordinate(1,2) gets (3 of new GamePiece('x'))
  		assert(board.spaceList.spaces(1)(2).pieces.size === 3)	
  		Coordinate(1,2) gets (2 of new GamePiece('y'))
  		assert(board.spaceList.spaces(1)(2).pieces.size === 5)
  	}
  	it("can check coordinates as in/out of bounds using \"is\"/\"is_not\"") {
  		Board { 2 x 3 }
  		assert(Coordinate(2,3) is_not in_bounds)
  		assert(Coordinate(1,2) is in_bounds)
  		assert(Coordinate(-1,0) is_not in_bounds)
  	}
  	it("can check coordinates/spaces for emptyness") {
  		Board{ 1 x 5 }
  		assert(board at Coordinate(0,3) is empty)
  		Coordinate(0,3) gets (2 of new GamePiece('z'))
  		assert(Coordinate(0,3) is_not empty)
  	}
  }
  describe("Moves") {
  	it("can be checked for legality (and accessed via \"chosen\")") {
  		var exceptedMove = ""
  		var move1 = GameMove.empty;
  		var move2 = GameMove.empty;
  		var move3 = GameMove.empty;
  		def legalityCheck = {
  			Legal provided ((chosen space 1) equals
  			  (Coordinate west_of (chosen space 2)))
  		}
  		var lastMove = 0
  		try {
  			move1 = GameMove(scala.collection.mutable.Buffer(
  				             Coordinate(0,1), Coordinate(0,2)))
  			move2 = GameMove(scala.collection.mutable.Buffer(
  				             Coordinate(3,2), Coordinate(4,2)))
  			move2 = GameMove(scala.collection.mutable.Buffer(
  				             Coordinate(1,2), Coordinate(4,2)))
  			current_move = move1
  			lastMove = 1  			
  			legalityCheck
  			current_move = move2
  			lastMove = 2
  			legalityCheck
  			current_move = move3
  			lastMove = 3
  			legalityCheck
  		} catch {
  			case e : IllegalMoveException => exceptedMove = e.move
  		}
  		assert(exceptedMove === move2.toString)
  		assert(lastMove === 2) // Move 3 shouldn't have happened
  	}
  }
}