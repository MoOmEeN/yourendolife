package com.moomeen.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.FileResource;
import com.vaadin.ui.Image;

public class ImageResourceLoader {
	
	private static Logger LOG = LoggerFactory.getLogger(ImageResourceLoader.class);

	private static File streamTwoFile(InputStream in, String filename) throws IOException {
		final File tempFile = File.createTempFile(filename, "." + "endo");
		tempFile.deleteOnExit();
		try (FileOutputStream out = new FileOutputStream(tempFile)) {
			IOUtils.copy(in, out);
		}
		return tempFile;
	}
	
	public static Image fromResourceImage(String fileName){
		Image image;
		try {
			image = new Image(
			        null, new FileResource(streamTwoFile(ImageResourceLoader.class.getClassLoader().getResourceAsStream("VAADIN/themes/mytheme/img/" + fileName), fileName)));
		} catch (IOException e) {
			LOG.error("Something went wrong while trying to load image: " + fileName, e);
			throw new RuntimeException(e);
		}
		return image;
	}
	

}
