package com.ulyssecarion.pdb.distances;

import java.util.ArrayList;
import java.util.List;

import com.ulyssecarion.pdb.distances.DistanceDataTree.OriginAtomNameTree;
import com.ulyssecarion.pdb.distances.DistanceDataTree.OriginElementTree;
import com.ulyssecarion.pdb.distances.DistanceDataTree.OriginGroupTree;
import com.ulyssecarion.pdb.distances.DistanceDataTree.TargetElementTree;
import com.ulyssecarion.pdb.distances.DistanceDataTree.TargetGroupTree;

public class DistanceDataTreeSearcher {
	public static List<DistanceResult> search(DistanceDataTree dataTree,
			DistanceQuery query) {
		List<DistanceResult> candidates = new ArrayList<>();

		for (OriginGroupTree originGroup : dataTree.get(query
				.getOriginGroupName())) {
			for (OriginElementTree originElem : originGroup.get(query
					.getOriginElement())) {
				for (OriginAtomNameTree originAtom : originElem.get(query
						.getOriginAtomName())) {
					for (TargetGroupTree targetGroup : originAtom.get(query
							.getTargetGroupName())) {
						for (TargetElementTree targetElem : targetGroup
								.get(query.getTargetElement())) {
							for (DistanceResult dr : targetElem.get(query
									.getTargetAtomName())) {
								if (dr.getDistance() >= query.getMinDistance()
										&& dr.getDistance() <= query
												.getMaxDistance()) {
									candidates.add(dr);
								}
							}
						}
					}
				}
			}
		}
		
		return candidates;
	}
}
