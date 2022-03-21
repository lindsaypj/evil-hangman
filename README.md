# evil-hangman

================================================================================
HOW THE GAME WORKS:
================================================================================
In a normal game of hangman, the computer picks a word that doesn’t change, and 
then the user is supposed to guess. The user guesses individual letters until the word is 
fully discovered. If you aren’t familiar with the general rules of the game of hangman,
review its Wikipedia page: http://en.wikipedia.org/wiki/Hangman_(game).

In our game of hangman, the computer delays picking a word until it is forced to. As a 
result, the computer is always considering a set of words that could be the answer. In 
order to fool the user into thinking it is playing fairly, the computer only considers words 
with the same letter pattern.

For example, suppose that the computer knows the words in the following dictionary:

ALLY BETA COOL DEAL ELSE FLEW GOOD HOPE IBEX

In a normal game of hangman, the computer would start the game by choosing a word 
to guess. In our game, the computer doesn't yet commit to an answer but instead 
narrows down its set of possible answers as the user makes guesses. In the log below, 
the user’s first guess is 'e'. The computer has to reveal where the letter 'e' appears but 
since it hasn't chosen an answer, it has several options. In particular, note that the
dictionary's words fall into 5 families:

  ---- is the pattern for [ally, cool, good]
  e--- is an empty pattern with no possible values.
  -e-- is the pattern for [beta, deal]
  --e- is the pattern for [flew, ibex]
  ---e is the pattern for [hope]
  e--e is the pattern for [else]
  all other patterns are empty.
  
The computer could choose to reveal any of these 5 patterns. It could use several 
different strategies for picking the family to display. For this project, your program will 
always choose the largest of the remaining word families. This will leave the
computer's options open and increase its chances of winning. For the example 
described above, the computer would pick ----. This reduces the possible answers it 
can consider to: ally cool good. Since the computer didn't reveal any letters, it 
counts this as a wrong guess and decreases the number of guesses left to 6.

Next, the user guesses the letter 'o'. The computer has two word families to consider:

  -oo- is the pattern for [cool, good]
  ---- is the pattern for [ally]

It picks the biggest family and reveals the letter 'o' in two places. This was a correct guess so 
the user still has 6 guesses left. The computer now has only two possible answers to choose 
from: cool good

When the user picks 'c', the computer removes 'cool' from its consideration and is now locked 
into using 'good' as the answer. With a large dictionary, it will take much longer for the computer 
to have to pick an answer!
