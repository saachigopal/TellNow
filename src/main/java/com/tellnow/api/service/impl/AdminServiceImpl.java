package com.tellnow.api.service.impl;

import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl {

	private static Integer userNumber;

	public static void userNumber(Integer userNumber) {
		AdminServiceImpl.userNumber = userNumber;
	}

	public static Integer getUserNumber() {
		return AdminServiceImpl.userNumber;
	}
}
