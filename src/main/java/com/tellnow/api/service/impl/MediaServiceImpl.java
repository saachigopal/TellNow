package com.tellnow.api.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tellnow.api.domain.MediaFile;
import com.tellnow.api.exceptions.NotAllowedMediaTypeException;
import com.tellnow.api.repository.MediaFileRepository;
import com.tellnow.api.security.AuthServiceImpl;
import com.tellnow.api.service.MediaService;
import com.tellnow.api.utils.TellNowUtils;

@Service
public class MediaServiceImpl implements MediaService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${file.allowed.content_type.images}")
	private String allowedImgTypes;

	@Value("${file.allowed.content_type.audio}")
	private String allowedAudioTypes;

	@Value("${file.allowed.content_type.video}")
	private String allowedVideoTypes;

	@Value("${media.dir}")
	private String imageDir;

	@Value("${media.file.url.prefix}")
	private String urlPrefix;

	@Autowired
	AuthServiceImpl authService;

	@Autowired
	MediaFileRepository mediaFileRepository;

	@Override
	public boolean isValidType(String contentType, TellnowMediaType type) {

		if (type == null) {
			type = TellnowMediaType.all;
		}
		List<MediaType> values = new ArrayList<MediaType>();
		switch (type) {
			case image:
				values = MediaType.parseMediaTypes(allowedImgTypes);
				break;
			case audio:
				values = MediaType.parseMediaTypes(allowedAudioTypes);
				break;
			case video:
				values = MediaType.parseMediaTypes(allowedVideoTypes);
				break;
			default:
				values = MediaType.parseMediaTypes(allowedImgTypes);
				values.addAll(MediaType.parseMediaTypes(allowedAudioTypes));
				values.addAll(MediaType.parseMediaTypes(allowedVideoTypes));
				break;

		}
		return values.contains(MediaType.parseMediaType(contentType));

	}

	@Override
	public MediaFile saveFile(String name, MultipartFile file, TellnowMediaType type) throws IOException, NotAllowedMediaTypeException {

		// System.out.println("MediaServiceImpl.saveFile()" + file.getContentType());
		if (!isValidType(file.getContentType(), type)) {
			throw new NotAllowedMediaTypeException("Not allowed media type "+ file.getContentType(), 0);
		}
		byte[] bytes = file.getBytes();
		String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(bytes);
		String parent = TellNowUtils.getImageFileDir(imageDir, authService.getLoggedInUser().getProfileId());
		File parentDir = new File(parent);
		if (!parentDir.exists()) {
			parentDir.mkdirs();
		}
		File fileToSave = new File(parentDir, name);
		BufferedOutputStream bufferedOutputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(fileToSave);
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			bufferedOutputStream.write(bytes);
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.close();
				fileOutputStream = null;
			}
			if (bufferedOutputStream != null) {
				bufferedOutputStream.close();
				bufferedOutputStream = null;
			}
		}
		
		MediaFile mFile = new MediaFile(name, authService.getLoggedInUser().getProfileId(), md5, fileToSave.getPath(), TellNowUtils.getUrl(urlPrefix, authService.getLoggedInUser().getProfileId(), name),  file.getContentType());
		return mediaFileRepository.save(mFile);

	}

	@Override
	public MediaFile getFile(String uid) {

		return mediaFileRepository.findByMd5Sum(uid);
	}

	@Override
	public Long getMediaFileId(String md5Sum) {
		return mediaFileRepository.getId(md5Sum);
	}

}
