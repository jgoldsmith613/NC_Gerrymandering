package com.rhc.gerrymandering.domain;

import org.optaplanner.core.impl.heuristic.selector.common.nearby.NearbyDistanceMeter;

public class BlockDistanceMeter implements NearbyDistanceMeter<Block, Block> {

	private static int count = 0;

	public double getNearbyDistance(Block origin, Block destination) {
	    double distance =  Math.pow(origin.getLatitude() - destination.getLatitude(),2) +  Math.pow(origin.getLongitude() - destination.getLongitude(),2);
		/*count++;
		if (count % 1000 == 0) {
			System.out.println(count);
		}*/
		return distance;
	}

}
