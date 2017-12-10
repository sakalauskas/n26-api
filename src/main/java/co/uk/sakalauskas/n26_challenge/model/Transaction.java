package co.uk.sakalauskas.n26_challenge.model;


public class Transaction {

    private double amount;

    private long timestamp;

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
