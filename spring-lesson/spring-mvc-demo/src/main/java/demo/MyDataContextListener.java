package demo;

import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author lhzlhz
 * @create 2020/9/25
 */
public class MyDataContextListener extends ContextLoaderListener {
	private ServletContext context;



	@Override
	public void contextInitialized(ServletContextEvent sce) {
		super.contextInitialized(sce);
		context = sce.getServletContext();

		//自己的逻辑
		context.setAttribute("msg","this is");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		this.context = null;
	}
}
