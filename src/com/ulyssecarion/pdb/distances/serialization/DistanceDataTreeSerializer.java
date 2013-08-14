package com.ulyssecarion.pdb.distances.serialization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.ulyssecarion.pdb.distances.DistanceDataTree;
import com.ulyssecarion.pdb.distances.DistanceResult;
import com.ulyssecarion.pdb.distances.precalculations.LigandDistanceDataTreeBuilder;

public class DistanceDataTreeSerializer {
	/**
	 * For temporary storage of DistanceDataTrees, because attempting to make
	 * DistanceDataTrees for each and every structure in the PDB at one time
	 * would cause memory issues.
	 */
	public static final String DDT_OUTPUT_FILE = "/Volumes/HD #1/trees"
			+ File.separator;

	/**
	 * For storage of distance results in the form of a directory. Folders in
	 * here are organized by groups, elements, and atom names.
	 */
	public static final String DIR_OUTPUT_FOLDER = "/Volumes/HD #1/data"
			+ File.separator;

	/**
	 * The extension used on serialized objects.
	 */
	public static final String EXTENSION = ".ser";

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
	 * Output in format:
	 * 
	 * <pre>
	 * 	PDBID~DISTANCE~ORIGIN~TARGET
	 * </pre>
	 * 
	 * (ORIGIN and TARGET are PDB atom serial numbers.)
	 * 
	 * @param result
	 * @param path
	 */
	public static void serializeOneResult(DistanceResult result, String path) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path, true));

			out.write(result.toSerializedForm());
			out.newLine();

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read in a list of distance results stored at a given path. Results are
	 * assumed to have been serialized by this class.
	 * 
	 * @param path
	 *            the name of the file where the distance results are stored
	 * @return the list of distance results stored at the passed path.
	 */
	public static List<DistanceResult> deserializeResults(String path) {
		List<DistanceResult> results = new ArrayList<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;

			while ((line = br.readLine()) != null) {
				results.add(DistanceResult.parseSerializedResult(line));
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return results;
	}

	/**
	 * Equivalent to {@link #deserializeResults(String)}, but takes a File
	 * instead.
	 * 
	 * @param file
	 *            the file where the distance results are stored
	 * @return the list of distance results stored at the passed file
	 */
	public static List<DistanceResult> deserializeResults(File file) {
		List<DistanceResult> results = new ArrayList<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				results.add(DistanceResult.parseSerializedResult(line));
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return results;
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

	public static void main(String[] args) {
		DistanceDataTree dataTree = new DistanceDataTree();
		LigandDistanceDataTreeBuilder.buildTreeFor(dataTree, "1STP");
		serializeDataTree(dataTree, "1STP");
	}
}
