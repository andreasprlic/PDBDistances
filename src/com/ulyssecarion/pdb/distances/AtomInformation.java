package com.ulyssecarion.pdb.distances;

import java.io.Serializable;

import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.ResidueNumber;

public class AtomInformation implements Serializable {
	private static final long serialVersionUID = -8124843543713094007L;
	private String chainID;
	private String groupName;
	private ResidueNumber residueNumber;
	private int serialNumber;

	public AtomInformation(String chainID, String groupName,
			ResidueNumber residueNumber, int serialNumber) {
		this.chainID = chainID;
		this.groupName = groupName;
		this.residueNumber = residueNumber;
		this.serialNumber = serialNumber;
	}

	public AtomInformation(Atom a) {
		this(a.getGroup().getChainId(), a.getGroup().getPDBName(), a.getGroup()
				.getResidueNumber(), a.getPDBserial());
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
	
	@Override
	public String toString() {
		return groupName + " Chain: " + chainID + " Res: " + residueNumber + " Atom No: " + serialNumber;
	}
}
