package kaptainwutax.featureutils.structure.generator.piece.stronghold;

import kaptainwutax.featureutils.structure.Stronghold;
import kaptainwutax.featureutils.structure.generator.StrongholdGenerator;
import kaptainwutax.seedutils.lcg.rand.JRand;
import kaptainwutax.seedutils.util.BlockBox;
import kaptainwutax.seedutils.util.Direction;
import kaptainwutax.seedutils.util.math.Vec3i;

import java.util.List;

public class PortalRoom extends Stronghold.Piece { //Called SHPortalRoom in mojmap

	public PortalRoom(int pieceId, BlockBox boundingBox, Direction facing) {
		super(pieceId);
		this.setOrientation(facing);
		this.boundingBox = boundingBox;
	}

	@Override
	public void populatePieces(StrongholdGenerator gen, Start start, List<Stronghold.Piece> pieces, JRand rand) {
		if(start != null) {
			start.portalRoom = this;
		}
	}

	public static PortalRoom createPiece(List<Stronghold.Piece> pieces, int x, int y, int z, Direction facing, int pieceId) {
		BlockBox box = BlockBox.rotated(x, y, z, -4, -1, 0, 11, 8, 16, facing);
		return Stronghold.Piece.isHighEnough(box) && Stronghold.Piece.getNextIntersectingPiece(pieces, box) == null ? new PortalRoom(pieceId, box, facing) : null;
	}

	public BlockBox getEndFrameBB() {
		Vec3i mins = this.applyVecTransform(new Vec3i(3,3,8));
		Vec3i maxes = this.applyVecTransform(new Vec3i(7,3,12));
		return new BlockBox(mins,maxes);
	}

}
