package com.rhc.gerrymandering.domain;

import org.optaplanner.core.impl.heuristic.selector.common.nearby.NearbyDistanceMeter;

public class BlockDistanceSingleMeter implements NearbyDistanceMeter<Block, Block> {

	private static int count = 0;

	public double getNearbyDistance(Block origin, Block destination) {
	    double distance =  Math.min(Math.abs(origin.getLatitude() - destination.getLatitude()), Math.abs(origin.getLongitude() - destination.getLongitude()));
		/*count++;
		if (count % 1000 == 0) {
			System.out.println(count);
		}*/
		return distance;
	}

}
