package szavak;

import java.sql.Connection;

/**
 * The DB interface used by the models to access the actual database
 * implementation.
 * 
 * @author Vozarpet
 *
 */
public interface IDB {

    /**
     * Returns a connection to the database, it is concurrency safe, calling code is
     * required to call {@link #putConnection()} when done with the connection.
     * 
     * @return {@link Connection}
     */
    public Connection getConnection();

    /**
     * Returns the connection so that it can be used by others.
     */
    public void putConnection();
}
