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
package org.fenixedu.academic.domain.accounting.paymentCodes.rectorate;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public class RectoratePaymentCode extends RectoratePaymentCode_Base {

    @Override
    protected void internalProcess(Person person, Money amount, DateTime whenRegistered, String sibsTransactionId, String comments) {

    }

    @Override
    public boolean isForRectorate() {
        return true;
    }

    public static List<RectoratePaymentCode> getAllRectoratePaymentCodes() {
        return Bennu.getInstance().getPaymentCodesSet().stream()
                .filter(PaymentCode::isForRectorate)
                .filter(paymentCode -> !StringUtils.isEmpty(paymentCode.getCode()))
                .map(paymentCode -> (RectoratePaymentCode) paymentCode)
                .collect(Collectors.toList());
    }

}
