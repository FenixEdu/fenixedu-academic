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
import java.util.Random;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.IndividualCandidacyPaymentCode;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * Code Format: 8<sequentialNumber{4}><typeDigit{2}><controlDigits{2}>
 * 
 */
public class IndividualCandidacyPaymentCodeGenerator extends PaymentCodeGenerator {

    public static Comparator<PaymentCode> COMPARATOR_BY_PAYMENT_SEQUENTIAL_DIGITS = new Comparator<PaymentCode>() {
        @Override
        public int compare(PaymentCode leftPaymentCode, PaymentCode rightPaymentCode) {
            final String leftSequentialNumber = getSequentialNumber(leftPaymentCode);
            final String rightSequentialNumber = getSequentialNumber(rightPaymentCode);

            int comparationResult = leftSequentialNumber.compareTo(rightSequentialNumber);

            return (comparationResult == 0) ? leftPaymentCode.getExternalId().compareTo(rightPaymentCode.getExternalId()) : comparationResult;
        }
    };

    private static final String CODE_FILLER = "0";
    private static final int NUM_TYPE_DIGITS = 2;
    private static final int NUM_CONTROL_DIGITS = 2;
    private static final int NUM_SEQUENTIAL_NUMBERS = 4;
    private static final String START = "8";

    @Override
    public boolean canGenerateNewCode(PaymentCodeType paymentCodeType, Person person) {
        final PaymentCode lastPaymentCode = findLastPaymentCode(paymentCodeType);
        return lastPaymentCode == null ? true : Integer.valueOf(getSequentialNumber(lastPaymentCode)) < 9999;
    }

    private PaymentCode findLastPaymentCode(PaymentCodeType paymentCodeType) {
        final List<IndividualCandidacyPaymentCode> individualCandidacyPaymentCodes =
                getAllIndividualCandidacyPaymentCodesForType(paymentCodeType);
        return individualCandidacyPaymentCodes.isEmpty() ? null : Collections.max(individualCandidacyPaymentCodes,
                COMPARATOR_BY_PAYMENT_SEQUENTIAL_DIGITS);
    }

    private List<IndividualCandidacyPaymentCode> getAllIndividualCandidacyPaymentCodesForType(
            final PaymentCodeType paymentCodeType) {
        Set<PaymentCode> allPaymentCodes = Bennu.getInstance().getPaymentCodesSet();

        List<IndividualCandidacyPaymentCode> outputList = new ArrayList<IndividualCandidacyPaymentCode>();
        CollectionUtils.select(allPaymentCodes, new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                PaymentCode paymentCode = (PaymentCode) arg0;
                return paymentCodeType.equals(paymentCode.getType());
            }

        }, outputList);

        return outputList;
    }

    @Override
    public String generateNewCodeFor(PaymentCodeType paymentCodeType, Person person) {
        final PaymentCode lastPaymentCode = findLastPaymentCode(paymentCodeType);
        int nextSequentialNumber = lastPaymentCode == null ? 0 : Integer.valueOf(getSequentialNumber(lastPaymentCode)) + 1;

        String sequentialNumberPadded =
                StringUtils.leftPad(String.valueOf(nextSequentialNumber), NUM_SEQUENTIAL_NUMBERS, CODE_FILLER);
        String typeDigitsPadded =
                StringUtils.leftPad(String.valueOf(paymentCodeType.getTypeDigit()), NUM_TYPE_DIGITS, CODE_FILLER);
        String controDigitsPadded =
                StringUtils.leftPad(String.valueOf((new Random()).nextInt(99)), NUM_CONTROL_DIGITS, CODE_FILLER);

        return START + sequentialNumberPadded + typeDigitsPadded + controDigitsPadded;
    }

    private static String getSequentialNumber(PaymentCode paymentCode) {
        String sequentialNumber =
                paymentCode.getCode().substring(1, paymentCode.getCode().length() - NUM_CONTROL_DIGITS - NUM_TYPE_DIGITS);

        return sequentialNumber;
    }

    @Override
    public boolean isCodeMadeByThisFactory(PaymentCode paymentCode) {
        return paymentCode.getCode().startsWith(START);
    }
}
