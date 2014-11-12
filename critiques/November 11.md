# November 11 Critique

## Syntax
I really like the syntax so far. How will you teach people what keywords are used?
I like how you are specifying the pieces and their characters. I think that will make
displaying your final game much easier and isn't too burdensome for the user. 

I agree that it would probably be a good idea to use curly braces to specify blocks
instead of colons. I'm also not sure how possible it will be to parse your intended
syntax. I would recommend trying to set the parser earlier than later, even if you 
haven't completely defined the IR, so you can figure out if it is possible or not.

I would probably try to simplify the way you specify legal moves. I feel it would get
very difficult to specify what moves are legal for chess with your current syntax. 

## Thoughts about Variables

Do you want to just essentially paste the definition of each 
term wherever the term is? That would likely be easier to implement, but may be harder
for the program writer to conceptualize. 

If you want more context to be included in the use of the variables, it will be somewhat
harder to implement but may be easier for the user to understand.

If you will only be using take/taken for the piece being used, it may be easier if you just
define either a single variable to hold the currently held piece and some other way to
specify which user is holding it or two variables, one for each user. 

