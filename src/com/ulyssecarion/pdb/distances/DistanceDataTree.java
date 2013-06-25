package com.ulyssecarion.pdb.distances;

import java.util.HashMap;
import java.util.Map;

import org.biojava.bio.structure.ResidueNumber;

public class DistanceDataTree {
	private int modelNumber;
	private String chainID;
	private ResidueNumber residueNumber;
	private int serialNumber;

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
	 * @param modelNumber
	 *            the model number of the result
	 * @param chainID
	 *            the chain ID of the result
	 * @param residueNumber
	 *            the residue number of the result
	 * @param serialNumber
	 *            the atom serial number of the result
	 */
	public DistanceDataTree(int modelNumber, String chainID,
			ResidueNumber residueNumber, int serialNumber) {
		this.modelNumber = modelNumber;
		this.chainID = chainID;
		this.residueNumber = residueNumber;
		this.serialNumber = serialNumber;
	}

	public void add(String key, DistanceDataTree val) {
		leaves.put(key, val);
	}

	public DistanceDataTree get(String key) {
		return leaves.get(key);
	}

	// these methods are only valid for leaf nodes

	public int getModelNumber() {
		return modelNumber;
	}

	public String getChainID() {
		return chainID;
	}

	public ResidueNumber getResidueNumber() {
		return residueNumber;
	}

	public int getSerialNumber() {
		return serialNumber;
	}
}
