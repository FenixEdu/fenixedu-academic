package net.sourceforge.fenixedu.util.sibs.incomming;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class SibsIncommingPaymentFileDetailLine {



    private static final String DATE_TIME_FORMAT = "yyyyMMddHHmm";

    private DateTime whenOccuredTransaction;

    private Money amount;

    private String sibsTransactionId;

    private String code;

    private static final int[] FIELD_SIZES = new int[] { 1, 2, 4, 8, 12, 10, 5, 2, 10, 5, 15, 9, 1, 1,
	    12, 3 };

    public static SibsIncommingPaymentFileDetailLine buildFrom(String rawLine) {
	final String[] fields = splitLine(rawLine);
	return new SibsIncommingPaymentFileDetailLine(getWhenOccuredTransactionFrom(fields),
		getAmountFrom(fields), getSibsTransactionIdFrom(fields), getCodeFrom(fields));
    }

    private SibsIncommingPaymentFileDetailLine(DateTime whenOccuredTransactionFrom, Money amountFrom,
	    String sibsTransactionIdFrom, String codeFrom) {
	this.whenOccuredTransaction = whenOccuredTransactionFrom;
	this.amount = amountFrom;
	this.sibsTransactionId = sibsTransactionIdFrom;
	this.code = codeFrom;
    }

    private static String getCodeFrom(String[] fields) {
	return fields[11];
    }

    private static String getSibsTransactionIdFrom(String[] fields) {
	return fields[9];
    }

    private static Money getAmountFrom(String[] fields) {
	return new Money(fields[5].substring(0, 8) + "." + fields[5].substring(8));
    }

    private static DateTime getWhenOccuredTransactionFrom(String[] fields) {
	try {
	    return new DateTime(new SimpleDateFormat(DATE_TIME_FORMAT).parse(fields[4]));
	} catch (ParseException e) {
	    throw new RuntimeException(e);
	}
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

    public Money getAmount() {
	return amount;
    }

    public void setAmount(Money amount) {
	this.amount = amount;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getSibsTransactionId() {
	return sibsTransactionId;
    }

    public void setSibsTransactionId(String sibsTransactionId) {
	this.sibsTransactionId = sibsTransactionId;
    }

    public DateTime getWhenOccuredTransaction() {
	return whenOccuredTransaction;
    }

    public void setWhenOccuredTransaction(DateTime whenOccuredTransaction) {
	this.whenOccuredTransaction = whenOccuredTransaction;
    }
}
