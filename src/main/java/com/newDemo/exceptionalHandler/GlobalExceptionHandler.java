package com.newDemo.exceptionalHandler;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newDemo.common.service.ResourceProperties;
import com.newDemo.constants.Constant;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	public static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	@Autowired ResourceProperties resourceProperties;
	private Properties configProp;
	
	/**
	 * load property file
	 * @throws IOException
	 */
	@PostConstruct
	public void init() throws IOException{
		configProp = resourceProperties.getResourceProperties(Constant.RESPONSE_MSG_FILE_PATH);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseBody
	public Map<String,Object> handleUserNotFoundException(HttpServletResponse resp,UserNotFoundException ex){
		return prepareExceptionInfo(resp,ex);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	@ResponseBody
	public Map<String,Object> handleAuthenticationException(HttpServletResponse resp,AuthenticationException ex){
		return prepareExceptionInfo(resp,ex);
	}
	
	private Map<String,Object> prepareExceptionInfo(HttpServletResponse resp, CustomGenericException ex){
		Map<String,Object> map = new HashMap<String,Object>(6);
		map.put("errCode", ex.getErrCode());
		map.put("errMsg", ex.getErrMsg());
		map.put("timestamp",new Date().getTime());
		map.put("status", ex.getStatus());
		map.put("statusCode", ex.getStatus().value());
		map.put("isSuccess","false");
		resp.setStatus(ex.getStatus().value());
		return map;
	}
 
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseBody
	public Map<String,Object> handleDataIntegrityViolationException(HttpServletResponse resp,DataIntegrityViolationException ex){
		Map<String,Object> map = new HashMap<String,Object>(4);
		map.put("localizedMsg", ex.getLocalizedMessage());
		map.put("errMsg", ex.getMessage());
		map.put("timestamp",new Date().getTime());
		map.put("status", HttpStatus.UNAUTHORIZED);
		map.put("statusCode", HttpStatus.UNAUTHORIZED.value());
		map.put("isSuccess","false");
		resp.setStatus(HttpStatus.UNAUTHORIZED.value());
		return map;
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Map<String, Object> handleAllException(HttpServletResponse resp, Exception ex) {
		Map<String, Object> map = new HashMap<String, Object>(6);
		map.put("msg", configProp.getProperty("main.exception.msg"));
		map.put("localizedMsg", ex.getLocalizedMessage());
		map.put("errMsg", ex.getMessage());
		map.put("timestamp",new Date().getTime());
		map.put("isSuccess", "false");
		resp.setStatus(500);
		ex.printStackTrace();
		return map;
	}
}
