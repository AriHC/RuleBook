# Language design and implementation overview

## Language design
_How does a user write programs in your language (e.g., do they type in commands, use a visual/graphical tool, speak, etc.)?_

My language is an internal DSL, hosted by Scala. Thus, a user writes a program, which they can then compile/run. The programs can use a mix of regular Scala code as well as RuleBook constructs.

_What is the basic computation that your language performs (i.e., what is the computational model)?_

At the basic level, a RuleBook program plays a board game when run. That is, it runs through a series of turns (accepting user input) until some win condition is met.

_What are the basic data structures in your DSL, if any? How does a the user create and manipulate data?_

The basic data structures provided are players, generic pieces, and a board. These can be created in their respective sections, and then manipulated later (such as in the move section).

_What are the basic control structures in your DSL, if any? How does the user specify or manipulate control flow?_

The program is broken into several different sectons, like a real rule book: players, board, set up, how to make a move, and win conditions. For sugar purposes, there is also a "definition" section where more defs can be placed, but you could also just define them elsewhere. These are the structures that define the game. The only real ability for control flow is in the move section, where a user can define what moves are legal (the user will be continually prompted for a move until they enter a vaild one). The other flow control is win (/tie/loss) conditions.

_What kind(s) of input does a program in your DSL require? What kind(s) of output does a program produce?_

The program will accept user input for a move (assuming it is written to, and it will output telling a user if a move is invalid). It outputs information about the current game state at regular intervals (what the board looks like, who's turn it is).

_Error handling: How can programs go wrong, and how does your language communicate those errors to the user?_

As an internal DSL with a target audience of people with programming experience, my DSL does little to support errors. General syntax errors should be caught by the scala compiler, but there could be other errors in the use that are syntactically valid but don't do what the programmer wants. For these, there is no feedback. As mentioned above, there is feedback given to the user if they provide invalid input. In the future, I may expand this to allow for custom error messages.

_What tool support (e.g., error-checking, development environments) does your project provide?_

As an internal DSL, this project is supported by whatever Scala tools the user would like. I'm currently using the ScalaIDE to check my sample program as it is being written, which is pretty cool.

_Are there any other DSLs for this domain? If so, what are they, and how does your language compare to these other languages?_

See [language description](description.md#existing-languages). As discussed there, my language is different from existing languages in that it is hopefully more readable, and more extensible by virtue of being internal.

## Language implementation
_Your choice of an internal vs. external implementation and how and why you made that choice._

RuleBook is an internal DSL. This was chosen to allow for easy extensibility. If there is anything not provided by my DSL, the user can write it themselves. This allows in theory any game to be encoded in a RuleBook, as opposed to an external language that would limit the number of describable games. Basically, RuleBook is more of a tool/sugary API, intended to provide a lot of basics that programmers may not want to implement themselves.

_Your choice of a host language and how and why you made that choice._

I'm using Scala, as the flexibility it provides in syntax makes it ideal for internal languages.

_Any significant syntax design decisions you've made and the reasons for those decisions._

Despite Scala in general being flexible, so far I've still run into issues with syntax, as you sometimes need to dig deep to find the flexibility. Right now, I've taken the approach of get something working, then clean up the syntax later if time allows. For example, right now Definitions can not be put in curly braces (all other sections take the form ```Move {...}``` or ```Players {...}```, but Definitions needs to be ```Definitions ...```). Since "if" is reserved by Scala, I had to make "Legal if" into "Legal_if". I suppose I could adjust punctuation or wording, but again, for now in the interest of time, I've been going with underscores over breaking things down into two words. Another major syntax decision I've had to make (for now, this one I'd really like to fix) is that specifying player pieces for setup currently looks like ```Player(2).has(5)(oPiece)``` - the parens and period make me sad.

_An overview of the architecture of your system._

As it currently stands, there is a global Game object. This object (currently) has a list of players, a board, a tracker of the current player, and a definition of what a move is. As I continue writing the language it will also gain definitions of checking for wins/ties/losses. A set of blocks are defined which can be used to manipulate these global values (these are the sections described above; "Players", "Board", "Move", etc.). Note that currently these are in a fie called "implicits" along with some actual implicit declarations, but I would like to refactor this and get those in their own file(s), I just don't want the user to have to import a bunch of things. I want them to be able to use ```import ruleBook._```, I just haven't yet spent the time to figure out the best way to organize it so this still works even when global definitions are spread accross files. When run, as previously described, the global ```Game``` object uses its values that were set by the program, and then cycles through turns (currently, the loop goes display the board, get a move, if it's illegal go back to the start of the loop, otherwise advance to the next player's turn. This will soon get win condition checking added).
