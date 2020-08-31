package com.lhz.spring.aop.demo.hotswap;

/**
 * @author lhzlhz
 * @create 2020/8/31
 */
import org.springframework.aop.MethodBeforeAdvice;
import java.lang.reflect.Method;
public class EmployeeValidator implements MethodBeforeAdvice  {
	public EmployeeValidator() {
	}

	public void before(Method method, Object[] args, Object target) throws Throwable {
		Employee emp = (EmployeeImpl)target;

		if (method.getName().equalsIgnoreCase("setSalary")) {
			if ("SALESMAN".equalsIgnoreCase(emp.getJobTitle())) {
				// if the job of this employee is SALESMAN, he/she may not earn more than 4000
				float newSalary =  ((Float)args [ 0 ]).floatValue();

				if (newSalary > 4000) {
					throw new RuntimeException("Salary may not exceed 4000 for Salesmen such as "+emp.getFirstName()+" "+emp.getLastName());
				}
			}
		}
		if (method.getName().equalsIgnoreCase("setFirstName")) {
			if ("Doe".equalsIgnoreCase(emp.getLastName())) {
				// we do not want any employee to be called John Doe
				if ("John".equalsIgnoreCase((String)args [ 0 ])) {
					throw new RuntimeException("Employees should not be called John Doe. Choose another First Name please.");
				}
			}
		}
		if (method.getName().equalsIgnoreCase("setLastName")) {
			if ("John".equalsIgnoreCase(emp.getFirstName())) {
				// we do not want any employee to be called John Doe
				if ("Doe".equalsIgnoreCase((String)args [ 0 ])) {
					throw new RuntimeException("Employees should not be called John Doe. Choose another Last Name please.");
				}
			}
		}
		if (method.getName().equalsIgnoreCase("setJobTitle")) {
			if ("MANAGER".equalsIgnoreCase((String)args [ 0 ])) {
				// provide a decent starting value for Manager's Salaries
				if (null==emp.getSalary())
				{ emp.setSalary(new Float(7500));
				}
			}
		}
	}
}