package com.newDemo.modules.user.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.newDemo.modules.user.domain.User;

public class UserValidator implements Validator{

	public static Logger log = LoggerFactory.getLogger(UserValidator.class);
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors err) {
		// TODO Auto-generated method stub
		log.debug("validating user");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "email", "email.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "username", "username.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "password", "password.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "firstName", "firstName.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "gender", "gender.empty");
	}

}
