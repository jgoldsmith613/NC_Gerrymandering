package com.rhc.gerrymandering.domain;

import org.opengis.feature.simple.SimpleFeature;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import com.vividsolutions.jts.geom.MultiPolygon;

@PlanningEntity()
public class Block {
	
	@Override
	public String toString() {
		return "Block-" + blockId;
	}

	@PlanningId()
	private String  blockId;
	private long population;
	private double latitude;
	private double longitude;

	private long expandedLat;
	private long expandedLong;
	
	private MultiPolygon polygon;
	private SimpleFeature feature;

	@PlanningVariable(valueRangeProviderRefs = "districts")
	private Integer district;
	
	public long getExpandedLat() {
		return expandedLat;
	}

	public long getExpandedLong() {
		return expandedLong;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blockId == null) ? 0 : blockId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Block other = (Block) obj;
		if (blockId == null) {
			if (other.blockId != null)
				return false;
		} else if (!blockId.equals(other.blockId))
			return false;
		return true;
	}

	public long getPopulation() {
		return population;
	}

	public void setPopulation(long population) {
		this.population = population;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
		this.expandedLat = ((Double)( latitude * 1000000L)).longValue();
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
		this.expandedLong = ((Double)( longitude* 1000000L)).longValue();

	}

	public Integer getDistrict() {
		return district;
	}

	public void setDistrict(Integer district) {
		this.district = district;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public MultiPolygon getPolygon() {
		return polygon;
	}

	public void setPolygon(MultiPolygon polygon) {
		this.polygon = polygon;
	}

	public SimpleFeature getFeature() {
		return feature;
	}

	public void setFeature(SimpleFeature feature) {
		this.feature = feature;
	}

}
