# Critique

## Syntax
I would definitely recommend having a tutorial or guide for the syntax here.
Does the order matter for the commands (Players, Board, etc.)? If it's possible
to have the order not matter, that would be better in my opinion.

A few suggestions for the syntax:
* Will there ever be a case where you need more than a number to describe the players or more than two numbers to describe the board? If not, I would probably remove the curly braces around those and add some other character to separate the label from the value. I would understand potentially not wanting to change this to maintain consistency, but I think that would clean it up a bit. This would change it to something like

```
Rules {
  Players: 2
  Board: 3 x 3
  ...
}
```
* Will you have a way to refer to players other than by "Player number X"? If not, just "Player X" flows more easily. 

Win and tie conditions are definitely important for the full program, so I agree
with prioritizing that.

How do you plan to specify what a turn is? Is move synonymous with turn, or do you
plan to support games that may have multiple moves in one turn?

