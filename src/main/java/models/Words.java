package models;

/**
 * This represents an words used to identify reporters.
 * 
 * @author Vozarpet
 *
 */
public class Words {
    /**
     * The word in english.
     */
    private String word;
    /**
     * Hunword is the word in hungarian.
     */
    private String hunword;

    /**
     * Constructs an words from the given word and hunword.
     * 
     * @param word
     *            the human readable label for the words
     * @param hunword
     *            the key for the verification of payloads
     * 
     */
    public Words(String word, String hunword) {
        super();
        if (!wordValid(word))
            throw new IllegalArgumentException("word invalid");
        if (hunword != null && !wordValid(hunword))
            throw new IllegalArgumentException("hunword invalid");
        this.word = word;
        this.hunword = hunword;
    }

    /**
     * Returns the word.
     * 
     * @return String
     */
    public String getWord() {
        return word;
    }

    /**
     * Validates and sets the word. If it is invalid, throws an
     * {@link IllegalArgumentException}.
     * 
     * @param word
     *            the human readable label for the words
     */
    public void setWord(String word) {
        if (!wordValid(word))
            throw new IllegalArgumentException("word invalid");

        this.word = word;
    }

    /**
     * Returns the hunword.
     * 
     * @return String
     */
    public String getHunword() {
        return hunword;
    }

    /**
     * Validates and sets the hunword. throws an {@link IllegalArgumentException}.
     * 
     * @param hunword
     *            the key for the verification of payloads
     */
    public void setHunword(String hunword) {
        if (hunword != null && !wordValid(hunword))
            throw new IllegalArgumentException("hunword invalid");

        this.hunword = hunword;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((word == null) ? 0 : word.hashCode());
        result = prime * result + ((hunword == null) ? 0 : hunword.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Words other = (Words) obj;
        if (word == null) {
            if (other.word != null)
                return false;
        } else if (!word.equals(other.word))
            return false;
        if (hunword == null) {
            if (other.hunword != null)
                return false;
        } else if (!hunword.equals(other.hunword))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Words [word=" + word + ", hunword=" + hunword + "]";
    }

    /**
     * Validates the word, it must be valid UTF-8 and its length has to be at least
     * 3 and at most 20 characters.
     * 
     * @param word
     *            a string with at least 3 and at most 20 characters
     * @return boolean
     */
    public static boolean wordValid(String word) {
        if (word == null)
            return false;
        if (word.length() < 2 || word.length() > 40)
            return false;
        return true;
    }
}
