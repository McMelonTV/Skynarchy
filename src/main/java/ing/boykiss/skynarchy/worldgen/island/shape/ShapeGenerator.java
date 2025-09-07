package ing.boykiss.skynarchy.worldgen.island.shape;

import java.util.List;

public interface ShapeGenerator {
    List<Integer> generateShape(
            int width,
            int length,
            double verticalStretch,
            double verticalShift,
            double horizontalShift
    );
}