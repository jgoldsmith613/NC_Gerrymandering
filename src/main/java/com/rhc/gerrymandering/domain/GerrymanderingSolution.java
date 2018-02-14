package com.rhc.gerrymandering.domain;

import java.util.ArrayList;
import java.util.Collection;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.simplelong.SimpleLongScore;

@PlanningSolution
public class GerrymanderingSolution {

	private SimpleLongScore score;

	@PlanningEntityCollectionProperty
	private Collection<Block> blocks = new ArrayList<Block>();
	
	
	
    public GerrymanderingSolution(){

    }

	@ValueRangeProvider(id = "districts")
	@ProblemFactCollectionProperty
	private Integer[] districts = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };
	
	
	@ProblemFactProperty
	public SumationInfo getSumationInfo(){
		long population = 0;
		for(Block blocks: blocks){
			population+= blocks.getPopulation();
		}
		
		return new SumationInfo(population, ((double)population)/districts.length);
	}
	
	@PlanningScore
	public SimpleLongScore getScore() {
		return score;
	}

	public void setScore(SimpleLongScore score) {
		this.score = score;
	}

	public Collection<Block> getBlocks() {
		return blocks;
	}

	public void setBlock(Collection<Block> blocks) {
		this.blocks = blocks;
	}

	

}
