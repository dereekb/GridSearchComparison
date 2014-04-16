package com.dereekb.gridsearch.algorithms.jps;

import java.util.ArrayList;
import java.util.Collection;

import com.dereekb.gridsearch.algorithms.Astar;
import com.dereekb.gridsearch.algorithms.components.AlgorithmDistance;
import com.dereekb.gridsearch.algorithms.components.AlgorithmHeuristic;
import com.dereekb.gridsearch.algorithms.components.GoalNodeChecker;
import com.dereekb.gridsearch.algorithms.components.NodeParentAccessor;
import com.dereekb.gridsearch.algorithms.graph.GraphNode;
import com.dereekb.gridsearch.algorithms.graph.GraphNodeExpander;
import com.dereekb.gridsearch.models.Position;

/**
 * @author dereekb
 * @param <T>
 */
public class JumpPointSearch<T extends GraphNode<?>> extends Astar<T> {

	private JumpPointSearchDelegate<T> delegate;
	private JumpPointSearchListener<T> listener;
	private GraphNodeExpander<T> expander;

	public JumpPointSearch(JumpPointSearchListener<T> listener,
	        NodeParentAccessor<T> linkModifier,
	        GraphNodeExpander<T> expander,
	        GoalNodeChecker<T> goalChecker,
	        AlgorithmHeuristic<T, Double> herustic,
	        AlgorithmDistance<T, Double> distanceFinder,
	        JumpPointSearchDelegate<T> delegate) {
		super(listener, linkModifier, expander, goalChecker, distanceFinder, herustic);
		this.delegate = delegate;
		this.expander = expander;
		this.listener = listener;
	}

	@Override
	public T search(T start,
	                T goal) {
		JSPInstance instance = new JSPInstance();
		T result = instance.search(start, goal);
		return result;
	}

	protected class JSPInstance extends Instance {

		// Identify Successors
		@Override
		protected Collection<T> expand(T node) {
			Collection<T> children = new ArrayList<T>();
			Collection<Position> successors = new ArrayList<Position>();
			Collection<Position> neighbors = null;

			neighbors = this.findNeighbors(node);

			Position nodePosition = node.getPosition();
			Position goalPosition = this.goal.getPosition();

			// Change

			for (Position neighbor : neighbors) {
				Position jumpPointPosition = this.jump(neighbor, nodePosition, goalPosition);

				if (jumpPointPosition != null) {
					listener.foundJumpPoint(jumpPointPosition);
					successors.add(jumpPointPosition);
				} else {
					listener.failedFindingJumpPoint(neighbor);
				}
			}

			for (Position successor : successors) {
				T child = delegate.nodeForPosition(successor);
				children.add(child);
			}

			listener.expandedNode(node, children);
			// System.out.println("Expanded node: " + node + " to: " + children);
			return children;
		}

		/**
		 * Finds neighbors. Prunes the direction that we just came from.
		 * 
		 * @param node
		 * @return
		 */
		private Collection<Position> findNeighbors(T node) {
			Collection<Position> neighbors = new ArrayList<Position>();
			GraphNode<?> parent = node.getParent();
			Position nodePosition = node.getPosition();

			if (parent != null) {
				Position parentPosition = parent.getPosition();

				Integer dX = nodePosition.xDirection(parentPosition);
				Integer dY = nodePosition.yDirection(parentPosition);

				boolean isDiagonal = (dX != 0 && dY != 0);

				if (isDiagonal) {

					Position vertical = nodePosition.moveVertically(dY);
					boolean canMoveVertical = delegate.isWalkablePosition(vertical);
					if (canMoveVertical) {
						neighbors.add(vertical);
					}

					Position horizontal = nodePosition.moveHorizontal(dX);
					boolean canMoveHorizontal = delegate.isWalkablePosition(horizontal);
					if (canMoveHorizontal) {
						neighbors.add(horizontal);
					}

					Position forwardCorner = nodePosition.moveHorizontallyAndVertically(dX, dY);
					if (canMoveVertical || canMoveHorizontal) {
						neighbors.add(forwardCorner);
					}

					Position backHorizontal = nodePosition.moveHorizontal(-dX);
					boolean canMoveBackwardHorizontal = delegate.isWalkablePosition(backHorizontal);
					Position secondCorner = nodePosition.moveHorizontallyAndVertically(-dX, dY);
					if ((canMoveBackwardHorizontal == false) && canMoveVertical) {
						neighbors.add(secondCorner);
					}

					Position backVertical = nodePosition.moveVertically(-dY);
					boolean canMoveBackwardVertical = delegate.isWalkablePosition(backVertical);
					Position thirdCorner = nodePosition.moveHorizontallyAndVertically(dX, -dY);
					if ((canMoveBackwardVertical == false) && canMoveHorizontal) {
						neighbors.add(thirdCorner);
					}

				} else {

					boolean noHorizontalChange = (dX == 0);

					if (noHorizontalChange) {
						Position vertical = nodePosition.moveVertically(dY);
						boolean canMoveVertically = delegate.isWalkablePosition(vertical);
						if (canMoveVertically) {
							neighbors.add(vertical);

							Position rightMove = nodePosition.moveHorizontal(1);
							boolean canMoveRight = delegate.isWalkablePosition(rightMove);
							if (canMoveRight == false) {
								neighbors.add(nodePosition.moveHorizontallyAndVertically(1, dY));
							}

							Position leftMove = nodePosition.moveHorizontal(-1);
							boolean canMoveLeft = delegate.isWalkablePosition(leftMove);
							if (canMoveLeft == false) {
								neighbors.add(nodePosition.moveHorizontallyAndVertically(-1, dY));
							}
						}

					} else {

						Position horizontal = nodePosition.moveHorizontal(dX);
						boolean canMoveHorizontal = delegate.isWalkablePosition(horizontal);
						if (canMoveHorizontal) {
							neighbors.add(horizontal);

							Position downMove = nodePosition.moveVertically(1);
							boolean canMoveDownVertical = delegate.isWalkablePosition(downMove);
							if (canMoveDownVertical == false) {
								neighbors.add(nodePosition.moveHorizontallyAndVertically(dX, 1));
							}

							Position upMove = nodePosition.moveVertically(-1);
							boolean canMoveUpVertical = delegate.isWalkablePosition(upMove);
							if (canMoveUpVertical == false) {
								neighbors.add(nodePosition.moveHorizontallyAndVertically(dX, -1));
							}
						}

					}

				}
			} else {
				neighbors = expander.expandablePositions(node);
			}

			return neighbors;
		}

