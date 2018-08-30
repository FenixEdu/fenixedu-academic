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
package org.fenixedu.academic.domain.accounting.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.PaymentCodeType;
import org.fenixedu.academic.domain.accounting.paymentCodes.rectorate.RectoratePaymentCode;
import org.fenixedu.bennu.core.domain.Bennu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RectoratePaymentCodeGenerator extends PaymentCodeGenerator {

    private static final Logger logger = LoggerFactory.getLogger(RectoratePaymentCodeGenerator.class);

    public static Comparator<PaymentCode> COMPARATOR_BY_PAYMENT_SEQUENTIAL_DIGITS = (leftPaymentCode, rightPaymentCode) -> {
        final String leftSequentialNumber = getSequentialNumber(leftPaymentCode);
        final String rightSequentialNumber = getSequentialNumber(rightPaymentCode);

        int comparationResult = leftSequentialNumber.compareTo(rightSequentialNumber);

        logger.info("left [{}], right [{}], result [{}]", leftSequentialNumber, rightSequentialNumber, comparationResult);

        return (comparationResult == 0) ? leftPaymentCode.getExternalId().compareTo(rightPaymentCode.getExternalId()) : comparationResult;
    };

    private static final String CODE_FILLER = "0";
    private static final int NUM_CONTROL_DIGITS = 2;
    private static final int NUM_SEQUENTIAL_NUMBERS = 6;

    @Override
    public boolean canGenerateNewCode(PaymentCodeType paymentCodeType, Person person) {
        final PaymentCode lastPaymentCode = findLastPaymentCode();
        return lastPaymentCode == null || Integer.valueOf(getSequentialNumber(lastPaymentCode)) < 999999;
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

        String sequentialNumberPadded =
                StringUtils.leftPad(String.valueOf(nextSequentialNumber), NUM_SEQUENTIAL_NUMBERS, CODE_FILLER);
        String controDigitsPadded =
                StringUtils.leftPad(String.valueOf((new Random()).nextInt(99)), NUM_CONTROL_DIGITS, CODE_FILLER);

        return START + sequentialNumberPadded + controDigitsPadded;
    }

    private static final String START = "9";

    @Override
    public boolean isCodeMadeByThisFactory(PaymentCode paymentCode) {
        return paymentCode.getCode().startsWith(START);
    }

    private static String getSequentialNumber(PaymentCode paymentCode) {
        return paymentCode.getCode().substring(1, paymentCode.getCode().length() - NUM_CONTROL_DIGITS);
    }

}
