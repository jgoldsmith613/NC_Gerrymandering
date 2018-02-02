package com.rhc.gerrymandering.app;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.optaplanner.benchmark.api.PlannerBenchmarkFactory;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import com.rhc.gerrymandering.domain.GerrymanderingSolution;
import com.rhc.gerrymandering.domain.ZipCode;
import com.rhc.gerrymandering.domain.ZipCodeDistances;
import com.rhc.gerrymandering.utils.IOUtils;

public class GerrymanderingApp 
{
	
	public static ZipCodeDistances zipCodeDistances;
	
    public static void main( String[] args ) throws IOException
    {
    	
    	Collection<ZipCode> zipCodes = IOUtils.readZipInfo("src/main/resources/nc_zip_info_full.csv");
    	
    	ZipCodeDistances zipCodeDistances = IOUtils.readZipCodeDistances("src/main/resources/nc_distances_full.csv");
    	GerrymanderingApp.zipCodeDistances = zipCodeDistances;

        SolverFactory<GerrymanderingSolution> solverFactory = SolverFactory
 				.createFromXmlResource("solver/gerrymanderingSolverConfig.xml");
 		Solver<GerrymanderingSolution> solver = solverFactory.buildSolver();

 		GerrymanderingSolution solution = new GerrymanderingSolution();
 		solution.setZipCodes(zipCodes);
 		solution.setZipCodeDistances(zipCodeDistances);
 		// Solve the problem
 		GerrymanderingSolution solverPlan = solver.solve(solution);
 		
 		
 		ScoreDirector<GerrymanderingSolution> sd = solver.getScoreDirectorFactory().buildScoreDirector();
 		sd.setWorkingSolution(solverPlan);
 		
 		for( ConstraintMatchTotal match : sd.getConstraintMatchTotals()){
 			System.out.println(match);
 			System.out.println(match.getConstraintMatchCount());
 			
 		}
 		
       // PlannerBenchmarkFactory benchmarkFactory = PlannerBenchmarkFactory.createFromSolverFactory(solverFactory);
        //PlannerBenchmark plannerBenchmark = benchmarkFactory.buildPlannerBenchmark(solution);
       // plannerBenchmark.benchmark();
 		
 		IOUtils.save(solverPlan.getZipCodes());
 		
 		printSumation(solverPlan.getZipCodes());
         
        //printSumation(solution.getZipCodes());
    	
    }

	private static void printSumation(Collection<ZipCode> zipCodes) {
		Map<Integer, Long> districtPops = new HashMap<Integer, Long>();
		
		for(ZipCode zipcode : zipCodes){
			if( districtPops.containsKey(zipcode.getDistrict())){
				districtPops.put(zipcode.getDistrict(), districtPops.get(zipcode.getDistrict()) + zipcode.getPopulation());
			}else{
				districtPops.put(zipcode.getDistrict(), zipcode.getPopulation());
			}
				
		}
		
		System.out.println(districtPops);
		
	}
}
