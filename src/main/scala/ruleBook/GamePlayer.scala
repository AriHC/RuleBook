/*
 * Base class for a player in the game.
 * Author: Ari Hausman-Cohen
 * For HMC CS111 Fall 2014
 */
package ruleBook
import scala.collection.mutable.Set
 
class GamePlayer(override val name: String) extends GamePieceHolder {
  override def toString = name
}

object GamePlayer {
  def apply(name:String) = new GamePlayer(name)
}