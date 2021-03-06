package com.ulyssecarion.pdb.distances;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.biojava.bio.structure.Element;

import com.ulyssecarion.pdb.distances.DistanceQuery.DistanceQueryBuilder;
import com.ulyssecarion.pdb.distances.serialization.DistanceDataTreeSerializer;

/**
 * Use this class to search through a directory structure for getting results
 * back for you distance query. If your passed DistanceQuery has all of its
 * parameters specified, then this query pretty much runs instantaneously
 * because it just has to open one file and loop through a handful of lines.
 * <p>
 * What this directory searcher does is it takes in a DistanceQuery and looks
 * through the directory structure for folders and files that match the
 * parameters in the distance query. For instance, if you specify that the
 * interaction you want takes place with a ligand called "XYZ", then we will
 * search through the "XYZ/" directory. If you also specify the element of your
 * ligand is Nitrogen, then we'll search through "XYZ/N/". If you don't specify
 * the atom name of your ligand atom, then we'll search through "XYZ/N/*&#47;"
 * (in other words, we'll search through all directories under "XYZ/N/"). If in
 * addition to the previous rules you specify that the target of the interaction
 * is on an Alanine, then the directories we'll search through must look like
 * this: "XYZ/N/*&#47;ALA/".
 * <p>
 * The directory structure looks like this:
 * 
 * <pre>
 * 	LigandGroup /
 * 		LigandElement /
 * 			LigandAtomName /
 * 				TargetGroup / 
 * 					TargetElement /
 * 						TargetAtomName.ser
 * </pre>

 * 
 * @author Ulysse Carion
 */
public class DistanceDataTreeDirSearcher {
	/**
	 * Searches through the directory structure for distance results matching a
	 * distance query. Searching is done starting from
	 * {@link DistanceDataTreeSerializer#DIR_OUTPUT_FOLDER}.
	 * <p>
	 * See the javadocs for this class for implementation details.
	 * 
	 * @param q
	 *            the distance query to find matches for
	 * @return a list of all matching distance results
	 */
	public static List<DistanceResult> search(DistanceQuery q) {
		List<DistanceResult> results = new ArrayList<>();

		File basefile = new File(DistanceDataTreeSerializer.DIR_OUTPUT_FOLDER);

		File[] originGroups = getCandidates(basefile, q.getOriginGroupName());

		for (File originGroup : originGroups) {
			// System.out.println(originGroup);
			File[] originElems = getCandidates(originGroup,
					q.getOriginElement());

			for (File originElem : originElems) {
				// System.out.println(originElem);
				File[] originAtoms = getCandidates(originElem,
						q.getOriginAtomName());

				for (File originAtom : originAtoms) {
					// System.out.println(originAtom);
					File[] targetGroups = getCandidates(originAtom,
							q.getTargetGroupName());

					for (File targetGroup : targetGroups) {
						// System.out.println(targetGroup);
						File[] targetElems = getCandidates(targetGroup,
								q.getTargetElement());

						for (File targetElem : targetElems) {
							// System.out.println(targetElem);
							List<DistanceResult> candidates = getDistanceResults(
									targetElem, q.getTargetAtomName());

							for (DistanceResult dr : candidates) {
								if (dr.getDistance() >= q.getMinDistance()
										&& dr.getDistance() <= q
												.getMaxDistance()) {
									results.add(dr);
								}
							}
						}
					}
				}
			}
		}

		return results;
	}

	public static void main(String[] args) {
		DistanceQuery q = new DistanceQueryBuilder().originGroup("IOD")
				.targetGroup("ALA").targetElement(Element.C).targetAtom("CA")
				.build();
		System.out.println(q);

		long start = System.nanoTime();
		List<DistanceResult> r = search(q);
		long stop = System.nanoTime();

		for (DistanceResult dr : r)
			System.out.println(dr);

		List<String> pdbIds = new ArrayList<>();

		for (DistanceResult dr : r)
			if (!pdbIds.contains(dr.getPdbID()))
				pdbIds.add(dr.getPdbID());

		System.out.println("Results: " + r.size());
		System.out.println("PDB IDs: " + pdbIds.size());
		System.out.println("Found in: " + (stop - start) / 1_000_000_000.0);
	}

	/**
	 * Get a list of files that could be worth searching for. If the parameter
	 * is null, then we return a list of all files because null means
	 * 'wildcard'. If it isn't null, then we use the passed argument's toString
	 * to find out what directory to search through next.
	 * <p>
	 * This method takes an Object because it needs to work for both Strings and
	 * Elements.
	 * 
	 * @param file
	 *            the current file
	 * @param parameter
	 *            the distance query parameter that decides where to search to
	 *            next
	 * @return the list of subfiles to search through next
	 */
	public static File[] getCandidates(File file, Object parameter) {
		if (parameter == null) {
			return file.listFiles();
		}

		File specificTarget = new File(file.getAbsolutePath() + File.separator
				+ parameter.toString());

		if (specificTarget.exists())
			return new File[] { specificTarget };

		return new File[] {};
	}

	/**
	 * Extracts distance results from a file.
	 * 
	 * @param file
	 *            the directory containing the serialized data
	 * @param parameter
	 *            the name of the target atom, or null if you want to match any
	 *            target atom name
	 * @return a list of distance results for your query
	 */
	public static List<DistanceResult> getDistanceResults(File file,
			String parameter) {
		if (parameter != null)
			parameter += DistanceDataTreeSerializer.EXTENSION;

		List<DistanceResult> results = new ArrayList<>();
		File[] candidateFileLocations = getCandidates(file, parameter);

		for (File candidateLocation : candidateFileLocations) {
			results.addAll(DistanceDataTreeSerializer
					.deserializeResults(candidateLocation));
		}

		return results;
	}
}
