package co.uk.sakalauskas.n26_challenge.service;

import co.uk.sakalauskas.n26_challenge.exception.OldTransactionException;
import co.uk.sakalauskas.n26_challenge.model.Stats;
import co.uk.sakalauskas.n26_challenge.model.Transaction;

public interface StatisticsService {

    void storeTransaction(Transaction transaction, long timestamp) throws OldTransactionException;

    Stats getAggregate(long currentTime);

}
