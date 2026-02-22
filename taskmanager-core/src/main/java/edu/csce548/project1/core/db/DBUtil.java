package edu.csce548.project1.core.db;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/*
 * DBUtil provides JDBC connections.
 * Config lookup order:
 * 1) Environment variables DB_URL/DB_USER/DB_PASSWORD
 * 2) External config file path from -Dtaskmanager.config (default: config.properties)
 * 3) Classpath config.properties
 */
public class DBUtil {

    private static final String CONFIG_PATH_PROPERTY = "taskmanager.config";
    private static final String DEFAULT_CONFIG_PATH = "config.properties";

    private static Properties loadFileConfig() throws Exception {
        String pathValue = System.getProperty(CONFIG_PATH_PROPERTY, DEFAULT_CONFIG_PATH);
        Path path = Path.of(pathValue);
        if (!Files.exists(path)) {
            return new Properties();
        }

        Properties p = new Properties();
        try (InputStream in = new FileInputStream(path.toFile())) {
            p.load(in);
        }
        return p;
    }

    private static Properties loadClasspathConfig() throws Exception {
        Properties p = new Properties();
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                p.load(in);
            }
        }
        return p;
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    private static String firstPresent(String... values) {
        for (String value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public static Connection getConnection() throws Exception {
        Properties fileConfig = loadFileConfig();
        Properties classpathConfig = loadClasspathConfig();

        String url = firstNonBlank(
                System.getenv("DB_URL"),
                fileConfig.getProperty("db.url"),
                classpathConfig.getProperty("db.url"));

        String user = firstNonBlank(
                System.getenv("DB_USER"),
                fileConfig.getProperty("db.user"),
                classpathConfig.getProperty("db.user"));

        String pass = firstPresent(
                System.getenv("DB_PASSWORD"),
                fileConfig.getProperty("db.password"),
                classpathConfig.getProperty("db.password"));

        if (url == null || user == null || pass == null) {
            throw new IllegalStateException(
                    "Database config missing. Set DB_URL/DB_USER/DB_PASSWORD or provide config.properties.");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // If the driver is missing from classpath, DriverManager will throw a useful message.
        }

        return DriverManager.getConnection(url, user, pass);
    }
}