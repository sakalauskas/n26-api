package co.uk.sakalauskas.n26_challenge.model;


public class Stats {

    private double sum;
    private double avg;
    private double max;
    private double min;
    private long count;

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getAvg() {
        return avg;
    }

    public double getSum() {
        return sum;
    }

    public long getCount() {
        return count;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
