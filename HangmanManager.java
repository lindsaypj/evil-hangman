import java.util.*; // List
import java.lang.*; // Throwables

public class HangmanManager {
   /* Maximum number of wrong guesses */
   private int numGuesses;
   /* Given length of words to pick from */
   private int length;
   /* Pattern to display to the user */
   private String displayPattern;
   /* Set of all remaining possible words */
   private TreeSet<String> possibleWords;
   /* Set of all letters guessed */
   private TreeSet<Character> lettersGuessed;
   /* Empty pattern of correct length */
   private String emptyPattern;
   
   /* Primary constructor that initiates a new game. Initializes the number of guesses,
    * length of word to guess, and display pattern.
    * 
    * @param dictionary List of all words in program dictionary
    * @param length the choosen number of characters for the word to be guessed
    * @param max number of guesses to get word
    */
   public HangmanManager(List<String> dictionary, int length, int max) {
      // Validate word length and max guesses
      if (length < 1 || max < 0) {
         throw new IllegalArgumentException();
      }
      else {
         // Initialize values
         this.length = length;
         numGuesses = max;
         emptyPattern = createEmptyPattern();
         displayPattern = emptyPattern;
         lettersGuessed = new TreeSet<Character>();
         possibleWords = new TreeSet<String>();
      
         for (String s : dictionary) { // Check each string in the list
            if (s.length() == length) {
               possibleWords.add(s); // Store all words of input length
            }
         }
      }
   }// End of Constructor
   
   
   /* Gets the current set of possible words
    *
    * @return set of words that could be a solution
    */
   public Set<String> words() {
      return possibleWords;
   }
   
   /* Gets the remaining number of guesses before defeat.
    *
    * @return number of remaining guesses
    */
   public int guessesLeft() {
      return numGuesses;
   }
   
   
   /* Gets a list of guessed letters
    * 
    * @return set of characters containing the previously guessed letters
    */
   public SortedSet<Character> guesses() {
      return lettersGuessed;
   }
   
   
   /* Gets the pattern representing the all of the remaining possible words
    * 
    * @return current display pattern
    */
   public String pattern() {
      // Validate word list Size
      if (possibleWords.size() == 0) {
         throw new IllegalStateException();
      }
      
      // Add " "s to the pattern for displaying
      String displayablePattern = "";
      for (int i = 0; i < length - 1; i++) {
         displayablePattern += displayPattern.charAt(i) + " ";
      }
      return displayablePattern + displayPattern.charAt(length-1); // Return pattern to be displayed
   }
   
   
   /* Method to process the guesses made by the user. Determines the next largest set of possible patterns
    * and stores the new pattern that matches set. Updates program information.
    *
    * @param guess the current charater to process
    * @return the number of guessed characters in new display pattern
    */
   public int record(char guess) {
      // Validate number of guesses & size of words list
      if (numGuesses < 1 || possibleWords.size() < 1) {
         throw new IllegalStateException();
      }
      // Validates that the guess was not already guessed
      else if (lettersGuessed.contains(guess)) {
         throw new IllegalArgumentException();
      }
      
      // Update Information using guess
      lettersGuessed.add(guess); // add guess to the list of guesses
      numGuesses--; // remove one guess from the remaining guesses
      
      // More than one word to choose from
      if (possibleWords.size() > 1) {
         // Map of Possible patters to the words matching those patterns
         mapPossiblePatterns(guess);
      }
      // Only ONE possible word left
      else {
         for (String onlyWord : possibleWords) {
            if (onlyWord.indexOf(guess) >= 0) {
               updatePattern(createPattern(onlyWord, guess));
               return charOccurance(displayPattern, guess);
            }
         }
      } 
      // Retruns the number of times that the guess occurs in the new pattern
      return charOccurance(displayPattern, guess);   
   } // End of record();
   
   
   // Creates a "----" Pattern for the given word and char
   private String createPattern(String word, char letter) {
      // Pattern to return
      String returnPattern = "";
      
      // Chech each index for given char (letter)
      for (int i = 0; i < length; i++) {
         if (word.charAt(i) == letter) {
            returnPattern = returnPattern + letter;
         }
         else {
            returnPattern = returnPattern + "-";
         }
      }
      return returnPattern;
   }// End of createPattern();
   
   // Create Empty Pattern
   private String createEmptyPattern() {
      String pattern = "";
      
      for(int i = 0; i < length; i++) {
         pattern += "-";
      }
      return pattern;
   }
   
   // Method to count the number of times that the char occurs in given pattern
   private int charOccurance(String pattern, char letter) {
      int counter = 0;
      for (int i = 0; i < length; i++) {
         if (pattern.charAt(i) == letter) {
            counter++;
         }
      }
      return counter;
   }
   
   // Combines the previously displayed pattern with the new guesses
   private void updatePattern(String newPattern) {
      if (!newPattern.equals(emptyPattern)) {
         char [] oldP = displayPattern.toCharArray();
         char [] newP = newPattern.toCharArray();
         
         // Update old pattern with new pattern parts
         for(int i = 0; i < length; i++) {
            if (!Character.toString(newP[i]).equals("-") && Character.toString(oldP[i]).equals("-")) {
               oldP[i] = newP[i];
            }
         }
         // Updates the display pattern by converting pattern array back to string
         displayPattern = String.valueOf(oldP);
      }
   }
   
   // Map of Possible patters to the words matching those patterns
   private void mapPossiblePatterns(char guess) {
      // Map to store the words to their respective possible patterns
      TreeMap<String, TreeSet<String>> possiblePatterns = new TreeMap<>();
      
      // look at each possible word and put in map according to associated pattern
      for (String word : possibleWords) {
         String key = createPattern(word, guess);
         if (!possiblePatterns.containsKey(key)) {
            possiblePatterns.put(key, new TreeSet<String>());
         }
         possiblePatterns.get(key).add(word); // Creates "--x-" Pattern and stores word in assosiated key
      }
         
      // Determine pattern with most possible words
      // Also Updates the display pattern with new pattern
      possibleWords = findMostPossible(possiblePatterns);
   }
   
   // Find pattern with greatest number of possible words
   // AKA: Determine New pattern to use
   private TreeSet<String> findMostPossible(TreeMap<String, TreeSet<String>> possiblePatterns) {
      int largestSetSize = -1; // number of words in largest set
      String newDisplayPattern = "";
      TreeSet<String> mostPossibleWords = new TreeSet<>(); // current largest Set of possible words (first of its size)
      
      for (String pattern : possiblePatterns.keySet()) {
         if (possiblePatterns.get(pattern).size() > largestSetSize) {
            // Clear old largest and input new info
            mostPossibleWords.clear();
            mostPossibleWords.addAll(possiblePatterns.get(pattern));
            largestSetSize = mostPossibleWords.size();
            
            // Save key as Display Pattern
            newDisplayPattern = pattern;
         }
      }
      // Update display Pattern with new pattern
      updatePattern(newDisplayPattern);
      
      // Return the new set of possible words
      return mostPossibleWords;
   }
   
}// End of class