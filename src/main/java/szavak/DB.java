package szavak;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DB is the connection manager object for the used database, in this case
 * SQLite, only a single user can use the connection at a time.
 * 
 * @author Vozarpet
 */
public class DB implements IDB {
    /**
     * The singleton instance.
     */
    private static DB instance = null;
    /**
     * The logger instance.
     */
    private static final Logger l = LoggerFactory.getLogger(DB.class);
    /**
     * The {@link Connection} object.
     */
    private Connection c = null;

    /**
     * Lock protects the sqlite database from concurrency a single outstanding
     * permit is allowed, and the lock is fair.
     */
    private final Semaphore lock = new Semaphore(1, true);

    /**
     * The class is not supposed to be instantiated with the new keyword.
     */
    private DB() {
    }

    /**
     * Returns the singleton DB instance.
     * 
     * @return DB
     */
    public static DB getInstance() {
        return instance;
    }

    /**
     * Initializes the singleton, its only argument tells it the path to the
     * database.
     * 
     * @param filename
     *            is a path to the database file
     * @return DB
     */
    public static DB init(String filename) {
        if (instance != null)
            throw new ExceptionInInitializerError("init must be called only once");

        instance = new DB();
        if (filename.length() == 0)
            filename = "db.sqlite";

        try {
            instance.c = DriverManager.getConnection("jdbc:sqlite:" + filename);
            instance.initTables();
            instance.c.setAutoCommit(false);
        } catch (SQLException e) {
            l.error("Database error: " + e.getMessage());
        }

        return instance;
    }

    /**
     * Initializes the SQLite DB, it is idempontent.
     * 
     * @throws SQLException
     *             on error
     */
    private void initTables() throws SQLException {
        Statement stmt = c.createStatement();
        String sql = ""
            + "PRAGMA foreign_keys = 1;"
            + "VACUUM;"
            + "CREATE TABLE IF NOT EXISTS words ("
            + "  id INTEGER PRIMARY KEY ASC,"
            + "  word TEXT,"
            + "  hunword TEXT,"
            + "  UNIQUE(word)"
            + ");"
            + "CREATE TABLE IF NOT EXISTS state ("
            + "  id INTEGER PRIMARY KEY ASC,"
            + "  correct INTEGER DEFAULT 0,"
            + "  incorrect INTEGER DEFAULT 0,"
            + "  FOREIGN KEY(correct) REFERENCES words(id),"
            + "  FOREIGN KEY(incorrect) REFERENCES words(id)"
            + ");";

        stmt.executeUpdate(sql);
        stmt.close();
    }

    @Override
    public Connection getConnection() {
        while (true) {
            try {
                if (!lock.tryAcquire(5, TimeUnit.SECONDS))
                    throw new RuntimeException("Timed out waiting for lock");

                break;
            } catch (InterruptedException e) {
                l.debug("Interrupted while waiting for lock, trying again");
                continue;
            }
        }

        return c;
    }

    @Override
    public void putConnection() {
        try {
            c.commit();
        } catch (SQLException e) {
            l.debug("commit failed: {}", e.getMessage());
        }

        lock.release();
    }

    /**
     * close is the idempotent destructor of the database, guarantees the database
     * is left in a consistent state.
     */
    public void close() {
        if (c == null)
            return;

        try {
            c.close();
            c = null;
        } catch (SQLException e) {
            l.error("Could not close database: " + e.getMessage());
        }
    }

    /**
     * Calls {@link #close()}.
     */
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}
