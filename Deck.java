import java.util.Random;

/** Represents a deck of cards, and operations that can be performed
 * on it.
 *
 * @author Arran Stewart (skeleton), Xinkai Chen 22404059 (implementation)
 *
 */
public class Deck {
    /** The size of a deck of cards. Four suits of thirteen cards,
     * plus two jokers.
     */
    public final static int DEFAULT_DECK_SIZE = 13 * 4 + 2;

    /** The value given to the first joker
     **/
    public final static int JOKER1 = 13 * 4 + 1;

    /** The value given to the second joker
     **/
    public final static int JOKER2 = 13 * 4 + 2;

    // Array holding ints representing the cards.
    // Card values should start from 1, and each int
    // should be unique within the array.
    private int[] cards;

    /** Create a deck of cards in the default order.
     */
    public Deck() {
        cards = new int[DEFAULT_DECK_SIZE];
        for (int i = 0; i < DEFAULT_DECK_SIZE; i++) {
            cards[i] = i + 1;
        }
    }

    /** Returns true when all values of the array arr are
     * different to each other; returns false otherwise.
     *
     * @param arr An array of int values to be checked
     * @return Whether all values in the array are different
     */
    public static boolean allDifferent(int[] arr) {
        for (int i = 0; i < arr.length; i++){
            for (int j = i + 1; j < arr.length; j++){
                if (arr[i] == arr[j]){
                    return false;
                }
            }
        }
        return true;
    }

    /** Construct a deck of cards from a String of comma-separated values.
     *
     * @param inputString A string, consisting of comma-separated integers.
     */
    public Deck(String inputString) {
        if (inputString.equals("")) {
            cards = new int[0];
            return;
        }

        String[] strings = inputString.split(",");

        cards = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            cards[i] = Integer.parseInt( strings[i] );
        }
        validateCards();
    }

    /** This method should check whether all the
     * values in the "cards" instance variable
     * are different.
     *    If they are not, it should throw an
     * IllegalArgumentException exception.
     *
     */
    private void validateCards() {
        if (!allDifferent(cards)){
            throw new IllegalArgumentException("Invalid cards!");
        }
    }

    public int size() {
        return cards.length;
    }

    public int getCard(int idx) {
        return cards[idx];
    }

    /** Shuffles elements of an array into a random permutation.
     *
     * @param arr Array to be shuffled.
     */
    public static void shuffleArray(int[] arr) {
        int temp, index;
        Random rand = new Random();
        // each item in the array is swapped at least once
        for (int i = 0; i < arr.length; i++) {
            index = rand.nextInt(arr.length);
            temp = arr[index];
            arr[index] = arr[i];
            arr[i] = temp;
        }
    }

    public void shuffle() {
        shuffleArray(cards);
    }

    /** Find the position in the deck of the card
     * with value cardVal.
     *
     * @param cardVal The card to find
     * @return The position of the card to find, or -1
     * 	   if it wasn't in the deck.
     */
    public int findCard(int cardVal) {
        for (int i = 0; i < size(); i++){
            if (cards[i] == cardVal){
                return i;
            }
        }
        return -1;
    }

    /** Shift a particular card down the deck by one place,
     * and if this would take you past the end of the deck,
     * treat the end of the deck as joining onto the beginning.
     *
     * In other words: for any card except the last card,
     * the card is swapped with the card immediately
     * after it. For the last card: it gets inserted after the
     * first card, and the second card and all subsequent cards
     * are "moved down one".
     *
     * If the argument passed is not found in the deck,
     * this method should throw an IllegalArgumentException
     * exception.
     *
     * @param cardVal The value of the card to be shifted.
     */
    public void shiftDownOne(int cardVal) {
        if (findCard(cardVal) == -1){
            throw new IllegalArgumentException("Invalid card value!");
        }
        else {
            int index = findCard(cardVal);
            int temp = cardVal;
            if (index == size() - 1){
                for (int i = size() - 1; i > 1; i--){
                    cards[i] = cards[i - 1];
                }
                cards[1] = temp;
            }
            else {
                cards[index] = cards[index + 1];
                cards[index + 1] = temp;
            }
        }
    }

    /** Perform a "triple cut": Given the positions of 2 cards in the deck,
     * divide the deck into three "chunks":
     *  chunk "A", the "top" - cards before either position
     *  chunk "B", the "middle" - cards lying between the 2 positions
     *  chunk "C", the "bottom" - cards after either position.
     *
     *  Reorder the deck by swapping the top and bottom chunks
     *  (chunks "A" and "C").
     *
     * @param pos1 Position of a "fixed" card, counting from 0.
     * @param pos2 Position of another "fixed" card, counting from 0.
     */
    public void tripleCut(int pos1, int pos2) {
        // make sure the positions are valid
        if (pos1 < 0 || pos1 >= DEFAULT_DECK_SIZE || pos2 < 0 || pos2 >= DEFAULT_DECK_SIZE){
            throw new IllegalArgumentException("Invalid position(s)!");
        }
        // get the size of chunk "A" and chunk "C"
        int a = pos1;
        int c = size() - 1 - pos2;
        // first, move chunk "C" to the middle (between chunk "A" and "B")
        for (int i = 0; i < c; i++){
            int temp = cards[size() - 1];
            for (int j = size() - 1; j > pos1; j--){
                cards[j] = cards[j - 1];
            }
            cards[pos1] = temp;
        }
        // then move chuck "A" to the bottom
        for (int k = 0; k < a; k++){
            int temp = cards[0];
            for (int l = 0; l < size() - 1; l++) {
                cards[l] = cards[l + 1];
            }
            cards[size() - 1] = temp;
        }
    }

    /** Perform a "count cut". Let n be a number of cards.
     * Remove n cards from the top of the deck, and place them
     * just above the last card.
     *
     * @param numCards
     */
    public void countCut(int numCards) {
        // make sure the number n is valid
        if (numCards < 0 || numCards >= DEFAULT_DECK_SIZE){
            throw new IllegalArgumentException("Invalid number of cards!");
        }
        // if 0 card is moved or first (size-1) cards are moved, do nothing
        else if (numCards == 0 || numCards == DEFAULT_DECK_SIZE - 1){
            return;
        }
        // for each time, place the first card above the last card
        else {
            for (int i = 0; i < numCards; i++) {
                int temp = cards[0];
                for (int j = 0; j < size() - 2; j++) {
                    cards[j] = cards[j + 1];
                }
                cards[size() - 2] = temp;
            }
        }
    }
}