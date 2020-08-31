package com.lhz.spring.aop.demo.hotswap;

/**
 * @author lhzlhz
 * @create 2020/8/31
 */
import java.util.Date;
public interface Employee  {
	public String getFirstName();
	public void setFirstName(String FirstName);
	public String getLastName();
	public void setLastName(String LastName);
	public String getJobTitle();
	public void setJobTitle(String JobTitle);
	public Float getSalary();
	public void setSalary(Float Salary);
	public Date getHiredate();
	public void setHiredate(Date Hiredate);
}