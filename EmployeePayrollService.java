package com.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {
	
	public enum IOService {CONSOLE_IO, FILE_IO, DB_IO, REST_IO}
	private List<EmployeePayrollData> employeePayrollList;
	private EmployeePayrollDBService employeePayrollDBService;

	public EmployeePayrollService() {
		employeePayrollDBService = EmployeePayrollDBService.getInstance();
	}

	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
		this();
		this.employeePayrollList = employeePayrollList;
	}

	public static void main(String[] args){
		EmployeePayrollService  employeePayrollService =new EmployeePayrollService();
		Scanner consoleInputReader = new Scanner(System.in);
       	employeePayrollService.readEmployeePayrollData(consoleInputReader);
		employeePayrollService.writeEmployeePayrollData(IOService.CONSOLE_IO);
	}

	public void writeEmployeePayrollData(IOService ioService) {
		if(ioService.equals(ioService.CONSOLE_IO))
			System.out.println("\nwriting Employee Payroll Roaster to console" + employeePayrollList);
		else if (ioService.equals(ioService.FILE_IO))
			new EmployeePayrollFileIOService().writeData(employeePayrollList, ioService);

	}
	public List<EmployeePayrollData> readEmployeePayrollData(Scanner consoleInputReader) {
		System.out.println("Enter Employee ID: ");
		int id =  consoleInputReader.nextInt();
		System.out.println("Enter Employee Name: ");
		String name = consoleInputReader.next();
		System.out.println("Enter Employee basic_pay: ");
		double basicpay = consoleInputReader.nextDouble();
		employeePayrollList.add(new EmployeePayrollData(id,name, basicpay));
		return employeePayrollList; 	

	}

	public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) {
		if(ioService.equals(ioService.DB_IO))
			this.employeePayrollList =  new EmployeePayrollDBService().readData();
		return this.employeePayrollList;
	}
	
	 public void printData(IOService ioService) {
		if(ioService.equals(ioService.FILE_IO))
			new EmployeePayrollFileIOService().printData();
	}
	public long countEntries(IOService ioService) {
		if(ioService.equals(ioService.FILE_IO))
			new EmployeePayrollFileIOService().countEntries();
			return 0;
	}

	public void updateEmployeebasicPay(String name, double basicpay) {
		int result = new EmployeePayrollDBService().updateEmployeeData(name,basicpay);
		if (result == 0) return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null) employeePayrollData.basicpay = basicpay;
	
	}

	private EmployeePayrollData getEmployeePayrollData(String name) {
		return this.employeePayrollList.stream()
				.filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name))
				.findFirst()
				.orElse(null);
	}

	public boolean checkEmployeePayrollInSynWithDB(String name, double basic_pay) {
		List<EmployeePayrollData> employeePayrollDataList =  new EmployeePayrollDBService().getEmployeePayrollData(name);
		
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
	}

	

}
