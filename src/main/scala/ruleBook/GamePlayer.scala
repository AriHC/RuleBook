package ruleBook
import scala.collection.mutable.Set

class NoPlayerPieceException(playerNumber:Int) extends NoPiecesException("Player " + playerNumber)
 
class GamePlayer(override val name: String) extends GamePieceHolder {
  override def toString = name
}

object GamePlayer {
  def apply(name:String) = new GamePlayer(name)
}