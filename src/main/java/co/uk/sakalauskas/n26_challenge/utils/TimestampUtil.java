package co.uk.sakalauskas.n26_challenge.utils;

import static javax.management.timer.Timer.ONE_SECOND;

public final class TimestampUtil {


    public static long toUnixTimestamp(long timestamp) {
        return timestamp / ONE_SECOND;
    }

    public static long getPreviousMinuteFromUnixTimestamp(long timestamp) {
        return timestamp - 60;
    }
}