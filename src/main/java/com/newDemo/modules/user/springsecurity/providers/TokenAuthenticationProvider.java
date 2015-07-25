package com.newDemo.modules.user.springsecurity.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.newDemo.modules.user.service.AuthenticationTokenService;
import com.newDemo.modules.user.springsecurity.UserAuthentication;

public class TokenAuthenticationProvider implements AuthenticationProvider {
	
	public static Logger log = LoggerFactory.getLogger(TokenAuthenticationProvider.class);
	private AuthenticationTokenService authenticationTokenService;

    public TokenAuthenticationProvider(AuthenticationTokenService authenticationTokenService) {
        this.authenticationTokenService = authenticationTokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	log.debug("authenticating token");
        return new UserAuthentication(authenticationTokenService.getUserbyToken((String)authentication.getPrincipal()));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}
