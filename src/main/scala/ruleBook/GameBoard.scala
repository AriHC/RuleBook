/*
 * Classes describing a game board
 * Author: Ari Hausman-Cohen
 * For HMC CS111 Fall 2014
 */
package ruleBook
import scala.collection.mutable.Set

// Basically a wrapper to define how a board's spaces are defined
case class SpaceList(spaces:Seq[Seq[GameSpace]])

// Coordinates
class CoordinateFormatException(val coord:String, val requiredFormat : String = "(row,column)") extends Exception
case class Coordinate(row:Int, col:Int) {
  override def toString = "(" + (row + 1).toString + "," + (col + 1).toString + ")"
}
object Coordinate {
  def apply(str:String) : Coordinate = {
    if ((str take 1 is "(") && (str takeRight 1 is ")")) {
      val nums = str drop 1 dropRight 1 split ","
      if (nums.size == 2) {
        try {
          return Coordinate(nums(0).toInt - 1, nums(1).toInt - 1) // Players write indexing from 1.
        }
        catch {
          case _:NumberFormatException => {}
        }
      }
    }
    throw new CoordinateFormatException(str)
  }

  // Access relative coordinates
  def north_of(coord:Coordinate) = {
    Coordinate(coord.row - 1, coord.col)
  }
  def north_east_of(coord:Coordinate) = {
    Coordinate(coord.row - 1, coord.col + 1)
  }
  def east_of(coord:Coordinate) = {
    Coordinate(coord.row, coord.col + 1)
  }
  def south_east_of(coord:Coordinate) = {
    Coordinate(coord.row + 1, coord.col + 1)
  }
  def south_of(coord:Coordinate) = {
    Coordinate(coord.row + 1, coord.col)
  }
  def south_west_of(coord:Coordinate) = {
    Coordinate(coord.row + 1, coord.col - 1)
  }
  def west_of(coord:Coordinate) = {
    Coordinate(coord.row, coord.col - 1)
  }
  def north_west_of(coord:Coordinate) = {
    Coordinate(coord.row - 1, coord.col - 1)
  }
}

// The board itself
class OutOfBoundsException(val coord:Coordinate) extends Exception
class GameBoard(val spaceList:SpaceList) {
  def at(coord:Coordinate) : GameSpace = {
    try {
      spaceList.spaces(coord.row)(coord.col)
    } catch {
      case e:IndexOutOfBoundsException => throw new OutOfBoundsException(coord)
    }
  }
  def at(row:Int, col:Int) : GameSpace = this.at(Coordinate(row-1, col-1))
  // iterate over coordinates
  def foreach_coordinate (action : (Coordinate)=>{}) = {
    var row = 0
    for (row <- 0 to spaceList.spaces.size - 1) {
      var col = 0
      for (col <- 0 to spaceList.spaces(0).size - 1) {
        action(Coordinate(row, col))
      }
    }
  }
  def display {
    // This method is optimized for boards that only have one piece per space.
    // It will still work in other cases, but the spaces may get misaligned.
    spaceList.spaces.foreach{ row =>
      row foreach { spacePieces =>
        if (spacePieces.isEmpty) {
          print("{_}")
        } else {
          print(spacePieces.piecesString)
        }
      }
      print("\n") 
    }	
  }
}

object GameBoard {
  def apply(spaces:SpaceList) = {
    new GameBoard(spaces)
  }
  def empty = {
    apply(SpaceList(Seq.empty[Seq[GameSpace]]))
  }
}