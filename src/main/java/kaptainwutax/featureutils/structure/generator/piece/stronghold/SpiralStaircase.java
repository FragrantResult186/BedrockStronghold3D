package kaptainwutax.featureutils.structure.generator.piece.stronghold;

import kaptainwutax.featureutils.structure.Stronghold;
import kaptainwutax.featureutils.structure.generator.StrongholdGenerator;
import kaptainwutax.seedutils.lcg.rand.JRand;
import kaptainwutax.seedutils.util.BlockBox;
import kaptainwutax.seedutils.util.Direction;

import java.util.List;

public class SpiralStaircase extends Stronghold.Piece { //SHStairsDown

	private boolean isStructureStart;

	public SpiralStaircase(int pieceType, JRand rand, int x, int z) {
		super(pieceType);
		this.isStructureStart = true;
		Direction oldDir = Direction.randomHorizontal(rand);
		Direction newDir = Direction.SOUTH;

		switch (oldDir) {
			case NORTH:
				newDir = Direction.SOUTH;
				break;
			case SOUTH:
				newDir = Direction.NORTH;
				break;
			case EAST:
				newDir = Direction.WEST;
				break;
			case WEST:
				newDir = Direction.EAST;
				break;
		}

		this.setOrientation(newDir);
		this.boundingBox = new BlockBox(x, 64, z, x + 5 - 1, 74, z + 5 - 1);
	}

	public SpiralStaircase(int pieceId, JRand rand, BlockBox boundingBox, Direction facing) {
		super(pieceId);
		this.isStructureStart = false;
		this.setOrientation(facing);
		rand.nextInt(5); //Random entrance.
		this.boundingBox = boundingBox;
	}

	@Override
	public void populatePieces(StrongholdGenerator gen, Start start, List<Stronghold.Piece> pieces, JRand rand) {
		if(this.isStructureStart) {
			gen.currentPiece = FiveWayCrossing.class;
		}

		this.method_14874(gen, start, pieces, rand, 1, 1);
	}

	public static SpiralStaircase createPiece(List<Stronghold.Piece> pieces, JRand rand, int x, int y, int z, Direction facing, int pieceId) {
		BlockBox box = BlockBox.rotated(x, y, z, -1, -7, 0, 5, 11, 5, facing);
		return Stronghold.Piece.isHighEnough(box) && Stronghold.Piece.getNextIntersectingPiece(pieces, box) == null ? new SpiralStaircase(pieceId, rand, box, facing) : null;
	}

}
