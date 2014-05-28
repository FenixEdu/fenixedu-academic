/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.accounting.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;

import org.apache.commons.lang.StringUtils;

/**
 * Code Format: <numericPartOfIstId{6}><typeDigit{1}><controlDigits{2}>
 */
@Deprecated
public class PersonRotationPaymentCodeGenerator extends PaymentCodeGenerator {

    public static Comparator<PaymentCode> COMPARATOR_BY_PAYMENT_CODE_CONTROL_DIGITS = new Comparator<PaymentCode>() {
        @Override
        public int compare(PaymentCode leftPaymentCode, PaymentCode rightPaymentCode) {
            final String leftCodeControlDigits =
                    leftPaymentCode.getCode().substring(leftPaymentCode.getCode().length() - CONTROL_DIGITS_LENGTH);
            final String rightCodeControlDigits =
                    rightPaymentCode.getCode().substring(rightPaymentCode.getCode().length() - CONTROL_DIGITS_LENGTH);

            int comparationResult = leftCodeControlDigits.compareTo(rightCodeControlDigits);

            return (comparationResult == 0) ? leftPaymentCode.getExternalId().compareTo(rightPaymentCode.getExternalId()) : comparationResult;
        }
    };

    private static final int CONTROL_DIGITS_LENGTH = 2;

    private static final String CODE_FILLER = "0";

    private static final int PERSON_CODE_LENGTH = 6;

    private static final int CODE_LENGTH = 9;

    public PersonRotationPaymentCodeGenerator() {
    }

    @Override
    public boolean canGenerateNewCode(final PaymentCodeType paymentCodeType, final Person person) {
        final PaymentCode lastPaymentCode = findLastPaymentCode(paymentCodeType, person);
        return (lastPaymentCode == null) ? true : (getSignificantNumberForCodeGeneration(lastPaymentCode) + 1 <= 99);
    }

    @Override
    public String generateNewCodeFor(final PaymentCodeType paymentCodeType, final Person person) {
        final PaymentCode lastPaymentCode = findLastPaymentCode(paymentCodeType, person);
        return lastPaymentCode == null ? generateFirstCodeForType(paymentCodeType, person) : generateNewCodeBasedOnLastPaymentCode(lastPaymentCode);
    }

    private PaymentCode findLastPaymentCode(final PaymentCodeType paymentCodeType, Person person) {
        final List<PaymentCode> paymentCodes = new ArrayList<PaymentCode>();
        for (PaymentCode code : person.getPaymentCodesBy(paymentCodeType)) {
            if (isCodeMadeByThisFactory(code)) {
                paymentCodes.add(code);
            }
        }
        return paymentCodes.isEmpty() ? null : Collections.max(paymentCodes, COMPARATOR_BY_PAYMENT_CODE_CONTROL_DIGITS);
    }

    private static String generateFirstCodeForType(final PaymentCodeType paymentCodeType, final Person person) {
        return generateFinalCode(paymentCodeType, person, 0);
    }

    private static String generateNewCodeBasedOnLastPaymentCode(PaymentCode paymentCode) {
        return generateNewCodeBasedOnSignificantNumber(paymentCode.getType(), paymentCode.getPerson(),
                getSignificantNumberForCodeGeneration(paymentCode));
    }

    private static int getSignificantNumberForCodeGeneration(final PaymentCode lastPaymentCode) {
        return Integer.valueOf(lastPaymentCode.getCode().substring(lastPaymentCode.getCode().length() - 2));
    }

    private static String generateNewCodeBasedOnSignificantNumber(final PaymentCodeType paymentCodeType, final Person person,
            int number) {
        return generateFinalCode(paymentCodeType, person, number + 1);
    }

    private static String generateFinalCode(final PaymentCodeType paymentCodeType, final Person person, int digits) {
        final String finalCode =
                getCodePrefix(paymentCodeType, person)
                        + StringUtils.leftPad(String.valueOf(digits), CONTROL_DIGITS_LENGTH, CODE_FILLER);

        if (finalCode.length() != CODE_LENGTH) {
            throw new RuntimeException("Unexpected code length for generated code");
        }

        return finalCode;

    }

    private static String getCodePrefix(final PaymentCodeType paymentCodeType, final Person person) {
        return getPersonCodeDigits(person) + paymentCodeType.getTypeDigit();
    }

    private static String getPersonCodeDigits(Person person) {
        if (person.getIstUsername().length() > 9) {
            throw new RuntimeException("SIBS Payment Code: " + person.getIstUsername() + " exceeded maximun size accepted");
        }
        return StringUtils.leftPad(person.getIstUsername().replace("ist", ""), PERSON_CODE_LENGTH, CODE_FILLER);
    }

    @Override
    public boolean isCodeMadeByThisFactory(PaymentCode paymentCode) {
        return paymentCode.getCode().startsWith(getPersonCodeDigits(paymentCode.getPerson()));
    }
}
