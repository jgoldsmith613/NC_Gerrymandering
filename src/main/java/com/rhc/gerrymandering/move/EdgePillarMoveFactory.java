package com.rhc.gerrymandering.move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.common.nearby.NearbyDistanceMatrix;
import org.optaplanner.core.impl.heuristic.selector.common.nearby.ParabolicDistributionNearbyRandom;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveIteratorFactory;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import com.rhc.gerrymandering.domain.Block;
import com.rhc.gerrymandering.domain.BlockDistanceMeter;
import com.rhc.gerrymandering.domain.GerrymanderingSolution;

public class EdgePillarMoveFactory implements MoveIteratorFactory<GerrymanderingSolution> {

	private NearbyDistanceMatrix matrix;
	private List<Block> blocks;
	private int max = 50;
	private ParabolicDistributionNearbyRandom parabolicRandom = new ParabolicDistributionNearbyRandom(max);

	@Override
	public long getSize(ScoreDirector<GerrymanderingSolution> scoreDirector) {
		// TODO Auto-generated method stub
		return scoreDirector.getWorkingSolution().getBlocks().size() * 1000;
	}

	@Override
	public Iterator<? extends Move<GerrymanderingSolution>> createOriginalMoveIterator(
			ScoreDirector<GerrymanderingSolution> scoreDirector) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<? extends Move<GerrymanderingSolution>> createRandomMoveIterator(
			ScoreDirector<GerrymanderingSolution> scoreDirector, Random workingRandom) {

		if (matrix == null) {
			Collection<Block> wsBlocks = scoreDirector.getWorkingSolution().getBlocks();
			// I don't get generics sometimes
			Collection<Object> objects = new ArrayList<Object>();
			objects.addAll(wsBlocks);
			matrix = new NearbyDistanceMatrix(new BlockDistanceMeter(), wsBlocks.size());
			for (Block block : wsBlocks) {
				matrix.addAllDestinations(block, objects.iterator(), max + 1);
			}
			blocks = new ArrayList<Block>();
			blocks.addAll(wsBlocks);
		}

		return new Iterator<EdgeSubPillarMove>() {

			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public EdgeSubPillarMove next() {
				Block randomDestination = null;
				Block randomOrigin = null;
				Collection<Block> originPiller = null;
				Collection<Block> destinationPiller = null;
				while (randomDestination == null) {
					randomOrigin = blocks.get(workingRandom.nextInt(blocks.size()));
				
					randomDestination = getNextDifferentBlock(randomOrigin);
				}
				int originPillarSize = workingRandom.nextInt(4) + 1;
				int destinationPillarSize = workingRandom.nextInt(4) + 1;
				originPiller = createPiller(randomOrigin, originPillarSize);
				destinationPiller = createPiller(randomDestination, destinationPillarSize);
				

				return new EdgeSubPillarMove(originPiller, destinationPiller);

			}

			private Collection<Block> createPiller(Block block, int pillarSize) {
				Collection<Block> piller = new HashSet<Block>();
				piller.add(block);
				HashSet<Integer> used = new HashSet<Integer>();
				int district = block.getDistrict();
                int reps = 0;
				while(piller.size() < pillarSize && reps < 30){
					//Max value wont matter since value set above
					int randomInt = parabolicRandom.nextInt(workingRandom, Integer.MAX_VALUE) + 1;
					while (used.contains(randomInt) || randomInt == 0 ) {
						randomInt = (randomInt + 1) % max;
						if(used.size() == max - 1){
							return piller;
						}
					}
					used.add(randomInt);
					Block potential = (Block) matrix.getDestination(block, randomInt);
					if (potential.getDistrict() == district) {
						piller.add(potential);
					}
					reps++;
				}
				
				
				return piller;

			}

			private Block getNextDifferentBlock(Block randomOrigin) {
				int localMax = 20;
				HashSet<Integer> used = new HashSet<Integer>();
				Block destination = null;
				int originDistrict = randomOrigin.getDistrict();
				// Try 20 random blocks nearby to see if any have a different
				// planning value
				for (int i = 0; i < 20; i++) {

					// max value set lower than global max for single swap
					int randomInt = parabolicRandom.nextInt(workingRandom, localMax) + 1;
					while (used.contains(randomInt) || randomInt == 0 ) {
						randomInt = (randomInt + 1) % localMax;
						if(used.size() == localMax - 1){
							return null;
						}
					}
					used.add(randomInt);
					Block potential = (Block) matrix.getDestination(randomOrigin, randomInt);
					if (potential.getDistrict() != originDistrict) {
						destination = potential;
						break;
					}
				}

				return destination;

			}
		};

	}

}
