package com.dereekb.gridsearch.models;

import java.util.ArrayList;
import java.util.List;

public class PositionExpander {

	private Integer minWidth = 0;
	private Integer minHeight = 0;
	private Integer maxWidth = 100;
	private Integer maxHeight = 100;

	private GraphWorldDelegate delegate;

	private boolean allowDiagonal = true;

	private boolean isAcceptablePosition(Position position) {
		boolean isAcceptable = true;
		boolean isBlocked = delegate.isBlockedPosition(position);

		isAcceptable = (isBlocked == false);

		Integer xPosition = position.getxPos();
		Integer yPosition = position.getyPos();
		isAcceptable &= (minWidth <= xPosition);
		isAcceptable &= (maxWidth > xPosition);
		isAcceptable &= (minHeight <= yPosition);
		isAcceptable &= (maxHeight > yPosition);

		return isAcceptable;
	}

	/**
	 * Generates all available neighboring positions.
	 * 
	 * @param position
	 * @return
	 */
	public List<Position> allNeighboringPositions(Position center) {
		List<Position> positions = new ArrayList<Position>();

		if (this.allowDiagonal) {
			// Do the top and bottom rows
			for (int y = -1; y < 2; y += 2) {
				for (int x = -1; x < 2; x += 1) {
					Position position = center.moveHorizontallyAndVertically(x, y);
					positions.add(position);
				}
			}
		} else {
			positions.add(center.moveVertically(-1));
			positions.add(center.moveVertically(1));
		}

		// Add left/right
		positions.add(center.moveHorizontal(-1));
		positions.add(center.moveHorizontal(1));

		return positions;
	}

	public List<Position> allAvailableNeighboringPositions(Position center) {
		Iterable<Position> positions = this.allNeighboringPositions(center);
		List<Position> acceptablePositions = new ArrayList<Position>();

		for (Position position : positions) {
			boolean isAcceptable = this.isAcceptablePosition(position);

			if (isAcceptable) {
				acceptablePositions.add(position);
			}
		}

		return acceptablePositions;
	}

	public Integer getMinWidth() {
		return minWidth;
	}

	public void setMinWidth(Integer minWidth) {
		this.minWidth = minWidth;
	}

	public Integer getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(Integer minHeight) {
		this.minHeight = minHeight;
	}

	public Integer getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(Integer maxWidth) {
		this.maxWidth = maxWidth;
	}

	public Integer getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(Integer maxHeight) {
		this.maxHeight = maxHeight;
	}

	public GraphWorldDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(GraphWorldDelegate delegate) {
		this.delegate = delegate;
	}

	public boolean isAllowDiagonal() {
		return allowDiagonal;
	}

	public void setAllowDiagonal(boolean allowDiagonal) {
		this.allowDiagonal = allowDiagonal;
	}

}
