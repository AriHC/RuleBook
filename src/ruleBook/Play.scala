package ruleBook

object Play {
	def apply(game:Game) = {
  	  println(game.name)
	  println(game.players)
	  game.board.display
	}
}