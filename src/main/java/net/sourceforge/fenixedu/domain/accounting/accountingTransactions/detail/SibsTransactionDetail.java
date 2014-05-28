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
package net.sourceforge.fenixedu.domain.accounting.accountingTransactions.detail;

import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class SibsTransactionDetail extends SibsTransactionDetail_Base {

    protected SibsTransactionDetail() {
        super();
    }

    public SibsTransactionDetail(DateTime whenRegistered, String sibsTransactionId, String sibsCode, String comments) {
        this();
        init(whenRegistered, sibsTransactionId, sibsCode, comments);
    }

    protected void init(DateTime whenRegistered, String sibsTransactionId, String sibsCode, String comments) {

        super.init(whenRegistered, PaymentMode.ATM, comments);

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
    public void setSibsTransactionId(String sibsTransactionId) {
        throw new DomainException(
                "error.accounting.accountingTransactions.detail.SibsTransactionDetail.cannot.modify.sibsTransactionId");

    }

    @Override
    public void setSibsCode(String sibsCode) {
        throw new DomainException("error.accounting.accountingTransactions.detail.SibsTransactionDetail.cannot.modify.sibsCode");
    }

    @Deprecated
    public boolean hasSibsCode() {
        return getSibsCode() != null;
    }

    @Deprecated
    public boolean hasSibsTransactionId() {
        return getSibsTransactionId() != null;
    }

}
