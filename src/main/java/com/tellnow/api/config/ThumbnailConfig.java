package com.tellnow.api.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.tellnow.api.utils.thumbnails.ImageThumbnailGeneratorImpl;
import com.tellnow.api.utils.thumbnails.ThumbnailGenerator;
import com.tellnow.api.utils.thumbnails.ThumbnailGeneratorEngine;
import com.tellnow.api.utils.thumbnails.ThumbnailGeneratorEngineImpl;
import com.tellnow.api.utils.thumbnails.VideoThumbnailGeneratorImpl;

@Configuration
public class ThumbnailConfig {

	@Autowired
	Environment env;

	@Bean
	public ThumbnailGeneratorEngine thumbnailGeneratorEngine() {

		ThumbnailGeneratorEngineImpl thumbnailGeneratorEngine = new ThumbnailGeneratorEngineImpl();

		// setup supported sizes
		String value = env.getProperty("thumbnails.sizes");
		String[] sizes = value.split("\\s*,\\s*");
		List<Integer> supportedSizes = new ArrayList<Integer>(sizes.length);
		for (int i = 0; i < sizes.length; i++) {
			supportedSizes.add(Integer.valueOf(sizes[i].trim()));
		}
		thumbnailGeneratorEngine.setSupportedSizes(supportedSizes);
		thumbnailGeneratorEngine.setDefaultThumbnailGenerator(imageThumbnailGenerator());
		thumbnailGeneratorEngine.setThumbnailsLocation(env.getProperty("thumbnails.location"));
		thumbnailGeneratorEngine.setGeneratedExtension(env.getProperty("thumbnails.generated.extension"));
		Map<String, ThumbnailGenerator> thumbnailGenerators = new HashMap<String, ThumbnailGenerator>();
		thumbnailGenerators.put("image/jpg", imageThumbnailGenerator());
		thumbnailGenerators.put("image/png", imageThumbnailGenerator());
		thumbnailGenerators.put("image/gif", imageThumbnailGenerator());

		thumbnailGenerators.put("video/mpeg", videoThumbnailGenerator());
		thumbnailGenerators.put("video/quicktime", videoThumbnailGenerator());
		
		
		thumbnailGeneratorEngine.setThumbnailGenerators(thumbnailGenerators);
		return thumbnailGeneratorEngine;
	}

	@Bean
	public ThumbnailGenerator imageThumbnailGenerator() {
		return new ImageThumbnailGeneratorImpl();
	}

	@Bean
	public ThumbnailGenerator videoThumbnailGenerator() {
		return new VideoThumbnailGeneratorImpl();
	}

}
