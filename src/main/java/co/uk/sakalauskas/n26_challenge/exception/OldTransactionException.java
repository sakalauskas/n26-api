package co.uk.sakalauskas.n26_challenge.exception;

public class OldTransactionException extends Exception {
    public OldTransactionException() {
        super("Transaction is older then last 60 seconds");
    }
}