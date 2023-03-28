package src.stats_accumulator;

public class StatsAccumulatorIml implements StatsAccumulator {
    private int minNumber = Integer.MAX_VALUE;
    private int maxNumber = Integer.MIN_VALUE;
    private int count = 0;
    private int sum;


    @Override
    public void add(int value) {
        if (value < minNumber) {
            minNumber = value;
        } else if (value > maxNumber) {
            maxNumber = value;
        }
        sum += value;
        count++;
    }

    @Override
    public int getMin() {
        return minNumber == Integer.MAX_VALUE ? 0 : minNumber;
    }

    @Override
    public int getMax() {
        return maxNumber == Integer.MIN_VALUE ? 0 : maxNumber;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Double getAvg() {
        return count > 0 ? sum / (double) count : count;
    }
}
