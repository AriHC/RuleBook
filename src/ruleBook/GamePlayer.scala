package ruleBook

class GamePlayer(var pieces : Seq[GamePiece]) {
  def has(num:Int)(piece:GamePiece) = {
    pieces = pieces ++ Seq.fill(num){piece}
  }
}

object GamePlayer {
  def apply() = new GamePlayer(Seq.empty[GamePiece])
}