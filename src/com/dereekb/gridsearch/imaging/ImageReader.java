package com.dereekb.gridsearch.imaging;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

public class ImageReader {

	private Set<Integer> watchColors = new HashSet<Integer>();
	private ImageReaderDelegate delegate;

	public ImageReader() {}

	public void scanFile(File file) {

		BufferedImage img = null;
		try {
			img = ImageIO.read(file);

			Integer width = img.getWidth();
			Integer height = img.getHeight();

			this.delegate.startedReading(width, height);

			for (int x = 0; x < width; x += 1) {
				for (int y = 0; y < height; y += 1) {

					Integer value = img.getRGB(x, y);

					Color color = new Color(value);
					this.delegate.readColor(color, x, y);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not read file.");
		}

	}

	public Set<Integer> getWatchColors() {
		return watchColors;
	}

	public void setWatchColors(Iterable<Color> colors) {
		this.watchColors = new HashSet<Integer>();

		for (Color color : colors) {
			this.watchColors.add(color.getRGB());
		}
	}

	public ImageReaderDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(ImageReaderDelegate delegate) {
		this.delegate = delegate;
	}

}
