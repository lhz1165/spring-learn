package com.lhz.spring.aop.demo.hotswap;

/**
 * @author lhzlhz
 * @create 2020/8/31
 */
import java.util.HashMap;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.HotSwappableTargetSource;
//
public class HotSwappeableTargets  {
	public HotSwappeableTargets() {
	}
	//
	HashMap swapperCache = new HashMap(10);

	/**
	 * 返回Employee代理对象
	 * @return
	 */
	public Employee getEmployee() {
		Employee emp = new EmployeeImpl();
		ProxyFactory pf = new ProxyFactory();

		//对于proxy，我们不是把emp应用直接的作为目标去执行方法的
		//我们使用一个额外的HotSwappableTargetSource来干预
		//因为TargetSource持有目标对象，再proxy做完增强之后都要再去调用目标方法
		//本质上意味着，在调用之间我们可以改变目标实例，通过设置TargetSource所持有的target对象。所以执行增强以后的方法就可以和之前的目标对象不一样。
		HotSwappableTargetSource swapper = new HotSwappableTargetSource(emp);
		pf.setTargetSource(swapper);
		pf.setInterfaces(new Class[]{Employee.class});
		pf.addAdvice(new EmployeeValidator());
		//
		// add the swapper to a swapperCache; this allows us to later on retrieve
		// the swapper using a reference to the proxy, so that we can swap the POJO
		// underlying the proxy; note that pf.getProxy() is the reference that
		// consumers of this getEmployee() method will use instead of a direct
		// reference to an EmployeeImpl object.
		swapperCache.put(pf.getProxy(),swapper);
		return (Employee)pf.getProxy();
	}
	//
	/**
	 * This method will replace the reference to oldEmployee with a reference to newEmployee.
	 * Anyone who held a reference to oldEmployee will still have a valid reference, as we
	 * only swap the target for the proxy. All references to oldEmployee are in fact references
	 * to a proxy for the oldEmployee. By changing the target for this proxy, all references
	 * remain in tact and indirectly now reference the newEmployee
	 *
	 * @param newEmployee
	 * @param oldEmployee
	 */
	private void swap(Employee oldEmployee, Employee newEmployee) {
		HotSwappableTargetSource swapper = (HotSwappableTargetSource)swapperCache.get(oldEmployee);
		Object oldTarget = swapper.swap(newEmployee);
	}
	//
	public static void main(String[] args) {
		HotSwappeableTargets mySecondAOP = new HotSwappeableTargets();
		// 没被代理的对象 （员工）
		Employee emp = new EmployeeImpl();
		emp.setFirstName("John");
		emp.setLastName("Doe");
		emp.setJobTitle("SALESMAN");
		emp.setSalary(new Float("1500"));
		//
		// 旧的代理对象 （经理）
		Employee scott = mySecondAOP.getEmployee();
		scott.setFirstName("John");
		scott.setLastName("Smith");
		scott.setJobTitle("MANAGER");
		// we have advised scott with an advise that sets a default salary value for Managers:
		System.out.println("My dear employee " + scott.getFirstName() + " " + scott.getLastName() + " (" + scott.getJobTitle() + ") = " + scott.getSalary());
		Employee alsoscott = scott; // alsoscott holds a reference to the same object (a proxy) that scott references
		//
		//把代理对象由【经理】替换为【员工】
		mySecondAOP.swap(scott, emp);
		System.out.println("My dear employee " + scott.getFirstName() + " " + scott.getLastName() + " (" + scott.getJobTitle() + ") = " + scott.getSalary());
		// 这打印的是员工的信息
		System.out.println("Also  " + alsoscott.getFirstName() + " " + alsoscott.getLastName() + " (" + alsoscott.getJobTitle() + ") = " + alsoscott.getSalary());

		// 员工引用
		emp.setSalary(new Float("5500"));
		System.out.println("My dear employee " + scott.getFirstName() + " " + scott.getLastName() + " (" + scott.getJobTitle() + ") = " + scott.getSalary());
		//由于代理对象是员工，所以设置金额抛出异常
		scott.setSalary(new Float("5000.32"));
		System.out.println("The new salary for " + scott.getFirstName()+ " " + scott.getLastName() + " (" + scott.getJobTitle() +  ") = " + scott.getSalary());
	}
}