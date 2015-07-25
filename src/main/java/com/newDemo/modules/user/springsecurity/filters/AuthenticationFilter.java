package com.newDemo.modules.user.springsecurity.filters;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

import com.newDemo.constants.Constant;

public class AuthenticationFilter extends GenericFilterBean {
	
	public static Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
	private AuthenticationManager authenticationManager;
	
	 public AuthenticationFilter(AuthenticationManager authenticationManager){
     	this.authenticationManager = authenticationManager;
     }
	 
	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = asHttp(request);
        HttpServletResponse httpResponse = asHttp(response);
        String token = httpRequest.getHeader("Authorization");
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        String line = null, username = null, password = null;
        String resourcePath = new UrlPathHelper().getPathWithinApplication(httpRequest);
        try {
            if (postToAuthenticate(httpRequest, resourcePath)) {
            	log.debug("authenticating username and password");
            	reader = request.getReader();
            	while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            	JSONObject json = new JSONObject(sb.toString());
    			username = (String) json.get("username");
    			password = (String) json.get("password");
                processUsernamePasswordAuthentication(httpResponse, username, password);
                reader.close();
                return;
            }
            if(token != null){
            	processTokenAuthentication(token);
            }
            chain.doFilter(request, response);
        } catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
            SecurityContextHolder.clearContext();
            logger.error("Internal authentication service exception", internalAuthenticationServiceException);
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
        } catch (org.apache.http.auth.AuthenticationException e) {
			// TODO Auto-generated catch block
        	((HttpServletResponse)response).setStatus(HttpStatus.UNAUTHORIZED.value());
        	logger.error("your account is not enabled please contact your admin");
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		} 
    }
	
	 private HttpServletRequest asHttp(ServletRequest request) {
	        return (HttpServletRequest) request;
	    }

	    private HttpServletResponse asHttp(ServletResponse response) {
	        return (HttpServletResponse) response;
	    }
	    
	    private boolean postToAuthenticate(HttpServletRequest httpRequest, String resourcePath) {
	        return Constant.AUTHENTICATE_URL.equalsIgnoreCase(resourcePath) && httpRequest.getMethod().equals("POST");
	    }

	    private void processUsernamePasswordAuthentication(HttpServletResponse httpResponse,String username, String password) throws IOException, org.apache.http.auth.AuthenticationException, JSONException {
	    	log.debug("processUsernamePasswordAuthentication");
	        Authentication resultOfAuthentication = tryToAuthenticateWithUsernameAndPassword(username, password);
	        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
	        JSONObject json = new JSONObject();
	        json.put("accessToken", resultOfAuthentication.getDetails().toString());
	        httpResponse.setStatus(HttpServletResponse.SC_OK);
	        httpResponse.addHeader("Content-Type", "application/json");
	        httpResponse.getWriter().write(json.toString());
	        httpResponse.getWriter().flush();
	        httpResponse.getWriter().close();
	    }

	    private Authentication tryToAuthenticateWithUsernameAndPassword(String username,String password) {
	    	log.debug("tryToAuthenticateWithUsernameAndPassword");
	        UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(username, password);
	        return tryToAuthenticate(requestAuthentication);
	    }
	    
	    private void processTokenAuthentication(String token) {
	    	log.debug("processTokenAuthentication");
	        Authentication resultOfAuthentication = tryToAuthenticateWithToken(token);
	        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
	    }
	    
	    private Authentication tryToAuthenticateWithToken(String token) {
	    	log.debug("tryToAuthenticateWithToken");
	        PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token, null);
	        return tryToAuthenticate(requestAuthentication);
	    }

	    private Authentication tryToAuthenticate(Authentication requestAuthentication) {
	    	log.debug("tryToAuthenticate");
	        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
	        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
	            throw new InternalAuthenticationServiceException("Unable to authenticate User for provided credentials");
	        }
	        log.debug("User successfully authenticated");
	        return responseAuthentication;
	    }
	    
}
