package com.tellnow.api.utils.thumbnails;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;

/**
 * Generate thumbnails for video.
 */
public class VideoThumbnailGeneratorImpl extends AbstractThumbnailGenerator implements ThumbnailGenerator {

	Logger logger = LoggerFactory.getLogger(getClass());

	private int largestDimension;

	private File fileOut;

	public Object createThumbnail(InputStream inputStream, File fileOut, int largestDimension, Object hint) throws IOException {
		this.largestDimension = largestDimension;
		this.fileOut = fileOut;
		if (hint instanceof Image) {
			BufferedImage image = (BufferedImage) hint;
			logger.info("createThumbnail(" + fileOut + ") reusing prior result image...");
			BufferedImage imageOut = createThumbnailImage(image, fileOut, largestDimension);

			// Return this image now as the hint for the next scaling iteration
			if (imageOut != null)
				hint = imageOut;

			return hint;
		} else {

			if (inputStream == null) {
				logger.warn("Could not read image file: " + inputStream);
				return hint;
			}
			IContainer iContainer = IContainer.make();
			ImageSnapListener imageSnapListener = new ImageSnapListener();
			BufferedImage thumbnail = null;
			if (iContainer.open(new DataInputStream(inputStream), IContainerFormat.make(), true, true) >= 0) {

				// iContainer.setInputBufferLength(inputStream.available());
				// if (iContainer.open("d:/var/tellnow/images/7a995ca0c5657c2b917c61702e326c10/testing.mov ", IContainer.Type.READ, null) >0){
				IMediaReader mediaReader = ToolFactory.makeReader(iContainer);

				// we want BufferedImages created in BGR 24bit color space
				mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);

				mediaReader.addListener(imageSnapListener);

				// read out the contents of the media file and
				// dispatch events to the attached listener

				while (mediaReader.readPacket() == null && !imageSnapListener.isSnapshotDone());
				try {
					thumbnail = createThumbnailImage(imageSnapListener.getImage(), fileOut, largestDimension);
				} catch (IOException e) {
					logger.error("", e);
					thumbnail = null;
				}
			}

			return thumbnail;
		}
	}

	@Override
	public Object createThumbnail(File file, File fileOut, int largestDimension, Object hint) {

		this.largestDimension = largestDimension;
		this.fileOut = fileOut;
		BufferedImage thumbnail = null;

		if (hint instanceof Image) {
			BufferedImage image = (BufferedImage) hint;
			logger.info("createThumbnail(" + fileOut + ") reusing prior result image...");
			BufferedImage imageOut = null;
			try {
				imageOut = createThumbnailImage(image, fileOut, largestDimension);
			} catch (IOException e) {
				logger.warn("createThumbnail IOException imageOut: " + file);
			}

			// Return this image now as the hint for the next scaling iteration
			if (imageOut != null)
				hint = imageOut;

			return hint;
		} else {

			if (file == null) {
				logger.warn("Could not read image file: " + file);
				return hint;
			}
			IMediaReader mediaReader = null;
			ImageSnapListener imageSnapListener = new ImageSnapListener();
			IContainer iContainer = IContainer.make();

			if (iContainer.open(file.getAbsolutePath(), IContainer.Type.READ, IContainerFormat.make()) >= 0) {

				mediaReader = ToolFactory.makeReader(iContainer);

				// we want BufferedImages created in BGR 24bit color space
				mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);

				mediaReader.addListener(imageSnapListener);

				// read out the contents of the media file and
				// dispatch events to the attached listener

				while (mediaReader.readPacket() == null && !imageSnapListener.isSnapshotDone());
				try {
					thumbnail = createThumbnailImage(imageSnapListener.getImage(), fileOut, largestDimension);
				} catch (IOException e) {
					logger.error("", e);
					thumbnail = null;
				}
				mediaReader.removeListener(imageSnapListener);
			}

			if (iContainer != null) {
				iContainer.close();
				iContainer = null;
			}

			return thumbnail;
		}
	}

	/**
	 * Create a thumbnail image and save it to disk.
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

	/**
	 * Create a thumbnail image and save it to disk.
	 * 
	 * This algorithm is based on: http://www.philreeve.com/java_high_quality_thumbnails.php
	 * 
	 * @param seconds
	 * 
	 * @param image
	 *            The image you want to scale.
	 * @return the image that was created, null if imageIn or fileOut is null.
	 * @throws java.io.IOException
	 *             if something goes wrong when saving as jpeg
	 */
	public BufferedImage createThumbnailImage(BufferedImage image) throws IOException {

		BufferedImage thumbnail = Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, largestDimension, largestDimension, Scalr.OP_ANTIALIAS);
		// JPEG-encode the image and write to file.
		saveImageAsJPEG(thumbnail, fileOut);

		return image;
	}

	private class ImageSnapListener extends MediaListenerAdapter {
		public final double SECONDS_BETWEEN_FRAMES = 5;

		// The video stream index, used to ensure we display frames from one and
		// only one video stream from the media container.
		private int mVideoStreamIndex = -1;
		// Time of last frame write
		private long mLastPtsWrite = Global.NO_PTS;
		public final long MICRO_SECONDS_BETWEEN_FRAMES = (long) (Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);
		BufferedImage image;

		public void onVideoPicture(IVideoPictureEvent event) {
			
			if (event.getStreamIndex() != mVideoStreamIndex) {
				// if the selected video stream id is not yet set, go ahead an
				// select this lucky video stream
				if (mVideoStreamIndex == -1) {
					mVideoStreamIndex = event.getStreamIndex();
				} // no need to show frames from this video stream
				else {
					return;
				}
			}

			// if uninitialized, back date mLastPtsWrite to get the very first frame
			if (mLastPtsWrite == Global.NO_PTS) {
				mLastPtsWrite = event.getTimeStamp() - MICRO_SECONDS_BETWEEN_FRAMES;
			}

			// if it's time to write the next frame
			if (event.getTimeStamp() - mLastPtsWrite >= MICRO_SECONDS_BETWEEN_FRAMES) {

				// indicate file written
				// double seconds = ((double) event.getTimeStamp()) / Global.DEFAULT_PTS_PER_SECOND;

				image = event.getImage();

				// update last write time
				mLastPtsWrite += MICRO_SECONDS_BETWEEN_FRAMES;
			}

		}

		public boolean isSnapshotDone() {
			return image != null;
		}

		public BufferedImage getImage() {
			return image;
		}
	}
}
