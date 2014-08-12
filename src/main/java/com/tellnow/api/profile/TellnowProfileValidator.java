package com.tellnow.api.profile;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tellnow.api.domain.TellnowProfile;

public class TellnowProfileValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		return TellnowProfile.class.equals(arg0);
	}

	@Override
	public void validate(Object target, Errors errors) {
		TellnowProfile profile = (TellnowProfile) target;

		if (profile.getFirstname() == null) {
			errors.rejectValue("name", "error code");
		}
	}

}
