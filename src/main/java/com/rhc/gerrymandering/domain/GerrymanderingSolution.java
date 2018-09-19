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
	private SumationInfo sumationInfo;

	@PlanningEntityCollectionProperty
	private Collection<Block> blocks = new ArrayList<Block>();

	public GerrymanderingSolution() {

	}

	@ValueRangeProvider(id = "districts")
	@ProblemFactCollectionProperty
	private Integer[] districts = { 1, 2, 3, 4, 5, 6, 7};

	@ProblemFactProperty
	public SumationInfo getSumationInfo() {
		return sumationInfo;
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

	public void createSumationInfo() {
		long population = 0;
		for (Block blocks : blocks) {
			population += blocks.getPopulation();
		}

		sumationInfo = new SumationInfo(population, ((double) population) / districts.length);
	}

}
