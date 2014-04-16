package com.dereekb.gridsearch.algorithms.benchmarks;

import java.util.Collection;

public class DefaultAlgorithmBenchmarker<T>
        implements AlgorithmBenchmarker<T> {

	private boolean goalFound = false;
	private Integer nodesVisited = 0;
	private Integer nodesExpanded = 0;
	private Integer nodesGenerated = 0;
	private Integer nodesExcluded = 0;

	private Long startTime;
	private Long endTime;

	public boolean isGoalFound() {
		return goalFound;
	}

	public void setGoalFound(boolean goalFound) {
		this.goalFound = goalFound;
	}

	public Integer getNodesVisited() {
		return nodesVisited;
	}

	public void setNodesVisited(Integer nodesVisited) {
		this.nodesVisited = nodesVisited;
	}

	public Integer getNodesExpanded() {
		return nodesExpanded;
	}

	public void setNodesExpanded(Integer nodesExpanded) {
		this.nodesExpanded = nodesExpanded;
	}

	@Override
	public void checkedNode(T node) {
		this.nodesVisited += 1;
	}

	@Override
	public void expandedNode(T node,
	                         Collection<T> children) {
		this.nodesExpanded += 1;
		this.nodesGenerated += children.size();
	}

	@Override
	public void deprecatedNode(T node) {
		this.nodesExcluded += 1;
	}

	@Override
	public void reset() {
		this.goalFound = false;
		this.nodesVisited = 0;
		this.nodesExpanded = 0;
		this.nodesGenerated = 0;
		this.nodesExcluded = 0;
		this.startTime = null;
		this.endTime = null;
	}

	public Double totalTimeInMs() {
		Double time = null;

		if (this.endTime != null) {
			time = new Double(this.endTime - this.startTime);
		}

		return time / 1000000;
	}

	@Override
	public void startedAlgorithm(T start,
	                             T goal) {
		this.reset();
		this.startTime = System.nanoTime();
	}

	@Override
	public void foundGoal(T node) {
		this.endTime = System.nanoTime();
		this.goalFound = (node != null);
	}

	@Override
	public String toString() {
		return "DefaultAlgorithmBenchmarker [goalFound=" + goalFound + ", nodesVisited=" + nodesVisited
		        + ", nodesExpanded=" + nodesExpanded + ", nodesGenerated=" + nodesGenerated + ", nodesExcluded="
		        + nodesExcluded + ", totalTime()=" + totalTimeInMs() + "ms" + ", startTime=" + startTime + ", endTime="
		        + endTime + "]";
	}

}
