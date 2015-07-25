package com.newDemo.exceptionalHandler;

import org.springframework.http.HttpStatus;

public interface CustomGenericException  {
	 
	public HttpStatus getStatus();
	public String getErrMsg();
	public String getMessage();
	public int getErrCode();
	
}