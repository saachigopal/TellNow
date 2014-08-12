package com.tellnow.api.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tellnow.api.domain.MediaFile;
import com.tellnow.api.service.impl.MediaServiceImpl;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Controller
@Api(value = "File API", description = "File controller")
public class FileController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	MediaServiceImpl mediaService;

	@Value("${media.dir}")
	private String mediaDir;

	@RequestMapping(value = "/data/image/{uid}", method = RequestMethod.GET)
	@ApiOperation(value="get image file", notes="host:port/data/image/{md5}")
	HttpEntity<byte[]> getFile(@PathVariable("uid") String uid) {

		MediaFile mediaFile = mediaService.getFile(uid);

		if (null != mediaFile) {

			// send it back to the client
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.parseMediaType(mediaFile.getContentType()));
			try {
				return new ResponseEntity<byte[]>(getFileContent(mediaFile), httpHeaders, HttpStatus.OK);
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage());//logger.error(e.getMessage(), e);
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			} catch (IOException e) {
				logger.error(e.getMessage());//logger.error(e.getMessage(), e);
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			}
		}

		return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/data/images/{user}/{file:.*}", method = RequestMethod.GET)
	@ApiOperation(value="get image file by url", notes="host:port/data/media/{user public id}/{file:.*} -uploaded name and extension")
	HttpEntity<byte[]> getFileByPath(@PathVariable("user") String user, @PathVariable("file") String file) {

		if (null != user && null != file) {

			// send it back to the client
			// HttpHeaders httpHeaders = new HttpHeaders();
			// httpHeaders.setContentType(MediaType.parseMediaType(mediaFile.getContentType()));
			try {
				return new ResponseEntity<byte[]>(getFileContent(mediaDir.concat("/").concat(user).concat("/").concat(file)), HttpStatus.OK);
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage());//logger.error(e.getMessage(), e);
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			} catch (IOException e) {
				logger.error(e.getMessage());//logger.error(e.getMessage(), e);
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			}
		}

		return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/data/media/{user}/{file:.*}", method = RequestMethod.GET)
	@ApiOperation(value="get media file by url", notes="host:port/data/media/{user public id}/{file:.*} -uploaded name and extension")
	HttpEntity<byte[]> getMediaFileByPath(@PathVariable("user") String user, @PathVariable("file") String file) {

		if (null != user && null != file) {

			// send it back to the client
			// HttpHeaders httpHeaders = new HttpHeaders();
			// httpHeaders.setContentType(MediaType.parseMediaType(mediaFile.getContentType()));
			try {
				return new ResponseEntity<byte[]>(getFileContent(mediaDir.concat("/").concat(user).concat("/").concat(file)), HttpStatus.OK);
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage());//logger.error(e.getMessage(), e);
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			} catch (IOException e) {
				logger.error(e.getMessage());//logger.error(e.getMessage(), e);
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			}
		}

		return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/data/media/{user}/thumbnails/{file:.*}", method = RequestMethod.GET)
	@ApiOperation(value="get media file thumbnail by url", notes="host:port/data/media/{user public id}/thumbnails/{file:.*} -<b>[uploaded file name]_[size(256/128/64)].jpg</b> -extension and supported size are configurable")
	HttpEntity<byte[]> getThumbnails(@PathVariable("user") String user, @PathVariable("file") String file) {

		if (null != user && null != file) {

			// send it back to the client
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.IMAGE_JPEG);
			try {
				return new ResponseEntity<byte[]>(getFileContent(mediaDir.concat("/").concat(user).concat("/thumbnails/").concat(file)), httpHeaders, HttpStatus.OK);
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage());//logger.error(e.getMessage(), e);
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			} catch (IOException e) {
				logger.error(e.getMessage());//logger.error(e.getMessage(), e);
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			}
		}

		return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
	}

	public byte[] getFileContent(MediaFile mfile) throws FileNotFoundException, IOException {

		File file = new File(mfile.getPath());

		return IOUtils.toByteArray(new FileInputStream(file));
	}

	public byte[] getFileContent(String path) throws FileNotFoundException, IOException {

		File file = new File(path);

		return IOUtils.toByteArray(new FileInputStream(file));
	}
}