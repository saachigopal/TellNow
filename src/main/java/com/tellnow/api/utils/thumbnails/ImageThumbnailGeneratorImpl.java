package com.tellnow.api.utils.thumbnails;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generate thumbnails for images.
 */
public class ImageThumbnailGeneratorImpl extends AbstractThumbnailGenerator implements ThumbnailGenerator {

	Logger logger = LoggerFactory.getLogger(getClass());

	public Object createThumbnail(InputStream inputStream, File fileOut, int largestDimension, Object hint) throws IOException {

		// What's the base image that we are starting with? If there's a hint, that's the scaled image
		// from the last time around, use that... (since we know we always iterate downwards in scale)
		BufferedImage imageIn;
		if (hint instanceof Image) {
			imageIn = (BufferedImage) hint;
			logger.info("createThumbnail(" + fileOut + ") reusing prior result image...");
		} else {
			logger.info("createThumbnail(" + fileOut + ") reading image from stream " + inputStream);
			imageIn = ImageIO.read(inputStream);
			inputStream.close();
		}

		if (imageIn == null) {
			logger.warn("Could not read image file: " + inputStream);
			return hint;
		}

		BufferedImage imageOut = createThumbnailImage(imageIn, fileOut, largestDimension);

		// Return this image now as the hint for the next scaling iteration
		if (imageOut != null)
			hint = imageOut;

		return hint;
	}

	/**
	 * Create a thumbnail image and save it to disk.
	 * 
	 * This algorithm is based on: http://www.philreeve.com/java_high_quality_thumbnails.php
	 * 
	 * @param image
	 *            The image you want to scale.
	 * @param fileOut
	 *            The output file.
	 * @param largestDimension
	 *            The largest dimension, so that neither the width nor height will exceed this value.
	 * 
	 * @return the image that was created, null if imageIn or fileOut is null.
	 * @throws java.io.IOException
	 *             if something goes wrong when saving as jpeg
	 */
	public BufferedImage createThumbnailImage(BufferedImage image, File fileOut, int largestDimension) throws IOException {
		if ((image == null) || (fileOut == null))
			return null;

		BufferedImage thumbnail = Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, largestDimension, largestDimension, Scalr.OP_ANTIALIAS);

		// JPEG-encode the image and write to file.
		saveImageAsJPEG(thumbnail, fileOut);

		return thumbnail;
	}

	@Override
	public Object createThumbnail(File file, File fileOut, int largestDimension, Object hint) {
		// What's the base image that we are starting with? If there's a hint, that's the scaled image
		// from the last time around, use that... (since we know we always iterate downwards in scale)
		BufferedImage imageIn = null;
		if (hint instanceof Image) {
			imageIn = (BufferedImage) hint;
			logger.info("createThumbnail(" + fileOut + ") reusing prior result image...");
		} else {
			logger.info("createThumbnail(" + fileOut + ") reading image from file " + file);
			try {
				imageIn = ImageIO.read(file);
			} catch (IOException e) {
				imageIn = null;
				logger.error("createThumbnail IOException in ", e);
			}
		}

		if (imageIn == null) {
			logger.warn("Could not read image file: " + file);
			return hint;
		}

		BufferedImage imageOut = null;
		try {
			imageOut = createThumbnailImage(imageIn, fileOut, largestDimension);
		} catch (IOException e) {
			logger.error("createThumbnail IOException out", e);
		}

		// Return this image now as the hint for the next scaling iteration
		if (imageOut != null)
			hint = imageOut;

		return hint;

	}

}
