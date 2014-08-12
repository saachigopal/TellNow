package com.tellnow.api.service;

import com.tellnow.api.domain.Device;

public interface DeviceService {

	Device save(Device device);

	void remove(String token);

	void remove(Device device);
}
