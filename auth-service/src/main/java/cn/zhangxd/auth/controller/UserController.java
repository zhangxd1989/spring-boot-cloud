package cn.zhangxd.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class UserController {

	@RequestMapping(value = "/me", method = RequestMethod.GET)
	public Principal getUser(Principal principal) {
		return principal;
	}
}
