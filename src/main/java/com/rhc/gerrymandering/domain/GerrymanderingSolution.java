package com.rhc.gerrymandering.domain;

import java.util.ArrayList;
import java.util.Collection;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@PlanningSolution
public class GerrymanderingSolution {

	private HardSoftScore score;

	@PlanningEntityCollectionProperty
	private Collection<ZipCode> zipCodes = new ArrayList<ZipCode>();
	
    public GerrymanderingSolution(){
    	zipCodes.add(new ZipCode());
    }

	@ValueRangeProvider(id = "districts")
	@ProblemFactCollectionProperty
	private Integer[] districts = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };

	@PlanningScore
	public HardSoftScore getScore() {
		return score;
	}

	public void setScore(HardSoftScore score) {
		this.score = score;
	}

	public Collection<ZipCode> getZipCodes() {
		return zipCodes;
	}

	public void setZipCodes(Collection<ZipCode> zipCodes) {
		this.zipCodes = zipCodes;
	}

}
