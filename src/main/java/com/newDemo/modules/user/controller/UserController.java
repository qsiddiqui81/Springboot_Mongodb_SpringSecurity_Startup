package com.newDemo.modules.user.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.newDemo.common.service.ResourceProperties;
import com.newDemo.constants.Constant;
import com.newDemo.exceptionalHandler.UserNotFoundException;
import com.newDemo.modules.user.domain.User;
import com.newDemo.modules.user.service.AuthenticationTokenService;
import com.newDemo.modules.user.service.SocialAuthService;
import com.newDemo.modules.user.service.UserService;
import com.newDemo.utils.ResponseHandlerUtil;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	
	public static Logger log = LoggerFactory.getLogger(UserController.class);
	@Autowired UserService userService;
	@Autowired AuthenticationTokenService authenticationTokenService;
	@Autowired SocialAuthService socialAuthService;
	@Autowired ResourceProperties resourceProperties;
	private Properties configProp;
	
	/**
	 * load message property file
	 * @throws IOException
	 */
	@PostConstruct
	public void init() throws IOException{
		configProp = resourceProperties.getResourceProperties(Constant.RESPONSE_MSG_FILE_PATH);
	}
	
	/*@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(new UserValidator());
	}*/
	
	/**
	 * this method used to create new user or update user
	 * @author oodles
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Map<String, Object> addUser(@Valid @RequestBody User user) {
		log.debug("adding new user");
		return ResponseHandlerUtil.generateResponse(
				configProp.getProperty("user.created"), HttpStatus.ACCEPTED,
				true, userService.addUser(user));
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public void getUsers(){
		log.debug("getting users");
	}
	
	/**
	 * This method used to logout user
	 * @return
	 */
	@Secured("ROLE_USER")
	@RequestMapping(value = "/logout" , method = RequestMethod.DELETE)
	public Map<String, Object> logout(){
		log.debug("logging out");
		User loggedInUser = (User)SecurityContextHolder.getContext().getAuthentication().getDetails();
		if(loggedInUser == null){
			throw new UserNotFoundException(configProp.getProperty("user.not.found"),HttpStatus.NOT_FOUND,HttpStatus.NOT_FOUND.value());
		}
		userService.logout(loggedInUser.getId());
		return ResponseHandlerUtil.generateResponse(configProp.getProperty("logout.success"),HttpStatus.ACCEPTED, true, null);
	}
	
	/*@RequestMapping(value = "/sociallogin", method = RequestMethod.POST)
	public Map<String, Object> login(@RequestBody Map map,HttpServletResponse resp) throws IOException{
			Map<String, Object> data = null;
			final ObjectMapper mapper = new ObjectMapper();
			data = socialAuthService.socialLogin((String) map.get("social_auth_token"), (String) map.get("login_type"), mapper.readValue(mapper.writeValueAsString(map.get("userdetails")), User.class));
			String newToken = authenticationTokenService.generateNewToken();
			AuthenticationToken authenticationToken = authenticationTokenService.getAuthenticationTokenByUserId(data.get("id").toString());
		if(authenticationToken != null){
			authenticationToken.setToken(newToken);
			authenticationTokenService.saveAuthenticationToken(authenticationToken);
		}else{
			authenticationTokenService.saveAuthenticationToken(new AuthenticationToken(newToken, (User) data.get("user")));
		}
		data.remove("user");
		data.put("accessToken", newToken);
		return ResponseHandlerUtil.generateResponse(configProp.getProperty("login.success"),HttpStatus.ACCEPTED, true, data);
	}*/
}