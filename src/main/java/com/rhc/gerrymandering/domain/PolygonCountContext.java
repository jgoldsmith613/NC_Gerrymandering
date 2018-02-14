package com.rhc.gerrymandering.domain;

import java.io.Serializable;
import java.util.ArrayList;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

public class PolygonCountContext implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Geometry union;

	public Geometry getUnion() {
		return union;
	}

	public void setUnion(Geometry union) {
		this.union = union;
	}
	
	public void add(Geometry geometry){
		this.union = union.union(geometry);
	}
	
	public void remove(Geometry geometry){
		this.union = union.difference(geometry);
	}
	
	public int count(){
		if(union == null){
			return 0;
		}
		return union.getNumGeometries();
	}



}
