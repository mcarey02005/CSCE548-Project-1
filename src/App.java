import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * Simple console application that connects to MySQL, prints row counts, and
 * shows sample task data.
 *
 * Purpose: - Prove database connectivity - Demonstrate basic data retrieval -
 * Serve as the console frontend for the project
 */
public class App {

    /**
     * Loads database configuration from config.properties. This keeps
     * credentials out of source code.
     */
    private static Properties loadConfig() throws Exception {
        Properties props = new Properties();

        // First try loading from the working directory
        try (InputStream in = new FileInputStream("config.properties")) {
            props.load(in);
        }

        return props;
    }

    public static void main(String[] args) {
        try {
            Properties config = loadConfig();

            String url = config.getProperty("db.url");
            String user = config.getProperty("db.user");
            String password = config.getProperty("db.password");

            // Establish database connection
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database.");

            // Print row counts
            String[] tables = {"users", "categories", "tasks"};
            for (String table : tables) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + table);
                rs.next();
                System.out.println(table + ": " + rs.getInt(1));
                rs.close();
                stmt.close();
            }

            // Show sample data using joins (proves foreign keys work)
            System.out.println("\nSample tasks:");
            String query
                    = "SELECT t.task_id, u.name AS user_name, c.name AS category_name, "
                    + "t.description, t.completed "
                    + "FROM tasks t "
                    + "JOIN users u ON t.user_id = u.user_id "
                    + "JOIN categories c ON t.category_id = c.category_id "
                    + "ORDER BY t.task_id "
                    + "LIMIT 10";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("task_id");
                String userName = rs.getString("user_name");
                String category = rs.getString("category_name");
                String description = rs.getString("description");
                boolean completed = rs.getBoolean("completed");

                System.out.println(
                        id + ") [" + userName + "] (" + category + ") - "
                        + description + (completed ? " [DONE]" : "")
                );
            }

            rs.close();
            stmt.close();
            conn.close();

            System.out.println("\nProgram finished successfully.");

        } catch (Exception e) {
            System.err.println("ERROR:");
            e.printStackTrace();
        }
    }
}