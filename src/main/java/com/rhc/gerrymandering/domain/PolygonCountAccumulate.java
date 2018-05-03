package com.rhc.gerrymandering.domain;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;

import org.kie.api.runtime.rule.AccumulateFunction;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class PolygonCountAccumulate implements AccumulateFunction<PolygonCountContext> {

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void accumulate(PolygonCountContext context, Object value) {
		Geometry change = (Geometry) value;
		if(context.getUnion() == null){
			context.setUnion(change);
		}else{
			context.add(change);
		}

	}

	@Override
	public PolygonCountContext createContext() {
		return new PolygonCountContext();
	}

	@Override
	public Object getResult(PolygonCountContext context) throws Exception {
		return context.getUnion().getNumGeometries();
	}

	@Override
	public Class<?> getResultType() {
		return Integer.class;
	}

	@Override
	public void init(PolygonCountContext context) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void reverse(PolygonCountContext context, Object value) throws Exception {
		Geometry change = (Geometry) value;
		context.remove(change);
	}

	@Override
	public boolean supportsReverse() {
		// TODO Auto-generated method stub
		return true;
	}

}
