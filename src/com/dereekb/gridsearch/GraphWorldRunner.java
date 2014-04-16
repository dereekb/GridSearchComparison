package com.dereekb.gridsearch;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dereekb.gridsearch.algorithms.components.AlgorithmHeuristic;
import com.dereekb.gridsearch.algorithms.components.SearchAlgorithm;
import com.dereekb.gridsearch.models.GraphWorld;
import com.dereekb.gridsearch.models.GraphWorldSerializer;
import com.dereekb.gridsearch.models.Position;
import com.dereekb.gridsearch.models.PositionExpander;
import com.dereekb.gridsearch.models.algorithm.GraphWorldAlgorithmDelegate;
import com.dereekb.gridsearch.models.algorithm.GraphWorldBenchmarker;
import com.dereekb.gridsearch.models.algorithm.GraphWorldNode;

/**
 * Runs the trials, and outputs information to the streams.
 * 
 * @author dereekb
 */
public class GraphWorldRunner {

	private GraphWorldSerializer serializer = new GraphWorldSerializer();
	private GraphWorldAlgorithmDelegate delegate = new GraphWorldAlgorithmDelegate();
	private AlgorithmHeuristic<GraphWorldNode, Double> heuristic;
	private List<GraphWorldRunnerPair> pairs = new ArrayList<GraphWorldRunnerPair>();

	private GraphWorld world;
	private Boolean randomizeStart = false;
	private Boolean randomizeEnd = false;

	private Boolean outputMaps = true;
	private Boolean outputText = true;

	private PrintStream outputStream = System.out;

	// CSV Stream by default
	private PrintStream minimalOutputStream = null;
	private String minimalOutputFormat = "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";

	private String outputPath = null;
	private String iterationsFormat = "%s-%d";
	private String outputPathFormat = "%s-%s-finished.%s";
	private String outputType = "png";

	/**
	 * Reads a world from the file.
	 */
	public void initializeWithFile(File file) {
		GraphWorld world = serializer.generateFromFile(file);
		this.setWorld(world);

		String filepath = file.getPath();
		String strippedPath = filepath.substring(0, filepath.length() - 4);
		this.outputPath = strippedPath;
	}

	public void run() {
		this.runPairs(null);
	}

	private void runPairs(Integer iteration) {
		this.prepareWorld();

		GraphWorldNode startingNode = new GraphWorldNode(world, world.getStartPosition());
		GraphWorldNode endingNode = new GraphWorldNode(world, world.getEndPosition());

		for (GraphWorldRunnerPair pair : pairs) {
			GraphWorldBenchmarker benchmark = pair.getBenchmarker();

			SearchAlgorithm<GraphWorldNode, Double> algorithm = pair.getAlgorithm();
			algorithm.search(startingNode, endingNode);

			String name = pair.getName();
			String runName = this.getNameForRun(name, iteration);
			this.printOutResults(benchmark, name, runName, iteration);
		}
	}

	private String getNameForRun(String name,
	                             Integer iteration) {

		String iterationName = name;

		if (iteration != null) {
			iterationName = String.format(iterationsFormat, name, iteration);
		}

		return iterationName;
	}

	public void run(Integer count) {

		if (this.minimalOutputStream != null) {
			this.printMinimalOutputHeader();
		}

		for (int i = 0; i < count; i += 1) {
			this.runPairs(i);
		}
	}

