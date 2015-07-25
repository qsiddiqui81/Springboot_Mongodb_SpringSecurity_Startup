package com.newDemo.modules.user.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newDemo.common.service.ResourceProperties;
import com.newDemo.constants.Constant;
import com.newDemo.enums.Roles;
import com.newDemo.modules.user.dao.UserDao;
import com.newDemo.modules.user.domain.User;

@Service
public class UserService{
	public static Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired UserDao userDao;
	@Autowired AuthenticationTokenService authenticationTokenService;
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
	
	/**
	 * * this method is used to create new user
	 * @author oodles
	 * @param user
	 */
	public User addUser(User user){
		log.debug("adding new user");
		List<Roles> roleList = new ArrayList<Roles>();
		roleList.add(Roles.ROLE_USER);
		user.setRoles(roleList);
		return (User) userDao.save(user);
	}
	
	/**
	 * change user password
	 * @param currentPassword
	 * @param newPassword
	 * @param confirmPassword
	 *//*
	public boolean changePassword(String currentPassword, String newPassword, String confirmPassword){
		log.debug("changing password...");
		User loggedInUser = (User)SecurityContextHolder.getContext().getAuthentication().getDetails();
		if(loggedInUser == null){
			throw new UserNotFoundException(configProp.getProperty("user.not.found"),HttpStatus.FORBIDDEN,HttpStatus.FORBIDDEN.value());
		}
		if(!checkUserPassword(currentPassword, loggedInUser))
			throw new PasswordException(configProp.getProperty("password.wrong"), HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value());
		if(!newPassword.equals(confirmPassword))
			throw new PasswordException(configProp.getProperty("password.confirm"), HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value());
		loggedInUser.setPassword(newPassword);
		userDao.save(loggedInUser);
		return true;
	}*/
	
	/**
	 * check user password
	 * @param currentPassword
	 * @param user
	 * @return
	 *//*
	private boolean checkUserPassword(String currentPassword, User user){
		log.debug("checking password...");
		return new BCryptPasswordEncoder().matches(currentPassword, user.getPassword())?true:false;
	}*/
	
	/**
	 * to get user by id
	 */
	public User getUserById(String userId){
		 return userDao.findUserById(userId);
	}
	
	/**
	 * @param email
	 * to get user by email
	 */
	public User getUserByEmail(String email){
		return userDao.findByEmail(email);
	}
	
	public List<User> getUsers(String userId){
		return userDao.getUsers(userId);
	}
	
	public Map<String, Object> populateUserInfo(User user){
		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("id", user.getId());
		userInfo.put("socialId", user.getSocialId());
		userInfo.put("firstName",user.getFirstName());
		userInfo.put("lastName", user.getLastName());
		userInfo.put("email",user.getEmail());
		userInfo.put("age", user.getAge());
		userInfo.put("gender", user.getGender());
		userInfo.put("address", user.getAddress());
		userInfo.put("aboutMe",user.getAboutMe());
		return userInfo;
	}
	
	public boolean logout(String userId){
		log.debug("logging out");
		return authenticationTokenService.removeToken(authenticationTokenService.getAuthenticationTokenByUserId(userId));
	}
}