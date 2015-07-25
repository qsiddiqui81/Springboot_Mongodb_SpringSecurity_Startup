package com.newDemo.common.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@Secured("ROLE_USER")
	@RequestMapping(value = "/api/v1/user/home", method = RequestMethod.GET)
	public String home(){
		System.out.println("welcome");
		return "home";
	}
}