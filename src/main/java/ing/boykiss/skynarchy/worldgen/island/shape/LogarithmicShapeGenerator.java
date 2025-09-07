package ing.boykiss.skynarchy.worldgen.island.shape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogarithmicShapeGenerator implements ShapeGenerator {

    @Override
    public List<Integer> generateShape(int width, int length, double verticalStretch,
                                       double verticalShift, double horizontalShift) {
        List<Integer> shape = new ArrayList<>();
        int x = 0;
        int z = 0;
        int[] delta = {0, -1};
        int rings = 1;

        for (int i = (int) Math.pow(Math.max(width, length), 2); i > 0; i--) {
            if (-width / 2 < x && x <= width / 2 && -length / 2 < z && z <= length / 2) {
                int slope = (int) Math.round(verticalStretch * Math.log(-1 * rings + horizontalShift) + verticalShift);
                shape.add(slope);
            }

            if (x == z || (x < 0 && x == -z) || (x > 0 && x == 1 - z)) {
                if (x == z) {
                    rings++;
                }
                // Change direction
                int temp = delta[0];
                delta[0] = -delta[1];
                delta[1] = temp;
            }

            x += delta[0];
            z += delta[1];
        }

        Collections.reverse(shape);
        return shape;
    }
}