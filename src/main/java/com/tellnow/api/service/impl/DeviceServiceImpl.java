package com.tellnow.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tellnow.api.domain.Device;
import com.tellnow.api.repository.DeviceRepository;
import com.tellnow.api.service.DeviceService;

@Service
public class DeviceServiceImpl implements DeviceService {

	@Autowired
	DeviceRepository deviceRepository;

	@Override
	public Device save(Device device) {
		Device deviceDB = deviceRepository.findByDeviceToken(device.getDeviceToken());
		if (deviceDB != null) {
			deviceDB.setProfile(device.getProfile());
			deviceRepository.save(deviceDB);
			return deviceDB;
		} else {
			deviceRepository.save(device);
			return device;
		}
	}

	@Override
	public void remove(Device device) {
		if (device != null) {
			deviceRepository.delete(device);
		}

	}

	@Override
	public void remove(String deviceToken) {
		// if (deviceToken!=null && deviceToken.length()!=71){
		//
		// }
		Device device = deviceRepository.findByDeviceToken(deviceToken);
		if (device == null) {
			String sanitazedID = insertCharacterForEveryNDistanceFromRight(8, deviceToken, ' ');
			device = deviceRepository.findByDeviceToken(sanitazedID);
		}		

		System.out.println("---device : " + deviceToken + " = " + device);
		if (device != null) {
			deviceRepository.delete(device);
		}

	}

	public static String insertCharacterForEveryNDistanceFromRight(int distance, String original, char c) {
		StringBuilder sb = new StringBuilder();
		char[] charArrayOfOriginal = original.toCharArray();
		for (int ch = charArrayOfOriginal.length; ch > 0; ch--) {
			if (ch % distance == 0 && ch != charArrayOfOriginal.length)
				sb.append(c).append(charArrayOfOriginal[charArrayOfOriginal.length - ch]);
			else sb.append(charArrayOfOriginal[charArrayOfOriginal.length - ch]);
		}
		return sb.toString();
	}
}
