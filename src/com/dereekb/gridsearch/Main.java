package com.dereekb.gridsearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.dereekb.gridsearch.algorithms.Astar;
import com.dereekb.gridsearch.algorithms.components.AlgorithmHeuristic;
import com.dereekb.gridsearch.algorithms.components.SearchAlgorithm;
import com.dereekb.gridsearch.algorithms.components.herustics.ManhattanDistanceHerustic;
import com.dereekb.gridsearch.algorithms.graph.GraphAlgorithmDistance;
import com.dereekb.gridsearch.algorithms.jps.JumpPointSearch;
import com.dereekb.gridsearch.models.Position;
import com.dereekb.gridsearch.models.algorithm.GraphWorldAlgorithmDelegate;
import com.dereekb.gridsearch.models.algorithm.GraphWorldBenchmarker;
import com.dereekb.gridsearch.models.algorithm.GraphWorldNode;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		// Read in the image
		String filepath = args[0];
		File file = new File(filepath);

		File csvOutputFile = new File(filepath + ".csv");
		PrintStream csvStream = new PrintStream(csvOutputFile);

		// Set the random position seed. Used for randomizing start/end.
		Integer seed = "Random".hashCode();
		Random random = new Random(seed);
		Position.setRandom(random);

		// Create a runner.
		GraphWorldRunner runner = new GraphWorldRunner();
		runner.initializeWithFile(file);

		AlgorithmHeuristic<GraphWorldNode, Double> herustic = null;

		herustic = new ManhattanDistanceHerustic<GraphWorldNode>();
		// herustic = new EuclidianDistanceHeuristic<GraphWorldNode>();
		// herustic = new AxisDistanceHeuristic<GraphWorldNode>();
		runner.setHerustic(herustic);

		// Amount of times to perform trials
		Integer iterations = 40;

		// Whether or not to output map results in png files.
		runner.setOutputMaps(true);

		runner.setRandomizeStart(true);
		runner.setRandomizeEnd(true);

		List<GraphWorldRunnerPair> pairs = new ArrayList<GraphWorldRunnerPair>();

		pairs.add(makeAstarPair(runner));
		pairs.add(makeJPSPair(runner));
		runner.setPairs(pairs);
		runner.setMinimalOutputStream(csvStream);

		runner.run(iterations);
	}

	private static GraphWorldRunnerPair makeAstarPair(GraphWorldRunner runner) {
		GraphWorldAlgorithmDelegate delegate = runner.getDelegate();
		GraphWorldBenchmarker benchmarker = new GraphWorldBenchmarker();
		GraphAlgorithmDistance<GraphWorldNode> distanceFinder = new GraphAlgorithmDistance<GraphWorldNode>();
		SearchAlgorithm<GraphWorldNode, Double> algorithm = new Astar<GraphWorldNode>(benchmarker, delegate, delegate,
		        delegate, distanceFinder, null);

		GraphWorldRunnerPair pair = new GraphWorldRunnerPair();
		pair.setAlgorithm(algorithm);
		pair.setBenchmarker(benchmarker);
		pair.setName("ASTAR");

		return pair;
	}

	private static GraphWorldRunnerPair makeJPSPair(GraphWorldRunner runner) {
		GraphWorldAlgorithmDelegate delegate = runner.getDelegate();
		GraphWorldBenchmarker benchmarker = new GraphWorldBenchmarker();
		GraphAlgorithmDistance<GraphWorldNode> distanceFinder = new GraphAlgorithmDistance<GraphWorldNode>();
		SearchAlgorithm<GraphWorldNode, Double> algorithm = new JumpPointSearch<GraphWorldNode>(benchmarker, delegate,
		        delegate, delegate, null, distanceFinder, delegate);

		GraphWorldRunnerPair pair = new GraphWorldRunnerPair();
		pair.setAlgorithm(algorithm);
		pair.setBenchmarker(benchmarker);
		pair.setName("JPS");

		return pair;
	}

}
