package com.ulyssecarion.pdb.distances;

import java.io.Serializable;

import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.ResidueNumber;

public class AtomInformation implements Serializable {
	private static final long serialVersionUID = -8124843543713094007L;
	private String chainID;
	private String groupName;
	private ResidueNumber residueNumber;
	private int serialNumber;

	/**
	 * Constructs an AtomInformation with the given information.
	 * 
	 * @param chainID
	 *            the id of the atom's chain; this would be returned by
	 *            {@link Group#getChainId()}.
	 * @param groupName
	 *            the name of the atom's group; this would be returned by
	 *            {@link Group#getPDBName()}.
	 * @param residueNumber
	 *            the residue number of this atom's group; this would be
	 *            returned by {@link Group#getResidueNumber()}.
	 * @param serialNumber
	 *            the serial number of this atom; this would be returned by
	 *            {@link Atom#getPDBserial()}.
	 */
	public AtomInformation(String chainID, String groupName,
			ResidueNumber residueNumber, int serialNumber) {
		this.chainID = chainID;
		this.groupName = groupName;
		this.residueNumber = residueNumber;
		this.serialNumber = serialNumber;
	}

	/**
	 * Constructs an AtomInformation for the given atom; this is probably easier
	 * to use than the full constructor
	 * {@link #AtomInformation(String, String, ResidueNumber, int)}.
	 * 
	 * @param a the atom to build an AtomInformation for.
	 */
	public AtomInformation(Atom a) {
		this(a.getGroup().getChainId().trim(),
				a.getGroup().getPDBName().trim(), a.getGroup()
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
		return groupName + " Chain: " + chainID + " Res: " + residueNumber
				+ " Atom No: " + serialNumber;
	}
}
