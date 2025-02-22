package fragrant.helper;

import fragrant.finder.BE_Stronghold;
import nl.jellejurre.biomesampler.BiomeSampler;
import java.util.Optional;

public class BiomeChecker {
    /**
     * 与えられたstructure seedに対して、バイオーム条件を満たす最初の完全なワールドシードを探索します
     *
     * @param structureSeed 32ビットのstructure seed
     * @return 条件を満たす最初のワールドシードの結果、見つからない場合は空のOptional
     */
    public static Optional<StrongholdSeedResult> getValidSeed(long structureSeed) {
        for (long upper = 0; upper < (1L << 32); upper++) {
            long testSeed = (upper << 32) | structureSeed;
            BiomeSampler biomeSampler = new BiomeSampler(testSeed);

            if (BE_Stronghold.checkVillageBiomes(testSeed, biomeSampler)) {
                Position.BlockPos[] positions = BE_Stronghold.getFirstThreeStrongholds(testSeed, biomeSampler);
                double distance = calcMaxDist(positions);

                return Optional.of(new StrongholdSeedResult(
                        testSeed,
                        structureSeed,
                        distance,
                        positions
                ));
            }
        }

        return Optional.empty();
    }

    /**
     * 与えられた座標配列内の全ての点の組み合わせの中から、最大の距離を計算します
     *
     * @param positions ストロングホールドの座標配列
     * @return 座標間の最大距離
     */
    public static double calcMaxDist(Position.BlockPos[] positions) {
        double maxDist = 0.0;

        for (int i = 0; i < positions.length; i++) {
            for (int j = i + 1; j < positions.length; j++) {
                Position.ChunkPos pos1 = positions[i].toChunkPos();
                Position.ChunkPos pos2 = positions[j].toChunkPos();
                double distance = getDist(pos1, pos2);
                maxDist = Math.max(maxDist, distance);
            }
        }

        return maxDist;
    }

    private static double getDist(Position.ChunkPos a, Position.ChunkPos b) {
        double dx = a.x() - b.x();
        double dz = a.z() - b.z();
        return Math.hypot(dx, dz);
    }

    /**
     * バイオームチェック結果を保持するデータクラス
     */
    public record StrongholdSeedResult(long worldSeed, long structureSeed, double distance,
                                       Position.BlockPos[] positions) {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("World Seed: %d\nStructure Seed: %d\nDistance: %.2f\nPositions:\n",
                worldSeed, structureSeed, distance));

            for (Position.BlockPos position : positions) {
                sb.append(String.format("(%d, %d)\n", position.x(), position.z()));
            }

            return sb.toString();
        }
    }
}