package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lhzlhz
 * @create 2020/9/9
 */

@Controller
public class IndexController{

	@RequestMapping("/hello")
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView("index");
		mav.addObject("message", "Hello Spring MVC");
		return mav;
	}
}
