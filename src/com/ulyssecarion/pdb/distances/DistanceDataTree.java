package com.ulyssecarion.pdb.distances;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
/**
 * Handles the hierarchy of parameters involved in building and searching
 * through the 6 parameters involved in finding a distance-based pair.
 * <p>
 * The six levels are each handled by DistanceDataTree or its static classes.
 * They all return other static classes of DistanceDataTree except
 * TargetAtomNameTree, which is at the bottom of the chain and simply returns
 * DistanceResults.
 * <p>
 * This class and most of its static classes implement the following methods:
 * <ul>
 * <li><code>add</code>: Adds data to this tree.</li>
 * <li><code>get</code>: Gets data from this tree and returns a subtree.</li>
 * <li><code>join</code>: Combines data from two trees.</li>
 * </ul>
 * 
 * All three of these methods do a lot of delegating; adding to an
 * OriginGroupTree will automatically cause the calling of OriginElementTree,
 * OriginAtomNameTree, etc.
 * <p>
 * The underlying implementation of these classes uses HashMaps.
 * 
 * @author Ulysse Carion
 */
public class DistanceDataTree implements Serializable {
	private static final long serialVersionUID = 6028342478914501676L;
	private final Map<String, OriginGroupTree> map;

	public DistanceDataTree() {
		map = new ConcurrentHashMap<>();
	}

	public List<DistanceResult> search(DistanceQuery query) {
		return DistanceDataTreeSearcher.search(this, query);
	}

