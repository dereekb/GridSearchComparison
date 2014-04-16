package com.dereekb.gridsearch.models;

import java.util.Set;

import com.dereekb.gridsearch.models.algorithm.GraphWorldNode;

/**
 * Represents a graph with impassable positions.
 * 
 * @author dereekb
 * 
 */
public class GraphWorld
        implements GraphWorldDelegate {

	private Integer width;
	private Integer height;

	private Position startPosition;
	private Position endPosition;

	private Set<Position> blockedPositions;

	public GraphWorld(Integer width, Integer height) {
		this.width = width;
		this.height = height;
	}

	public Position getRandomOpenPosition() {
		Position position = null;

		for (int i = 0; i < 1000 && position == null; i += 1) {
			position = Position.random(width, height);

			if (this.blockedPositions.contains(position) || position.equals(startPosition)
			        || position.equals(endPosition))
			{
				position = null;
			}
		}

		if (position == null) {
			throw new RuntimeException("Could not create random position!");
		}

		return position;
	}

	public Set<Position> getBlockedPositions() {
		return blockedPositions;
	}

	public void setImpassablePositions(Set<Position> impassablePositions) {
		this.blockedPositions = impassablePositions;
	}

	public Position getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(Position startPosition) {
		this.startPosition = startPosition;
	}

	public Position getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(Position endPosition) {
		this.endPosition = endPosition;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	@Override
	public boolean isBlockedPosition(Position position) {
		return this.blockedPositions.contains(position);
	}

	@Override
	public boolean isWalkablePosition(Position position) {
		boolean withinBounds = true;

		Integer xPosition = position.getxPos();
		Integer yPosition = position.getyPos();

		withinBounds &= (0 <= xPosition);
		withinBounds &= (this.width > xPosition);
		withinBounds &= (0 <= yPosition);
		withinBounds &= (this.height > yPosition);

		return withinBounds && (this.isBlockedPosition(position) == false);
	}

	@Override
	public GraphWorldNode createNodeForPosition(Position position) {
		GraphWorldNode node = new GraphWorldNode(this, position);
		return node;
	}

}
