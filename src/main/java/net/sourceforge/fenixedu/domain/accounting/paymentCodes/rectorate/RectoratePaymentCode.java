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
package net.sourceforge.fenixedu.domain.accounting.paymentCodes.rectorate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

public class RectoratePaymentCode extends RectoratePaymentCode_Base {

    protected RectoratePaymentCode(final YearMonthDay startDate, final YearMonthDay endDate, final Money minAmount,
            final Money maxAmount) {
        super();
        init(PaymentCodeType.RECTORATE, startDate, endDate, minAmount, maxAmount, null);
    }

    @Override
    protected void internalProcess(Person person, Money amount, DateTime whenRegistered, String sibsTransactionId, String comments) {

    }

    @Override
    public boolean isForRectorate() {
        return true;
    }

    public static RectoratePaymentCode create(final LocalDate startDate, final LocalDate endDate, final Money minAmount,
            final Money maxAmount) {
        return new RectoratePaymentCode(startDate.toDateTimeAtStartOfDay().toYearMonthDay(), endDate.toDateTimeAtStartOfDay()
                .toYearMonthDay(), minAmount, maxAmount);
    }

    public static List<RectoratePaymentCode> getAllRectoratePaymentCodes() {
        List<RectoratePaymentCode> result = new ArrayList<RectoratePaymentCode>();

        Collection<PaymentCode> paymentCodes = Bennu.getInstance().getPaymentCodesSet();

        for (PaymentCode paymentCode : paymentCodes) {
            if (paymentCode.isForRectorate() && !StringUtils.isEmpty(paymentCode.getCode())) {
                result.add((RectoratePaymentCode) paymentCode);
            }
        }

        return result;
    }

}
