package com.ulyssecarion.pdb.distances.precalculations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Calc;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.StructureException;
import org.biojava.bio.structure.align.util.AtomCache;
import org.biojava.bio.structure.io.FileParsingParameters;
import org.biojava3.structure.StructureIO;

import com.ulyssecarion.pdb.distances.DistanceDataTree;
import com.ulyssecarion.pdb.distances.DistanceResult;

/**
 * Creates a data tree for a single PDB entry, but limiting origin groups to
 * strictly ligands.
 * <p>
 * If you want to create more data than just stuff about ligand-based
 * interactions, then you should create a modified version of this class.
 * 
 * @author Ulysse Carion
 */
public class LigandDistanceDataTreeBuilder {
	/**
	 * If a ligand atom is any further from a potential target atom, it is not
	 * recorded.
	 */
	private static final double MAX_DISTANCE = 5.0;

	private static AtomCache cache;

	static {
		cache = new AtomCache();
		FileParsingParameters params = cache.getFileParsingParams();
		params.setStoreEmptySeqRes(true);
		params.setAlignSeqRes(true);
		params.setLoadChemCompInfo(true);
	}

	public static void main(String[] args) {
		DistanceDataTree d = new DistanceDataTree();
		buildTreeFor(d, "101D");
	}

	/**
	 * Adds information about a particular PDB entry to a DistanceDataTree.
	 * 
	 * @param dataTree
	 *            the DistanceDataTree to add to
	 * @param pdbID
	 *            the PDB ID of the structure to add information about
	 */
	public static void buildTreeFor(DistanceDataTree dataTree, String pdbID) {
		StructureIO.setAtomCache(cache);

		int bioAssemblyCount = StructureIO.getNrBiologicalAssemblies(pdbID);
		int bioAssemblyId = bioAssemblyCount > 0 ? 1 : 0;

		Structure structure = null;
		try {
			structure = StructureIO.getBiologicalAssembly(pdbID, bioAssemblyId);
		} catch (IOException | StructureException e) {
			e.printStackTrace();
			return;
		}
		
		List<Atom> atoms = getAtoms(structure);
		List<Group> ligands = getLigands(structure);

		try {
			for (Atom a : atoms) {
				if (ligands.contains(a.getGroup())) {
					for (Atom b : atoms) {
						double distance = Calc.getDistance(a, b);

						if (a.getGroup() != b.getGroup()
								&& !b.getGroup().isWater()
								&& distance < MAX_DISTANCE) {
							DistanceResult dr = new DistanceResult(pdbID,
									distance, a, b);
							dataTree.add(a, b, dr);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<Atom> getAtoms(Structure s) {
		List<Atom> atoms = new ArrayList<>();

		List<Chain> model = s.getModel(0);
		for (Chain chain : model) {
			for (Group group : chain.getAtomGroups()) {
				atoms.addAll(group.getAtoms());
			}
		}

		return atoms;
	}

	private static List<Group> getLigands(Structure s) {
		List<Group> ligands = new ArrayList<>();

		List<Chain> model = s.getModel(0);
		for (Chain chain : model) {
			ligands.addAll(chain.getAtomLigands());
		}

		return ligands;
	}
}
