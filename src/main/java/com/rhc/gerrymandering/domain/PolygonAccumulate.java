package com.rhc.gerrymandering.domain;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;

import org.kie.api.runtime.rule.AccumulateFunction;

import com.vividsolutions.jts.geom.Geometry;

public class PolygonAccumulate implements AccumulateFunction<PolygonContext> {

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void accumulate(PolygonContext context, Object value) {
		Geometry change = (Geometry) value;
		Iterator<Geometry> it = context.getSections().iterator();
		while (it.hasNext()) {
			Geometry section = it.next();
			if (change.intersects(section)) {
				change = (Geometry) change.union(section);
				it.remove();
			}
		}
		context.getSections().add(change);

	}

	@Override
	public PolygonContext createContext() {
		return new PolygonContext();
	}

	@Override
	public Object getResult(PolygonContext context) throws Exception {
		return Long.valueOf(context.getSections().size());
	}

	@Override
	public Class<?> getResultType() {
		return Long.class;
	}

	@Override
	public void init(PolygonContext context) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void reverse(PolygonContext context, Object value) throws Exception {
		Geometry change = (Geometry) value;
		Iterator<Geometry> it = context.getSections().iterator();
		while (it.hasNext()) {
			Geometry section = it.next();
			if (change.intersects(section)) {
				change = (Geometry) section.difference(change);
				it.remove();
				break;
			}
		}

	}

	@Override
	public boolean supportsReverse() {
		// TODO Auto-generated method stub
		return true;
	}

}
