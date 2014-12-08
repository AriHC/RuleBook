/*
 * Base class for a piece in the game
 * Author: Ari Hausman-Cohen
 * For HMC CS111 Fall 2014
 */
package ruleBook

class GamePiece (val display:Char) {
  def copy = { new GamePiece(display) }
}