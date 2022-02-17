package com.hubino.usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.hubino.usermanagement.model.TableUserInfo;

public class UserDAO {

	private String jdbcURL = "jdbc:mysql://localhost:3306/jsp-servlet?useSSL=false";
	private String jdbcUsername = "root";
	private String jdbcPassword = "R@vanan99";

	private static final String INSERT_USERS_SQL = "INSERT INTO users (name, email, country) VALUES " + "(?, ?, ?)";
	private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id=?";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_USERS_SQL = "delete from users where id=?";
	private static final String UPDATE_USERS_SQL = "update users set name = ?,email = ?,country = ? where id = ?";

	protected Connection getConnection() {
		Connection connection = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;

	}
	
	/**
	 * This method is to insert the user info.
	 * @param tableUserInfo
	 * @throws SQLException
	 */
	public void insertUser(TableUserInfo tableUserInfo) throws SQLException {
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);){
			preparedStatement.setString(1, tableUserInfo.getName());
			preparedStatement.setString(2, tableUserInfo.getEmail());
			preparedStatement.setString(3, tableUserInfo.getCountry());
			preparedStatement.executeUpdate();
		}
	}
	
	/**
	 * This method is to update the user info.
	 * @param tableUserInfo
	 * @return
	 * @throws SQLException
	 */
	public boolean updateUser(TableUserInfo tableUserInfo) throws SQLException {
		boolean rowUpdated;
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS_SQL);){
			preparedStatement.setString(1, tableUserInfo.getName());
			preparedStatement.setString(2, tableUserInfo.getEmail());
			preparedStatement.setString(3, tableUserInfo.getCountry());
			preparedStatement.setInt(4, tableUserInfo.getId());
			rowUpdated = preparedStatement.executeUpdate() > 0;
		}
		return rowUpdated;
	}
	
	public TableUserInfo selectUser(int id) throws SQLException {
		TableUserInfo tableUserInfo = null;
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);){
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String country = resultSet.getString("country");
				tableUserInfo = new TableUserInfo(id, name, email, country);
			}
		}
		return tableUserInfo;
	}
	
	public List<TableUserInfo> selectAllUser() throws SQLException {
		List<TableUserInfo> tableUserInfo = null;
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);){
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				int id = Integer.parseInt(resultSet.getString("id"));
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String country = resultSet.getString("country");
				tableUserInfo.add(new TableUserInfo(id, name, email, country));
			}
		}
		return tableUserInfo;
	}
	
	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USERS_SQL);){
			preparedStatement.setInt(1, id);
			rowDeleted = preparedStatement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

}
