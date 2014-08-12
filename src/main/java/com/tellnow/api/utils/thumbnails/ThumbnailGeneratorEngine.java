package com.tellnow.api.utils.thumbnails;

import java.io.File;
import java.io.InputStream;

/**
 * An engine in charge of generating thumbnails for files
 */
public interface ThumbnailGeneratorEngine {

	void generateThumbnails(String fileNamePrefix, InputStream inputStream, String contentType);

	void generateThumbnails(String fileNamePrefix, File file, String contentType);
}