	private void printOutResults(GraphWorldBenchmarker benchmark,
	                             String algorithm,
	                             String name,
	                             Integer iteration) {

		if (outputMaps) {
			try {
				String path = String.format(outputPathFormat, outputPath, name, outputType);
				File outputFile = new File(path);
				this.serializer.writeBenchmarkToFile(world, benchmark, outputFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (outputText) {
			this.outputStream.println(benchmark.toString());
		}

		if (this.minimalOutputStream != null) {
			this.printMinimalOutput(benchmark, algorithm, outputPath, iteration);
		}
	}

	private void printMinimalOutputHeader() {
		String format = String.format(this.minimalOutputFormat, "iteration", "startX", "startY", "goalX", "goalY",
		        "algorithm", "heuristic", "map", "width", "height", "size", "density", "distance", "blocked",
		        "nodesExpanded", "nodesGenerated", "nodesVisited", "nodesExcluded", "jumpPoints", "jumpPointChecks",
		        "jumpCalls", "foundGoal", "steps", "time");
		this.minimalOutputStream.println(format);
	}

	private void printMinimalOutput(GraphWorldBenchmarker benchmark,
	                                String algorithm,
	                                String map,
	                                Integer iteration) {

		// Print start X,Y
		Position start = benchmark.getStart().getPosition();
		Integer startX = start.getxPos();
		Integer startY = start.getyPos();

		// Print end X,Y
		Position goal = benchmark.getGoal().getPosition();
		Integer goalX = goal.getxPos();
		Integer goalY = goal.getyPos();

		// Print Width/Height
		Integer width = world.getWidth();
		Integer height = world.getHeight();
		Integer size = width * height;

		// Print Blocked
		Integer blockedCount = world.getBlockedPositions().size();
		Double density = (1.0 * blockedCount / (width * height));

		// Print Herustic
		String herusticName = this.heuristic.getName();

		GraphWorldNode startNode = benchmark.getStart();
		GraphWorldNode goalNode = benchmark.getGoal();
		Double distance = this.heuristic.calculateHerusticValue(startNode, goalNode);

		// Print Nodes Expanded
		Integer expandedNodes = benchmark.getNodesExpanded();

		// Print Nodes Generated
		Integer generatedNodes = benchmark.getNodesGeneratedSet().size();

		// Print Nodes
		Integer visitedNodes = benchmark.getNodesVisited();

		// Print Nodes Excluded
		Integer excludedNodes = benchmark.getNodesExcludedSet().size();

		// JPS
		// Jump Points
		Integer jumpPoints = benchmark.getJumpPoints().size();

		// Jump Positions Checked
		Integer jumpPositions = benchmark.getJpsPositionsChecked().size();

		Integer jumpCalls = benchmark.getTotalJumpCalls();

		// Completed
		Boolean foundGoal = benchmark.foundGoal();

		// Final Path Steps
		List<Position> finalPath = benchmark.getFinalPath();
		Integer steps = (finalPath != null) ? finalPath.size() : null;
		String stepsString = (steps != null) ? steps.toString() : "n/a";

		// Running Time (ms)
		Double time = benchmark.totalTimeInMs();

		String output = String.format(this.minimalOutputFormat, iteration, startX, startY, goalX, goalY, algorithm,
		        herusticName, map, width, height, size, density, distance, blockedCount, expandedNodes, generatedNodes,
		        visitedNodes, excludedNodes, jumpPoints, jumpPositions, jumpCalls, foundGoal, stepsString, time);
		this.minimalOutputStream.println(output);
	}

	private void prepareWorld() {

		if (this.randomizeStart) {
			world.setStartPosition(null);

			Position start = world.getRandomOpenPosition();
			world.setStartPosition(start);
		}

		if (this.randomizeEnd) {
			world.setEndPosition(null);

			Position end = world.getRandomOpenPosition();
			world.setEndPosition(end);
		}
	}

	private static PositionExpander expanderForWorld(GraphWorld world) {
		PositionExpander expander = new PositionExpander();

		expander.setDelegate(world);
		expander.setMaxHeight(world.getHeight());
		expander.setMaxWidth(world.getWidth());

		return expander;
	}

	public GraphWorldSerializer getSerializer() {
		return serializer;
	}

	public void setSerializer(GraphWorldSerializer serializer) {
		this.serializer = serializer;
	}

	public GraphWorldAlgorithmDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(GraphWorldAlgorithmDelegate delegate) {
		this.delegate = delegate;
	}

	public GraphWorld getWorld() {
		return world;
	}

	public void setWorld(GraphWorld world) {
		this.world = world;

		PositionExpander expander = GraphWorldRunner.expanderForWorld(world);
		delegate.setDelegate(world);
		delegate.setExpander(expander);
	}

	public Boolean getOutputMaps() {
		return outputMaps;
	}

	public void setOutputMaps(Boolean outputMaps) {
		this.outputMaps = outputMaps;
	}

	public AlgorithmHeuristic<GraphWorldNode, Double> getHerustic() {
		return heuristic;
	}

	public void setHerustic(AlgorithmHeuristic<GraphWorldNode, Double> herustic) {
		this.heuristic = herustic;
	}

	public Boolean getRandomizeStart() {
		return randomizeStart;
	}

	public void setRandomizeStart(Boolean randomizeStart) {
		this.randomizeStart = randomizeStart;
	}

	public Boolean getRandomizeEnd() {
		return randomizeEnd;
	}

	public void setRandomizeEnd(Boolean randomizeEnd) {
		this.randomizeEnd = randomizeEnd;
	}

	public Boolean getOutputText() {
		return outputText;
	}

	public void setOutputText(Boolean outputText) {
		this.outputText = outputText;
	}

	public PrintStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(PrintStream outputStream) {
		this.outputStream = outputStream;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public String getOutputPathFormat() {
		return outputPathFormat;
	}

	public void setOutputPathFormat(String outputPathFormat) {
		this.outputPathFormat = outputPathFormat;
	}

	public String getOutputType() {
		return outputType;
	}

	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}

	public List<GraphWorldRunnerPair> getPairs() {
		return pairs;
	}

	public void setPairs(List<GraphWorldRunnerPair> pairs) {
		this.pairs = pairs;

		for (GraphWorldRunnerPair pair : pairs) {
			pair.setHerustic(this.heuristic);
		}
	}

	public PrintStream getMinimalOutputStream() {
		return minimalOutputStream;
	}

	public void setMinimalOutputStream(PrintStream minimalOutputStream) {
		this.minimalOutputStream = minimalOutputStream;
	}

	public String getIterationsFormat() {
		return iterationsFormat;
	}

	public void setIterationsFormat(String iterationsFormat) {
		this.iterationsFormat = iterationsFormat;
	}
}
