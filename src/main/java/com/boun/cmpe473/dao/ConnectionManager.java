package com.boun.cmpe473.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
	// Database credentials
	private static String JDBC_DRIVER;
	private static String DB_URL;
	private static String USER;
	private static String PASS;

	private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

	public void initialize() throws IOException {
		Properties properties = new Properties();
		InputStream stream = getClass().getClassLoader().getResourceAsStream("database.properties");
		properties.load(stream);

		JDBC_DRIVER = properties.getProperty("db.mysql.driver.class");
		DB_URL = properties.getProperty("db.mysql.connection.string");
		USER = properties.getProperty("db.mysql.user");
		PASS = properties.getProperty("db.mysql.password");
	}

	public Connection openConnection() {
		try {
			Class.forName(JDBC_DRIVER);
			Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
			threadLocal.set(connection);
			return connection;
		} catch (Exception se) {
			throw new RuntimeException(se);
		}
	}

	public void releaseConnection() {
		try {
			Connection connection = threadLocal.get();
			if (connection != null) {
				threadLocal.set(null);
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		return threadLocal.get();
	}

}
