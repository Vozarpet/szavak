package szavak;

import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import models.Words;
import models.WordsDAO;

/**
 * Initial sample data generator entry point.
 * 
 * @author Vozarpet
 *
 */
public class GenerateSampleData {
    /**
     * The logger instance.
     */
    private static final Logger l = LoggerFactory.getLogger(GenerateSampleData.class);

    /**
     * The basic list of test words identifiers to insert from.
     */
    private static final String[] words = { "Yes", "No", "Maybe", "Available" };

    /**
     * The basic list of test hunwords identifiers to insert from.
     */
    private static final String[] hunwords = { "Igen", "Nem", "Talán", "Elérhető" };

    /**
     * The main entry point for generating sample data.
     * 
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        DB db = DB.init("");
        // TURN OFF file syncing
        try (Statement stmt = db.getConnection().createStatement()) {
            stmt.executeUpdate("COMMIT; PRAGMA synchronous = 0; BEGIN TRANSACTION;");
        } catch (SQLException e) {
            l.error("SQL Error: {}", e.getMessage());
        } finally {
            db.putConnection();
        }

        generateWords();

        db.close();
    }

    /**
     * Generates sample words.
     */
    private static void generateWords() {
        WordsDAO wordsDAO = new WordsDAO();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            String hunword = hunwords[i];
            Words w = new Words(word, hunword);
            wordsDAO.insert(w);
        }
    }

}
