package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static String user;
    private static String password;
    private static String url;

    public static Connection getConnection() {
        readConfig();
        try{
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void readConfig(){
        Properties prop = new Properties();
        try(FileInputStream file = new FileInputStream("./src/config.properties")){
            prop.load(file);
            String host = prop.getProperty("db.host");
            String port = prop.getProperty("db.port");
            String database = prop.getProperty("db.database");
            user = prop.getProperty("db.user");
            password = prop.getProperty("db.pass");
            url = "jdbc:postgresql://"+ host +"/"+ database + "?sslmode=require";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
