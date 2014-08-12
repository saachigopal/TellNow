package com.tellnow.api.utils.thumbnails;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

/**
 * Class providing convenience method for abstract thumbnail generators
 */
public class AbstractThumbnailGenerator {

	/**
	 * Save an image as a JPEG file on disk.
	 * 
	 * @param image
	 *            The raw image to save.
	 * @param fileOut
	 *            The location where you want to save the file.
	 * 
	 * @return true if successful, false if unsuccessful.
	 * @throws java.io.IOException
	 *             if something goes wrong closing the stream
	 */
	public boolean saveImageAsJPEG(BufferedImage image, File fileOut) throws IOException {
		OutputStream streamOut = null;
		boolean bSuccess = false;
		try {
			fileOut.mkdirs();
			bSuccess = ImageIO.write(image, "jpeg", fileOut);
		} finally {
			if (streamOut != null) {
				streamOut.close();
			}
		}
		return bSuccess;
	}

}
