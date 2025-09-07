package ing.boykiss.skynarchy.worldgen.island;

public record WorldIslandConfig(
        boolean placeFeatures,
        int proximity,
        double verticalStretch,
        double verticalShift,
        double horizontalShift
) {
}