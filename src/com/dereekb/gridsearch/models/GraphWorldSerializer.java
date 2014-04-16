package com.dereekb.gridsearch.models;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.dereekb.gridsearch.imaging.ImageReader;
import com.dereekb.gridsearch.imaging.ImageReaderDelegate;
import com.dereekb.gridsearch.imaging.ImageWriter;
import com.dereekb.gridsearch.models.algorithm.GraphWorldBenchmarker;
import com.dereekb.gridsearch.models.algorithm.GraphWorldNode;

public class GraphWorldSerializer {

	private enum GraphWorldSerializerType {
		START,
		END,
		BLOCK,
		NONE
	}

	private static final Color startColor = Color.GREEN;
	private static final Color endColor = Color.RED;
	private static final Color generatedColor = Color.MAGENTA;
	private static final Color visitedColor = Color.CYAN;
	private static final Color jumpPointColor = Color.ORANGE;
	private static final Color jumpCheckedColor = Color.LIGHT_GRAY;
	private static final Color blockColor = Color.BLACK;
	private static final Color pathColor = Color.BLUE;
	private static final Color noneColor = Color.WHITE;

	private static Map<Color, GraphWorldSerializerType> map;

	static {
		map = new HashMap<Color, GraphWorldSerializerType>();

		map.put(startColor, GraphWorldSerializerType.START);
		map.put(endColor, GraphWorldSerializerType.END);
		map.put(blockColor, GraphWorldSerializerType.BLOCK);
		map.put(noneColor, GraphWorldSerializerType.NONE);
	}

	private class GraphWorldReaderInstance
	        implements ImageReaderDelegate {

		Integer width;
		Integer height;

		Position start;
		Position end;
		Set<Position> blocked = new HashSet<Position>();

		@Override
		public void readColor(Color color,
		                      Integer x,
		                      Integer y) {

			GraphWorldSerializerType type = map.get(color);

			if (type == null) {
				type = GraphWorldSerializerType.BLOCK;
			}

			switch (type) {
				case END: {
					end = new Position(x, y);
				}
					break;
				case NONE: {

				}
					break;
				case START: {
					start = new Position(x, y);
				}
					break;
				case BLOCK:
				default:
					blocked.add(new Position(x, y));
					break;
			}
		}

		@Override
		public void startedReading(Integer width,
		                           Integer height) {
			this.width = width;
			this.height = height;
		}

		public GraphWorld generateWorld() {
			GraphWorld world = new GraphWorld(width, height);

			world.setStartPosition(start);
			world.setEndPosition(end);
			world.setImpassablePositions(blocked);

			return world;
		}
	}

	public GraphWorld generateFromFile(File file) {

		ImageReader reader = new ImageReader();
		GraphWorldReaderInstance instance = new GraphWorldReaderInstance();

		reader.setDelegate(instance);
		reader.setWatchColors(map.keySet());

		reader.scanFile(file);

		GraphWorld world = instance.generateWorld();
		return world;
	}

	private BufferedImage newBufferedImage(int width,
	                                       int height) {

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics2D graphics = image.createGraphics();

		graphics.setPaint(noneColor);
		graphics.fillRect(0, 0, width, height);

		return image;
	}

	private void drawPositionsInColor(Iterable<Position> path,
	                                  Integer pathColorRGB,
	                                  BufferedImage image) {
		for (Position pos : path) {
			image.setRGB(pos.getxPos(), pos.getyPos(), pathColorRGB);
		}
	}

	private void drawFilteredPositionsInColor(Iterable<Position> path,
	                                          Integer pathColorRGB,
	                                          BufferedImage image) {
		Integer width = image.getWidth();
		Integer height = image.getHeight();

		for (Position pos : path) {
			Integer x = pos.getxPos();
			Integer y = pos.getyPos();

			if ((x >= 0 && x < width && y >= 0 && y < height)) {
				image.setRGB(pos.getxPos(), pos.getyPos(), pathColorRGB);
			}
		}
	}

	private void drawWorldOnImage(GraphWorld world,
	                              BufferedImage image) {

		// Write the start/end
		Position startPosition = world.getStartPosition();
		Integer startRGB = startColor.getRGB();
		image.setRGB(startPosition.getxPos(), startPosition.getyPos(), startRGB);

		Position endPosition = world.getEndPosition();
		Integer endRGB = endColor.getRGB();
		image.setRGB(endPosition.getxPos(), endPosition.getyPos(), endRGB);

		// Write the blocked points
		Integer blockColorRGB = blockColor.getRGB();
		Iterable<Position> blockedPositions = world.getBlockedPositions();
		this.drawPositionsInColor(blockedPositions, blockColorRGB, image);
	}

	private void drawPathOnImage(Iterable<Position> path,
	                             BufferedImage image) {
		// Draw the path
		Integer pathColorRGB = pathColor.getRGB();
		this.drawPositionsInColor(path, pathColorRGB, image);
	}

	public BufferedImage createBufferedImageOfWorld(GraphWorld world) {

		BufferedImage image = this.newBufferedImage(world.getWidth(), world.getHeight());
		this.drawWorldOnImage(world, image);
		return image;
	}

	public BufferedImage createBufferedImageOfWorld(GraphWorld world,
	                                                Iterable<Position> path) {

		BufferedImage image = this.newBufferedImage(world.getWidth(), world.getHeight());
		this.drawPathOnImage(path, image);
		this.drawWorldOnImage(world, image);
		return image;
	}

	public BufferedImage createBufferedImageOfBenchmark(GraphWorld world,
	                                                    GraphWorldBenchmarker benchmark) {

		BufferedImage image = this.newBufferedImage(world.getWidth(), world.getHeight());

		Iterable<Position> jpsChecks = benchmark.getJpsPositionsChecked();
		Integer jumpCheckColorRGB = jumpCheckedColor.getRGB();
		this.drawFilteredPositionsInColor(jpsChecks, jumpCheckColorRGB, image);

		Iterable<Position> jumpPoints = benchmark.getJumpPoints();
		Integer jumpPointColorRGB = jumpPointColor.getRGB();
		this.drawPositionsInColor(jumpPoints, jumpPointColorRGB, image);

		Set<GraphWorldNode> generated = benchmark.getNodesGeneratedSet();
		Set<Position> generatedPositions = Position.getPositionsSet(generated);
		Integer generatedColorRGB = generatedColor.getRGB();
		this.drawPositionsInColor(generatedPositions, generatedColorRGB, image);

		Set<GraphWorldNode> visited = benchmark.getNodesVisitedSet();
		Set<Position> visitedPositions = Position.getPositionsSet(visited);
		Integer visitedColorRGB = visitedColor.getRGB();
		this.drawPositionsInColor(visitedPositions, visitedColorRGB, image);

		Iterable<Position> path = benchmark.getFinalPath();

		if (path != null) {
			this.drawPathOnImage(path, image);
		}

		this.drawWorldOnImage(world, image);

		return image;
	}

	public void writeToFile(BufferedImage image,
	                        File file) throws IOException {
		ImageWriter writer = new ImageWriter();
		writer.writeImageToFile(image, file);
	}

	public void writeBenchmarkToFile(GraphWorld world,
	                                 GraphWorldBenchmarker benchmark,
	                                 File file) throws IOException {

		BufferedImage image = this.createBufferedImageOfBenchmark(world, benchmark);
		this.writeToFile(image, file);
	}

}
