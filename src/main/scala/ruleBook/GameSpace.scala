package ruleBook

class GameSpace(override val name:String) extends GamePieceHolder
object GameSpace {
  def apply(name:String) = new GameSpace(name)
  def apply = new GameSpace("anonymous")
}