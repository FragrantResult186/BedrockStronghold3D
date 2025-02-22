package fragrant;

import java.util.Optional;

import fragrant.helper.Position;
import kaptainwutax.featureutils.structure.generator.StrongholdGenerator;
import kaptainwutax.featureutils.structure.generator.piece.StructurePiece;
import kaptainwutax.featureutils.structure.generator.piece.stronghold.PortalRoom;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedutils.util.BlockBox;
import fragrant.helper.BiomeChecker.StrongholdSeedResult;
import fragrant.helper.BiomeChecker;

public class StrongholdTest {
    public static void main(String[] args) {
        for (long structureSeed = 0; structureSeed <= 3; structureSeed++) {
            Optional<StrongholdSeedResult> result = BiomeChecker.getValidSeed(structureSeed);

            if (result.isPresent()) {
                StrongholdSeedResult validSeed = result.get();
                System.out.println("=== Found Valid World Seed for Structure Seed " + structureSeed + " ===");
                System.out.println("World Seed: " + validSeed.worldSeed());
                System.out.println("Structure Seed: " + validSeed.structureSeed());

                Position.BlockPos[] strongholds = validSeed.positions();

                for (int i = 0; i < strongholds.length; i++) {
                    Position.BlockPos pos = strongholds[i];
                    System.out.println("\nStronghold " + (i + 1) + ": " + pos.x() + ", " + pos.z());

                    StrongholdGenerator generator = new StrongholdGenerator(MCVersion.v1_16);
                    Position.ChunkPos chunkPos = pos.toChunkPos();
                    generator.generate((int)validSeed.structureSeed(), chunkPos.x(), chunkPos.z());

                    // Find portal room
                    boolean foundPortal = false;
                    for (StructurePiece<?> piece : generator.pieceList) {
                        if (piece instanceof PortalRoom) {
                            BlockBox portal = ((PortalRoom) piece).getEndFrameBB();
                            int portalCenterX = (portal.minX + portal.maxX) / 2;
                            int portalCenterZ = (portal.minZ + portal.maxZ) / 2;
                            System.out.println("  Portal Room: " + portalCenterX + ", " + portalCenterZ);
                            foundPortal = true;
                            break;
                        }
                    }

                    if (!foundPortal) {
                        System.out.println("  No Portal Room found");
                    }
                }
                System.out.println("\n-------------------\n");
            }
        }
    }
}