package com.newDemo.exceptionalHandler;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends RuntimeException implements CustomGenericException{
	private static final long serialVersionUID = -6502596312985405760L;
	 private final String errMsg;
	 private final HttpStatus status;
	 private final int errCode;
	 
	 public AuthenticationException(String errMsg,HttpStatus status, int errCode){
		 this.errMsg = errMsg;
		 this.status = status;
		 this.errCode = errCode;
	 }

	 @Override
		public HttpStatus getStatus() {
			// TODO Auto-generated method stub
			return status;
		}

		@Override
		public String getErrMsg() {
			// TODO Auto-generated method stub
			return errMsg;
		}

		@Override
		public int getErrCode() {
			// TODO Auto-generated method stub
			return errCode;
		}

}
