package com.tellnow.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.tellnow.api.domain.VerificationSecurityCode;
import com.tellnow.api.repository.VerificationSecurityCodeRepository;

@Service
public class SecurityCodeValidator {

	@Autowired
	VerificationSecurityCodeRepository securityCodeRepository;

	private boolean expired;
	private boolean profileExists;

	public boolean validate(String phoneNumber, String code) {
		VerificationSecurityCode securityCode = securityCodeRepository.findByPhoneNumber(phoneNumber);
		if (securityCode == null) {
			this.profileExists = false;
			return false;
		} else {
			this.profileExists = true;
			if (securityCode.getSecurityCode().equalsIgnoreCase(DigestUtils.md5DigestAsHex(code.getBytes()))) {
				if (!securityCode.hasExpired()) {
					this.expired = false;
					return true;
				} else {
					this.expired = true;
					return false;
				}
			} else {
				this.expired = false;
				return false;
			}
		}
	}

	public boolean securityCodeExpired() {
		return this.expired;
	}

	public boolean phoneNumberExists() {
		return this.profileExists;
	}

}
