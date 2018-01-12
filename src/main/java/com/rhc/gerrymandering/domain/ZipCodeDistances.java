package com.rhc.gerrymandering.domain;

import java.util.HashMap;
import java.util.Map;

public class ZipCodeDistances {
	
	private Map<Integer, Map<Integer, Long>> distanceMap;
	
	public ZipCodeDistances() {
	    distanceMap = new HashMap<Integer, Map<Integer,Long>>();
	}
	
	public void addDistancePair(Integer origin, Integer destination, Double distance){
		if(!distanceMap.containsKey(origin)){
			distanceMap.put(origin, new HashMap<Integer, Long>());
		}
		
		distanceMap.get(origin).put(destination, ((Double)(distance*100)).longValue());
	}
	
	public long getDistancePair(int origin, int destination){
		return distanceMap.get(origin).get(destination);
	}

}
