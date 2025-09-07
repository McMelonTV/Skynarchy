package ing.boykiss.skynarchy.worldgen.island;

import ing.boykiss.skynarchy.worldgen.island.shape.ShapeGenerators;
import ing.boykiss.skynarchy.worldgen.island.theme.IslandTheme;
import ing.boykiss.skynarchy.worldgen.island.theme.IslandThemes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public record WorldIsland(
        Random random,
        BlockPos location,
        ServerLevel level,
        IslandTheme theme,
        int amount,
        IslandSize size
) {
    public static void createIsland(Random random, ServerLevel level, BlockPos pos, IslandSize size, int amount) {
        IslandTheme theme = IslandThemes.GRASS.getTheme();

        WorldIsland island = new WorldIsland(random, pos, level, theme, amount, size);

        WorldIslandConfig config = new WorldIslandConfig(
                true,
                30,
                1.0,
                1.0,
                1.0
        );

        island.generate(config, true);
    }

    private void clearArea() {
        int radius = 64;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -30; y <= 30; y++) {
                    BlockPos pos = location.offset(x, y, z);
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }

    private void generate(WorldIslandConfig config, boolean clearArea) {
        if (clearArea) clearArea();
        List<WorldIslandProperties> satelliteIslands = generateSatelliteIslands(config);

        for (WorldIslandProperties satelliteIsland : satelliteIslands) {
            List<Integer> shape = ShapeGenerators.LOGARITHMIC.generateShape(
                    satelliteIsland.width(),
                    satelliteIsland.length(),
                    satelliteIsland.verticalStretch(),
                    satelliteIsland.verticalShift(),
                    satelliteIsland.horizontalShift()
            );

            satelliteIsland.placeBlocks(shape);

            if (config.placeFeatures()) {
                satelliteIsland.placeFeatures();
            }
        }

        WorldIslandProperties mainIsland = createMainIsland();
        List<Integer> mainShape = ShapeGenerators.LOGARITHMIC.generateShape(
                mainIsland.width(),
                mainIsland.length(),
                mainIsland.verticalStretch(),
                mainIsland.verticalShift(),
                mainIsland.horizontalShift()
        );

        mainIsland.placeBlocks(mainShape);

        if (config.placeFeatures()) {
            mainIsland.placeFeatures();
        }

        level.setBlock(location, Blocks.BEDROCK.defaultBlockState(), 3);
    }

    private List<WorldIslandProperties> generateSatelliteIslands(WorldIslandConfig config) {
        List<WorldIslandProperties> islands = new ArrayList<>();
        BlockPos location = location();

        for (int i = 0; i < amount; i++) {
            int xOffset = random.nextInt(config.proximity() * 2 + 1) - config.proximity();
            int yOffset = random.nextInt(9) - 4;
            int zOffset = random.nextInt(config.proximity() * 2 + 1) - config.proximity();

            int width = size.getRandomSize();
            int length = size.getRandomSize();

            WorldIslandProperties satellite = new WorldIslandProperties(
                    random,
                    location.offset(xOffset, yOffset, zOffset),
                    level,
                    theme,
                    width,
                    length,
                    config.verticalStretch() + (random.nextDouble() * 2 - 1),
                    config.verticalShift(),
                    config.horizontalShift() + (random.nextDouble() * 2 - 1)
            );

            islands.add(satellite);
        }

        return islands;
    }

    private WorldIslandProperties createMainIsland() {
        int width = IslandSize.LARGE.getRandomSize();
        int length = IslandSize.LARGE.getRandomSize();

        return new WorldIslandProperties(
                random,
                location,
                level,
                theme,
                width,
                length,
                1.0,
                1.0,
                random.nextInt(5) + width - 4
        );
    }
}