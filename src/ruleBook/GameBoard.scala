package ruleBook

case class SpaceList(spaces:Seq[Seq[Option[GamePiece]]])

class GameBoard(val spaceList:SpaceList) {
  
  def display {
    spaceList.spaces.foreach{ row =>
      row.foreach{ piece =>
        piece match {
          case None => print("_")
          case Some(GamePiece(display)) => print(display)
        }
        print(" ")
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
    apply(SpaceList(Seq.empty[Seq[Option[GamePiece]]]))
  }
}