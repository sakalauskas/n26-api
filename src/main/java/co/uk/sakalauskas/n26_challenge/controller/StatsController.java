package co.uk.sakalauskas.n26_challenge.controller;

import co.uk.sakalauskas.n26_challenge.exception.OldTransactionException;
import co.uk.sakalauskas.n26_challenge.model.Stats;
import co.uk.sakalauskas.n26_challenge.model.Transaction;
import co.uk.sakalauskas.n26_challenge.service.StatisticsService;
import co.uk.sakalauskas.n26_challenge.utils.TimestampUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {

    private final StatisticsService service;

    @Autowired
    public StatsController(StatisticsService statisticsService) {
        this.service = statisticsService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/transactions")
    public ResponseEntity post(@RequestBody Transaction transaction) {
        try {
            final long unix = TimestampUtil.toUnixTimestamp(System.currentTimeMillis());

            service.storeTransaction(transaction, unix);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (OldTransactionException e) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(path = "/statistics")
    public Stats statistics() {
        final long unix = TimestampUtil.toUnixTimestamp(System.currentTimeMillis());

        return service.getAggregate(unix);
    }

}
