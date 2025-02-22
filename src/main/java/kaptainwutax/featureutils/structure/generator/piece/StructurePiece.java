package kaptainwutax.featureutils.structure.generator.piece;

import kaptainwutax.seedutils.util.BlockBox;
import kaptainwutax.seedutils.util.Direction;
import kaptainwutax.seedutils.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;

public class StructurePiece<T extends StructurePiece<T>> {

	protected int pieceId;
	public List<T> children = new ArrayList<>();

	protected BlockBox boundingBox;
	protected Direction facing;

	protected int applyXTransform(int x, int z) {
		if (this.facing == null) {
			return x;
		} else {
			switch(this.facing) {
				case NORTH:
				case SOUTH:
					return this.boundingBox.minX + x;
				case WEST:
					return this.boundingBox.maxX - z;
				case EAST:
					return this.boundingBox.minX + z;
				default:
					return x;
			}
		}
	}

	public int applyYTransform(int y) {
		return this.getFacing() == null ? y : y + this.boundingBox.minY;
	}

	protected int applyZTransform(int x, int z) {
		if (this.facing == null) {
			return z;
		} else {
			switch(this.facing) {
				case NORTH:
					return this.boundingBox.maxZ - z;
				case SOUTH:
					return this.boundingBox.minZ + z;
				case WEST:
				case EAST:
					return this.boundingBox.minZ + x;
				default:
					return z;
			}
		}
	}

	public Vec3i applyVecTransform(Vec3i vector) {
		int x = vector.getX();
		int y = vector.getY();
		int	z = vector.getZ();
		return new Vec3i(applyXTransform(x,z),applyYTransform(y),applyZTransform(x,z));
	}

	public StructurePiece(int pieceId) {
		this.pieceId = pieceId;
	}

	public Direction getFacing() {
		return this.facing;
	}

	public BlockBox getBoundingBox() {
		return this.boundingBox;
	}

	public void setOrientation(Direction facing) {
		this.facing = facing;
	}

}
