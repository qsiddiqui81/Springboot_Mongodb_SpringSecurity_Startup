package com.newDemo.modules.user.springsecurity.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.newDemo.modules.user.domain.AuthenticationToken;
import com.newDemo.modules.user.domain.User;
import com.newDemo.modules.user.service.AuthenticationTokenService;
import com.newDemo.modules.user.springsecurity.AuthenticationWithToken;
import com.newDemo.modules.user.springsecurity.UserAuthentication;

public class AuthProvider implements AuthenticationProvider{
	
	public static Logger log = LoggerFactory.getLogger(AuthProvider.class);
	private AuthenticationTokenService authenticationTokenService;
	
	 public AuthProvider(AuthenticationTokenService authenticationTokenService) {
		 this.authenticationTokenService = authenticationTokenService;
	}
	
	 @Override
	    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		 	log.debug("authenticating user");
	        User user = authenticationTokenService.getAuthentication((String)authentication.getPrincipal(), (String) authentication.getCredentials());
	        UserAuthentication userAuthentication = new UserAuthentication(user);
	        AuthenticationWithToken resultOfAuthentication = new AuthenticationWithToken(userAuthentication.getDetails(), null, userAuthentication.getAuthorities());
	        String newToken = authenticationTokenService.generateNewToken();
	        AuthenticationToken authenticationToken = authenticationTokenService.getAuthenticationTokenByUserId(user.getId());
	        if(authenticationToken != null){
				authenticationToken.setToken(newToken);
				authenticationTokenService.saveAuthenticationToken(authenticationToken);
			}else{
				authenticationTokenService.saveAuthenticationToken(new AuthenticationToken(newToken, user));
			}
	        resultOfAuthentication.setToken(newToken);
	        return resultOfAuthentication;
	    }
	 
	 @Override
	    public boolean supports(Class<?> authentication) {
	        return authentication.equals(UsernamePasswordAuthenticationToken.class);
	    }

}
