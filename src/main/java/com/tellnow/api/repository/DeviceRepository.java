package com.tellnow.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellnow.api.domain.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {
	Device findByDeviceToken(String deviceToken);
}
