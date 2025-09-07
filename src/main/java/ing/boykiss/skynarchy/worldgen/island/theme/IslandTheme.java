package ing.boykiss.skynarchy.worldgen.island.theme;

import ing.boykiss.skynarchy.util.random.WeightedSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public record IslandTheme(
        WeightedSet<ResourceLocation> features,
        WeightedSet<BlockState> surfaceBlocks,
        WeightedSet<BlockState> topBlocks,
        WeightedSet<BlockState> bottomBlocks
) {
}
