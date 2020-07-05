package lhz.spring01;

import lhz.spring01.annotaion.Autowried;

/**
 * @author lhzlhz
 * @create 2020/6/27
 */
public class UserController {

	@Autowried
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
