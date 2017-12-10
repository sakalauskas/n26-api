package co.uk.sakalauskas.n26_challenge.dao;

import co.uk.sakalauskas.n26_challenge.model.Stats;

public interface StatisticsDao {


    void updateStatisticsForTimestamp(long timestamp, Stats statistics);

    Stats getByTimestamp(long timestamp);

    void removeOldTransactions(long currentTime);

}