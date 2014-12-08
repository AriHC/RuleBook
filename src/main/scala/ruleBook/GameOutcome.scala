/*
 * The three types of game outcomes.
 * Author: Ari Hausman-Cohen
 * For HMC CS111 Fall 2014
 */
package ruleBook

abstract class GameOutcome
case class Win(player: GamePlayer) extends GameOutcome
case class Loss(player: GamePlayer) extends GameOutcome
case class Stalemate() extends GameOutcome