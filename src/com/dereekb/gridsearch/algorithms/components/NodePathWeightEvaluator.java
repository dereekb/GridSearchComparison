package com.dereekb.gridsearch.algorithms.components;

public interface NodePathWeightEvaluator<T, W> {

	public W getNodePathWeight(T current,
	                           T target);

}
