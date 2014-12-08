/*
 * A GameMove is a wrapper around a sequence of spaces.
 * This could bear to be better generalized.
 * Author: Ari Hausman-Cohen
 * For HMC CS111 Fall 2014
 */
package ruleBook
import scala.collection.mutable.Buffer

class GameMove(val spaces : Buffer[Coordinate]) {
  def space (index : Int) = spaces(index - 1)
  override def toString = {
    var result = "{"
    spaces foreach { space =>
      result += space.toString + " "
    }
    result.trim + "}"
  }
}

object GameMove {
  def apply (spaces : Buffer[Coordinate]) = new GameMove(spaces)
  def empty = apply(Buffer.empty[Coordinate])
}