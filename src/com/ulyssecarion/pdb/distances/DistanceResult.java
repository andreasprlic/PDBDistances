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
	private AtomInformation origin;
	private AtomInformation target;

	public DistanceResult(String pdbID, double distance,
			AtomInformation origin, AtomInformation target) {
		this.pdbID = pdbID;
		this.distance = distance;
		this.origin = origin;
		this.target = target;
	}

	public DistanceResult(String pdbID, double distance, Atom origin,
			Atom target) {
		this(pdbID, distance, new AtomInformation(origin), new AtomInformation(
				target));
	}

	public String getPdbID() {
		return pdbID;
	}

	public double getDistance() {
		return distance;
	}

	public AtomInformation getOrigin() {
		return origin;
	}

	public AtomInformation getTarget() {
		return target;
	}

	@Override
	public String toString() {
		return origin + " -> " + target + " @ " + distance + "A";
	}
}
