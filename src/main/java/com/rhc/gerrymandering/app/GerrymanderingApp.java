package com.rhc.gerrymandering.app;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.SwingUtilities;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.BasicPolygonStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.JMapFrame;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import com.rhc.gerrymandering.domain.Block;
import com.rhc.gerrymandering.domain.GerrymanderingSolution;
import com.rhc.gerrymandering.drawing.PausingMapPaneListener;
import com.vividsolutions.jts.geom.MultiPolygon;

public class GerrymanderingApp {

	// getting wierd error from geo libary on return so doing this temporarily
	public static Collection<Block> blocks = null;

	public static JMapFrame frame;

	public static Integer lock = 1;

	// public static ExecutorService mapService =
	// Executors.newSingleThreadExecutor();

	public static LinkedBlockingQueue<GerrymanderingSolution> queue = new LinkedBlockingQueue<GerrymanderingSolution>();

	public static void main(String[] args) throws IOException, InterruptedException {

		// List<String> scores = new ArrayList<String>();
		// List<GerrymanderingSolution> solutions = new
		// ArrayList<GerrymanderingSolution>();

		SolverFactory<GerrymanderingSolution> solverFactory = SolverFactory
				.createFromXmlResource("solver/gerrymanderingSolverConfig.xml");

		//PlannerBenchmarkFactory benchmarkFactory = PlannerBenchmarkFactory.createFromSolverFactory(solverFactory);

		Solver<GerrymanderingSolution> solver = solverFactory.buildSolver();

		GerrymanderingSolution solution = new GerrymanderingSolution();
		try {

			createBlocks();
		} catch (IllegalArgumentException e) {
			System.out.println("I got an exception\n" + e);
			System.exit(1);
		}
		solution.setBlock(blocks);
		solution.createSumationInfo();

		// PlannerBenchmark plannerBenchmark =
		// benchmarkFactory.buildPlannerBenchmark(solution);
		// plannerBenchmark.benchmark();

		// Solve the problem

		solver.addEventListener(new SolverEventListener<GerrymanderingSolution>() {
			public void bestSolutionChanged(BestSolutionChangedEvent<GerrymanderingSolution> event) {
				// scores.add(event.getNewBestScore().toString());
				// solutions.add(event.getNewBestSolution());

				/*
				 * mapService.execute(new Runnable() {
				 * 
				 * @Override public void run() {
				 * createMap(event.getNewBestSolution().getBlocks(),
				 * event.getNewBestScore().toString(), null); } });
				 */

				queue.add(event.getNewBestSolution());

			}
		});

		RunnableSolver runnableSolver = new RunnableSolver(solver, solution);
		Thread solvingTread = new Thread(runnableSolver);
		solvingTread.setPriority(Thread.MAX_PRIORITY);

		Thread mappingThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (solvingTread.isAlive()) {
						GerrymanderingSolution next = queue.take();
						while(queue.peek() != null){
							next = queue.poll();
						}
					
						createMap(next.getBlocks(), next.getScore().toString(), null);
						//Thread.sleep(400);
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		solvingTread.start();
		mappingThread.start();

		while (solvingTread.isAlive()) {
			solvingTread.join(1000);
		}

		// GerrymanderingSolution solverPlan = solver.solve(solution);

		// SolverFactory<GerrymanderingSolution> solverFactory2 = SolverFactory
		// .createFromXmlResource("solver/gerrymanderingSolverConfigPoly.xml");
		// Solver<GerrymanderingSolution> solver2 =
		// solverFactory2.buildSolver();

		// solverPlan = solver2.solve(solverPlan);

		ScoreDirector<GerrymanderingSolution> sd = solver.getScoreDirectorFactory().buildScoreDirector();
		GerrymanderingSolution solverPlan = runnableSolver.getFinalSolution();
		sd.setWorkingSolution(solverPlan);

		for (ConstraintMatchTotal match : sd.getConstraintMatchTotals()) {
			System.out.println(match);
			System.out.println(match.getConstraintMatchCount());

		}

		// PlannerBenchmarkFactory benchmarkFactory =
		// PlannerBenchmarkFactory.createFromSolverFactory(solverFactory);
		// PlannerBenchmark plannerBenchmark =
		// benchmarkFactory.buildPlannerBenchmark(solution);
		// plannerBenchmark.benchmark();

		// IOUtils.save(solverPlan.getZipCodes());

		printSumation(solverPlan.getBlocks());

		createMap(solverPlan.getBlocks(), solverPlan.getScore().toString(), null);

		// frame = null;
		// createMap(solutions.get(0).getBlocks(), scores.get(0), null);
		// frame = null;
		// createMap(solutions.get(1).getBlocks(), scores.get(1), null);
		// printSumation(solution.getZipCodes());

	}

	private static void createMap(Collection<Block> blocks, String score, Collection<Block> empty) {

		Map<Integer, DefaultFeatureCollection> layers = new HashMap<Integer, DefaultFeatureCollection>();

		for (Block block : blocks) {
			if (block.getDistrict() != null && !layers.containsKey(block.getDistrict())) {
				layers.put(block.getDistrict(), new DefaultFeatureCollection());
			}
			layers.get(block.getDistrict()).add(block.getFeature());
			if (block.getDistrict().equals(1)) {
				// System.out.println(block);
			}
		}

		/*
		 * DefaultFeatureCollection emptyCollection = new
		 * DefaultFeatureCollection(); for (Block block : empty) {
		 * emptyCollection.add(block.getFeature()); }
		 */

		MapContent vMap = new MapContent();
		vMap.setTitle(score);

		// Create a basic Style to render the features
		// Style style = createStyle(file, featureSource);

		// Add the features and the associated Style object to
		// the MapContent as a new Layer

		StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
		FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

		Stroke stroke = styleFactory.createStroke(filterFactory.literal(Color.BLACK), filterFactory.literal(0.1),
				filterFactory.literal(0.1));

		Color[] colors = createColors(7);
		int i = 0;
		for (Integer district : layers.keySet()) {
			Fill fill = styleFactory.createFill(filterFactory.literal(colors[i]), filterFactory.literal(0.5));
			i++;
			BasicPolygonStyle style = new BasicPolygonStyle(fill, stroke);
			Layer layer = new FeatureLayer(layers.get(district), style);
			vMap.addLayer(layer);

		}

		// synchronized (lock) {

		if (frame == null) {
			frame = showMap();
			frame.getMapPane().addMapPaneListener(new PausingMapPaneListener(frame.getMapPane()));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Now display the map
		MapContent old = frame.getMapContent();
		
		frame.setTitle(score);
		frame.setMapContent(vMap);
		if (old != null) {
			old.dispose();
		}
		// }

	}

	private static Color[] createColors(int count) {
		Color[] colors = new Color[count];
		float interval = 360 / count;
		float x = interval / 2;
		for (int i = 0; i < count; i++) {
			Color c = Color.getHSBColor(x / 360, 1, 1);
			x += interval;
			colors[i] = c;
		}
		return colors;
	}

	@SuppressWarnings("unchecked")
	public static void createBlocks() throws IOException {

		Map<String, Block> blockMap = readBlockInfo("DEC_10_PL_G001_with_ann.csv");

		Collection<Block> blocks = new ArrayList<Block>();
		Collection<Block> empty = new ArrayList<Block>();
		
	
		ClassLoader classLoader = GerrymanderingApp.class.getClassLoader();
		File file = new File(classLoader.getResource("tl_2010_08_bg10.shp").getFile());
		//File file = new File("/Users/jjarae/source/bpms/demos/optaplanner/CO_Gerrymandering/src/main/resources/tl_2010_08_bg10.shp");
		// File file = new
		// File("/home/justin/Downloads/2010/nc-blocks/temp/tabblock2010_37_pophu.shp");

		Map<String, Object> map = new HashMap<>();
		map.put("url", file.toURI().toURL());

		DataStore dataStore = DataStoreFinder.getDataStore(map);
		String typeName = dataStore.getTypeNames()[0];

		FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
		Filter filter = Filter.INCLUDE; // ECQL.toFilter("BBOX(THE_GEOM,
										// 10,20,30,40)")

		FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);
		try (FeatureIterator<SimpleFeature> features = collection.features()) {
			while (features.hasNext()) {
				SimpleFeature feature = features.next();
				MultiPolygon p = ((MultiPolygon) feature.getDefaultGeometryProperty().getValue());

				String id = (String) feature.getAttribute("GEOID10");
				id = id.replaceFirst("^0+(?!$)", "");
				Block block = blockMap.get(id);
				// Block block = new Block();
				// block.setBlockId((String) feature.getAttribute("BLOCKID10"));
				// block.setPopulation(((Number)
				// feature.getAttribute("POP10")).longValue());
				block.setPolygon(p);
				block.setLatitude(p.getCentroid().getY());
				block.setLongitude(p.getCentroid().getX());
				block.setFeature(feature);

				// System.out.println(block.getPopulation());

				if (block.getPopulation() != 0) {
					blocks.add(block);
				}

				// System.out.print(feature.getID());
				// System.out.print(": ");
				// System.out.println(feature.getDefaultGeometryProperty().getValue());
			}

			/*
			 * if (blocks.size() != blockMap.keySet().size()) {
			 * System.out.println("failing"); System.out.println(blocks.size() +
			 * " : " + blockMap.keySet().size() + " : " + empty.size());
			 * System.exit(1); }
			 */

			GerrymanderingApp.blocks = blocks;

			// return new Collection[] { blocks, empty };
		} finally {
			dataStore.dispose();
		}

	}

	public static Map<String, Block> readBlockInfo(String filename) {

		Map<String, Block> blocks = new HashMap<String, Block>();

		Iterable<CSVRecord> records = null;
		ClassLoader classLoader = GerrymanderingApp.class.getClassLoader();
		File file = new File(classLoader.getResource(filename).getFile());
		try {
			Reader in = new FileReader(file);
			records = CSVFormat.DEFAULT.withHeader().parse(in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (CSVRecord record : records) {
			Block block = new Block();
			block.setBlockId(record.get("Id2"));
			block.setPopulation(Long.parseLong(record.get("AREA CHARACTERISTICS - Population Count (100%)")));
			blocks.put(block.getBlockId(), block);

		}

		return blocks;

	}

	private static void printSumation(Collection<Block> blocks) {
		Map<Integer, Long> districtPops = new HashMap<Integer, Long>();

		for (Block block : blocks) {
			if (districtPops.containsKey(block.getDistrict())) {
				districtPops.put(block.getDistrict(), districtPops.get(block.getDistrict()) + block.getPopulation());
			} else {
				districtPops.put(block.getDistrict(), block.getPopulation());
			}

		}

		System.out.println(districtPops);

	}

	public static JMapFrame showMap() {
		JMapFrame frame = new JMapFrame();
		frame.enableStatusBar(true);
		frame.enableToolBar(true);
		frame.initComponents();

		frame.setSize(2400, 1600);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.setVisible(true);
			}
		});

		return frame;
	}

	public static class RunnableSolver implements Runnable {

		private Solver<GerrymanderingSolution> solver;
		private GerrymanderingSolution solution;
		private GerrymanderingSolution finalSolution;

		public RunnableSolver(Solver<GerrymanderingSolution> solver, GerrymanderingSolution solution) {
			this.solver = solver;
			this.solution = solution;
		}

		@Override
		public void run() {
			setFinalSolution(solver.solve(solution));

		}

		public GerrymanderingSolution getFinalSolution() {
			return finalSolution;
		}

		public void setFinalSolution(GerrymanderingSolution finalSolution) {
			this.finalSolution = finalSolution;
		}

	}
}
