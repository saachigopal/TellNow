package com.tellnow.api.utils.thumbnails;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tellnow.api.security.AuthServiceImpl;

/**
 * Default impl for the Thumbnail generator engine
 */
public class ThumbnailGeneratorEngineImpl implements ThumbnailGeneratorEngine {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	AuthServiceImpl authService;
	
	private String generatedExtension;

	/**
	 * @param generatedExtension
	 *            The extension for the generated thumbnails
	 */
	public void setGeneratedExtension(String generatedExtension) {
		this.generatedExtension = generatedExtension;
	}

	public String getGeneratedExtension() {
		return generatedExtension;
	}

	private Map<String, ThumbnailGenerator> thumbnailGenerators;

	/**
	 * @param thumbnailGenerators
	 *            The thumbnail generators known by this engine mapped to a content type
	 */
	public void setThumbnailGenerators(Map<String, ThumbnailGenerator> thumbnailGenerators) {
		this.thumbnailGenerators = thumbnailGenerators;
	}

	private List<Integer> supportedSizes;

	/**
	 * @param supportedSizes
	 *            The suported sizes for the batch of generated thumbs
	 */
	public void setSupportedSizes(List<Integer> supportedSizes) {
		this.supportedSizes = supportedSizes;
	}

	private ThumbnailGenerator defaultThumbnailGenerator;

	/**
	 * @param defaultThumbnailGenerator
	 *            the default thumbnail generator to be used for unregistered mime types
	 */
	public void setDefaultThumbnailGenerator(ThumbnailGenerator defaultThumbnailGenerator) {
		this.defaultThumbnailGenerator = defaultThumbnailGenerator;
	}

	private String thumbnailsLocation;

	/**
	 * @param thumbnailsLocation
	 *            location for the generated thumbnails
	 */
	public void setThumbnailsLocation(String thumbnailsLocation) {
		this.thumbnailsLocation = thumbnailsLocation;
	}

	/**
	 * @param fileNamePrefix
	 *            the prefix for the generated thumbnails
	 * @param inputStream
	 *            the stream to generate thumbnails for
	 * @param contentType
	 *            the content type of this input stream for example image/jpeg
	 */
	public void generateThumbnails(String fileNamePrefix, InputStream inputStream, String contentType) {
		logger.info(fileNamePrefix + "-" + contentType);
		ThumbnailGenerator thumbnailGenerator = thumbnailGenerators.get(contentType);
		thumbnailGenerator = thumbnailGenerator != null ? thumbnailGenerator : defaultThumbnailGenerator;
		if (thumbnailGenerator != null) {
			Object hint = null;
			for (int dimension : supportedSizes) {
				
				File fileOut = new File(thumbnailsLocation.replaceAll("PROFILE_ID", authService.getUsername()), fileNamePrefix + "_" + dimension + generatedExtension);
				try {
					hint = thumbnailGenerator.createThumbnail(inputStream, fileOut, dimension, hint);
					logger.debug("Generated thumbnail for: " + inputStream + " in " + fileOut + " for type " + contentType);
				} catch (Exception e) {
					logger.error("Error generating thumbnail for: " + inputStream + " in " + fileOut + " for type " + contentType, e);
				}

			}
		} else {
			logger.warn("Thumbnail generator not found for content type: " + contentType + " and no default generator was provided");
		}
	}

	@Override
	public void generateThumbnails(String fileNamePrefix, File file, String contentType) {
		logger.info(fileNamePrefix + "-" + contentType);
		ThumbnailGenerator thumbnailGenerator = thumbnailGenerators.get(contentType);
		thumbnailGenerator = thumbnailGenerator != null ? thumbnailGenerator : defaultThumbnailGenerator;
		if (thumbnailGenerator != null) {
			Object hint = null;
			for (int dimension : supportedSizes) {
				
				File fileOut = new File(thumbnailsLocation.replaceAll("PROFILE_ID", authService.getUsername()), fileNamePrefix + "_" + dimension + generatedExtension);
				try {
					hint = thumbnailGenerator.createThumbnail(file, fileOut, dimension, hint);
					logger.debug("Generated thumbnail for: " + file + " in " + fileOut + " for type " + contentType);
				} catch (Exception e) {
					logger.error("Error generating thumbnail for: " + file + " in " + fileOut + " for type " + contentType, e);
				}

			}
		} else {
			logger.warn("Thumbnail generator not found for content type: " + contentType + " and no default generator was provided");
		}
	}
}
