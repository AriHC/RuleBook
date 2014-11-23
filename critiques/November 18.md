# Critique

## Specifying Moves

Typically for games like chess, moves are specified something like the following
```
black rook from A2 to C3
```
I would probably recommend specifying the actual moves like that. As far as defining
what moves are valid, that will be a bit more complicated. From trying to think about
what kinds of definitions you would need for chess, I can't think of a structure that
wouldn't take an exorbitant number of lines. 

Perhaps you could have a structure where each piece has a set of states and a set of 
moves for each state. That way you could account for the behavior of kinged checkers
and such. It may also make sense for each move to have a "limit direction" option that
you can set, so that you could specify that normal checkers can only move forward.
Maybe something like the following:
```
checker state 0:
    from XY to (X+1)(Y+1), state 0
    from XY to (X-1)(Y+1), state 0
```
This is assuming that X is the horizontal axis and Y is vertical. Obviously this would be
more complicated for chess.

```
rook state 0:
    from XY to (X+1)(Y+2), state 0
    from XY to (X+1)(Y-2), state 0
    from XY to (X+2)(Y+1), state 0
    from XY to (X+2)(Y-1), state 0
    from XY to (X-1)(Y+2), state 0
    from XY to (X-1)(Y-2), state 0
    from XY to (X-2)(Y+1), state 0
    from XY to (X-2)(Y-1), state 0
```
For a game like tic-tac-toe, you could possibly specify a player-set or pile, something to 
tell that the piece isn't on the board.
```
X state 0:
    from player-set to A1, state 0
```
Although this is less applicable to tic-tac-toe, I could imagine other games where you could
place a piece from your hand/set and then move it afterward. 

To access a specific piece, I'd probably do that by location on the grid (like A7).



