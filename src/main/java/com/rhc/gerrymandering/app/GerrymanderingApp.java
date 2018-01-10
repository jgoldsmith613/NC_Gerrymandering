package com.rhc.gerrymandering.app;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import com.rhc.gerrymandering.domain.GerrymanderingSolution;

public class GerrymanderingApp 
{
    public static void main( String[] args )
    {

        SolverFactory<GerrymanderingSolution> solverFactory = SolverFactory
 				.createFromXmlResource("solver/gerrymanderingSolverConfig.xml");
 		Solver<GerrymanderingSolution> solver = solverFactory.buildSolver();

 		// Solve the problem
 		GerrymanderingSolution solverPlan = solver.solve(new GerrymanderingSolution());
 		
 		System.out.println(solverPlan);
         
         
    	
    }
}
