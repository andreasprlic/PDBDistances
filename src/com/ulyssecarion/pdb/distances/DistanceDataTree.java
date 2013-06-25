package com.ulyssecarion.pdb.distances;

import java.util.HashMap;
import java.util.Map;

import org.biojava.bio.structure.ResidueNumber;

public class DistanceDataTree {
	private DistanceResult result;
	private Map<String, DistanceDataTree> leaves;

	/**
	 * Constructor for non-leaf nodes.
	 */
	public DistanceDataTree() {
		leaves = new HashMap<>();
	}

	/**
	 * Constructor for leaf nodes.
	 * 
	 * @param result
	 *            information about the result found.
	 */
	public DistanceDataTree(DistanceResult result) {
		this.result = result;
	}

	public void add(String key, DistanceDataTree val) {
		leaves.put(key, val);
	}

	public DistanceDataTree get(String key) {
		return leaves.get(key);
	}

	public DistanceResult getResult() {
		return result;
	}
}
