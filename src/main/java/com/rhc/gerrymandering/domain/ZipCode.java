package com.rhc.gerrymandering.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class ZipCode {

	private int zipCode;
	private int population;
	private int latitude;
	private int longitude;

	@PlanningVariable(valueRangeProviderRefs = "districts")
	private Integer district;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + latitude;
		result = prime * result + longitude;
		result = prime * result + population;
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
		if (latitude != other.latitude)
			return false;
		if (longitude != other.longitude)
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

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public Integer getDistrict() {
		return district;
	}

	public void setDistrict(Integer district) {
		this.district = district;
	}

}
