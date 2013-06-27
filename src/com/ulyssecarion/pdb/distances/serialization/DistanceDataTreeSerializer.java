package com.ulyssecarion.pdb.distances.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Set;

import com.ulyssecarion.pdb.distances.DistanceDataTree;
import com.ulyssecarion.pdb.distances.DistanceDataTree.OriginGroupTree;

public class DistanceDataTreeSerializer {
	private static final String DDT_OUTPUT_FILE = "serialized_ddtrees"
			+ File.separator;
	private static final String ORIGIN_GROUP_OUTPUT_FILE = "serialized_groups"
			+ File.separator;
	private static final String EXTENSION = ".ser";

	public static void serializeByOriginGroups(DistanceDataTree dataTree) {
		Set<String> originGroups = dataTree.getOriginGroupNames();

		for (String originGroup : originGroups) {
			serialize(dataTree.get(originGroup), originGroup);
		}
	}

	public static OriginGroupTree deserializeOriginGroup(String originGroupName) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					ORIGIN_GROUP_OUTPUT_FILE + originGroupName + EXTENSION));

			OriginGroupTree dataTree = (OriginGroupTree) in.readObject();

			in.close();
			return dataTree;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void serialize(List<OriginGroupTree> originGroup, String name) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(ORIGIN_GROUP_OUTPUT_FILE + name
							+ EXTENSION));

			// get() returns multiple only if the group name is null;
			// getting only the first index is a safe process in this case
			out.writeObject(originGroup.get(0));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Serializes an entire DistanceDataTree; you must specify the name of the
	 * file you want to save to and use this same name to get the
	 * DistanceDataTree back.
	 * 
	 * @param dataTree
	 *            the DistanceDataTree to save
	 * @param name
	 *            the name of the file to save to
	 * @see #deserializeDataTree(String)
	 */
	public static void serializeDataTree(DistanceDataTree dataTree, String name) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(DDT_OUTPUT_FILE + name + EXTENSION));
			out.writeObject(dataTree);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deserializes a DistanceDataTree saved at the file whose name is specified
	 * 
	 * @param name
	 *            the location of the DistanceDataTree (this was the argument
	 *            you passed to
	 *            {@link #serializeDataTree(DistanceDataTree, String)})
	 * @return the DistanceDataTree found, or null if an error occurred.
	 * @see #serializeDataTree(DistanceDataTree, String)
	 */
	public static DistanceDataTree deserializeDataTree(String name) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					DDT_OUTPUT_FILE + name + EXTENSION));

			DistanceDataTree dataTree = (DistanceDataTree) in.readObject();

			in.close();
			return dataTree;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
