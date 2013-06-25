package com.ulyssecarion.pdb.distances;

import org.biojava.bio.structure.Element;

public class DistanceQuery {
	private String originGroupName;
	private Element originElement;
	private String originAtomName;
	
	private String targetGroupName;
	private Element targetElement;
	private String targetAtomName;
	
	private double minDistance;
	private double maxDistance;
	
	public DistanceQuery(String originGroupName, Element originElement,
			String originAtomName, String targetGroupName,
			Element targetElement, String targetAtomName, double minDistance,
			double maxDistance) {
		this.originGroupName = originGroupName;
		this.originElement = originElement;
		this.originAtomName = originAtomName;
		this.targetGroupName = targetGroupName;
		this.targetElement = targetElement;
		this.targetAtomName = targetAtomName;
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
	}

	public String getOriginGroupName() {
		return originGroupName;
	}

	public Element getOriginElement() {
		return originElement;
	}

	public String getOriginAtomName() {
		return originAtomName;
	}

	public String getTargetGroupName() {
		return targetGroupName;
	}

	public Element getTargetElement() {
		return targetElement;
	}

	public String getTargetAtomName() {
		return targetAtomName;
	}

	public double getMinDistance() {
		return minDistance;
	}

	public double getMaxDistance() {
		return maxDistance;
	}
}
