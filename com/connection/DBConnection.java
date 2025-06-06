package com.connection;
import java.sql.*;
import java.util.Properties;
import java.io.InputStream;

public class DBConnection {
	public static Connection getConnection() {
        Connection conn = null;
        try {
            Properties props = new Properties();
            InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties");
            props.load(input);

            String url = props.getProperty("url");
            String user = props.getProperty("user");
            String password = props.getProperty("password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

}
