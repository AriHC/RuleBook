# Project description and plan

## Motivation
Board games are fun and intellectually stimulating way to pass time, but few make the leap over to the digital realm. But many board games are basically just finite state machines where players take turns providing input, meaning they should be fairly simple to model in code. Furthermore, board games have a lot to gain from digitization - AI can be developed so you can play alone, the internet can be leveraged to allow playing with random others from around the globe, and computers can ensure that rules are enforced and play proceeds correctly. Of course, to even begin working on these things, developers first have to translate their game into code, a fairly labor-intensive process, which may be the reason most games stay physical only. My goal with this project is to find areas of high commonality between games and abstract them out to a Domain Specific Language (DSL). This DSL could then be used as a tool to quickly convert existing board games into digital form, or for game designers to easily test and iterate on new games, without having to put in the labor of making physical pieces.

## Language domain
The high level goal for the domain of this language would be all board games, but that's an incredibly broad spectrum. As such, I am narrowing the focus at the start to games which use only the following concepts:
* Turn based
* Fixed set of ordered actions in a turn
* Board that can be expressed as an m by n rectangle of squares
* Finite number of pieces that can be moved (in some fixed manner) around the board, removed, or added to a space

An example game in this domain would be chess. If I complete this domain, I will slowly relax the constraints, such as allowing more complex boards. Small iterations is the key here - for example, I may still require square spaces, just not in a rectangular configuration. This would allow games such as some one player peg jumping games.

As a stretch, I would like the system to eventually be able to handle games wiht cards/decks, and handle aspects such as shuffling, drawing, and playing cards.

### Existing languages
There are some languages out there already that begin to address this domain. Some examples I found are [Zillions of Games (ZoG)](http://www.zillions-of-games.com/) and the [Game Description Language](http://logic.stanford.edu/classes/cs227/2013/readings/gdl_spec.pdf). Based on the syntax snipets I was able to find, these seem quite similar to what I had in mind - you describe game states, legal moves, etc. I can look at their syntaxes for elemnts to consider adding, or a general idea of how one may specify certain concepts, but I see two primary areas where the languages are lacking.
1. Readability. It can be pretty hard to understand what the code is specifying (see my design ideas [below](#Language design)).
2. Extensibility and logic. The languages seem to have a fixed set of commands, limiting them to a very specific scope (though as the Zillions of Games community has shown, game spaces are huge, and there is still a giant variety of games to be explored within even a narrow scope). My primary audience is those looking to convert existing board games to the digital sphere, and each game is likely to have something unique and special about it, that may not be accounted for by an external DSL.

## Language design
"Rules that run." In the perfect world, a RuleBook program would read just like the rules to a board game. A slightly different way of phrasing that, but more achievable, is that by reading a program one would know how to play the game (i.e. the syntax might not be perfect, but it will be clear enough to a non-programmer). A program will, much like a board game rulebook, be divided into some basic sections - board, pieces, setup, turn overview, details of legal moves, win conditions.
When run, a program will play the game, accepting user inputs to describe moves each player is making. Output will be some visual representation of the current state of the game. 

I see this as being a very object-oriented design, with variables for players, pieces, board squares, etc. Since board games are a very physical thing, abstracting to OOP should be a natural transition.

From a practical perspective, a lot of rules in rulebooks have implicit meanings that would need to be more fully described. I want to use some form of indirection (i.e. variables/methods) to allow the main program to say, for example "A bishop can move diagonally" and then elsewhere define in more precise terms what "diagonally" means (if I don't give that as a built-in). One idea I have for this is to give some sort of markup (i.e. the rule might read "A bishop can move (term:) diagonally"), and then have an appendix section where all the terms are specified more completely.

To make the above factors more practical, I intend to write RuleBook as an internal DSL, potentially on top of Scala as a host language (primarily for its highly manipulable syntax, though I also anticipate clean pattern matching being helpful to check states/moves). This means that compile time errors will be handled by Scala. At this time, the only runtime error I percieve occuring is "invalid move", at which point the user will be told as much and prompted to provide another move. The system will also potentially need to detect when players have no legal moves.

## Example computations
### Tic-Tac-Toe
This was used as an example computation for Zillions of Games and the Game Description Language, and I like the idea. Tic-tac-toe is a very simple game, with a relatively low number of moves and states. This makes it a good "Hello, World!" like program for languages in this domain. In tic-tac-toe, players place Xs or Os on a 3x3 board until either one player has 3 in a row (horizontal, vertical, or either diagonal), or the board is filled without such occurance and a tie is formed. 
### Chess
A standard two-player chess game (or variations) should be describable (and thus runnable) in RuleBook. That is, a program that displays an initially set up chess board, then loops:
* Take a move input from whoevers turn it is (enforcing allowable moves)
* Update the board
until checkmate is reached.