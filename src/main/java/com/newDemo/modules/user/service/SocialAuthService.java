package com.newDemo.modules.user.service;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.plus.Person;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

import com.newDemo.common.service.ResourceProperties;
import com.newDemo.constants.Constant;
import com.newDemo.exceptionalHandler.AuthenticationException;
import com.newDemo.modules.user.dao.UserDao;
import com.newDemo.modules.user.domain.User;


@Service
public class SocialAuthService {
	
	public static Logger log = LoggerFactory.getLogger(SocialAuthService.class);
	@Autowired UserService userService;
	@Autowired UserDao userDao;
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
	 * This method validate the token of respective social site and return existing or new user
	 * @param socialAuthToken
	 * @param loginType
	 * @param userDetail
	 * @return
	 * @throws IOException 
	 */
	public Map<String, Object> socialLogin(String socialAuthToken,String loginType, User userDetail) throws IOException{
		log.debug("logging in");
		Map<String, Object> map = null;
		if(loginType != null){
			switch(loginType){
			case "facebook": 
				if(validateFbToken(socialAuthToken, userDetail));
					map = saveUser(userDetail);
				break;
			case "google":
				if(validateGoogleToken(socialAuthToken, userDetail));
					map = saveUser(userDetail);
				break;
			case "twitter":
				if(validateTwitterToken(socialAuthToken, userDetail));
					map = saveUser(userDetail);
				break;
			default:
				throw new AuthenticationException(configProp.getProperty("invalid.parameter.logintype"),HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
			}
		}else{
			throw new AuthenticationException(configProp.getProperty("logintype.null"),HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
		}
		return map;
	}
	
	private Map<String, Object> saveUser(User userDetail){
		User user = null;
		Map<String, Object> userInfo = null;
		user = userService.getUserByEmail(userDetail.getEmail());
		if(user == null){
			user = userDetail;
			user.setAccountEnabled(true);
		}
		userService.addUser(user);
		userInfo = userService.populateUserInfo(user);
		userInfo.put("user", user);
		return userInfo;
	}
	
	/**
	 * This method is used to validate fb token and return user object 
	 * @param socialAuthToken
	 * @param userDetail
	 * @return
	 * @throws IOException 
	 */
	private boolean validateFbToken(String socialAuthToken, User userDetail) throws IOException{
		log.debug("validate facebook token");
		Facebook facebook = new FacebookTemplate(socialAuthToken, "newDemo");
		org.springframework.social.facebook.api.User facebookUser = facebook.userOperations().getUserProfile();  // throw exception if token is not authenticated
		if(facebookUser.getId().equals(userDetail.getSocialId())){
			return true;
		}else{
			throw new AuthenticationException(configProp.getProperty("invalid.socialid"), HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value());
		}
	}
	
	/**
	 * This method is used to validate google+ token and return user object
	 * @param socialAuthToken
	 * @param userDetail
	 * @return
	 * @throws IOException 
	 */
	private boolean validateGoogleToken(String socialAuthToken, User userDetail) throws IOException{
		log.debug("validate google token");
		Google google = new GoogleTemplate(socialAuthToken);
		Person person = google.plusOperations().getGoogleProfile(); // throw exception if token is not authenticated
		if(person.getId().equals(userDetail.getSocialId())){
			return true;
		}
		else{
			throw new AuthenticationException(configProp.getProperty("invalid.socialid"), HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value());
		}
	}
	
	private boolean validateTwitterToken(String socialAuthToken, User userDetail){
		log.debug("validate twitter token");
		Twitter twitter = new TwitterTemplate("HIMcvgDylmYj89bfP0gBJ9LCP","59bQde4ocC0fdxJDdZwkWYctgJoNc1b2qVdvcbddfUcrE2MvzT","302561664-myOfmwx4XtfX9WWLJgqtxPgV1fYKb02BKefJxTI7","924KFefXJS22cJuvCP1iRQ5cpRStCxQ1dsJfcvfWVRv0O");
		TwitterProfile twitterProfile = twitter.userOperations().getUserProfile(); // throw exception if token is not authenticated
		System.out.println(String.valueOf(twitterProfile.getId()).equals(userDetail.getSocialId()));
		if(String.valueOf(twitterProfile.getId()).equals(userDetail.getSocialId())){
			return true;
		}
		else{
			throw new AuthenticationException(configProp.getProperty("invalid.socialid"), HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value());
		}
	}
	
	/*private User assignRoleToUser(User user){
		log.debug("Assigning roles to user...");
		ArrayList<Roles> roles = new ArrayList<Roles>(1);
		roles.add(Roles.ROLE_USER);
		user.setRoles(roles);
		user.setAccountEnabled(true);
		return user;
	}*/
	
	/**
	 * This method used to logout user
	 * @return
	 */
	/*public boolean logout(){
		log.debug("logging out");
		User loggedInUser = (User)SecurityContextHolder.getContext().getAuthentication().getDetails();
		if(loggedInUser == null){
			throw new UserNotFoundException(configProp.getProperty("user.not.found"),HttpStatus.FORBIDDEN,HttpStatus.FORBIDDEN.value());
		}
		return authenticationTokenService.removeToken(authenticationTokenService.getAuthenticationTokenByUserId(loggedInUser.getId()));
	}*/
}