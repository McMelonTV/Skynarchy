package ing.boykiss.skynarchy.worldgen.island;

import ing.boykiss.skynarchy.worldgen.island.theme.IslandTheme;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public record WorldIslandProperties(
        Random random,
        BlockPos location,
        ServerLevel level,
        IslandTheme theme,
        int width,
        int length,
        double verticalStretch,
        double verticalShift,
        double horizontalShift
) {
    public void placeBlocks(List<Integer> shape) {
        if (shape.isEmpty()) return;

        if (shape.size() > 1) {
            shape.set(0, shape.get(1));
        }

        int x = 0;
        int z = 0;
        int[] delta = {0, -1};

        for (int i = shape.size() - 2; i > 0; i--) {
            if (-width / 2 < x && x <= width / 2 && -length / 2 < z && z <= length / 2) {
                if (shape.get(i) > 0) {
                    int randomOffset = random.nextInt(5) - 2;
                    BlockPos offset = location.offset(x, randomOffset, z);

                    double averageSize = (width + length) / 5.0;
                    double distance = Math.sqrt(Math.pow(offset.getX() - location.getX(), 2) +
                            Math.pow(offset.getZ() - location.getZ(), 2));

                    if (distance < averageSize) {
                        placePillar(offset, shape.get(i), randomOffset);
                    }
                } else {
                    break;
                }
            }

            if (x == z || (x < 0 && x == -z) || (x > 0 && x == 1 - z)) {
                // Change direction
                int temp = delta[0];
                delta[0] = -delta[1];
                delta[1] = temp;
            }

            x += delta[0];
            z += delta[1];
        }
    }

    private void placePillar(BlockPos pos, int height, int randomOffset) {
        for (int j = 0; j < height + randomOffset; j++) {
            BlockPos belowPos = pos.below(j);
            Optional<BlockState> block = theme.bottomBlocks().randomEntry(random);
            block.ifPresent(blockState -> level.setBlock(belowPos, blockState, 3));
        }

        for (int j = 0; j < height - randomOffset; j++) {
            BlockPos abovePos = pos.above(j);
            Optional<BlockState> block = theme.topBlocks().randomEntry(random);
            block.ifPresent(blockState -> level.setBlock(abovePos, blockState, 3));
        }

        BlockPos capPos = pos.above(height - randomOffset);
        Optional<BlockState> capBlock = theme.surfaceBlocks().randomEntry(random);
        capBlock.ifPresent(blockState -> level.setBlock(capPos, blockState, 3));
    }

    public void placeFeatures() {
        if (theme.features().isEmpty()) {
            return;
        }

        int x = 0;
        int z = 0;
        int[] delta = {0, -1};

        for (int i = (int) Math.pow(Math.max(width, length), 2); i > 0; i--) {
            if (-width / 2 < x && x <= width / 2 && -length / 2 < z && z <= length / 2) {
                BlockPos rayStart = location.offset(x, 100, z);
                BlockPos surfacePos = findSurface(level, rayStart);

                if (surfacePos != null && isCapBlock(level, surfacePos)
                        && surfacePos.getY() >= location.getY() - 10) {

                    Optional<ResourceLocation> feature = theme.features().randomEntry(random);
                    if (feature.isPresent() && random.nextInt(100) < 10) { // 10% chance
                        placeFeature(level, feature.get(), surfacePos.above());
                    }
                }
            }

            if (x == z || (x < 0 && x == -z) || (x > 0 && x == 1 - z)) {
                int temp = delta[0];
                delta[0] = -delta[1];
                delta[1] = temp;
            }

            x += delta[0];
            z += delta[1];
        }
    }

    private BlockPos findSurface(ServerLevel level, BlockPos start) {
        for (int y = start.getY(); y > level.getMinY(); y--) {
            BlockPos pos = new BlockPos(start.getX(), y, start.getZ());
            if (!level.getBlockState(pos).isAir() && level.getBlockState(pos.above()).isAir()) {
                return pos;
            }
        }
        return null;
    }

    private boolean isCapBlock(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return theme.surfaceBlocks().exists(state);
    }

    private void placeFeature(ServerLevel level, ResourceLocation assetId, BlockPos pos) {
        System.out.println("Placing asset: " + assetId + " at " + pos);
    }
}