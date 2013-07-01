package com.ulyssecarion.pdb.distances.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Set;

import org.biojava.bio.structure.Group;

import com.ulyssecarion.pdb.distances.DistanceDataTree;
import com.ulyssecarion.pdb.distances.DistanceDataTree.OriginGroupTree;
import com.ulyssecarion.pdb.distances.DistanceResult;
import com.ulyssecarion.pdb.distances.precalculations.LigandDistanceDataTreeBuilder;

public class DistanceDataTreeSerializer {
	public static final String DDT_OUTPUT_FILE = "serialized_ddtrees"
			+ File.separator;
	public static final String ORIGIN_GROUP_OUTPUT_FILE = "serialized_groups"
			+ File.separator;
	public static final String DDT_GROUP_OUTPUT_FILE = "serialized_trees_by_groups"
			+ File.separator;
	public static final String DIR_OUTPUT_FOLDER = "data_dir" + File.separator;

	public static final String EXTENSION = ".ser";

	/**
	 * Serialize a DistanceDataTree and create a separate file for each
	 * OriginGroupTree in that DistanceDataTree. This can be used in conjunction
	 * with {@link #deserializeOriginGroup(String)}.
	 * 
	 * @param dataTree
	 *            the DistanceDataTree to serialize.
	 * @see #deserializeOriginGroup(String)
	 */
	public static void serializeByOriginGroups(DistanceDataTree dataTree) {
		Set<String> originGroups = dataTree.getOriginGroupNames();

		for (String originGroup : originGroups) {
			System.out.println("Serializing group: " + originGroup);
			serialize(dataTree.get(originGroup), originGroup);
		}
	}

	/**
	 * Deserializes a single OriginGroupTree. This should be called after having
	 * generated a DistanceDataTree and having serialized that tree with
	 * {@link #serializeByOriginGroups(DistanceDataTree)}.
	 * 
	 * @param originGroupName
	 *            the name of the origin group to deserialize; this should look
	 *            like what you might get back from {@link Group#getPDBName()},
	 *            but trimmed (call {@link String#trim()}).
	 * @return the OriginGroupTree found, or null if something went wrong.
	 * @see #serializeByOriginGroups(DistanceDataTree)
	 */
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

	public static void serializeResult(List<DistanceResult> result, String path) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(path));
			out.writeObject(result);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<DistanceResult> deserializeResults(String path) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					path));

			@SuppressWarnings("unchecked")
			List<DistanceResult> result = (List<DistanceResult>) in
					.readObject();

			in.close();
			return result;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
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
			if (name.endsWith(EXTENSION))
				name = name.substring(0, 4);

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

	public static void saveAllDataTreesByGroups() {
		File dataTreeDir = new File(DDT_OUTPUT_FILE);
		File[] dataTrees = dataTreeDir.listFiles();

		for (File f : dataTrees) {
			String name = f.getName().substring(0, 4);

			System.out.println("Working on " + name);
			long start = System.currentTimeMillis();
			DistanceDataTree dataTree = deserializeDataTree(name);
			long stop = System.currentTimeMillis();
			System.out.println("Deserialized in " + ((stop - start) / 1000.0)
					+ " s.");

			start = System.currentTimeMillis();
			saveDataTreeGroups(dataTree, name);
			stop = System.currentTimeMillis();
			System.out.println("Saved the origin group tables in "
					+ ((stop - start) / 1000.0) + " s.");
		}
	}

	private static void saveDataTreeGroups(DistanceDataTree dataTree,
			String name) {
		String path = DDT_GROUP_OUTPUT_FILE + name + File.separator;

		(new File(path)).mkdir();

		try {
			for (String groupName : dataTree.getOriginGroupNames()) {
				System.out.println("\tSaving a group: " + groupName);
				ObjectOutputStream out = new ObjectOutputStream(
						new FileOutputStream(path + groupName));
				out.writeObject(dataTree.get(groupName).get(0));
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		DistanceDataTree d = new DistanceDataTree();
		LigandDistanceDataTreeBuilder.buildTreeFor(d, "100D");
		serializeDataTree(d, "1000");

		// buildDirectoryStructureFromSerializedDataTrees();
	}
}
