package com.rhc.gerrymandering.move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import com.rhc.gerrymandering.domain.Block;
import com.rhc.gerrymandering.domain.GerrymanderingSolution;

public class EdgeSubPillarMove extends AbstractMove<GerrymanderingSolution> {

	private Collection<Block> origin;
	private Collection<Block> destination;

	public EdgeSubPillarMove(Collection<Block> origin, Collection<Block> destination) {
		this.origin = origin;
		this.destination = destination;
	}

	@Override
	public boolean isMoveDoable(ScoreDirector<GerrymanderingSolution> scoreDirector) {
		return true;
	}

	@Override
	public Collection<? extends Object> getPlanningEntities() {
		ArrayList<Block> entities = new ArrayList<>();
		entities.addAll(origin);
		entities.addAll(destination);
		return entities;
	}

	@Override
	public Collection<? extends Object> getPlanningValues() {
		return Arrays.asList(origin.iterator().next().getDistrict(), destination.iterator().next().getDistrict());
	}

	@Override
	protected AbstractMove<GerrymanderingSolution> createUndoMove(ScoreDirector<GerrymanderingSolution> scoreDirector) {
		return new EdgeSubPillarMove(destination, origin);
	}

	@Override
	protected void doMoveOnGenuineVariables(ScoreDirector<GerrymanderingSolution> scoreDirector) {
		Integer originDistrict = origin.iterator().next().getDistrict();
		Integer destinationDistrict = destination.iterator().next().getDistrict();

		doVariableChanged(scoreDirector, origin, destinationDistrict);
		doVariableChanged(scoreDirector, destination, originDistrict);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
		return result;
	}

	private void doVariableChanged(ScoreDirector<GerrymanderingSolution> scoreDirector, Collection<Block> blocks,
			Integer district) {
		for (Block block : blocks) {
			scoreDirector.beforeVariableChanged(block, "district");
			block.setDistrict(district);
			scoreDirector.afterVariableChanged(block, "district");

		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EdgeSubPillarMove other = (EdgeSubPillarMove) obj;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		return true;
	}

}
