package ing.boykiss.skynarchy.util.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class WeightedSet<T> {
    private final List<ThresholdEntry<T>> entries = new ArrayList<>();
    private long totalCount = 0;

    public WeightedSet(List<WeightedEntry<T>> entries) {
        entries.forEach(this::add);
    }

    private void add(WeightedEntry<T> entry) {
        if (entry.weight() <= 0) {
            return;
        }

        totalCount += entry.weight();
        entries.add(new ThresholdEntry<>(entry.item(), totalCount));
    }

    public boolean exists(T o) {
        for (ThresholdEntry<T> entry : entries) {
            if (o.equals(entry.item)) return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public Optional<T> randomEntry(Random random) {
        if (totalCount == 0) {
            return Optional.empty();
        }

        long randomIndex = random.nextLong(totalCount);

        int left = 0, right = entries.size() - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (randomIndex < entries.get(mid).threshold()) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        return Optional.of(entries.get(left).item());
    }

    public record WeightedEntry<T>(T item, int weight) {
    }

    private record ThresholdEntry<T>(T item, long threshold) {
    }
}
