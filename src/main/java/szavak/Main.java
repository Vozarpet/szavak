/**
 * 
 */
package szavak;

import java.io.IOException;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The application entry point.
 * 
 * @author Vozarpet
 *
 */
public class Main {
    /**
     * The main entry point.
     * 
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        Logger l = LoggerFactory.getLogger(Main.class);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        l.debug("Started");

        String dbfile = "";

        DB db = DB.init(dbfile);
        try {
            while (true) {
                int b = System.in.read();
                if (b == -1 || b == 'q')
                    break;

                l.debug("read byte: {}", b);
            }
        } catch (IOException e) {
            l.debug("Could not read from stdin: {}", e.getMessage());
        }

        System.out.println("Stopping");

        db.close();
        System.exit(0);
    }
}
