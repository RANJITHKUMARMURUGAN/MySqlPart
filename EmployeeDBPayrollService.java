package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {
	
	private PreparedStatement employeePayrollDataStatement;
	private List<EmployeePayrollData> employeePayrollList;
	private static EmployeePayrollDBService employeePayrollDBService;
	EmployeePayrollDBService() {	
	}

	public static EmployeePayrollDBService getInstance() {
		if (employeePayrollDBService == null)
			employeePayrollDBService = new EmployeePayrollDBService();
		return employeePayrollDBService;

	}

	//public Object read;
	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service";
		String userName = "root";
		String password = "123";
		Connection connection;
		System.out.println("Connecting to data base:" +jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName,password);
		System.out.println("Connection is sucessful " + connection);
		return connection;
	}
	public List<EmployeePayrollData> readData() {
		String sql = "Select * from employee_payroll;";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try {
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			while(result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				double basicpay = result.getDouble("basicpay");
				LocalDate startDate  = result.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, basicpay, startDate));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return employeePayrollList;
	}
	//	Statement
	public List<EmployeePayrollData> getEmployeePayrollData(String name){
		List<EmployeePayrollData> employeePayrollList = null;
		if (this.employeePayrollDataStatement == null)
			this.prepareStatementForEmployeeData();
		try {
			employeePayrollDataStatement.setString(1, name);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			employeePayrollList = this.getEmployeePayrollData(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;

	}

	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
		// TODO Auto-generated method stub
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try {
			while(resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				double salary = resultSet.getDouble("salary");
				LocalDate startDate  = resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return employeePayrollList;
	}
		

	private List<EmployeePayrollData> getEmployeePayrollData1(String name) {
		// TODO Auto-generated method stub
		List <EmployeePayrollData> employeePayrollData = null;
		if (this.employeePayrollDataStatement == null)
			this.prepareStatementForEmployeeData();
		try {
			employeePayrollDataStatement.setString(1, name);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			employeePayrollList = this.getEmployeePayrollData(resultSet);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return employeePayrollList;
	}
	//prepareStatement 
	private void prepareStatementForEmployeeData() {
		try {
			Connection connection = this.getConnection();
			String sql = "Select * from employee_payroll where name = ?";
			employeePayrollDataStatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int updateEmployeeData(String name, double basicpay) {
		return this.updateEmployeeDataUsingStatement(name, basicpay);
	}
	private int updateEmployeeDataUsingStatement(String name, double basicpay) {
		// TODO Auto-generated method stub
		String sql = String.format("update employee_payroll set basic_pay = %.2f where name = 'Teria';", basicpay);
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);	
		} catch (SQLException e) {
			e.printStackTrace(); 
	
		}
		return 0;
	}
}
