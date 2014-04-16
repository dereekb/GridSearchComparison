package com.dereekb.gridsearch.models;

public enum Direction {

	NORTH(1),
	WEST(2),
	SOUTH(3),
	EAST(4),
	INFINITY(-1);

	private final int bit;

	private Direction(int bit) {
		this.bit = bit;
	}

	/**
	 * @return Returns the integer value for this direction.
	 */
	public int getBit() {
		return bit;
	}

	public Direction clockwiseDirection() {
		Direction newDirection = INFINITY;

		switch (this) {
			case NORTH:
				newDirection = Direction.EAST;
				break;
			case SOUTH:
				newDirection = Direction.WEST;
				break;
			case EAST:
				newDirection = Direction.SOUTH;
				break;
			case WEST:
				newDirection = Direction.NORTH;
				break;
			default:
				break;
		}

		return newDirection;
	}

	public Direction counterClockwiseDirection() {
		Direction newDirection = INFINITY;

		switch (this) {
			case NORTH:
				newDirection = Direction.WEST;
				break;
			case SOUTH:
				newDirection = Direction.EAST;
				break;
			case EAST:
				newDirection = Direction.NORTH;
				break;
			case WEST:
				newDirection = Direction.SOUTH;
				break;
			default:
				break;
		}

		return newDirection;
	}

	public Direction oppositeDirection() {
		Direction newDirection = INFINITY;

		switch (this) {
			case NORTH:
				newDirection = Direction.SOUTH;
				break;
			case SOUTH:
				newDirection = Direction.NORTH;
				break;
			case EAST:
				newDirection = Direction.WEST;
				break;
			case WEST:
				newDirection = Direction.EAST;
				break;
			default:
				break;
		}

		return newDirection;
	}

}