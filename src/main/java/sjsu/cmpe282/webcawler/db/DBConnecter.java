package sjsu.cmpe282.webcawler.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBConnecter {
	private String dbDriver = "com.mysql.jdbc.Driver";
	private String username = "root";
	private String password = "root";
	private String URL = "jdbc:mysql://localhost:3306/resumerank";

	public Connection connectDatabase() {
		Connection connection = null;
		try {
			Class.forName(dbDriver).newInstance();
			connection = DriverManager.getConnection(URL, username, password);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static void closeStatement(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	public static void closeAllConnection(ResultSet rs, Statement ps , Connection con){
		DBConnecter.closeStatement(ps);
		DBConnecter.closeResultSet(rs);
		DBConnecter.closeConnection(con);
	}
	

}
