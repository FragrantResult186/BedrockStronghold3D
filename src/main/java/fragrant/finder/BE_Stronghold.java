package fragrant.finder;

import fragrant.helper.Position;
import nl.jellejurre.biomesampler.BiomeSampler;
import nl.jellejurre.biomesampler.minecraft.Biome;

import java.util.Set;

public class BE_Stronghold {
    private static final Set<Biome> VALID_VILLAGE_BIOMES = Set.of(
            Biome.PLAINS, Biome.SUNFLOWER_PLAINS, Biome.SNOWY_PLAINS,
            Biome.DESERT, Biome.TAIGA, Biome.SNOWY_TAIGA,
            Biome.SAVANNA, Biome.MEADOW
    );

    public static Position.BlockPos[] getFirstThreeStrongholds(long worldSeed, BiomeSampler biomeSampler) {
        Position.BlockPos[] posArr = new Position.BlockPos[3];
        int[] mt = BE_Random.genNums((int) worldSeed, 2);

        double angle = 6.2831855 * BE_Random.int2Float(mt[0]);
        int chunkDist = BE_Random.uMod(mt[1], 16) + 40;

        int count = 0;
        while (count < 3) {
            int cx = (int) Math.floor(Math.cos(angle) * chunkDist);
            int cz = (int) Math.floor(Math.sin(angle) * chunkDist);

            boolean found = false;
            outer:
            for (int x = cx - 8; x < cx + 8; x++) {
                for (int z = cz - 8; z < cz + 8; z++) {
                    Position.ChunkPos chunkPos = new Position.ChunkPos(x, z);
                    if (is_village_chunk(worldSeed, chunkPos, biomeSampler)) {
                        posArr[count++] = new Position.BlockPos(x * 16 + 4, z * 16 + 4);
                        found = true;
                        break outer;
                    }
                }
            }

            if (found) {
                angle += 1.8849558;
                chunkDist += 8;
            } else {
                angle += 0.78539819;
                chunkDist += 4;
            }
        }

        return posArr;
    }

    public static boolean is_village_chunk(long worldSeed, Position.ChunkPos pos, BiomeSampler biomeSampler) {
        int adjX = pos.x(), adjZ = pos.z();
        if (adjX < 0) adjX -= 33;
        if (adjZ < 0) adjZ -= 33;

        int seed = 10387312 + (int) worldSeed - 245998635 * (adjZ / 34) - 1724254968 * (adjX / 34);
        int[] mt = BE_Random.genNums(seed, 4);

        int r1 = BE_Random.uMod(mt[0], 26), r2 = BE_Random.uMod(mt[1], 26);
        int r3 = BE_Random.uMod(mt[2], 26), r4 = BE_Random.uMod(mt[3], 26);

        int x_offset = pos.x() % 34, z_offset = pos.z() % 34;
        if (x_offset < 0) x_offset += 34;
        if (z_offset < 0) z_offset += 34;

        boolean isValidChunk = (r1 + r2) / 2 == x_offset && (r3 + r4) / 2 == z_offset;

        if (isValidChunk) {
            int blockX = pos.x() * 16 + 8;
            int blockZ = pos.z() * 16 + 8;

            Biome biome = biomeSampler.getBiomeFromBlockPos(blockX, 64, blockZ);

            return VALID_VILLAGE_BIOMES.contains(biome);
        }

        return false;
    }
}