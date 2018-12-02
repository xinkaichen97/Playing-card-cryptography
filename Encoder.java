/**
 * @author Arran Stewart (skeleton), Xinkai Chen 22404059 (implementation)
 *
 */
public class Encoder {

    public static final int ALPHABET_SIZE = 26;
    private final Deck deck;

    /** Create an Encoder using the default ordering
     * of a deck of cards.
     *
     */
    public Encoder() {
        deck = new Deck();
    }

    /** Create an Encoder using a particular deck
     * of cards to generate the key.
     *
     */
    public Encoder(Deck d) {
        deck = d;
    }

    /** Remove all non-alphabetic characters from a string,
     * and convert all alphabetic characters to upper-case.
     *
     * @param s Input string
     * @return Sanitized string
     */
    public static String sanitize(String s) {
        if (s != null) {
            char[] letters = s.toCharArray();
            StringBuilder sanitized = new StringBuilder("");
            //String sanitized = "";
            for (int i = 0; i < letters.length; i++) {
                if (Character.isLetter(letters[i])){
                    sanitized.append(letters[i]);
                }
            }
            return sanitized.toString().toUpperCase();
        }
        else {
            return null;
        }
    }

    /** Return the position in the alphabet of an uppercase
     * character, starting from 1 (i.e., charToInt('A') returns 1,
     * charToInt('B') returns 2, and so on).
     *
     * @param c Character to convert to an int
     * @return Result of conversion
     */
    public static int charToInt(char c) {
        if (c > 'Z' || c < 'A'){
            throw new IllegalArgumentException("Illegal character!");
        }
        return c - 'A' + 1;
    }

    /** Given a position in the alphabet (starting from 1),
     * return the character at that position.
     * (i.e. intToChar(1) returns 'A', intToChar(2) returns 'B',
     * and so on). If a number larger than 26 is passed in,
     * subtract 26 from it before applying this conversion.
     *
     * @param i int to convert to a character
     * @return Result of conversion
     */
    public static char intToChar(int i) {
        // make sure the number is positive
        if (i <= 0){
            throw new IllegalArgumentException("Non-positive integer!");
        }
        if (i > 26){
            i -= 26;
        }
        return (char) (i + 64);
    }

    /** Encode a character (inputChar) using a character from the keystream
     * (keyChar).
     *
     * To do this, firstly convert both characters into integers,
     * as described in charToInt.
     *
     * Then add the numbers together. If the result is greater than 26,
     * subtract 26 from it; then convert that result back to a character.
     *
     * @param inputChar Character from message
     * @param keyChar Character from keystream
     * @return Encoded character
     */
    public static char encodeChar(char inputChar, char keyChar) {
        int num1 = charToInt(inputChar);
        int num2 = charToInt(keyChar);
        if (num1 + num2 > 26){
            return intToChar(num1 + num2 - 26);
        }
        else {
            return intToChar(num1 + num2);
        }
    }


    /** Decode a character (inputChar) from an encrypted message using a character
     * from the keystream (keyChar).
     *
     * Convert both characters to integers, as described for
     * charToInt. If inputChar is less than or equal to keyChar,
     * add 26 to it. Then subtract keyChar from inputChar,
     * and convert the result to a character.
     *
     * @param inputChar Character from an encrypted message
     * @param keyChar Character from keystream
     * @return Decoded character
     */
    public static char decodeChar(char inputChar, char keyChar) {
        int num1 = charToInt(inputChar);
        int num2 = charToInt(keyChar);
        if (num1 <= num2){
            return intToChar(num1 + 26 - num2);
        }
        else {
            return intToChar(num1 - num2);
        }
    }

    /** Encode the string inputText using the keystream characters
     * in keyChars, by repeatedly calling encodeChar.
     *
     * @param inputText Message text to encode
     * @param keyChars Characters from keystream
     * @return Encoded message
     */
    public static String encodeString(String inputText, String keyChars) {
        char[] letters = inputText.toCharArray();
        char[] keys = keyChars.toCharArray();
        StringBuilder encoded = new StringBuilder("");
        // after using the last character of keyChars (if happens), go back to the first character
        for (int i = 0; i < letters.length; i++){
            encoded.append(encodeChar(letters[i], keys[i % keys.length]));
        }
        return encoded.toString();
    }

    /** Decode the string inputText using the keystream characters
     * in keyChars, by repeatedly calling decodeChar.
     *
     * @param inputText Encoded text which needs to be decoded
     * @param keyChars Characters from keystream
     * @return Decoded message
     */
    public static String decodeString(String inputText, String keyChars) {
        char[] letters = inputText.toCharArray();
        char[] keys = keyChars.toCharArray();
        StringBuilder decoded = new StringBuilder("");
        for (int i = 0; i < letters.length; i++){
            decoded.append(decodeChar(letters[i], keys[i % keys.length]));
        }
        return decoded.toString();
    }

    /** Apply the Pontoon algorithm to generate the
     * next character in the *keystream*. The character
     * returned will depend on the state of the "deck"
     * instance variable when the method is called.
     *
     * @return A keystream character
     */
    public char nextKeyStreamChar() {
        // make sure the deck is a 54-card one
        if (Deck.DEFAULT_DECK_SIZE != 54){
            throw new IllegalArgumentException("Encoder cannot work without a 54-card deck!");
        }
        int output = Deck.JOKER2;
        while (output == Deck.JOKER1 || output == Deck.JOKER2) {
            deck.shiftDownOne(Deck.JOKER1);
            deck.shiftDownOne(Deck.JOKER2);
            deck.shiftDownOne(Deck.JOKER2);
            int jk1_index = deck.findCard(Deck.JOKER1);
            int jk2_index = deck.findCard(Deck.JOKER2);
            // make sure the first parameter of tripleCut is less than the second one
            if (jk1_index < jk2_index) {
                deck.tripleCut(jk1_index, jk2_index);
            }
            else {
                deck.tripleCut(jk2_index, jk1_index);
            }
            int value = deck.getCard(Deck.DEFAULT_DECK_SIZE - 1);
            if (value == 54) {
                value -= 1;
            }
            deck.countCut(value);
            int index = deck.getCard(0);
            if (index == 54) {
                output = deck.getCard(index - 1);
            }
            else {
                output = deck.getCard(index);
            }
        }
        return intToChar(output);
    }

    /** Encrypt a string, using the deck to generate
     * *keystream* characters which can be passed
     * to encodeChar.
     *
     * @param inputString The string to encrypt
     * @return The result of encryption
     */
    public String encrypt(String inputString) {
        char[] input = inputString.toCharArray();
        StringBuilder encrypted = new StringBuilder("");
        for (char chr : input){
            char key = nextKeyStreamChar();
            encrypted.append(encodeChar(chr, key));
        }
        return encrypted.toString();
    }

    /** Decrypt a string, using the deck to generate
     * *keystream* characters which can be passed
     * to decodeChar.
     *
     * @param inputString The string to decrypt
     * @return The result of decryption
     */
    public String decrypt(String inputString) {
        char[] input = inputString.toCharArray();
        StringBuilder decrypted = new StringBuilder("");
        for (char chr : input){
            char key = nextKeyStreamChar();
            decrypted.append(decodeChar(chr, key));
        }
        return decrypted.toString();
    }
}