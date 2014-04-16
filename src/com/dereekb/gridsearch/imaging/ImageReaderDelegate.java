package com.dereekb.gridsearch.imaging;

import java.awt.Color;

public interface ImageReaderDelegate {

	public void startedReading(Integer width,
	                           Integer height);

	public void readColor(Color color,
	                      Integer x,
	                      Integer y);

}
