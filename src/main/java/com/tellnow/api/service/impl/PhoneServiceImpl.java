package com.tellnow.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tellnow.api.domain.Phone;
import com.tellnow.api.repository.PhoneRepository;
import com.tellnow.api.service.PhoneService;

@Service
public class PhoneServiceImpl implements PhoneService {

	@Autowired
	PhoneRepository phoneRepository;
	
	@Override
	public Phone save(Phone phone) {
		Phone phoneDB = phoneRepository.findByphoneNumber(phone.getPhoneNumber());
		if(phoneDB!=null){
			phoneDB.setProfile(phone.getProfile());
			phoneRepository.save(phoneDB);
			return phoneDB;
		} else {
			phoneRepository.save(phone);
			return phone;
		}
	}
}
