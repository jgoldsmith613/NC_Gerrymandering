package com.rhc.gerrymandering.domain;

public class SumationInfo {

	private long totalPopulation;
	private long averagePerDistrict;

	public SumationInfo(long totalPopulation, double averagePerDistrict) {
		this.totalPopulation = totalPopulation;
		//truncating
		this.averagePerDistrict = (long)averagePerDistrict;
	}

	public long getTotalPopulation() {
		return totalPopulation;
	}

	public void setTotalPopulation(long totalPopulation) {
		this.totalPopulation = totalPopulation;
	}

	public long getAveragePerDistrict() {
		return averagePerDistrict;
	}

	public void setAveragePerDistrict(long averagePerDistrict) {
		this.averagePerDistrict = averagePerDistrict;
	}

}
