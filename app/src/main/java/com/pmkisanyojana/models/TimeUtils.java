package com.pmkisanyojana.models;

public class TimeUtils {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(Long time) {
        Long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }
        long diff = now - time;

        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return "" + (diff / MINUTE_MILLIS) + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return "" + (diff / HOUR_MILLIS) + " hours ago";

        } else if (diff < 48 * HOUR_MILLIS) {
            return (diff / HOUR_MILLIS)+" yesterday";

        } else {
            return "" + (diff / DAY_MILLIS) + " days ago";
        }
    }

    public static int getTimesAgo(Long time) {
        Long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return 0;
        }
        long diff = now - time;

        if (diff < 50 * MINUTE_MILLIS) {
            return (int) (diff / MINUTE_MILLIS);
        } else if (diff < 24 * HOUR_MILLIS) {
            return (int) (diff / HOUR_MILLIS);

        } else {
            return (int) (diff / DAY_MILLIS);
        }
    }
}
