/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.util.sibs.incomming;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class SibsIncommingPaymentFileDetailLine implements Serializable {

    private static final String DATE_TIME_FORMAT = "yyyyMMddHHmm";
    private static final long serialVersionUID = 6203220766899694357L;

    private DateTime whenOccuredTransaction;

    private Money amount;

    private String sibsTransactionId;

    private String code;

    private SibsIncommingPaymentFileHeader header;

    private String rawLine;

    private static final int[] FIELD_SIZES = { 1, 2, 4, 8, 12, 10, 5, 2, 10, 20, 0, 9, 1, 1, 12, 3 };

    public static SibsIncommingPaymentFileDetailLine buildFrom(SibsIncommingPaymentFileHeader header, String rawLine) {
        final String[] fields = splitLine(rawLine);
        return new SibsIncommingPaymentFileDetailLine(rawLine, header, getWhenOccuredTransactionFrom(fields), getAmountFrom(fields),
                getSibsTransactionIdFrom(fields), getCodeFrom(fields));
    }

    private SibsIncommingPaymentFileDetailLine(String rawLine, SibsIncommingPaymentFileHeader header, DateTime whenOccuredTransactionFrom, Money amountFrom,
            String sibsTransactionIdFrom, String codeFrom) {
        this.rawLine = rawLine;
        this.header = header;
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

    public String export() {
        return String.format("%s%n%s", header.getRawHeader(), rawLine);
    }

    public static SibsIncommingPaymentFileDetailLine importFromString(String rawHeaderAndLine) {
        if (Strings.isNullOrEmpty(rawHeaderAndLine)) {
            return null;
        }
        final List<String> headerAndLine = Splitter.on("\n").splitToList(rawHeaderAndLine);

        if (headerAndLine.size() != 2) {
            throw new UnsupportedOperationException("SibsIncommingPaymentFileDetailLine not formatted accordingly:" + rawHeaderAndLine);
        }

        final SibsIncommingPaymentFileHeader sibsIncommingPaymentFileHeader = SibsIncommingPaymentFileHeader.buildFrom(headerAndLine.get(0));
        return buildFrom(sibsIncommingPaymentFileHeader, headerAndLine.get(1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SibsIncommingPaymentFileDetailLine that = (SibsIncommingPaymentFileDetailLine) o;
        return Objects.equals(header.getRawHeader(), that.header.getRawHeader()) && Objects.equals(rawLine, that.rawLine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header.getRawHeader(), rawLine);
    }

    public SibsIncommingPaymentFileHeader getHeader() {
        return header;
    }
}
