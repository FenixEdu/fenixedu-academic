package net.sourceforge.fenixedu.util.sibs.incomming;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.joda.time.YearMonthDay;

public class SibsIncommingPaymentFileHeader {

    private static final String DATE_FORMAT = "yyyyMMdd";

    private static final int[] FIELD_SIZES = new int[] { 1, 4, 8, 8, 9, 9, 5, 3, 2, 4, 47 };

    private YearMonthDay whenProcessedBySibs;

    private Integer version;

    private SibsIncommingPaymentFileHeader(YearMonthDay whenProcessedBySibs, Integer version) {
	this.whenProcessedBySibs = whenProcessedBySibs;
	this.version = version;
    }

    public static SibsIncommingPaymentFileHeader buildFrom(String rawLine) {
	final String[] fields = splitLine(rawLine);
	return new SibsIncommingPaymentFileHeader(getWhenProcessedBySibsFrom(fields),
		getVersionFrom(fields));
    }

    private static YearMonthDay getWhenProcessedBySibsFrom(String[] fields) {
	try {
	    return new YearMonthDay(new SimpleDateFormat(DATE_FORMAT).parse(fields[4].substring(0,
		    DATE_FORMAT.length())));
	} catch (ParseException e) {
	    throw new RuntimeException(e);
	}
    }

    private static Integer getVersionFrom(String[] fields) {
	return Integer.valueOf(fields[5].substring(DATE_FORMAT.length()));
    }

    private final static String[] splitLine(final String line) {
	int lastIndex = 0;
	final String[] result = new String[FIELD_SIZES.length];
	for (int i = 0; i < FIELD_SIZES.length; i++) {
	    result[i] = line.substring(lastIndex, lastIndex + FIELD_SIZES[i]);
	    lastIndex += FIELD_SIZES[i];
	}
	return result;
    }

    public YearMonthDay getWhenProcessedBySibs() {
	return whenProcessedBySibs;
    }

    public Integer getVersion() {
	return version;
    }

}
