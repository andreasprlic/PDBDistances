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

	/**
	 * Constructs a DistanceQuery with the given parameters.<br />
	 * <b>Note: any argument that is passed as 'null' is considered a wildcard
	 * and will match anything.</b>
	 * 
	 * @param originGroupName
	 *            the group name of the origin atom
	 * @param originElement
	 *            the element of the origin atom
	 * @param originAtomName
	 *            the name of the origin atom
	 * @param targetGroupName
	 *            the group name of the target atom
	 * @param targetElement
	 *            the element of the target atom
	 * @param targetAtomName
	 *            the name of the target atom
	 * @param minDistance
	 *            the closest the two atoms can be
	 * @param maxDistance
	 *            the furthest the two atoms can be
	 */
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

	public class DistanceQueryBuilder {
		private String originGroupName;
		private Element originElement;
		private String originAtomName;

		private String targetGroupName;
		private Element targetElement;
		private String targetAtomName;

		private double minDistance;
		private double maxDistance;

		public DistanceQueryBuilder() {
			originGroupName = originAtomName = targetGroupName = targetAtomName = null;
			originElement = targetElement = null;
			minDistance = 0;
			maxDistance = 10;
		}

		public DistanceQueryBuilder originGroup(String originGroupName) {
			this.originGroupName = originGroupName;
			return this;
		}

		public DistanceQueryBuilder originElement(Element originElement) {
			this.originElement = originElement;
			return this;
		}

		public DistanceQueryBuilder originAtom(String originAtomName) {
			this.originAtomName = originAtomName;
			return this;
		}

		public DistanceQueryBuilder targetGroup(String targetGroupName) {
			this.targetGroupName = targetGroupName;
			return this;
		}

		public DistanceQueryBuilder targetElement(Element targetElement) {
			this.targetElement = targetElement;
			return this;
		}

		public DistanceQueryBuilder targetAtom(String targetAtomName) {
			this.targetAtomName = targetAtomName;
			return this;
		}

		public DistanceQueryBuilder minDistance(double minDistance) {
			this.minDistance = minDistance;
			return this;
		}

		public DistanceQueryBuilder maxDistance(double maxDistance) {
			this.maxDistance = maxDistance;
			return this;
		}

		public DistanceQuery build() {
			return new DistanceQuery(originGroupName, originElement,
					originAtomName, targetGroupName, targetElement,
					targetAtomName, minDistance, maxDistance);
		}
	}
}
