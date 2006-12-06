package net.sourceforge.fenixedu.domain.accounting.util;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.lang.StringUtils;

/**
 * Code Format: <studentNumber{6}><typeDigit{1}><controlDigits{2}>
 * 
 */
public class PaymentCodeGenerator {

    private static final int CONTROL_DIGITS_LENGTH = 2;

    private static final String CODE_FILLER = "0";

    private static final int STUDENT_NUMBER_LENGTH = 6;

    private static final int CODE_LENGTH = 9;

    private PaymentCodeGenerator() {

    }

    public static boolean canGenerateNewCode(final PaymentCodeType paymentCodeType, final Student student) {
	final PaymentCode lastPaymentCode = findLastPaymentCode(paymentCodeType, student);
	return (lastPaymentCode == null) ? true
		: (getSignificantNumberForCodeGeneration(lastPaymentCode) + 1 <= 99);
    }

    public static String generateNewCodeFor(final PaymentCodeType paymentCodeType, final Student student) {
	final PaymentCode lastPaymentCode = findLastPaymentCode(paymentCodeType, student);

	return lastPaymentCode == null ? generateFirstCodeForType(paymentCodeType, student)
		: generateNewCodeBasedOnLastPaymentCode(lastPaymentCode);
    }

    private static PaymentCode findLastPaymentCode(final PaymentCodeType paymentCodeType, Student student) {
	final List<PaymentCode> paymentCodes = student.getPaymentCodesBy(paymentCodeType);
	return paymentCodes.isEmpty() ? null : Collections.max(paymentCodes,
		PaymentCode.COMPARATOR_BY_CODE);
    }

    private static String generateFirstCodeForType(final PaymentCodeType paymentCodeType,
	    final Student student) {
	return generateFinalCode(paymentCodeType, student, 0);
    }

    private static String generateNewCodeBasedOnLastPaymentCode(PaymentCode paymentCode) {
	return generateNewCodeBasedOnSignificantNumber(paymentCode.getType(), paymentCode.getStudent(),
		getSignificantNumberForCodeGeneration(paymentCode));
    }

    private static int getSignificantNumberForCodeGeneration(final PaymentCode lastPaymentCode) {
	return Integer.valueOf(lastPaymentCode.getCode().substring(
		lastPaymentCode.getCode().length() - 2));
    }

    private static String generateNewCodeBasedOnSignificantNumber(final PaymentCodeType paymentCodeType,
	    final Student student, int number) {
	return generateFinalCode(paymentCodeType, student, number + 1);
    }

    private static String generateFinalCode(final PaymentCodeType paymentCodeType,
	    final Student student, int digits) {
	final String finalCode = getCodePrefix(paymentCodeType, student)
		+ StringUtils.leftPad(String.valueOf(digits), CONTROL_DIGITS_LENGTH);

	if (finalCode.length() != CODE_LENGTH) {
	    throw new RuntimeException("Unexpected code length for generated code");
	}

	return finalCode;

    }

    private static String getCodePrefix(final PaymentCodeType paymentCodeType, final Student student) {
	return StringUtils.leftPad(student.getNumber().toString(), STUDENT_NUMBER_LENGTH, CODE_FILLER)
		+ paymentCodeType.getTypeDigit();
    }

    public static Integer getStudentNumberFrom(final String code) {
	return Integer.valueOf(code.substring(0, STUDENT_NUMBER_LENGTH));
    }

}