	public void add(Atom origin, Atom target, DistanceResult dr) {
		add(origin.getGroup().getPDBName().trim(), origin.getElement(), origin
				.getName().trim(), target.getGroup().getPDBName().trim(),
				target.getElement(), target.getName().trim(), dr);
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

	public List<OriginGroupTree> get(String originGroup) {
		if (originGroup == null)
			return getAll();
		return Arrays.asList(map.get(originGroup));
	}

	private List<OriginGroupTree> getAll() {
		List<OriginGroupTree> children = new ArrayList<>();
		for (String originGroup : map.keySet()) {
			children.add(map.get(originGroup));
		}
		return children;
	}

	public Set<String> getOriginGroupNames() {
		return map.keySet();
	}

	@Override
	public String toString() {
		return map.keySet().toString();
	}

	public void join(DistanceDataTree other) {
		for (String originGroup : other.map.keySet()) {
			if (map.containsKey(originGroup)) {
				map.get(originGroup).join(other.map.get(originGroup));
			} else {
				map.put(originGroup, other.map.get(originGroup));
			}
		}
	}

	// Begin internal classes:

	// Elements --> OriginElementTree
	public static class OriginGroupTree implements Serializable {
		private static final long serialVersionUID = 1923590352095758899L;
		private final Map<Element, OriginElementTree> map;

		public OriginGroupTree(Element originElement, String originAtomName,
				String targetGroup, Element targetElement,
				String targetAtomName, DistanceResult dr) {
			map = new ConcurrentHashMap<>();
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

		public List<OriginElementTree> get(Element originElement) {
			if (originElement == null)
				return getAll();
			return Arrays.asList(map.get(originElement));
		}

		private List<OriginElementTree> getAll() {
			List<OriginElementTree> children = new ArrayList<>();
			for (Element originGroup : map.keySet()) {
				children.add(map.get(originGroup));
			}
			return children;
		}

		public Set<Element> getKeys() {
			return map.keySet();
		}

		public void join(OriginGroupTree other) {
			for (Element originElement : other.map.keySet()) {
				if (map.containsKey(originElement)) {
					map.get(originElement).join(other.map.get(originElement));
				} else {
					map.put(originElement, other.map.get(originElement));
				}
			}
		}
	}

	// Atom Names --> OriginAtomNameTree
	public static class OriginElementTree implements Serializable {
		private static final long serialVersionUID = 5388345241741841348L;
		private final Map<String, OriginAtomNameTree> map;

		public OriginElementTree(String originAtomName, String targetGroup,
				Element targetElement, String targetAtomName, DistanceResult dr) {
			map = new ConcurrentHashMap<>();
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

		public List<OriginAtomNameTree> get(String originAtomName) {
			if (originAtomName == null)
				return getAll();
			return Arrays.asList(map.get(originAtomName));
		}

		private List<OriginAtomNameTree> getAll() {
			List<OriginAtomNameTree> children = new ArrayList<>();
			for (String originGroup : map.keySet()) {
				children.add(map.get(originGroup));
			}
			return children;
		}

		public Set<String> getKeys() {
			return map.keySet();
		}

		public void join(OriginElementTree other) {
			for (String originAtomName : other.map.keySet()) {
				if (map.containsKey(originAtomName)) {
					map.get(originAtomName).join(other.map.get(originAtomName));
				} else {
					map.put(originAtomName, other.map.get(originAtomName));
				}
			}
		}
	}

	// Group Names --> TargetGroupTree
	public static class OriginAtomNameTree implements Serializable {
		private static final long serialVersionUID = -7548849242772077642L;
		private final Map<String, TargetGroupTree> map;

		public OriginAtomNameTree(String targetGroup, Element targetElement,
				String targetAtomName, DistanceResult dr) {
			map = new ConcurrentHashMap<>();
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

		public List<TargetGroupTree> get(String targetGroup) {
			if (targetGroup == null)
				return getAll();
			return Arrays.asList(map.get(targetGroup));
		}

		private List<TargetGroupTree> getAll() {
			List<TargetGroupTree> children = new ArrayList<>();
			for (String originGroup : map.keySet()) {
				children.add(map.get(originGroup));
			}
			return children;
		}

		public Set<String> getKeys() {
			return map.keySet();
		}

		public void join(OriginAtomNameTree other) {
			for (String targetGroup : other.map.keySet()) {
				if (map.containsKey(targetGroup)) {
					map.get(targetGroup).join(other.map.get(targetGroup));
				} else {
					map.put(targetGroup, other.map.get(targetGroup));
				}
			}
		}
	}

	// Elements --> TargetElementTree
	public static class TargetGroupTree implements Serializable {
		private static final long serialVersionUID = 6475448405310353036L;
		private final Map<Element, TargetElementTree> map;

		public TargetGroupTree(Element targetElement, String targetAtomName,
				DistanceResult dr) {
			map = new ConcurrentHashMap<>();
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

		public List<TargetElementTree> get(Element targetElement) {
			if (targetElement == null)
				return getAll();
			return Arrays.asList(map.get(targetElement));
		}

		private List<TargetElementTree> getAll() {
			List<TargetElementTree> children = new ArrayList<>();
			for (Element originGroup : map.keySet()) {
				children.add(map.get(originGroup));
			}
			return children;
		}

		public Set<Element> getKeys() {
			return map.keySet();
		}

		public void join(TargetGroupTree other) {
			for (Element targetElement : other.map.keySet()) {
				if (map.containsKey(targetElement)) {
					map.get(targetElement).join(other.map.get(targetElement));
				} else {
					map.put(targetElement, other.map.get(targetElement));
				}
			}
		}
	}

	// Atom Names --> DistanceResult
	public static class TargetElementTree implements Serializable {
		private static final long serialVersionUID = 2380451003692094447L;
		private final Map<String, List<DistanceResult>> map;

		public TargetElementTree(String targetAtomName, DistanceResult dr) {
			map = new ConcurrentHashMap<>();
			add(targetAtomName, dr);
		}

		public void add(String targetAtomName, DistanceResult dr) {
			if (!map.containsKey(targetAtomName)) {
				map.put(targetAtomName, Collections
						.synchronizedList(new ArrayList<DistanceResult>()));
			}

			map.get(targetAtomName).add(dr);
		}

		public List<DistanceResult> get(String targetAtomName) {
			if (targetAtomName == null)
				return getAll();
			return map.get(targetAtomName);
		}

		private List<DistanceResult> getAll() {
			List<DistanceResult> children = new ArrayList<>();
			for (String originGroup : map.keySet()) {
				children.addAll(map.get(originGroup));
			}
			return children;
		}

		public Set<String> getKeys() {
			return map.keySet();
		}

		public void join(TargetElementTree other) {
			for (String targetAtomName : other.map.keySet()) {
				if (map.containsKey(targetAtomName)) {
					map.get(targetAtomName).addAll(
							other.map.get(targetAtomName));
				} else {
					map.put(targetAtomName, other.map.get(targetAtomName));
				}
			}
		}
	}
}
