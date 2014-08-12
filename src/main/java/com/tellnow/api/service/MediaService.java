package com.tellnow.api.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.tellnow.api.domain.MediaFile;
import com.tellnow.api.exceptions.NotAllowedMediaTypeException;

public interface MediaService {

	public enum TellnowMediaType {
		image, audio, video, all
	}

	boolean isValidType(String contentType, TellnowMediaType type);

	MediaFile saveFile(String name, MultipartFile file, TellnowMediaType type) throws IOException, NotAllowedMediaTypeException;

	MediaFile getFile(String path);

	Long getMediaFileId(String md5Sum);
}
