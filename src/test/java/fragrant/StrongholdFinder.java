package fragrant;

import fragrant.finder.BE_Stronghold;
import fragrant.helper.Position;
import nl.jellejurre.biomesampler.BiomeSampler;

public class StrongholdFinder {
    public static Position.BlockPos[] findValidStrongholds(long worldSeed, BiomeSampler biomeSampler) {
        return BE_Stronghold.getFirstThreeStrongholds(worldSeed, biomeSampler);
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