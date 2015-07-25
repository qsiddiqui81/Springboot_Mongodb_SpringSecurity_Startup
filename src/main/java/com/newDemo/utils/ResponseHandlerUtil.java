package com.newDemo.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.newDemo.constants.Constant;

public class ResponseHandlerUtil {

	public static Map<String, Object> generateResponse(String message,
			HttpStatus status, boolean isSuccess, Object responseObj) {
		Map<String, Object> map = new HashMap<String, Object>(5);
		try {
			map.put(Constant.MESSAGE, message);
			map.put(Constant.STATUS, status);
			map.put(Constant.IS_SUCCESS, isSuccess);
			map.put(Constant.DATA, responseObj);
			map.put(Constant.TIME_STAMP, new Date());
			return map;
		} catch (Exception e) {
			map.clear();
			map.put(message, e.getMessage());
			map.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			map.put("timestamp", new Date());
			return map;
		}
	}
}
