
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/*
 * DBUtil: reads config.properties and returns a Connection.
 * Keeps connection logic centralized for DAOs.
 */
public class DBUtil {

    private static Properties loadConfig() throws Exception {
        Properties p = new Properties();
        try (InputStream in = new FileInputStream("config.properties")) {
            p.load(in);
        }
        return p;
    }

    public static Connection getConnection() throws Exception {
        Properties cfg = loadConfig();
        String url = cfg.getProperty("db.url");
        String user = cfg.getProperty("db.user");
        String pass = cfg.getProperty("db.password");
        // Explicit driver registration is optional if jar is on classpath, but safe:
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // If driver isn't found, let getConnection throw helpful exception
        }
        return DriverManager.getConnection(url, user, pass);
    }
}