# RuleBook

Welcome to RuleBook, a DSL for coding board games (created for HMC CS 111 - full [project requirements](http://www.cs.hmc.edu/~benw/teaching/cs111_fa14/project.html)).

See [language description](documents/description.md) for more details on the ideal endpoint of this project.

## Getting Started

This repository is set up to work with the [Scala Built Tool (SBT)](http://www.scala-sbt.org/index.html). Once that is installed, from the top level directory, you should be able to run ```sbt test``` to run some simple tests of the language and make sure things are working properly.

The jar is also included in the top level directory, or can be built yourself by running sbt pacakge, then picking it out of the correct target folder where it ends up (you should see a message along the lines of ```Packaging C:/Path/To/RuleBook/target/scala-2.10/rulebook_2.10-1.0.jar```). To use RuleBook, simply ```import ruleBook``` (I recommend probably just going ahead and using ```import ruleBook._```), making sure the jar is in your build path. Using SBT, one way to do this is to put the jar in the lib folder. Some sample programs using this method can be found in the [samples](samples/) folder. Try running them by running ```sbt run``` in that folder.

## Playing RuleBook Games
As it currently stands, players take turns making moves. A prompt should appear letting you know who's turn it is, and what kind of move is expected. A move is specified as a series of space-separated coordinates. For example, ```(1,2) (3,4)```. The parentheses and comma are necessary, and do not put extra spaces. If your move is not specified, or is illegal (such as trying to place a piece over someone else's in tic-tac-toe), you'll get an error message and a prompt to enter a new move. Keep playing until the game ends.

## Writing RuleBook Games
A stylistic principle of RuleBook is that games should be readable. To that end, the code is organized like a real rule book for a game might be. Let's step through the code in [tic-tac-toe](samples/src/main/scala/Tic_tac_toe.scala). (If you're just looking for the variables and functions you have access too, check out the [cheat sheet](#cheat-sheet) at the bottom)

### Initialization
As mentioned above, all we need to do to have access to RuleBook is ```import ruleBook._```

If you haven't noticed yet, RuleBook is an internal DSL, meaning any Scala is legal code. We create our game with some basic Scala - ```object tic_tac_toe_with_movement extends App {...}```

Now we get into the RuleBook. We wrap everything in ```Rules {...}```. This tells RuleBook to build a game from the enclosed rules, then run it.

We'll now go over the sections of the rules. The sections should be left in this order, as it both mimics a common physical RuleBook order, and some sections depend upon the ordering.

### Players
```
Players {
  2
}
```
The players section defines how many players there are in the game. Players will all take turns in player order over the course of the game. All you need here is the number of players. Players can be accessed as ```Player number n```, which returns the nth player object (indexed from 1). You can also access the ```current_player``` - whoever's turn it is, or the ```current_player_number```, indexed from 1.

### Board
```    
Board {
   3 x 3
}
```
The board section describes the board. All boards in RuleBook are (currently) rectangles. Specify this using ```rows x cols``` in this section. For later use, when we start referencing coordinates, (0,0) is the top left, and (rows - 1,cols - 1) is the bottom right. When users provide input, they get to index from 1 though (so (1,1) for top left, (rows,cols) for bottom right). Coordinates can be constructed in two ways:
  * ```Coordinate(row, col)``` - index from 0
  * ```Coordinate("(row,col)")``` - this is really intended to convert user input strings to coordinates, and so indexes from 1. It also will throw a ```CoordinateFormatException``` if the string input is incorrectly formatted.
Usually, you just want coordinates the user inputs (which we'll talk about later), or relative coordinates, which can be accessed by ```Coordinate north_west_of [Coordinate]``` - for all 8 relative directions (north/south are lower/higher row indecies, and east/west are higher/lower col indecies).

### Set up
```
Set_up {
  Player number 1 gets (5 of new xPiece)
  Player number 2 gets (5 of new oPiece)
}
```
Like in any physical game, you need to describe the setup. How many pieces does each person have? Are there pieces on the board to start? If we wanted to start pieces on the board, we can access board spaces in a few ways: 
  * ```board at (row, col)``` will get the ```GameSpace``` at (row,col), indexing from 1 like a player would. This is the recommended notation for this section, as players are expected to read this, and will be thrown off if you index from 0.
  * ```board at [Coordinate]``` takes in a ```Coordinate``` object and returns the corresponding ```GameSpace```. Remember from [above](#board) that the primary ```Coordinate``` constructor indexes from 0.
  * ```[Coordinate]``` by itself will implicitly convert to a ```GameSpace``` if appropriate 
```GameSpace``` and ```Player``` both inherit from ```GamePieceHolder```. This means they have a set of game pieces. Notice that we give them pieces by saying that ```[holder] gets [set of pieces]```. One way to create a set of pieces is with ```[num] of [piece]```. The piece must be copyable (the base ```GamePiece``` class implements a simple copy constructor that just copies the display character), because we need to create unique instances. Again, these are stored in a set, so if you inherit from piece, beware how you implement equality if you choose to do so.

### Move
```
Move {
  // These rules describe tic-tac-toe where instead of placing
  // a piece you can move someone else's instead. Just because.
  Choose up_to 2 board spaces
  if ((number_of spaces chosen) is 1) {
    place_piece
  }
  else if ((number_of spaces chosen) is 2) {
    move_piece
  }
}
```
Movement is probably the most complicated piece of the game. Here's how it works. You should
  * Get the user's move
  * Check if the move is legal
  * Move the pieces
If the legality check doesn't pass, no movement will happen, and they'll get to pick another move. It's important to note that if you move something before the legality check, that _will not be undone_ if the move is illegal.

Let's break this down.

#### Getting user input
For now, all moves must be a list of spaces. For details on formatting, see [how to play](#playing-rulebook-games). As a developer, all you need to know is this:
```
Choose [up_to/exactly/at_least] [number] board space[s]
```
This will present the user with a prompt, where they will select their spaces (if they input incorrectly they will be asked to try again until they get it right). After this happens, you can access the chosen spaces with ```chosen space [num]``` - indexing from 1. This returns the ```Coordinate``` they've selected (which, remember, can be implicitly converted to a ```GameSpace```, or explicitly using ```board at [coord]```). Notice for convenience you can ask for the ```number_of spaces chosen```.

#### Checking move legality
You'll notice the move code above, for user readability, simply calls some methods, defined in the ```Definitions``` section. The idea is that a player should not need to look at the definitions. That's where you can put your uglier code, but the functions should be well named. Let's look at the ```place_piece``` function:
```
def place_piece = {
    Legal provided { (chosen space 1) is empty }
    Take pieces (1) from current_player
    board at (chosen space 1) gets taken_pieces
}
```
The ```Legal provided [bool]``` checks the clause, and doesn't let the move proceed past that point if it fails. For convenience, ```is``` and ```is_not``` ```empty``` and ```out_of_bounds``` have been defined for checking Coordinates. A board space is _not_ limited to holding only one piece, though many games may choose to enforce this as part of their rules. (Another convenience - ```is```, ```is_not```, ```and```, and ```or``` have also been defined in a general sense (functioning as ```==```, ```!=```, ```&&```, and ```||```), though the implicit conversions that make this happen may not always work the best. Remember that due to operator notation you can also use ```a equals b```).

#### Moving the pieces
The next part of this is ```Take pieces ([number]) from [holder]``` - this is the opposite of ```[holder] gets [set of pieces]```. Take removes the requested number of pieces from the holder (erroring if the holder is out). Note that there is not currently a way to specify specific pieces to be taken, you would need to use different holders. The taken pieces can be accessed as ```taken_pieces``` until ```Take``` is called again. So in this code we see that one piece is taken from the ```current_player```'s stash, and put onto the board where they wanted to put it.

### Ending Conditions
```
Win_Condition {
  three_in_a_row
}
    
Tie_Condition {
    all_spaces_full
}
```
You can specify ```Win_Condition```, ```Tie_Condition```, and ```Loss_Condition```. If this condition is met at the end of a player's turn, they will be notified that they win/lose/tie and the game will end. The contents of these sections must return a ```Boolean```.

### Definitions 
```       
Definitions
    ...
    class tic_tac_toe_piece(display:Char, val owner:Int) extends GamePiece(display) {
        def owned_by(person:Int) = person is owner
        override def copy = new tic_tac_toe_piece(display, owner)
    }
    case class xPiece() extends tic_tac_toe_piece('x',1)
    case class oPiece() extends tic_tac_toe_piece('o',2) 
```
In this section, put whatever Scala code you want. It's suggested for things like methods and class inheritances. Note that this section _DOES NOT_ get braced in, otherwise problems will occur. Sorry. Notice in this example we're extending the piece class - that way we can specify not just a ```display``` character, but also an owner (this matters here, but in other games, like mancala, pieces may be up for grabs by either player). Note that we override the copy method of ```GamePiece``` because we want to be able to copy all relevant information when generating our pieces during setup.

## Cheat Sheet

### Ordering
```
Rules {
  Players {
    ...
  }
  Board { 
    ...
  }
  Set_up {
    ...
  }
  Move {
    ...
  }
  Win/Tie/Lose_Condition { // up to 1 of each, order doesn't matter
    ...
  }
  Definitions
    ...
}
```

### Classes and related methods
The following are methods and classes you may want to access. Others exist, but are either not commonly used, or not intended for access.
  * ```Rules```
    * Construct with your rules to run your game.
  * ```Coordinate```
    * ```Coordinate([row], [col])``` - Constructor
    * ```Coordinate([string])``` - Constructor. String should be of the form "(row,col)"
    * ```Coordinate south_east_of [coord]``` - get relative coordinates. north_of, west_of, etc. also available.
  	* ```[coord] is empty``` - check if a coordinate is empty. ```is_not``` also available.
  	* ```[coord] is in_bounds``` - check if a coordinate is on the board. ```is_not``` also available.
  * ```GameBoard```
    * Construct using ```Board``` section and ```[rows] x [cols]``` notation
    * ```board``` - get the global game board
    * ```[board] at [coord]``` - access specific locations on a ```GameBoard```. Returns a ```GameSpace```
    * ```[board] at ([row], [col])``` - same functionality as above, _indexes from 1_
    * ```[board] foreach_coordinate [func]``` - iterate through coordinates
    * ```[board].display()``` - display a board and pieces on it.
  * ```GameMove```
     * ```[move].spaces``` - a ```Buffer``` (mutable list) of ```Coordinate```
     * ```GameMove.empty``` - an empty move
     * ```[move] space [index]``` - one space of a move, _indexes from 1_
  * ```GamePiece```
    * ```val display:Char```
    * ```[piece].copy``` - a new piece with the same ```display```
    * ```[number] of [piece]``` - generates a ```Set``` with [number] copies of [piece].
  * ```GamePieceHolder``` (trait)
    * ```val name:String```
    * ```val pieces:Set[GamePiece]``` - mutable set
    * ```[holder] foreach [function]``` - iterate over pieces
    * ```[holder] gets [set of pieces]``` - give a holder more pieces
    * ```[holder].isEmpty``` - true if has no pieces
    * ```[holder] take [num]``` - removes pieces from the holder. Can throw ```OutOfPiecesException(name)```
  * ```GamePlayer``` (extends ```GamePieceHolder```)
    * Initialize players using ```Players``` section (i.e. ```Players { 2 }```)
    * ```Player number [num]``` - access a player, _indexes from 1_
    * ```current_player``` - access the current player
    * ```current_player_number``` - get the current player's number, _indexes from 1_
  * ```GameSpace``` (extends ```GamePieceHolder```)
  * The ```Move``` section
    * ```Choose [qualifier] [num] spaces``` - qualifier can be ```exactly```, ```up_to```, or ```at_least```. Feel free to use ```space``` instead of ```spaces``` if it is gramatically appropriate.
  	* ```chosen```, ```current_move``` - both return the ```GameMove``` - corresponding to the most recent selection by a player.
  	* ```number_of spaces [move]``` - the number of spaces in a game move (e.g. ```number_of spaces chosen```).
  	* ```Legal provided [bool]``` - the player must choose a new move if the bool is false.
  	* ```Take pieces ([num]) from [holder]``` - takes the specified number of pieces from a piece holder.
  	* ```taken_pieces``` - the pieces taken by the most recent ```Take``` command
  * General:
  	* ```[a] is [b]``` - ```==```. ```is_not``` for ```!=```. Not guaranteed to work for all classes.
