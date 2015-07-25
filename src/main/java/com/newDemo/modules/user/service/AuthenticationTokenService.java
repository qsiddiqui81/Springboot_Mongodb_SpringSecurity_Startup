package com.newDemo.modules.user.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mongodb.WriteResult;
import com.newDemo.modules.user.dao.AuthenticationDao;
import com.newDemo.modules.user.dao.UserDao;
import com.newDemo.modules.user.domain.AuthenticationToken;
import com.newDemo.modules.user.domain.User;

@Service
public class AuthenticationTokenService{
	
	public static Logger log = LoggerFactory.getLogger(AuthenticationTokenService.class);
	@Autowired	UserDao userDao;
	@Autowired	AuthenticationDao authenticationDao;

	  public String generateNewToken() {
		  	log.debug("generating new token");
	        return UUID.randomUUID().toString();
	  }
	  
	  public User getAuthentication(String email, String password){
		  log.debug("authenticating user");
		  User user = userDao.findByEmail(email);
		  if(user == null)
			  throw new UsernameNotFoundException("This user does not exist");
		  if(!new BCryptPasswordEncoder().matches(password, user.getPassword())){
			  throw new BadCredentialsException("Invalid Credentials");
		  }
		  return user;
	  }
	  
	  public void saveAuthenticationToken(AuthenticationToken authenticationToken){	
		   authenticationDao.save(authenticationToken);
	  }
	  
	  
	  
	  public User getUserbyToken(String token){
		  log.debug("GET TOKEN...USER VERIFIED");
		  AuthenticationToken authToken = authenticationDao.findAuthenticationToken(token);
		  if(authToken == null)
			  throw new BadCredentialsException("Invalid token");
		  return authToken.getUser();
	  }
	  
	  public AuthenticationToken getAuthenticationTokenByUserId(String userId){
		  AuthenticationToken authenticationToken = authenticationDao.findByUserId(userId);
		  return authenticationToken;
	  }
	  
	  public boolean removeToken(AuthenticationToken entity){
		  log.debug("removing token");
		  WriteResult wr = authenticationDao.delete(entity);
		  if(wr != null){
			  SecurityContextHolder.clearContext(); // to remove current loggedin user from context
			  return true;
		  }
		  else
			  return false;
	  }
}
