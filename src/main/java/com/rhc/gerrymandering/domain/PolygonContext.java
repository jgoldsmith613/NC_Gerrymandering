package com.rhc.gerrymandering.domain;

import java.io.Serializable;
import java.util.ArrayList;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

public class PolygonContext implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Geometry> sections = new ArrayList<Geometry>();

	public ArrayList<Geometry> getSections() {
		return sections;
	}

	public void setSections(ArrayList<Geometry> sections) {
		this.sections = sections;
	}

}
