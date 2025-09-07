package ing.boykiss.skynarchy.worldgen.island.theme;

import ing.boykiss.skynarchy.util.random.WeightedSet;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public enum IslandThemes {
    GRASS(new IslandTheme(
            new WeightedSet<>(List.of(

            )),
            new WeightedSet<>(List.of(
                    new WeightedSet.WeightedEntry<>(Blocks.GRASS_BLOCK.defaultBlockState(), 100),
                    new WeightedSet.WeightedEntry<>(Blocks.MOSS_BLOCK.defaultBlockState(), 25)
            )),
            new WeightedSet<>(List.of(
                    new WeightedSet.WeightedEntry<>(Blocks.DIRT.defaultBlockState(), 100),
                    new WeightedSet.WeightedEntry<>(Blocks.ROOTED_DIRT.defaultBlockState(), 10)
            )),
            new WeightedSet<>(List.of(
                    new WeightedSet.WeightedEntry<>(Blocks.COBBLESTONE.defaultBlockState(), 100),
                    new WeightedSet.WeightedEntry<>(Blocks.STONE.defaultBlockState(), 100),
                    new WeightedSet.WeightedEntry<>(Blocks.IRON_ORE.defaultBlockState(), 10),
                    new WeightedSet.WeightedEntry<>(Blocks.REDSTONE_ORE.defaultBlockState(), 10),
                    new WeightedSet.WeightedEntry<>(Blocks.COAL_ORE.defaultBlockState(), 10),
                    new WeightedSet.WeightedEntry<>(Blocks.COPPER_ORE.defaultBlockState(), 15)
            ))
    ));

    private final IslandTheme theme;

    IslandThemes(IslandTheme theme) {
        this.theme = theme;
    }

    public IslandTheme getTheme() {
        return theme;
    }
}
