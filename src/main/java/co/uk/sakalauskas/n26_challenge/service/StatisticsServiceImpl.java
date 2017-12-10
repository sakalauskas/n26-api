package co.uk.sakalauskas.n26_challenge.service;

import co.uk.sakalauskas.n26_challenge.dao.StatisticsDao;
import co.uk.sakalauskas.n26_challenge.exception.OldTransactionException;
import co.uk.sakalauskas.n26_challenge.model.Stats;
import co.uk.sakalauskas.n26_challenge.model.Transaction;
import co.uk.sakalauskas.n26_challenge.utils.TimestampUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsDao dao;

    @Autowired
    public StatisticsServiceImpl(StatisticsDao dao) {
        this.dao = dao;
    }

    @Override
    public void storeTransaction(Transaction transaction, long now) throws OldTransactionException {

        long timestamp = TimestampUtil.toUnixTimestamp(transaction.getTimestamp());

        // 60 secs
        if (TimestampUtil.getPreviousMinuteFromUnixTimestamp(now) > timestamp) {
            throw new OldTransactionException();
        }

        synchronized (this) {
            dao.removeOldTransactions(now);

            final Stats old = dao.getByTimestamp(timestamp);

            final Stats stats = createUpdatedStatistics(transaction, old);

            dao.updateStatisticsForTimestamp(timestamp, stats);
        }
    }

    private Stats createUpdatedStatistics(Transaction transaction, Stats previous) {

        Stats stats = new Stats();

        final double amount = transaction.getAmount();
        stats.setSum(previous.getSum() + amount);
        stats.setMax(Math.max(previous.getMax(), amount));
        // we don't want zeros as our min value
        Optional<Double> min = getMin(previous.getMin(), amount);
        if (min.isPresent()) {
            stats.setMin(min.get());
        }
        stats.setCount(previous.getCount() + 1);

        return stats;
    }

    @Override
    public synchronized Stats getAggregate(long now) {

        final long start = TimestampUtil.getPreviousMinuteFromUnixTimestamp(now);

        Stats aggregate = new Stats();

        for (int i = 0; i <= 60; i++) {
            long nextSecond = start + i;
            final Stats byTimestamp = dao.getByTimestamp(nextSecond);

            updateAggregate(aggregate, byTimestamp);
        }

        if (aggregate.getSum() > 0) {
            aggregate.setAvg(aggregate.getSum() / aggregate.getCount());
        }

        return aggregate;
    }

    private void updateAggregate(Stats aggregate, Stats statisticsBySecond) {
        aggregate.setSum(aggregate.getSum() + statisticsBySecond.getSum());

        aggregate.setMax(Math.max(statisticsBySecond.getMax(), aggregate.getMax()));

        // we don't want zeros as our min value
        Optional<Double> min = getMin(aggregate.getMin(), statisticsBySecond.getMin());
        if (min.isPresent()) {
            aggregate.setMin(min.get());
        }

        aggregate.setCount(aggregate.getCount() + statisticsBySecond.getCount());
    }

    /**
     * Retrieves a minimum value excluding the 0's
     *
     * @param values
     * @return
     */
    private Optional<Double> getMin(Double...values) {
        return Stream.of(values).filter(x -> x > 0).min(Double::compare);
    }
}
