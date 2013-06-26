package com.ulyssecarion.pdb.distances;

import java.util.HashMap;
import java.util.Map;

import org.biojava.bio.structure.Element;

public class DistanceDataTree {
	public static class OriginGroupTree {
		private Map<Element, OriginElementTree> children;

		public OriginGroupTree() {
			children = new HashMap<>();
		}

		public void add(Element key, OriginElementTree value) {
			children.put(key, value);
		}

		public OriginElementTree get(Element key) {
			return children.get(key);
		}
	}

	public static class OriginElementTree {
		private Map<String, OriginAtomNameTree> children;

		public OriginElementTree() {
			children = new HashMap<>();
		}

		public void add(String key, OriginAtomNameTree value) {
			children.put(key, value);
		}

		public OriginAtomNameTree get(String key) {
			return children.get(key);
		}
	}

	public static class OriginAtomNameTree {
		private Map<String, TargetGroupTree> children;

		public OriginAtomNameTree() {
			children = new HashMap<>();
		}

		public void add(String key, TargetGroupTree value) {
			children.put(key, value);
		}

		public TargetGroupTree get(String key) {
			return children.get(key);
		}
	}

	public static class TargetGroupTree {
		private Map<Element, TargetElementTree> children;

		public TargetGroupTree() {
			children = new HashMap<>();
		}

		public void add(Element key, TargetElementTree value) {
			children.put(key, value);
		}

		public TargetElementTree get(Element key) {
			return children.get(key);
		}
	}

	public static class TargetElementTree {
		private Map<String, TargetAtomNameTree> children;

		public TargetElementTree() {
			children = new HashMap<>();
		}

		public void add(String key, TargetAtomNameTree value) {
			children.put(key, value);
		}

		public TargetAtomNameTree get(String key) {
			return children.get(key);
		}
	}

	public static class TargetAtomNameTree {
		private Map<String, DistanceResult> children;

		public TargetAtomNameTree() {
			children = new HashMap<>();
		}

		public void add(String key, DistanceResult value) {
			children.put(key, value);
		}

		public DistanceResult get(String key) {
			return children.get(key);
		}
	}
}
