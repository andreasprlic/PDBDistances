package com.ulyssecarion.pdb.distances.precalculations;

/**
 * Creates a data tree for a single PDB entry, but limiting origin groups to
 * strictly ligands.
 * 
 * @author Ulysse Carion
 */
public class LigandDistanceDataTreeBuilder {
	/**
	 * If a ligand atom is any further from a potential target atom, it is not
	 * recorded.
	 */
	private static final double MAX_DISTANCE = 8.0;
}
