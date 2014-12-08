package ruleBook

abstract class GameOutcome
case class Win(player: GamePlayer) extends GameOutcome
case class Loss(player: GamePlayer) extends GameOutcome
case class Stalemate() extends GameOutcome