		private Position jump(Position child,
		                      Position parent,
		                      Position goal) {

			listener.checkingPosition(child);
			Position jumpPoint = null;

			Integer dX = child.xChange(parent);
			Integer dY = child.yChange(parent);

			boolean isGoal = child.equals(goal);

			if (isGoal) {
				jumpPoint = child;
			} else if (delegate.isWalkablePosition(child) == false) {
				jumpPoint = null;
			} else {
				boolean isDiagonalMove = (dX != 0 && dY != 0);

				/*
				 * Checking for forced neighbors along the diagonal.
				 */
				if (isDiagonalMove) {

					Position a = child.moveHorizontallyAndVertically(-dX, dY);
					Position b = child.moveHorizontal(-dX);

					Position c = child.moveHorizontallyAndVertically(dX, -dY);
					Position d = child.moveVertically(-dY);

					boolean canMoveToA = delegate.isWalkablePosition(a);
					boolean canMoveToB = delegate.isWalkablePosition(b);
					boolean canMoveToC = delegate.isWalkablePosition(c);
					boolean canMoveToD = delegate.isWalkablePosition(d);

					boolean isForcedA = (canMoveToA && (canMoveToB == false));
					boolean isForcedB = (canMoveToC && (canMoveToD == false));

					// A
					if (isForcedA || isForcedB) {
						jumpPoint = child;
					}
				} else {
					if (dX != 0) {

						Position a = child.moveHorizontallyAndVertically(dX, 1);
						Position b = child.moveVertically(1);
						Position c = child.moveHorizontallyAndVertically(dX, -1);
						Position d = child.moveVertically(-1);

						boolean canMoveToA = delegate.isWalkablePosition(a);
						boolean canMoveToB = delegate.isWalkablePosition(b);
						boolean canMoveToC = delegate.isWalkablePosition(c);
						boolean canMoveToD = delegate.isWalkablePosition(d);

						boolean isForcedA = (canMoveToA && (canMoveToB == false));
						boolean isForcedB = (canMoveToC && (canMoveToD == false));

						// B
						if (isForcedA || isForcedB) {
							jumpPoint = child;
						}
					} else {

						Position a = child.moveHorizontallyAndVertically(1, dY);
						Position b = child.moveHorizontal(1);
						Position c = child.moveHorizontallyAndVertically(-1, dY);
						Position d = child.moveHorizontal(-1);

						boolean canMoveToA = delegate.isWalkablePosition(a);
						boolean canMoveToB = delegate.isWalkablePosition(b);
						boolean canMoveToC = delegate.isWalkablePosition(c);
						boolean canMoveToD = delegate.isWalkablePosition(d);

						boolean isForcedA = (canMoveToA && (canMoveToB == false));
						boolean isForcedB = (canMoveToC && (canMoveToD == false));

						// C
						if (isForcedA || isForcedB) {
							jumpPoint = child;
						}
					}
				}

				if (isDiagonalMove && jumpPoint == null) {

					Position a = child.moveHorizontal(dX);
					Position b = child.moveVertically(dY);

					Position jumpA = this.jump(a, child, goal);
					Position jumpB = this.jump(b, child, goal);

					if (jumpA != null || (jumpB != null)) {
						jumpPoint = child;
					}
				}

				if (jumpPoint == null) {
					Position a = child.moveHorizontal(dX);
					Position b = child.moveVertically(dY);

					boolean canA = delegate.isWalkablePosition(a);
					boolean canB = delegate.isWalkablePosition(b);

					if (canA || canB) {
						Position newNeighbor = child.moveHorizontallyAndVertically(dX, dY);
						jumpPoint = this.jump(newNeighbor, child, goal);
					}
				}
			}

			return jumpPoint;
		}

	}

	public JumpPointSearchDelegate<T> getDelegate() {
		return delegate;
	}

	public void setDelegate(JumpPointSearchDelegate<T> delegate) {
		this.delegate = delegate;
	}

	public GraphNodeExpander<T> getExpander() {
		return expander;
	}

	public void setExpander(GraphNodeExpander<T> expander) {
		this.expander = expander;
	}

	public JumpPointSearchListener<T> getListener() {
		return listener;
	}

	public void setListener(JumpPointSearchListener<T> listener) {
		super.setListener(listener);
		this.listener = listener;
	}

}
