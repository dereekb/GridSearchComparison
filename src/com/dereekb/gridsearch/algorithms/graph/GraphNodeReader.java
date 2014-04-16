package com.dereekb.gridsearch.algorithms.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.dereekb.gridsearch.models.Position;
import com.dereekb.gridsearch.models.Positionable;

public class GraphNodeReader {

	public List<Position> allPositionsBetween(Position start,
	                                          Position end) {
		List<Position> path = new ArrayList<Position>();

		Position current = start;
		while (current != null) {
			current = current.positionTowards(end);

			if (current != null) {
				path.add(current);
			}
		}

		return path;
	}

	public List<Position> readPositionsPath(GraphNode<?> node) {
		List<Position> path = new ArrayList<Position>();

		GraphNode<?> previousNode = node;
		GraphNode<?> currentNode = previousNode.getParent();
		Position previousPosition = previousNode.getPosition();
		path.add(previousPosition);

		while (currentNode != null) {
			Position position = currentNode.getPosition();
			path.add(position);

			List<Position> connectingPath = this.allPositionsBetween(previousPosition, position);
			path.addAll(connectingPath);

			previousNode = currentNode;
			previousPosition = position;
			currentNode = currentNode.getParent();
		}

		return path;
	}

	public List<Position> quickReadPositionsPath(GraphNode<?> node) {
		List<Position> path = new ArrayList<Position>();

		GraphNode<?> currentNode = node;

		while (currentNode != null) {
			Position position = currentNode.getPosition();
			path.add(position);
			currentNode = currentNode.getParent();
		}

		return path;
	}

	public List<Position> getPositionsFromNodes(Collection<? extends GraphNode<?>> nodes) {
		List<Position> positions = new ArrayList<Position>();

		for (Positionable node : nodes) {
			Position position = node.getPosition();
			positions.add(position);
		}

		return positions;
	}

}
