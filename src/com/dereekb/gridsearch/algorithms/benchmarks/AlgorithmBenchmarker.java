package com.dereekb.gridsearch.algorithms.benchmarks;

import com.dereekb.gridsearch.algorithms.components.AlgorithmListener;

public interface AlgorithmBenchmarker<T>
        extends AlgorithmListener<T> {

	public void reset();

}
