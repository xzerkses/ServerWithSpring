package com.example.demo.validator;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@Component
public class UserFormValidator implements Validator {
	
	@Autowired
	private UserService userService;
	
	private EmailValidator emailValidator = EmailValidator.getInstance();

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class==clazz;
	
	}

	@Override
	public void validate(Object targetObj, Errors errors) {
		User userForm =(User)targetObj;
		System.out.println("validating");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty.userForm.username");
		if(userForm.getUsername().length()<4 ||userForm.getUsername().length()>36) {
			errors.rejectValue("username", "Size.userForm.username");
		}
		if(userService.findByUsername(userForm.getUsername())!=null) {
			errors.rejectValue("username", "Dublicate.userForm.username");
		}
		//check email validity
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.userForm.email");
		if(!this.emailValidator.isValid(userForm.getEmail())) {
			errors.rejectValue("email", "Pattern.userForm.email");
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.userForm.password");
		if(userForm.getPassword().length()<8 ||userForm.getPassword().length()>36) {
			errors.rejectValue("username", "Size.userForm.password");
		}
		if(!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
			errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
		}

	}

}
