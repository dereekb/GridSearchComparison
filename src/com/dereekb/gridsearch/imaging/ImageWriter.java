package com.dereekb.gridsearch.imaging;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageWriter {

	private String imageType = "png";

	public ImageWriter() {}

	public ImageWriter(String imageType) {
		this.imageType = imageType;
	}

	public void writeImageToFile(BufferedImage image,
	                             File file) throws IOException {
		ImageIO.write(image, imageType, file);
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

}
