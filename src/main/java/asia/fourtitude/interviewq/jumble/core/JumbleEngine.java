package asia.fourtitude.interviewq.jumble.core;

import asia.fourtitude.interviewq.jumble.exception.JumbleEngineException;
import asia.fourtitude.interviewq.jumble.util.Validator;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class JumbleEngine {

    /**
     * From the input `word`, produces/generates a copy which has the same
     * letters, but in different ordering.
     * <p>
     * Example: from "elephant" to "aeehlnpt".
     * <p>
     * Evaluation/Grading:
     * a) pass unit test: JumbleEngineTest#scramble()
     * b) scrambled letters/output must not be the same as input
     *
     * @param word The input word to scramble the letters.
     * @return The scrambled output/letters.
     */
    public String scramble(String word) {
        // Convert the word into a list of characters
        List<Character> characters = new ArrayList<>();
        for (char c : word.toCharArray()) {
            characters.add(c);
        }

        String scrambledWord;
        do {
            // Shuffle the list of characters
            Collections.shuffle(characters);
            // Build a new string from the shuffled characters
            StringBuilder sb = new StringBuilder();
            for (char c : characters) {
                sb.append(c);
            }
            scrambledWord = sb.toString();
        } while (scrambledWord.equals(word)); // Reshuffle if the same

        return scrambledWord;
    }

    /**
     * Retrieves the palindrome words from the internal
     * word list/dictionary ("src/main/resources/words.txt").
     * <p>
     * Word of single letter is not considered as valid palindrome word.
     * <p>
     * Examples: "eye", "deed", "level".
     * <p>
     * Evaluation/Grading:
     * a) able to access/use resource from classpath
     * b) using inbuilt Collections
     * c) using "try-with-resources" functionality/statement
     * d) pass unit test: JumbleEngineTest#palindrome()
     *
     * @return The list of palindrome words found in system/engine.
     * @see https://www.google.com/search?q=palindrome+meaning
     */
    public Collection<String> retrievePalindromeWords() {
        List<String> palindromes = new ArrayList<>();

        // Use try-with-resources to read from the resource file
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("words.txt")))) {

            palindromes = reader.lines() // Create a stream of lines
                    .map(String::trim)
                    .filter(word -> isPalindrome(word) && word.length() > 1) // Check for valid palindrome
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new JumbleEngineException(e.getMessage());
        }

        return palindromes;
    }

    // Helper method to check if a word is a palindrome
    private boolean isPalindrome(String word) {
        int left = 0;
        int right = word.length() - 1;

        while (left < right) {
            if (word.charAt(left) != word.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    /**
     * Picks one word randomly from internal word list.
     * <p>
     * Evaluation/Grading:
     * a) pass unit test: JumbleEngineTest#randomWord()
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param length The word picked, must of length.
     * @return One of the word (randomly) from word list.
     * Or null if none matching.
     */
    public String pickOneRandomWord(Integer length) {
        List<String> words = loadWords(); // Load words from the file
        Random random = new Random();

        // If length is null, return a random word of any length
        if (length == null) {
            return words.get(random.nextInt(words.size()));
        }

        // Filter words by the specified length
        List<String> filteredWords = new ArrayList<>();
        for (String word : words) {
            if (word.length() == length) {
                filteredWords.add(word);
            }
        }

        // Return a random word if there are matching words, or null if none found
        if (!filteredWords.isEmpty()) {
            return filteredWords.get(random.nextInt(filteredWords.size()));
        }
        return null; // No matching word found
    }

    private List<String> loadWords() {
        List<String> wordList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("words.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordList.add(line.trim());
            }
        } catch (IOException e) {
            throw new JumbleEngineException(e.getMessage());
        }
        return wordList;
    }

    /**
     * Checks if the `word` exists in internal word list.
     * Matching is case insensitive.
     * <p>
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param word The input word to check.
     * @return true if `word` exists in internal word list.
     */

    public boolean exists(String word) {
        Set<String> wordSet = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("words.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordSet.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            throw new JumbleEngineException(e.getMessage());
        }

        // Check for null or empty input
        if (word == null || word.trim().isEmpty()) {
            return false;
        }

        // Check for existence in the set (case insensitive)
        return wordSet.contains(word.trim().toLowerCase());
    }

    /**
     * Finds all the words from internal word list which begins with the
     * input `prefix`.
     * Matching is case insensitive.
     * <p>
     * Invalid `prefix` (null, empty string, blank string, non letter) will
     * return empty list.
     * <p>
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param prefix The prefix to match.
     * @return The list of words matching the prefix.
     */
    public Collection<String> wordsMatchingPrefix(String prefix) {
        // Load words into a set for fast lookup
        Set<String> wordSet = loadWordsForMatchingPrefix();

        // Check for invalid prefix (null, empty, blank, or non-letter)
        if (prefix == null || prefix.trim().isEmpty() || !prefix.matches("[a-zA-Z]+")) {
            return new ArrayList<>(); // Return empty list for invalid prefix
        }

        List<String> matchingWords = new ArrayList<>();
        String lowerPrefix = prefix.toLowerCase();

        // Find words that start with the given prefix
        for (String word : wordSet) {
            if (word.startsWith(lowerPrefix)) {
                matchingWords.add(word);
            }
        }

        return matchingWords;
    }

    private Set<String> loadWordsForMatchingPrefix() {
        Set<String> wordSet = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("words.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordSet.add(line.trim().toLowerCase()); // Store words in lower case
            }
        } catch (IOException e) {
            throw new JumbleEngineException(e.getMessage());
        }
        return wordSet;
    }

    /**
     * Finds all the words from internal word list that is matching
     * the searching criteria.
     * <p>
     * `startChar` and `endChar` must be 'a' to 'z' only. And case insensitive.
     * `length`, if have value, must be positive integer (>= 1).
     * <p>
     * Words are filtered using `startChar` and `endChar` first.
     * Then apply `length` on the result, to produce the final output.
     * <p>
     * Must have at least one valid value out of 3 inputs
     * (`startChar`, `endChar`, `length`) to proceed with searching.
     * Otherwise, return empty list.
     * <p>
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param startChar The first character of the word to search for.
     * @param endChar   The last character of the word to match with.
     * @param length    The length of the word to match.
     * @return The list of words matching the searching criteria.
     */
    public Collection<String> searchWords(Character startChar, Character endChar, Integer length) {
        // Load words into a set for fast lookup
        List<String> wordSet = loadFromFile();

        // Check for at least one valid input
        boolean hasValidInput = Validator.isValidChar(startChar) || Validator.isValidChar(endChar) || (length != null && length >= 1);

        if (!hasValidInput) {
            return new ArrayList<>();
        }

        List<String> matchingWords = new ArrayList<>();

        final Character finalStartChar = startChar == null ? null : Character.toLowerCase(startChar);
        final Character finalEndChar = endChar == null ? null : Character.toLowerCase(endChar);

        // all matches start and end character
        matchingWords = wordSet.stream()
                .map(String::toLowerCase)
                .filter(word -> (startChar != null && word.charAt(0) == finalStartChar) ||
                        (endChar != null && word.charAt(word.length() - 1) == finalEndChar) ||
                        (length != null && word.length() == length))
                .collect(Collectors.toList());

        if (length != null) {
            matchingWords = matchingWords.stream()
                    .filter(word -> (startChar != null && endChar != null) && (word.charAt(0) == finalStartChar && word.charAt(word.length() - 1) == finalEndChar && word.length() == length) ||
                            (startChar == null && endChar != null) && (word.charAt(word.length() - 1) == finalEndChar && word.length() == length) ||
                            (startChar != null && endChar == null) && (word.charAt(0) == finalStartChar && word.length() == length) ||
                            (startChar == null && endChar == null) && (word.length() == length))
                    .collect(Collectors.toList());
        }

        if (length == null) {
            matchingWords = matchingWords.stream()
                    .filter(word -> (startChar != null && endChar != null) && (word.charAt(0) == finalStartChar && word.charAt(word.length() - 1) == finalEndChar) ||
                            (startChar == null && endChar != null) && (word.charAt(word.length() - 1) == finalEndChar) ||
                            (startChar != null && endChar == null) && (word.charAt(0) == finalStartChar))
                    .collect(Collectors.toList());
        }
        return matchingWords;
    }

    private List<String> loadFromFile() {
        List<String> wordSet = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("words.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordSet.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as needed
        }
        return wordSet;
    }

    /**
     * Generates all possible combinations of smaller/sub words using the
     * letters from input word.
     * <p>
     * The `minLength` set the minimum length of sub word that is considered
     * as acceptable word.
     * <p>
     * If length of input `word` is less than `minLength`, then return empty list.
     * <p>
     * Example: From "yellow" and `minLength` = 3, the output sub words:
     * low, lowly, lye, ole, owe, owl, well, welly, woe, yell, yeow, yew, yowl
     * <p>
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param word      The input word to use as base/seed.
     * @param minLength The minimum length (inclusive) of sub words.
     *                  Expects positive integer.
     *                  Default is 3.
     * @return The list of sub words constructed from input `word`.
     */
    public List<String> generateSubWords(String word, Integer minLength) {
        // Validate inputs
        if (word == null || minLength == null || word.trim().isEmpty() || word.length() < minLength) {
            return Collections.emptyList();
        }

        Set<String> result = new HashSet<>();
        int length = word.length();

        // Generate combinations
        for (int i = minLength; i <= length; i++) {
            generateCombinations(word.toCharArray(), "", i, result);
        }

        return new ArrayList<>(result);
    }

    private void generateCombinations(char[] letters, String current, int length, Set<String> result) {
        for (int i = 0; i < letters.length; i++) {
            generateCombinations(letters, current + letters[i], length, result);
        }
    }

    /**
     * Creates a game state with word to guess, scrambled letters, and
     * possible combinations of words.
     * <p>
     * Word is of length 6 characters.
     * The minimum length of sub words is of length 3 characters.
     *
     * @param length    The length of selected word.
     *                  Expects >= 3.
     * @param minLength The minimum length (inclusive) of sub words.
     *                  Expects positive integer.
     *                  Default is 3.
     * @return The game state.
     */
    public GameState createGameState(Integer length, Integer minLength) {
        Objects.requireNonNull(length, "length must not be null");
        if (minLength == null) {
            minLength = 3;
        } else if (minLength <= 0) {
            throw new IllegalArgumentException("Invalid minLength=[" + minLength + "], expect positive integer");
        }
        if (length < 3) {
            throw new IllegalArgumentException("Invalid length=[" + length + "], expect greater than or equals 3");
        }
        if (minLength > length) {
            throw new IllegalArgumentException("Expect minLength=[" + minLength + "] greater than length=[" + length + "]");
        }
        String original = this.pickOneRandomWord(length);
        if (original == null) {
            throw new IllegalArgumentException("Cannot find valid word to create game state");
        }
        String scramble = this.scramble(original);
        Map<String, Boolean> subWords = new TreeMap<>();
        for (String subWord : this.generateSubWords(original, minLength)) {
            subWords.put(subWord, Boolean.FALSE);
        }
        return new GameState(original, scramble, subWords);
    }

}
