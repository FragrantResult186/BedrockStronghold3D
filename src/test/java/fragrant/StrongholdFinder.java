package fragrant;

import fragrant.finder.BE_Stronghold;
import fragrant.helper.Position;
import nl.jellejurre.biomesampler.BiomeSampler;

public class StrongholdFinder {
    public static Position.BlockPos[] findValidStrongholds(long worldSeed, BiomeSampler biomeSampler) {
        Position.BlockPos[] strongholds = BE_Stronghold.getFirstThreeStrongholds(worldSeed, biomeSampler);

        // 最初の3つのストロングホールドの位置をチェック
        while (!BE_Stronghold.checkVillageBiomes(worldSeed, biomeSampler)) {
            // バイオームチェックが失敗した場合、次の3つの位置を計算
            Position.BlockPos[] current = strongholds.clone();

            // 新しい位置を計算
            for (int i = 0; i < strongholds.length; i++) {
                Position.ChunkPos chunkPos = current[i].toChunkPos();
                boolean found = false;

                // 現在の位置から次の有効な位置を探す
                for (int x = chunkPos.x() + 1; !found; x++) {
                    for (int z = -x; z <= x; z++) {
                        Position.ChunkPos newPos = new Position.ChunkPos(x, z);
                        if (BE_Stronghold.is_village_chunk(worldSeed, newPos, biomeSampler)) {
                            strongholds[i] = newPos.toBlockPos();
                            found = true;
                            break;
                        }
                    }
                }
            }
        }

        return strongholds;
    }

    public static void main(String[] args) {
        long seed = 1L;
        BiomeSampler biomeSampler = new BiomeSampler(seed);

        Position.BlockPos[] validStrongholds = findValidStrongholds(seed, biomeSampler);

        System.out.println("Valid stronghold positions for seed " + seed + ":");
        for (int i = 0; i < validStrongholds.length; i++) {
            Position.BlockPos pos = validStrongholds[i];
            System.out.printf("Stronghold %d: (%d, %d)%n", i + 1, pos.x(), pos.z());
        }
    }
}