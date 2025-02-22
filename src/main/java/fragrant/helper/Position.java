package fragrant.helper;

public class Position {
    public record ChunkPos(int x, int z) {
        public BlockPos toBlockPos() {
                return new BlockPos(x * 16 + 4, z * 16 + 4);
            }
            @Override
            public String toString() {
                return String.format("ChunkPos{x=%d, z=%d}", x, z);
            }
    }

    public record BlockPos(int x, int z) {
        public ChunkPos toChunkPos() {
                return new ChunkPos(x >> 4, z >> 4);
            }
            @Override
            public String toString() {
                return String.format("BlockPos{x=%d, z=%d}", x, z);
            }
    }
}
