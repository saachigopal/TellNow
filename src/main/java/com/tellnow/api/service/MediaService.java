package com.tellnow.api.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

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
	
	MediaFile getFile(String path, String md5sum);

	Long getMediaFileId(String md5Sum);
	
	Set<MediaFile> findOrphaned();
	
	int delete(MediaFile mediaFile);
	
	int delete(Collection<MediaFile> mediaFiles);
	
	int deleteOrphaned();
}
