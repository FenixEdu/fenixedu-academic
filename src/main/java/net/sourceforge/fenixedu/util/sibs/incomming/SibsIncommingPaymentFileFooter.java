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
package net.sourceforge.fenixedu.util.sibs.incomming;

import net.sourceforge.fenixedu.util.Money;

public class SibsIncommingPaymentFileFooter {

    private static final int[] FIELD_SIZES = new int[] { 1, 8, 17, 12, 12, 50 };

    private Money transactionsTotalAmount;

    private Money totalCost;

    private SibsIncommingPaymentFileFooter(Money transactionsTotalAmount, Money totalCost) {
        this.transactionsTotalAmount = transactionsTotalAmount;
        this.totalCost = totalCost;
    }

    public static SibsIncommingPaymentFileFooter buildFrom(String rawLine) {
        final String[] fields = splitLine(rawLine);
        return new SibsIncommingPaymentFileFooter(getTransactionsTotalAmountFrom(fields), getCostFrom(fields));
    }

    private static Money getCostFrom(String[] fields) {
        return new Money(fields[3].substring(0, 10) + "." + fields[3].substring(10));
    }

    private static Money getTransactionsTotalAmountFrom(String[] fields) {
        return new Money(fields[2].substring(0, 15) + "." + fields[2].substring(15));
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

    public Money getTotalCost() {
        return totalCost;
    }

    public Money getTransactionsTotalAmount() {
        return transactionsTotalAmount;
    }

}
