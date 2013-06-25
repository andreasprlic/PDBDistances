package com.ulyssecarion.pdb.distances;

import org.biojava.bio.structure.ResidueNumber;

public class AtomInformation {
	private int modelNumber;
	private String chainID;
	private String groupName;
	private ResidueNumber residueNumber;
	private int serialNumber;

	public AtomInformation(int modelNumber, String chainID, String groupName,
			ResidueNumber residueNumber, int serialNumber) {
		this.modelNumber = modelNumber;
		this.chainID = chainID;
		this.groupName = groupName;
		this.residueNumber = residueNumber;
		this.serialNumber = serialNumber;
	}

	public int getModelNumber() {
		return modelNumber;
	}

	public String getChainID() {
		return chainID;
	}

	public String getGroupName() {
		return groupName;
	}

	public ResidueNumber getResidueNumber() {
		return residueNumber;
	}

	public int getSerialNumber() {
		return serialNumber;
	}
}
