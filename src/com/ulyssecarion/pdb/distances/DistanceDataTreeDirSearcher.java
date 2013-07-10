package com.ulyssecarion.pdb.distances;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.biojava.bio.structure.Element;

import com.ulyssecarion.pdb.distances.DistanceQuery.DistanceQueryBuilder;
import com.ulyssecarion.pdb.distances.serialization.DistanceDataTreeSerializer;

public class DistanceDataTreeDirSearcher {
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
		DistanceQuery q = new DistanceQueryBuilder().originGroup("ZN")
				.originElement(Element.Zn).targetElement(Element.O).build();
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

	public static List<DistanceResult> getDistanceResults(File file,
			String parameter) {
		List<DistanceResult> results = new ArrayList<>();
		File[] candidateFileLocations = getCandidates(file, parameter);

		for (File candidateLocation : candidateFileLocations) {
			// System.out.println(candidateLocation);
			results.addAll(DistanceDataTreeSerializer
					.deserializeResults(candidateLocation));
		}

		return results;
	}
}
