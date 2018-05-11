package models;

import java.util.List;

/**
 * The Words interface used by the controllers to access data.
 * 
 * @author Vozarpet
 *
 */
public interface IWordsDAO {
    /**
     * Returns the {@link Words} that belongs to the given identifier, null if it
     * does not exist.
     * 
     * @param identifier
     *            the unique identifier
     * @return {@link Words}
     */
    public Words getWord(Integer identifier);

    /**
     * Inserts a new word into the database.
     * 
     * @param ep
     *            the word to insert
     */
    public void insert(Words ep);

    /**
     * Gets a list of {@link Words} from the database.
     * 
     * @param start
     *            what offset to start from
     * @param limit
     *            the maximum number of items to return
     * @return {@link Words}
     */
    public List<Words> getWords(int start, int limit);
}
