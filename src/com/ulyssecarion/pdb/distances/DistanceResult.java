package com.ulyssecarion.pdb.distances;

public class DistanceResult {
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
}
