package com.dereekb.gridsearch.algorithms.components;

/**
 * Used for determining the goal state.
 * 
 * Allows less reliance on <T>'s isEqual() function.
 * 
 * @author dereekb
 * 
 */
public interface GoalNodeChecker<T> {

	/**
	 * Checks to see if the node satisfies the requirements to meet the goal.
	 * 
	 * @param node
	 * @param goal
	 * @return True if the node satisifies the goal requirements compared to the goal node.
	 */
	public boolean isGoalNode(T node,
	                          T goal);

}
