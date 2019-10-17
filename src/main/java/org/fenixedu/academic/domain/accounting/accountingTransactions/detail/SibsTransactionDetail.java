/**
 * Copyright © 2002 Instituto Superior Técnico
 * <p>
 * This file is part of FenixEdu Academic.
 * <p>
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.accounting.accountingTransactions.detail;

import org.fenixedu.academic.domain.accounting.PaymentMethod;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.sibs.incomming.SibsIncommingPaymentFileDetailLine;
import org.joda.time.DateTime;

import com.google.common.base.Strings;

public class SibsTransactionDetail extends SibsTransactionDetail_Base {

    protected SibsTransactionDetail() {
        super();
    }

    public SibsTransactionDetail(SibsIncommingPaymentFileDetailLine sibsDetailLine, DateTime whenRegistered, String sibsTransactionId, String sibsCode, String comments) {
        this();
        init(whenRegistered, sibsTransactionId, sibsCode, comments);
        super.setSibsLine(sibsDetailLine);
    }

    protected void init(DateTime whenRegistered, String sibsTransactionId, String sibsCode, String comments) {

        super.init(whenRegistered, PaymentMethod.getSibsPaymentMethod(), sibsCode, comments);

        checkParameters(sibsTransactionId, sibsCode);

        super.setSibsTransactionId(sibsTransactionId);
        super.setSibsCode(sibsCode);
    }

    private void checkParameters(String sibsTransactionId, String sibsCode) {
        if (sibsTransactionId == null) {
            throw new DomainException(
                    "error.accounting.accountingTransactions.detail.SibsTransactionDetail.sibsTransactionId.cannot.be.null");
        }

        if (sibsCode == null) {
            throw new DomainException(
                    "error.accounting.accountingTransactions.detail.SibsTransactionDetail.sibsCode.cannot.be.null");
        }

    }

    @Override
    public String getPaymentReference() {
        if (Strings.isNullOrEmpty(super.getPaymentReference())) {
            return getSibsCode();
        }
        return super.getPaymentReference();
    }
}
