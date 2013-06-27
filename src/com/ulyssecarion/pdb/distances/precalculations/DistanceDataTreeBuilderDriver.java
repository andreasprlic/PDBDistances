package com.ulyssecarion.pdb.distances.precalculations;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.ulyssecarion.pdb.distances.DistanceDataTree;
import com.ulyssecarion.pdb.distances.serialization.DistanceDataTreeSerializer;

public class DistanceDataTreeBuilderDriver {
	private static final int SAVE_EVERY = 1000;
	private static final int START_AT = 14000;

	/**
	 * Builds a DistanceDataTree for every 1000 PDB IDs in the database. They
	 * can later be deserialized, joined, and used to find entries by distances.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader("pdbids.txt"));

		List<String> pdbIDs = new ArrayList<>();
		String line;
		while ((line = br.readLine()) != null) {
			pdbIDs.add(line);
		}

		for (int i = START_AT; i < pdbIDs.size(); i += SAVE_EVERY) {
			DistanceDataTree dataTree = new DistanceDataTree();

			System.out.println("Working on block starting with: "
					+ pdbIDs.get(i));

			long start = System.currentTimeMillis();
			for (int j = i; j < i + SAVE_EVERY && j < pdbIDs.size(); j++) {
				System.out.println("Generating table for: " + pdbIDs.get(j)
						+ " (" + j + ")");
				LigandDistanceDataTreeBuilder.buildTreeFor(dataTree,
						pdbIDs.get(j));
			}
			long stop = System.currentTimeMillis();
			System.out.println("That section took " + (stop - start) + " ms.");

			DistanceDataTreeSerializer.serializeDataTree(dataTree,
					pdbIDs.get(i));
		}

		br.close();
	}
}
