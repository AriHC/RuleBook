package samples

import ruleBook._

object tic_tac_toe extends App {
  Play { Game ("tic_tac_toe").
    Players {
      2
    }.
    Board {
      1 x 5
    }
  }
}