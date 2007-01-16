package net.sourceforge.fenixedu.util.sibs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class SibsIncomingPaymentFile {

    private static final String DETAIL_REGISTER_TYPE = "2";

    public static class DetailLine {

	private static final String DATE_TIME_FORMAT = "yyyyMMddHHmm";

	private DateTime whenOccuredTransaction;

	private Money amount;

	private String sibsTransactionId;

	private String code;

	private static final int[] FIELD_SIZE = new int[] { 1, 2, 4, 8, 12, 10, 5, 2, 10, 5, 15, 9, 1,
		1, 12, 3 };

	public static DetailLine buildFrom(String rawLine) {
	    final String[] fields = splitLine(rawLine);
	    return new DetailLine(getWhenOccuredTransactionFrom(fields), getAmountFrom(fields),
		    getSibsTransactionIdFrom(fields), getCodeFrom(fields));
	}

	public DetailLine(DateTime whenOccuredTransactionFrom, Money amountFrom,
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
	    final String[] result = new String[FIELD_SIZE.length];
	    for (int i = 0; i < FIELD_SIZE.length; i++) {
		result[i] = line.substring(lastIndex, lastIndex + FIELD_SIZE[i]);
		lastIndex += FIELD_SIZE[i];
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

    private List<DetailLine> detailLines;

    private SibsIncomingPaymentFile() {
	this.detailLines = new ArrayList<DetailLine>();
    }

    public static SibsIncomingPaymentFile parse(InputStream stream) {

	final SibsIncomingPaymentFile sibsIncomingPaymentFile = new SibsIncomingPaymentFile();
	final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

	try {
	    String line = reader.readLine();
	    while (line != null) {
		if (isDetail(line)) {
		    sibsIncomingPaymentFile.addLine(line);
		}
		line = reader.readLine();
	    }
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}

	return sibsIncomingPaymentFile;

    }

    private static boolean isDetail(String line) {
	return line.startsWith(DETAIL_REGISTER_TYPE);
    }

    public void addLine(final String detail) {
	this.detailLines.add(DetailLine.buildFrom(detail));
    }

    public List<DetailLine> getDetailLines() {
	return Collections.unmodifiableList(detailLines);
    }
    
    public int getDetailLinesCount() {
	return this.detailLines.size();
    }
}
