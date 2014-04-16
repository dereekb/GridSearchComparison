package com.dereekb.gridsearch.models.algorithm;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dereekb.gridsearch.algorithms.benchmarks.DefaultAlgorithmBenchmarker;
import com.dereekb.gridsearch.algorithms.graph.GraphNodeReader;
import com.dereekb.gridsearch.algorithms.jps.JumpPointSearchListener;
import com.dereekb.gridsearch.models.Position;

public class GraphWorldBenchmarker extends DefaultAlgorithmBenchmarker<GraphWorldNode>
        implements JumpPointSearchListener<GraphWorldNode> {

	private GraphWorldNode start;
	private GraphWorldNode goal;
	private GraphWorldNode end;

	private Set<GraphWorldNode> nodesVisitedSet;
	private Set<GraphWorldNode> nodesExpandedSet;
	private Set<GraphWorldNode> nodesGeneratedSet;
	private Set<GraphWorldNode> nodesExcludedSet;

	private Integer totalJumpCalls;
	private Set<Position> jpsPositionsChecked;
	private Set<Position> jumpPoints;

	@Override
	public void checkedNode(GraphWorldNode node) {
		super.checkedNode(node);
		nodesVisitedSet.add(node);
	}

	@Override
	public void failedFindingJumpPoint(Position neighbor) {

	}

	@Override
	public void expandedNode(GraphWorldNode node,
	                         Collection<GraphWorldNode> children) {
		super.expandedNode(node, children);
		this.nodesExpandedSet.add(node);
		this.nodesGeneratedSet.addAll(children);
	}

	@Override
	public void deprecatedNode(GraphWorldNode node) {
		super.deprecatedNode(node);
		this.nodesExcludedSet.add(node);
	}

	@Override
	public void startedAlgorithm(GraphWorldNode start,
	                             GraphWorldNode goal) {
		super.startedAlgorithm(start, goal);
		this.start = start;
		this.goal = goal;
	}

	@Override
	public void checkingPosition(Position position) {
		this.totalJumpCalls += 1;
		this.jpsPositionsChecked.add(position);
	}

	@Override
	public void foundJumpPoint(Position position) {
		this.jumpPoints.add(position);
	}

	@Override
	public void foundGoal(GraphWorldNode node) {
		super.foundGoal(node);
		this.end = node;
	}

	public List<Position> getFinalPath() {
		List<Position> path = null;

		if (this.end != null) {
			GraphNodeReader reader = new GraphNodeReader();
			path = reader.readPositionsPath(this.end);
		}

		return path;
	}

	@Override
	public void reset() {
		super.reset();

		nodesVisitedSet = new HashSet<GraphWorldNode>();
		nodesExpandedSet = new HashSet<GraphWorldNode>();
		nodesGeneratedSet = new HashSet<GraphWorldNode>();
		nodesExcludedSet = new HashSet<GraphWorldNode>();

		this.totalJumpCalls = 0;
		this.jpsPositionsChecked = new HashSet<Position>();
		this.jumpPoints = new HashSet<Position>();
	}

	public GraphWorldNode getStart() {
		return start;
	}

	public void setStart(GraphWorldNode start) {
		this.start = start;
	}

	public GraphWorldNode getGoal() {
		return goal;
	}

	public void setGoal(GraphWorldNode goal) {
		this.goal = goal;
	}

	public GraphWorldNode getEnd() {
		return end;
	}

	public void setEnd(GraphWorldNode end) {
		this.end = end;
	}

	public boolean foundGoal() {
		return (this.end != null);
	}

	public Set<GraphWorldNode> getNodesVisitedSet() {
		return nodesVisitedSet;
	}

	public void setNodesVisitedSet(Set<GraphWorldNode> nodesVisitedSet) {
		this.nodesVisitedSet = nodesVisitedSet;
	}

	public Set<GraphWorldNode> getNodesExpandedSet() {
		return nodesExpandedSet;
	}

	public void setNodesExpandedSet(Set<GraphWorldNode> nodesExpandedSet) {
		this.nodesExpandedSet = nodesExpandedSet;
	}

	public Set<GraphWorldNode> getNodesGeneratedSet() {
		return nodesGeneratedSet;
	}

	public void setNodesGeneratedSet(Set<GraphWorldNode> nodesGeneratedSet) {
		this.nodesGeneratedSet = nodesGeneratedSet;
	}

	public Set<GraphWorldNode> getNodesExcludedSet() {
		return nodesExcludedSet;
	}

	public void setNodesExcludedSet(Set<GraphWorldNode> nodesExcludedSet) {
		this.nodesExcludedSet = nodesExcludedSet;
	}

	public Set<Position> getJpsPositionsChecked() {
		return jpsPositionsChecked;
	}

	public void setJpsPositionsChecked(Set<Position> jpsPositionsChecked) {
		this.jpsPositionsChecked = jpsPositionsChecked;
	}

	public Set<Position> getJumpPoints() {
		return jumpPoints;
	}

	public void setJumpPoints(Set<Position> jumpPoints) {
		this.jumpPoints = jumpPoints;
	}

	@Override
	public String toString() {
		return "GraphWorldBenchmarker [jpsPositionsChecked=" + jpsPositionsChecked.size() + ", jumpPoints="
		        + jumpPoints.size() + ", S=" + super.toString() + "]";
	}

	public Integer getTotalJumpCalls() {
		return totalJumpCalls;
	}

	public void setTotalJumpCalls(Integer totalJumpCalls) {
		this.totalJumpCalls = totalJumpCalls;
	}

}
