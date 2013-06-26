package com.ulyssecarion.pdb.distances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Element;

// This lets you go in the following order:
// Origin Element -->
// Origin Atom Name -->
// Target Group Name -->
// Target Element -->
// Target Atom Name -->
// Result
// The only remaining variable, the origin group name, is taken care of by DistanceDataTree itself.
public class DistanceDataTree {
	private final Map<String, OriginGroupTree> map;

	public DistanceDataTree() {
		map = new HashMap<>();
	}

	public void add(Atom origin, Atom target, DistanceResult dr) {
		add(origin.getGroup().getPDBName(), origin.getElement(),
				origin.getName(), target.getGroup().getPDBName(),
				target.getElement(), target.getName(), dr);
	}

	public void add(String originGroup, Element originElement,
			String originAtomName, String targetGroup, Element targetElement,
			String targetAtomName, DistanceResult dr) {
		if (map.containsKey(originGroup)) {
			map.get(originGroup).add(originElement, originAtomName,
					targetGroup, targetElement, targetAtomName, dr);
		} else {
			map.put(originGroup, new OriginGroupTree(originElement,
					originAtomName, targetGroup, targetElement, targetAtomName,
					dr));
		}
	}

	public OriginGroupTree get(String originGroup) {
		return map.get(originGroup);
	}

	@Override
	public String toString() {
		return map.keySet().toString();
	}

	// Begin internal classes:

	// Elements --> OriginElementTree
	public static class OriginGroupTree {
		private final Map<Element, OriginElementTree> map;

		public OriginGroupTree(Element originElement, String originAtomName,
				String targetGroup, Element targetElement,
				String targetAtomName, DistanceResult dr) {
			map = new HashMap<>();
			add(originElement, originAtomName, targetGroup, targetElement,
					targetAtomName, dr);
		}

		public void add(Element originElement, String originAtomName,
				String targetGroup, Element targetElement,
				String targetAtomName, DistanceResult dr) {
			if (map.containsKey(originElement)) {
				map.get(originElement).add(originAtomName, targetGroup,
						targetElement, targetAtomName, dr);
			} else {
				map.put(originElement, new OriginElementTree(originAtomName,
						targetGroup, targetElement, targetAtomName, dr));
			}
		}

		public OriginElementTree get(Element originElement) {
			return map.get(originElement);
		}
	}

	// Atom Names --> OriginAtomNameTree
	public static class OriginElementTree {
		private final Map<String, OriginAtomNameTree> map;

		public OriginElementTree(String originAtomName, String targetGroup,
				Element targetElement, String targetAtomName, DistanceResult dr) {
			map = new HashMap<>();
			add(originAtomName, targetGroup, targetElement, targetAtomName, dr);
		}

		public void add(String originAtomName, String targetGroup,
				Element targetElement, String targetAtomName, DistanceResult dr) {
			if (map.containsKey(originAtomName)) {
				map.get(originAtomName).add(targetGroup, targetElement,
						targetAtomName, dr);
			} else {
				map.put(originAtomName, new OriginAtomNameTree(targetGroup,
						targetElement, targetAtomName, dr));
			}
		}

		public OriginAtomNameTree get(String originAtomName) {
			return map.get(originAtomName);
		}
	}

	// Group Names --> TargetGroupTree
	public static class OriginAtomNameTree {
		private final Map<String, TargetGroupTree> map;

		public OriginAtomNameTree(String targetGroup, Element targetElement,
				String targetAtomName, DistanceResult dr) {
			map = new HashMap<>();
			add(targetGroup, targetElement, targetAtomName, dr);
		}

		public void add(String targetGroup, Element targetElement,
				String targetAtomName, DistanceResult dr) {
			if (map.containsKey(targetGroup)) {
				map.get(targetGroup).add(targetElement, targetAtomName, dr);
			} else {
				map.put(targetGroup, new TargetGroupTree(targetElement,
						targetAtomName, dr));
			}
		}

		public TargetGroupTree get(String targetGroup) {
			return map.get(targetGroup);
		}

		@Override
		public String toString() {
			return map.keySet().toString();
		}
	}

	// Elements --> TargetElementTree
	public static class TargetGroupTree {
		private final Map<Element, TargetElementTree> map;

		public TargetGroupTree(Element targetElement, String targetAtomName,
				DistanceResult dr) {
			map = new HashMap<>();
			add(targetElement, targetAtomName, dr);
		}

		public void add(Element targetElement, String targetAtomName,
				DistanceResult dr) {
			if (map.containsKey(targetElement)) {				
				map.get(targetElement).add(targetAtomName, dr);
			} else {
				map.put(targetElement,
						new TargetElementTree(targetAtomName, dr));
			}
		}

		public TargetElementTree get(Element targetElement) {
			return map.get(targetElement);
		}
	}

	// Atom Names --> DistanceResult
	public static class TargetElementTree {
		private final Map<String, List<DistanceResult>> map;

		public TargetElementTree(String targetAtomName, DistanceResult dr) {
			map = new HashMap<>();
			add(targetAtomName, dr);
		}

		public void add(String targetAtomName, DistanceResult dr) {
			if (!map.containsKey(targetAtomName)) {
				map.put(targetAtomName, new ArrayList<DistanceResult>());
			}

			map.get(targetAtomName).add(dr);
		}

		public List<DistanceResult> get(String targetAtomName) {
			return map.get(targetAtomName);
		}
	}
}
