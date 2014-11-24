package ruleBook

class GamePiece (val display:Char) {
  def copy = { new GamePiece(display) }
}