/*
 * Trait for any class that can hold pieces (players, spaces, etc.)
 * Author: Ari Hausman-Cohen
 * For HMC CS111 Fall 2014
 */
package ruleBook
import scala.collection.mutable.Set

class OutOfPiecesException(val name:String) extends Exception
trait GamePieceHolder {
    val name : String
	val pieces : Set[GamePiece] = Set.empty[GamePiece]
    def gets(newPieces:Set[GamePiece]) = {
      pieces ++= newPieces
    }
	def isEmpty = pieces.isEmpty
	def exists = true // Just so we can call to trigger any out of bounds errors.
	def foreach(u : GamePiece=>Any) = pieces.foreach(u(_))
	def piecesString = {
	  var result = "{"
	  pieces foreach { piece =>
	    result += piece.display + " "
	  }
	  result.trim + "}"
	}
	def take(num:Int) = {
	  // Try to grab the required number of pieces
	  if (pieces.size < num) {
	    throw new OutOfPiecesException(this.name)
	  }
	  // If there are enough, great
	  val result = pieces.take(num)
	  pieces --= result
	  result 
	}
}