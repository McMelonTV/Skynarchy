package ing.boykiss.skynarchy.worldgen.island;

public enum IslandSize {
    SMALL(16, 4),
    MEDIUM(24, 4),
    LARGE(32, 4);

    private final int maxSize;
    private final int variance;

    IslandSize(int maxSize, int variance) {
        this.maxSize = maxSize;
        this.variance = variance;
    }

    public int getRandomSize() {
        return maxSize - (int) (Math.random() * variance);
    }
}
