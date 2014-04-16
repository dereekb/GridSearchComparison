package com.dereekb.gridsearch;

import com.dereekb.gridsearch.algorithms.components.AlgorithmHeuristic;
import com.dereekb.gridsearch.algorithms.components.SearchAlgorithm;
import com.dereekb.gridsearch.models.algorithm.GraphWorldBenchmarker;
import com.dereekb.gridsearch.models.algorithm.GraphWorldNode;

public class GraphWorldRunnerPair {

	private String name;
	private GraphWorldBenchmarker benchmarker;
	private SearchAlgorithm<GraphWorldNode, Double> algorithm;

	public SearchAlgorithm<GraphWorldNode, Double> getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(SearchAlgorithm<GraphWorldNode, Double> algorithm) {
		this.algorithm = algorithm;
	}

	public void setHerustic(AlgorithmHeuristic<GraphWorldNode, Double> herustic) {
		this.algorithm.setHerustic(herustic);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GraphWorldBenchmarker getBenchmarker() {
		return benchmarker;
	}

	public void setBenchmarker(GraphWorldBenchmarker benchmarker) {
		this.benchmarker = benchmarker;
	}
}
