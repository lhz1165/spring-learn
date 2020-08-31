package com.lhz.spring.aop.demo.hotswap;

/**
 * @author lhzlhz
 * @create 2020/8/31
 */
import java.util.Date;
public class EmployeeImpl implements Employee {
	String FirstName;
	String LastName;
	String JobTitle;
	Float Salary;
	Date Hiredate;
	public EmployeeImpl() {
	}
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String FirstName) {
		this.FirstName = FirstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String LastName) {
		this.LastName = LastName;
	}
	public String getJobTitle() {
		return JobTitle;
	}
	public void setJobTitle(String JobTitle) {
		this.JobTitle = JobTitle;
	}
	public Float getSalary() {
		return Salary;
	}
	public void setSalary(Float Salary) {
		this.Salary = Salary;
	}
	public Date getHiredate() {
		return Hiredate;
	}
	public void setHiredate(Date Hiredate) {
		this.Hiredate = Hiredate;
	}
}