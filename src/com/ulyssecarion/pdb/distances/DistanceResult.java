package com.ulyssecarion.pdb.distances;

import java.io.Serializable;

import org.biojava.bio.structure.Atom;

/**
 * Reports a match between two atoms, and provides information for finding the
 * origin and target atoms again.
 * 
 * @author Ulysse Carion
 */
public class DistanceResult implements Serializable {
	private static final long serialVersionUID = 6520222050181037241L;
	private String pdbID;
	private double distance;
	private int origin;
	private int target;

	public DistanceResult(String pdbID, double distance, int origin, int target) {
		this.pdbID = pdbID;
		this.distance = distance;
		this.origin = origin;
		this.target = target;
	}

	public DistanceResult(String pdbID, double distance, Atom origin,
			Atom target) {
		this(pdbID, distance, origin.getPDBserial(), target.getPDBserial());
	}

	public String getPdbID() {
		return pdbID;
	}

	public double getDistance() {
		return distance;
	}

	public int getOrigin() {
		return origin;
	}

	public int getTarget() {
		return target;
	}

	@Override
	public String toString() {
		return origin + " -> " + target + " @ " + distance + "A (" + pdbID
				+ ")";
	}

	public String toSerializedForm() {
		int dist = (int) (distance * 10);

		return pdbID + "~" + dist + "~" + origin + "~" + target;
	}

	public static DistanceResult parseSerializedResult(String serializedForm) {
		String[] parts = serializedForm.split("~");

		String pdbID = parts[0];
		double distance = Integer.parseInt(parts[1]) / 10.0;
		int origin = Integer.parseInt(parts[2]);
		int target = Integer.parseInt(parts[3]);

		return new DistanceResult(pdbID, distance, origin, target);
	}
}
