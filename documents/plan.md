# Project plan

## Language evaluation

RuleBook aims for programs to be straightforward descriptions of board games. Thus, design choices will be evaluated based on the following criteria:
* Similarity to English - particularly English commonly used in game rule books. I will evaluate potential syntaxes by comparing games written using the different options to various versions of rules for that game. When possible I will use printed rule books, though when I do not have any I will turn to rule descriptions found on the internet. Syntaxes closer to the original rules will be preferred when possible. 
* Understandability - While perfect syntax may not always be possible, someone unfamiliar with RuleBook should be able to read a program and easily know how the game works. This may be accomplished by user testing; presenting programs to users and asking them about the game. To avoid users just being familiar with a game and figuring it out, I may make games that are slightly different, and ask users to describe how they differ from the traditional version of the game. I feel that in some DSLs for this domain (see "existing languages" subsection in [language description](description.md)), such differences would take a while to spot as the encoding is hard to understand. Hopefully in RuleBook users can quickly see what's wrong.
* Writability - I will be the primary tester of this. It should be reasonably easy to translate existing games into RuleBook. I will implement at least a few games in RuleBook, and use my own experiences doing so to guide what needs to be made easier.

In terms of implementation quality, since RuleBook is an internal DSL I don't need to worry about parsing, but I will write unit tests to ensure that all features work as expected.

## Implementation plan

Deadlines:
* 11/9 - Pseudo syntax for canonical examples (tic-tac-toe and chess)
* 11/16 - Working code for 1/4 of features necessary for tic-tac-toe, updated syntax as necessary, feature plan for remaining tic-tac-toe required features
* 11/23 - All features necessary for tic-tac-toe implemented.
* 11/30 - 1/2 of additional features for chess done, feature plan for remaining chess features
* 12/7 - All features necessary for chess implemented. 
* 12/12 - Example game that is not tic-tac-toe or chess that can be written using the existing features of RuleBook by this point.

To clarify, working code includes unit tests to prove it is working.

A feature plan is a concrete description of what I want the syntax of a feature to look like (basically method signature and accompanying syntactic sugar).

I budgeted fewer features to be implemented by 11/16 as I anticipate a large part of the tic-tac-toe features will be the bigger framework issues - setting up a general m by n board, setting up players and giving them pieces, etc. After these are established, hopefully adding rule features will be more simple and build upon my previous work.