package com.holla.group1.holla.util;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class DateTimeFormatter {
    public static String getTimestampAgo(DateTime dateTime) {
        Period delta = new Period(dateTime, new DateTime());
        return getPeriodFormatter(delta).print(delta);
    }

    public static PeriodFormatter getPeriodFormatter(Period period) {
        PeriodFormatter formatter;

        if (period.getYears() != 0) {
            formatter = new PeriodFormatterBuilder().appendYears().appendSuffix("y").printZeroNever().toFormatter();
        } else if (period.getMonths() != 0) {
            formatter = new PeriodFormatterBuilder().appendMonths().appendSuffix("mo").printZeroNever().toFormatter();
        } else if (period.getWeeks() != 0) {
            formatter = new PeriodFormatterBuilder().appendWeeks().appendSuffix("w").printZeroNever().toFormatter();
        } else if (period.getDays() != 0) {
            formatter = new PeriodFormatterBuilder().appendDays().appendSuffix("d").printZeroNever().toFormatter();
        } else if (period.getHours() != 0) {
            formatter = new PeriodFormatterBuilder().appendHours().appendSuffix("h").printZeroNever().toFormatter();
        } else if (period.getMinutes() != 0) {
            formatter = new PeriodFormatterBuilder().appendMinutes().appendSuffix("m").printZeroNever().toFormatter();
        } else {
            formatter = new PeriodFormatterBuilder().appendSeconds().appendSuffix("s").printZeroNever().toFormatter();
        }

        return formatter;
    }
}
