package net.sourceforge.fenixedu.domain.accounting.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.rectorate.RectoratePaymentCode;

import org.apache.commons.lang.StringUtils;

public class RectoratePaymentCodeGenerator extends PaymentCodeGenerator {

    public static Comparator<PaymentCode> COMPARATOR_BY_PAYMENT_SEQUENTIAL_DIGITS = new Comparator<PaymentCode>() {
	@Override
	public int compare(PaymentCode leftPaymentCode, PaymentCode rightPaymentCode) {
	    final String leftSequentialNumber = getSequentialNumber(leftPaymentCode);
	    final String rightSequentialNumber = getSequentialNumber(rightPaymentCode);

	    int comparationResult = leftSequentialNumber.compareTo(rightSequentialNumber);

	    System.out.println(String.format("left [%s], right [%s], result [%s]", leftSequentialNumber, rightSequentialNumber,
		    comparationResult));

	    return (comparationResult == 0) ? leftPaymentCode.getIdInternal().compareTo(rightPaymentCode.getIdInternal())
		    : comparationResult;
	}
    };

    private static final String CODE_FILLER = "0";
    private static final int NUM_CONTROL_DIGITS = 2;
    private static final int NUM_SEQUENTIAL_NUMBERS = 6;

    @Override
    public boolean canGenerateNewCode(PaymentCodeType paymentCodeType, Person person) {
	final PaymentCode lastPaymentCode = findLastPaymentCode();
	return lastPaymentCode == null ? true : Integer.valueOf(getSequentialNumber(lastPaymentCode)) < 999999;
    }

    private PaymentCode findLastPaymentCode() {

	final List<RectoratePaymentCode> rectoratePaymentCodes = RectoratePaymentCode.getAllRectoratePaymentCodes();
	return rectoratePaymentCodes.isEmpty() ? null : Collections.max(rectoratePaymentCodes,
		COMPARATOR_BY_PAYMENT_SEQUENTIAL_DIGITS);
    }

    @Override
    public String generateNewCodeFor(PaymentCodeType paymentCodeType, Person person) {
	final PaymentCode lastPaymentCode = findLastPaymentCode();
	int nextSequentialNumber = lastPaymentCode == null ? 0 : Integer.valueOf(getSequentialNumber(lastPaymentCode)) + 1;

	String sequentialNumberPadded = StringUtils.leftPad(String.valueOf(nextSequentialNumber), NUM_SEQUENTIAL_NUMBERS,
		CODE_FILLER);
	String controDigitsPadded = StringUtils.leftPad(String.valueOf((new Random()).nextInt(99)), NUM_CONTROL_DIGITS,
		CODE_FILLER);

	return START + sequentialNumberPadded + controDigitsPadded;
    }

    private static final String START = "9";

    @Override
    public boolean isCodeMadeByThisFactory(PaymentCode paymentCode) {
	return paymentCode.getCode().startsWith(START);
    }

    private static String getSequentialNumber(PaymentCode paymentCode) {
	String sequentialNumber = paymentCode.getCode().substring(1, paymentCode.getCode().length() - NUM_CONTROL_DIGITS);

	return sequentialNumber;
    }

}
