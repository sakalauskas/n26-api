package co.uk.sakalauskas.n26_challenge.dao;

import co.uk.sakalauskas.n26_challenge.model.Stats;
import co.uk.sakalauskas.n26_challenge.utils.TimestampUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StatisticsDaoImpl implements StatisticsDao {

    private Map<Long, Stats> statistics = new HashMap<>();

    @Override
    public synchronized void updateStatisticsForTimestamp(long timestamp, Stats stats) {
        this.statistics.put(timestamp, stats);
    }

    @Override
    public synchronized Stats getByTimestamp(long timestamp) {
        return statistics.getOrDefault(timestamp, new Stats());
    }

    @Override
    public synchronized void removeOldTransactions(long currentTime) {
        final long start = TimestampUtil.getPreviousMinuteFromUnixTimestamp(currentTime);

        Map<Long, Stats> upToDateStatistics = new HashMap<>();
        for (int i = 0; i <= 60; i++) {
            long second = start + i;
            final Stats statisticsBySecond = this.statistics.get(second);
            if (statisticsBySecond != null) {
                upToDateStatistics.put(second, statisticsBySecond);
            }
        }

        statistics = upToDateStatistics;
    }
}