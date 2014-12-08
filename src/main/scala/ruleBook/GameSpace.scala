/*
 * Class for a space on a board.
 * Author: Ari Hausman-Cohen
 * For HMC CS111 Fall 2014
 */
package ruleBook

class GameSpace(override val name:String) extends GamePieceHolder
object GameSpace {
  def apply(name:String) = new GameSpace(name)
  def apply = new GameSpace("anonymous")
}