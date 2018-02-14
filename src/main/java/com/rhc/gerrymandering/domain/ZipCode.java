package com.rhc.gerrymandering.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

public class ZipCode {

	private int zipCode;
	private long population;
	private String latitude;
	private String longitude;
	
	private long expandedLat;
	private long expandedLong;

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
		result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
		result = prime * result + (int) (population ^ (population >>> 32));
		result = prime * result + zipCode;
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
		ZipCode other = (ZipCode) obj;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		if (population != other.population)
			return false;
		if (zipCode != other.zipCode)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZipCode [zipCode=" + zipCode + ", population=" + population + ", latitude=" + latitude + ", longitude="
				+ longitude + ", district=" + district + "]";
	}

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public long getPopulation() {
		return population;
	}

	public void setPopulation(long population) {
		this.population = population;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
		this.expandedLat = ((Double)(Double.valueOf(latitude) * 100000L)).longValue();
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
		this.expandedLong = ((Double)(Double.valueOf(longitude) * 100000L)).longValue();

	}

	public Integer getDistrict() {
		return district;
	}

	public void setDistrict(Integer district) {
		this.district = district;
	}

}
