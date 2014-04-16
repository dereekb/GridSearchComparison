package com.dereekb.gridsearch.algorithms.graph;

import com.dereekb.gridsearch.algorithms.components.KeyedNode;
import com.dereekb.gridsearch.models.Position;
import com.dereekb.gridsearch.models.Positionable;

public class GraphNode<V>
        implements KeyedNode<V, Position>, Positionable {

	private GraphNode<V> parent;
	private final Position position;
	private V value;

	public GraphNode(V value, Position position) {
		this.value = value;
		this.position = position;
		this.parent = null;
	}

	@Override
	public Position getPosition() {
		return this.position;
	}

	@Override
	public Position getKey() {
		return this.position;
	}

	@Override
	public V getNodeValue() {
		return this.value;
	}

	public GraphNode<V> getParent() {
		return parent;
	}

	public void setParent(GraphNode<V> parent) {
		this.parent = parent;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
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
		GraphNode<?> other = (GraphNode<?>) obj;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "G.Node[pos=" + position + "]";
	}

}
