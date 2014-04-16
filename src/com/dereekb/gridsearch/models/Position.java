package com.dereekb.gridsearch.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Represents a robot's position.
 * 
 * @author dereekb
 * 
 */
public class Position {

	private final Integer xPos;
	private final Integer yPos;

	public Position(Integer avenue, Integer street) {
		this.xPos = avenue;
		this.yPos = street;
	}

	public Position(Position position) {
		this.xPos = position.getxPos();
		this.yPos = position.getyPos();
	}

	public Integer getxPos() {
		return xPos;
	}

	public Integer getyPos() {
		return yPos;
	}

	public Position moveVertically(Integer change) {
		return new Position(this.xPos, this.yPos + change);
	}

	public Position moveHorizontal(Integer change) {
		return new Position(this.xPos + change, this.yPos);
	}

	public Position moveHorizontallyAndVertically(Integer x,
	                                              Integer y) {
		return new Position(this.xPos + x, this.yPos + y);
	}

	public Position positionTowards(Position position) {
		Position newPosition = null;

		if (this.equals(position) == false) {
			Integer xDifference = position.xPos - this.xPos;
			Integer yDifference = position.yPos - this.yPos;

			Integer clampedXDifference = Math.max(-1, Math.min(1, xDifference));
			Integer clampedYDifference = Math.max(-1, Math.min(1, yDifference));

			newPosition = new Position(this.moveHorizontallyAndVertically(clampedXDifference, clampedYDifference));
		}

		return newPosition;
	}

	public Position positionTowards(Direction direction) {
		Position position = this;

		switch (direction) {
			case NORTH: {
				position = position.moveVertically(1);
			}
				break;
			case SOUTH: {
				position = position.moveVertically(-1);
			}
				break;
			case EAST: {
				position = position.moveHorizontal(1);
			}
				break;
			case WEST: {
				position = position.moveHorizontal(-1);
			}
				break;
			default:
				break;
		}

		return position;
	}

	public int xChange(Position position) {
		return this.xPos - position.xPos;
	}

	public int yChange(Position position) {
		return this.yPos - position.yPos;
	}

	public int xDifference(Position position) {
		return Math.abs(this.xChange(position));
	}

	public int yDifference(Position position) {
		return Math.abs(this.yChange(position));
	}

	public int xDirection(Position position) {
		Integer xChange = this.xChange(position);
		return (xChange / Math.max(Math.abs(xChange), 1));
	}

	public int yDirection(Position position) {
		Integer yChange = this.yChange(position);
		return (yChange / Math.max(Math.abs(yChange), 1));
	}

	public double distance(Position position) {
		Integer xDifference = this.xDifference(position);
		Integer yDifference = this.yDifference(position);

		Double a2b2 = Math.pow(xDifference, 2) + Math.pow(yDifference, 2);
		Double distance = Math.sqrt(a2b2);
		return distance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((xPos == null) ? 0 : xPos.hashCode());
		result = prime * result + ((yPos == null) ? 0 : yPos.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (xPos == null) {
			if (other.xPos != null)
				return false;
		} else if (!xPos.equals(other.xPos))
			return false;
		if (yPos == null) {
			if (other.yPos != null)
				return false;
		} else if (!yPos.equals(other.yPos))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + xPos + ", " + yPos + ")";
	}

	public static Set<Position> getPositionsSet(Collection<? extends Positionable> collection) {
		Set<Position> positions = new HashSet<Position>();

		for (Positionable positionable : collection) {
			Position position = positionable.getPosition();
			positions.add(position);
		}

		return positions;
	}

	private static Random random = new Random();

	public static Position random(Integer width,
	                              Integer height) {
		Integer x = random.nextInt(width);
		Integer y = random.nextInt(height);

		return new Position(x, y);
	}

	public static Random getRandom() {
		return random;
	}

	public static void setRandom(Random random) {
		Position.random = random;
	}

}
