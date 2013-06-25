package com.ulyssecarion.pdb.distances;

import org.biojava.bio.structure.ResidueNumber;

public class AtomInformation {
	private String pdbID;
	private int modelNumber;
	private String chainID;
	private String groupName;
	private ResidueNumber residueNumber;
	private int serialNumber;

	public AtomInformation(String pdbID, int modelNumber, String chainID,
			String groupName, ResidueNumber residueNumber, int serialNumber) {
		this.pdbID = pdbID;
		this.modelNumber = modelNumber;
		this.chainID = chainID;
		this.groupName = groupName;
		this.residueNumber = residueNumber;
		this.serialNumber = serialNumber;
	}

	public String getPdbID() {
		return pdbID;
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
