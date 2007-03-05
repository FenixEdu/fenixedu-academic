package net.sourceforge.fenixedu.util.sibs;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

/**
 * 
 * @author naat
 * 
 */
public class SibsOutgoingPaymentFile {

    private static final String DATE_FORMAT = "yyyyMMdd";

    private static final String NUMBER_FILLER = "0";

    private static class Header {

	private static final String HEADER_REGISTER_TYPE = "0";

	private static final String FILE_TYPE = "AEPS";

	private static final String OMISSION_SEQUENCE_NUMBER = "1";

	private static final String CURRENCY_CODE = "978";

	private static final int WHITE_SPACES_IN_HEADER = 3;

	private String sourceInstitutionId;

	private String destinationInstitutionId;

	private String entityCode;

	public Header(String sourceInstitutionId, String destinationInstitutionId, String entityCode) {
	    this.sourceInstitutionId = sourceInstitutionId;
	    this.destinationInstitutionId = destinationInstitutionId;
	    this.entityCode = entityCode;
	}

	public String render() {
	    final StringBuilder header = new StringBuilder();
	    header.append(HEADER_REGISTER_TYPE);
	    header.append(FILE_TYPE);
	    header.append(this.sourceInstitutionId);
	    header.append(this.destinationInstitutionId);
	    header.append(new YearMonthDay().toString(DATE_FORMAT));
	    header.append(OMISSION_SEQUENCE_NUMBER);
	    // last file's data if it was already sent
	    header.append("00000000");
	    header.append(OMISSION_SEQUENCE_NUMBER);
	    header.append(this.entityCode);
	    header.append(CURRENCY_CODE);
	    header.append(StringUtils.leftPad("", WHITE_SPACES_IN_HEADER));
	    header.append("\n");

	    return header.toString();
	}
    }

    private static class Footer {

	private static final String FOOTER_REGISTER_TYPE = "9";

	private static final int NUMBER_OF_LINES_DESCRIPTOR_LENGTH = 8;

	public static final int WHITE_SPACES_IN_FOOTER = 41;

	public Footer() {
	}

	public String render(int totalLines) {
	    final StringBuilder footer = new StringBuilder();
	    footer.append(FOOTER_REGISTER_TYPE);
	    footer.append(StringUtils.leftPad(String.valueOf(totalLines),
		    NUMBER_OF_LINES_DESCRIPTOR_LENGTH, NUMBER_FILLER));
	    footer.append(StringUtils.leftPad("", WHITE_SPACES_IN_FOOTER));
	    footer.append("\n");

	    return footer.toString();
	}
    }

    private static class Line {
	private static final String LINE_REGISTER_TYPE = "1";

	// Line Processing code (usually 80 but it can be 82)
	private static final String LINE_PROCESSING_CODE = "80";

	private static int DECIMAL_PLACES_FACTOR = 100;

	private static final int WHITE_SPACES_IN_LINE = 2;

	private static final int AMOUNT_LENGTH = 10;

	private String code;

	private Money minAmount;

	private Money maxAmount;

	private YearMonthDay startDate;

	private YearMonthDay endDate;

	public Line(String code, Money minAmount, Money maxAmount, YearMonthDay startDate,
		YearMonthDay endDate) {

	    checkAmounts(code, minAmount, maxAmount);

	    this.code = code;
	    this.minAmount = minAmount;
	    this.maxAmount = maxAmount;
	    this.startDate = startDate;
	    this.endDate = endDate;
	}

	private void checkAmounts(String code, Money minAmount, Money maxAmount) {
	    if (minAmount.lessOrEqualThan(Money.ZERO)) {
		throw new RuntimeException(MessageFormat.format(
			"Min amount for code {0} must be greater than zero", code));
	    }

	    if (maxAmount.lessOrEqualThan(Money.ZERO)) {
		throw new RuntimeException(MessageFormat.format(
			"Max amount for code {0} must be greater than zero", code));
	    }

	}

	public String render() {
	    final StringBuilder result = new StringBuilder();

	    result.append(LINE_REGISTER_TYPE);
	    result.append(LINE_PROCESSING_CODE);
	    result.append(this.code);
	    result.append(this.endDate.toString(DATE_FORMAT));
	    result.append(leftPadAmount(this.maxAmount));
	    result.append(this.startDate.toString(DATE_FORMAT));
	    result.append(leftPadAmount(this.minAmount));
	    result.append(StringUtils.leftPad("", WHITE_SPACES_IN_LINE));
	    result.append("\n");

	    return result.toString();
	}

	private String leftPadAmount(final Money amount) {
	    return StringUtils.leftPad(String.valueOf(amount.multiply(
		    BigDecimal.valueOf(DECIMAL_PLACES_FACTOR)).longValue()), AMOUNT_LENGTH,
		    NUMBER_FILLER);
	}
    }

    private Header header;

    private List<Line> lines;

    private Footer footer;

    private Set<String> existingCodes;

    public SibsOutgoingPaymentFile(String sourceInstitutionId, String destinationInstitutionId,
	    String entity) {
	this.header = new Header(sourceInstitutionId, destinationInstitutionId, entity);
	this.lines = new ArrayList<Line>();
	this.footer = new Footer();
	this.existingCodes = new HashSet<String>();
    }

    public void addLine(String code, Money minAmount, Money maxAmount, YearMonthDay startDate,
	    YearMonthDay endDate) {
	if (existingCodes.contains(code)) {
	    throw new RuntimeException(MessageFormat.format("Code {0} is duplicated", code));
	}

	existingCodes.add(code);

	this.lines.add(new Line(code, minAmount, maxAmount, startDate, endDate));
    }

    public String render() {
	final StringBuilder result = new StringBuilder();

	result.append(this.header.render());

	for (final Line line : this.lines) {
	    result.append(line.render());
	}

	result.append(this.footer.render(this.lines.size()));

	return result.toString();
    }

    @Override
    public String toString() {
	return render();
    }

    public void save(final File destinationFile) {
	BufferedOutputStream outputStream = null;
	try {
	    outputStream = new BufferedOutputStream(new FileOutputStream(destinationFile));
	    outputStream.write(render().getBytes());
	    outputStream.flush();
	} catch (FileNotFoundException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	} finally {
	    if (outputStream != null) {
		try {
		    outputStream.close();
		} catch (IOException e) {
		    throw new RuntimeException(e);
		}
	    }
	}
    }

}
