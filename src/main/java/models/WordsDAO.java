package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import szavak.DB;
import szavak.IDB;

/**
 * This class implements the database access for words.
 * 
 * @author Vozarpet
 *
 */
public class WordsDAO implements IWordsDAO {
    /**
     * The logger instance.
     */
    private static final Logger l = LoggerFactory.getLogger(WordsDAO.class);
    /**
     * The database singleton.
     */
    private IDB db;

    /**
     * Initializes the object with the {@link DB} singleton.
     */
    public WordsDAO() {
        super();
        db = DB.getInstance();
    }

    /**
     * Initializes the object with the database from the argument.
     * 
     * @param db
     *            the concrete database
     */
    public WordsDAO(IDB db) {
        super();
        this.db = db;
    }

    @Override
    public void insert(Words w) {
        Connection c = db.getConnection();

        try (PreparedStatement stmt = c.prepareStatement("INSERT INTO words (word, hunword) VALUES (?, ?)")) {
            stmt.setString(1, w.getWord());
            stmt.setString(2, w.getHunword());
            stmt.execute();
        } catch (SQLException e) {
            l.error("SQL Error: {}", e.getMessage());
        } finally {
            db.putConnection();
        }
    }

    @Override
    public Words getWord(Integer identifier) {
        Connection c = db.getConnection();
        Words ret = null;

        try (PreparedStatement stmt = c.prepareStatement("SELECT * FROM words WHERE id = ? LIMIT 1")) {
            stmt.setInt(1, identifier);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ret = new Words(rs.getString("word"), rs.getString("hunword"));
                break;
            }

            rs.close();
        } catch (SQLException e) {
            l.error("SQL Error: {}", e.getMessage());
        } finally {
            db.putConnection();
        }

        return ret;
    }

    @Override
    public List<Words> getWords(int start, int limit) {
        Connection c = db.getConnection();
        LinkedList<Words> ret = new LinkedList<>();

        try (PreparedStatement stmt = c.prepareStatement("SELECT * FROM words ORDER BY word LIMIT ?, ?")) {
            stmt.setInt(1, start);
            stmt.setInt(2, limit);

            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                ret.add(new Words(rs.getString("word"), rs.getString("hunword")));

            rs.close();
        } catch (SQLException e) {
            l.error("SQL Error: {}", e.getMessage());
        } finally {
            db.putConnection();
        }

        return ret;
    }
}
