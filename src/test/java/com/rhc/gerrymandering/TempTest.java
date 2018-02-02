package com.rhc.gerrymandering;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.MultiPolygon;

public class TempTest {

	@Test
	public void shouldReadSHapeFile() throws IOException {
		File file = new File("/home/justin/Downloads/2010/tabblock2010_10_pophu.shp");
		Map<String, Object> map = new HashMap<>();
		map.put("url", file.toURI().toURL());

		DataStore dataStore = DataStoreFinder.getDataStore(map);
		String typeName = dataStore.getTypeNames()[0];

		FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
		Filter filter = Filter.INCLUDE; // ECQL.toFilter("BBOX(THE_GEOM,
										// 10,20,30,40)")

		FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);
		try (FeatureIterator<SimpleFeature> features = collection.features()) {
			MultiPolygon baseFeature = (MultiPolygon)features.next().getDefaultGeometryProperty().getValue();
			while (features.hasNext()) {
				SimpleFeature feature = features.next();
				MultiPolygon p = ((MultiPolygon)feature.getDefaultGeometryProperty().getValue());
				if(p.intersects(baseFeature)){
					System.out.println("yayayay");
					System.out.println(p.getCentroid());
					for(Object o: feature.getProperties()){
						System.out.println(o);
					}
				}
				
				//System.out.print(feature.getID());
				//System.out.print(": ");
				//System.out.println(feature.getDefaultGeometryProperty().getValue());
			}
		}
	}

}
