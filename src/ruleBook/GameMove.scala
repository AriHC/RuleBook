package ruleBook
import scala.collection.mutable.Buffer

class GameMove(val spaces : Buffer[Coordinate]) {
  def space (index : Int) = spaces(index - 1)
}

object GameMove {
  def apply (spaces : Buffer[Coordinate]) = new GameMove(spaces)
  def empty = apply(Buffer.empty[Coordinate])
}