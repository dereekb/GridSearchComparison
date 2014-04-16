package com.dereekb.gridsearch.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import com.dereekb.gridsearch.algorithms.components.AlgorithmDistance;
import com.dereekb.gridsearch.algorithms.components.AlgorithmHeuristic;
import com.dereekb.gridsearch.algorithms.components.AlgorithmListener;
import com.dereekb.gridsearch.algorithms.components.GoalNodeChecker;
import com.dereekb.gridsearch.algorithms.components.NodeExpander;
import com.dereekb.gridsearch.algorithms.components.NodeParentAccessor;
import com.dereekb.gridsearch.algorithms.components.NodeSorter;
import com.dereekb.gridsearch.algorithms.components.SearchAlgorithm;

/**
 * Astar search algorithm
 * 
 * @author dereekb
 * 
 */
public class Astar<T>
        implements SearchAlgorithm<T, Double> {

	protected AlgorithmListener<T> listener;
	protected NodeParentAccessor<T> linkModifier;
	protected NodeExpander<T> expander;
	protected GoalNodeChecker<T> goalChecker;
	protected AlgorithmHeuristic<T, Double> herustic;
	protected AlgorithmDistance<T, Double> distanceFinder;

	public Astar(AlgorithmListener<T> listener,
	        NodeParentAccessor<T> linkModifier,
	        NodeExpander<T> expander,
	        GoalNodeChecker<T> goalChecker,
	        AlgorithmDistance<T, Double> distanceFinder,
	        AlgorithmHeuristic<T, Double> herustic) {
		super();
		this.listener = listener;
		this.linkModifier = linkModifier;
		this.expander = expander;
		this.goalChecker = goalChecker;
		this.herustic = herustic;
		this.distanceFinder = distanceFinder;
	}

	@Override
	public T search(T start,
	                T goal) {
		Instance instance = new Instance();
		T result = instance.search(start, goal);
		return result;
	}

	protected class Instance {

		protected class AstarGoalChecker
		        implements GoalNodeChecker<AstarNodeWrapper> {

			@Override
			public boolean isGoalNode(AstarNodeWrapper node,
			                          AstarNodeWrapper goal) {

				boolean isGoal = goalChecker.isGoalNode(node.getNode(), goal.getNode());
				return isGoal;
			}

		}

		/**
		 * Used for sorting and comparing nodes.
		 * 
		 * @author dereekb
		 * 
		 */
		protected class AstarNodeSorter
		        implements NodeSorter<AstarNodeWrapper> {

			@Override
			public int compare(AstarNodeWrapper o1,
			                   AstarNodeWrapper o2) {
				Double cost1 = o1.getCost();
				Double cost2 = o2.getCost();
				Integer comparison = cost1.compareTo(cost2);
				return comparison;
			}

			@Override
			public List<AstarNodeWrapper> sortNodes(Iterable<AstarNodeWrapper> nodes) {
				List<AstarNodeWrapper> nodesList = new ArrayList<AstarNodeWrapper>();

				for (AstarNodeWrapper node : nodes) {
					nodesList.add(node);
				}

				Collections.sort(nodesList, this);
				return nodesList;
			}

		}

		/**
		 * Used for wrapping nodes to keep past/future cost.
		 * 
		 * @author dereekb
		 * 
		 * @param <T>
		 */
		protected class AstarNodeWrapper {

			private AstarNodeWrapper parent;

			private Double pastCost = 0.0;
			private Double futureCost = 0.0;

			private final T value;

			public AstarNodeWrapper(T value) {
				this.value = value;
			}

			public AstarNodeWrapper(T value, Double pastCost, Double futureCost) {
				super();
				this.value = value;
				this.pastCost = pastCost;
				this.futureCost = futureCost;
			}

			public Double getPastCost() {
				return pastCost;
			}

			public void setPastCost(Double pastCost) {
				this.pastCost = pastCost;
			}

			public Double getFutureCost() {
				return futureCost;
			}

			public void setFutureCost(Double futureCost) {
				this.futureCost = futureCost;
			}

			public Double getCost() {
				return this.pastCost + this.futureCost;
			}

			public T getNode() {
				return this.value;
			}

			public AstarNodeWrapper getParent() {
				return parent;
			}

			public void setParent(AstarNodeWrapper parent) {
				this.parent = parent;
				linkModifier.setParentNode(this.value, parent.getNode());
			}

			@Override
			public String toString() {
				return "AstarNodeWrapper [c=" + this.getCost() + " '(f=" + pastCost + " + g=" + futureCost + ")', val="
				        + value + "]";
			}

		}

		protected final AstarGoalChecker astarGoalChecker = new AstarGoalChecker();

		protected T start;
		protected T goal;

		protected AstarNodeWrapper wrappedStart = null;
		protected AstarNodeWrapper wrappedGoal = null;

		protected HashMap<T, AstarNodeWrapper> openSet = new HashMap<T, AstarNodeWrapper>();
		protected PriorityQueue<AstarNodeWrapper> nodesQueue = new PriorityQueue<AstarNodeWrapper>(20,
		        new AstarNodeSorter());
		protected HashMap<T, AstarNodeWrapper> closedSet = new HashMap<T, AstarNodeWrapper>();

		protected void addToMap(AstarNodeWrapper node,
		                        HashMap<T, AstarNodeWrapper> map) {
			map.put(node.getNode(), node);
		}

		protected void removeFromMap(AstarNodeWrapper node,
		                             HashMap<T, AstarNodeWrapper> map) {
			map.remove(node.getNode());
		}

		protected Collection<T> expand(T node) {
			Collection<T> children = expander.expandNode(node);
			listener.expandedNode(node, children);
			return children;
		}

		public T search(T start,
		                T goal) {
			listener.startedAlgorithm(start, goal);

			this.start = start;
			this.goal = goal;

			this.wrappedStart = new AstarNodeWrapper(start);
			this.wrappedGoal = new AstarNodeWrapper(goal);

			T finalNode = null;

			this.addToMap(wrappedStart, openSet);
			this.nodesQueue.offer(wrappedStart);

			while (openSet.isEmpty() == false) {
				AstarNodeWrapper currentNode = nodesQueue.poll();
				T currentNodeValue = currentNode.getNode();

				listener.checkedNode(currentNodeValue); // Listener
				this.removeFromMap(currentNode, openSet);

				boolean isGoal = astarGoalChecker.isGoalNode(currentNode, wrappedGoal);

				if (isGoal) {
					finalNode = currentNodeValue;
					break;
				} else {
					this.addToMap(currentNode, closedSet);

					Collection<T> children = this.expand(currentNodeValue);

					Double currentPastCost = currentNode.getPastCost();
					for (T child : children) {
						boolean visited = closedSet.containsKey(child);

						if (visited == false) {
							Double distance = distanceFinder.getDistance(currentNodeValue, child);
							Double pastCost = currentPastCost + distance;

							AstarNodeWrapper childNode = this.openSet.get(child);
							boolean existsInOpenSet = (childNode != null);
							if (existsInOpenSet == false) {

								Double futureCost = herustic.calculateHerusticValue(child, goal);
								childNode = new AstarNodeWrapper(child, pastCost, futureCost);
								childNode.setParent(currentNode);

								this.addToMap(childNode, openSet);
								this.nodesQueue.add(childNode);
							} else {
								boolean betterPath = pastCost < childNode.getPastCost();
								if (betterPath) {
									childNode.setParent(currentNode);
									childNode.setPastCost(pastCost);

									Double futureCost = herustic.calculateHerusticValue(child, goal);
									childNode.setFutureCost(futureCost);

									this.nodesQueue.remove(childNode);
									this.nodesQueue.offer(childNode);
								}
							}
						}
					}

					listener.deprecatedNode(currentNodeValue);
				}
			}

			listener.foundGoal(finalNode);
			return finalNode;
		}
	}

	public AlgorithmListener<T> getListener() {
		return listener;
	}

	@Override
	public void setListener(AlgorithmListener<T> listener) {
		this.listener = listener;
	}

	public NodeParentAccessor<T> getLinkModifier() {
		return linkModifier;
	}

	public void setLinkModifier(NodeParentAccessor<T> linkModifier) {
		this.linkModifier = linkModifier;
	}

	public NodeExpander<T> getExpander() {
		return expander;
	}

	public void setExpander(NodeExpander<T> expander) {
		this.expander = expander;
	}

	public GoalNodeChecker<T> getGoalChecker() {
		return goalChecker;
	}

	public void setGoalChecker(GoalNodeChecker<T> goalChecker) {
		this.goalChecker = goalChecker;
	}

	public AlgorithmHeuristic<T, Double> getHerustic() {
		return herustic;
	}

	public void setHerustic(AlgorithmHeuristic<T, Double> herustic) {
		this.herustic = herustic;
	}

}
