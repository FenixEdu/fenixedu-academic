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
package org.fenixedu.academic.domain.accounting;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class AccountingTransactionDetail extends AccountingTransactionDetail_Base {

    protected AccountingTransactionDetail() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
        super.setWhenProcessed(new DateTime());
    }

    public AccountingTransactionDetail(final DateTime whenRegistered, final PaymentMode paymentMode) {
        this(whenRegistered, paymentMode, null);
    }

    public AccountingTransactionDetail(final DateTime whenRegistered, final PaymentMode paymentMode, final String comments) {
        this();
        init(whenRegistered, paymentMode, comments);
    }

    private void checkParameters(DateTime whenRegistered, PaymentMode paymentMode) {

        if (whenRegistered == null) {
            throw new DomainException("error.accounting.AccountingTransactionDetail.whenRegistered.cannot.be.null");
        }

        if (paymentMode == null) {
            throw new DomainException("error.accounting.AccountingTransactionDetail.paymentMode.cannot.be.null");
        }
    }

    protected void init(DateTime whenRegistered, PaymentMode paymentMode, final String comments) {

        checkParameters(whenRegistered, paymentMode);

        super.setWhenRegistered(whenRegistered);
        super.setPaymentMode(paymentMode);
        super.setComments(comments);
    }

    @Override
    public void setWhenRegistered(DateTime whenRegistered) {
        throw new DomainException("error.accounting.AccountingTransactionDetail.cannot.modify.whenRegistered");
    }

    @Override
    public void setPaymentMode(PaymentMode paymentMode) {
        throw new DomainException("error.accounting.AccountingTransactionDetail.cannot.modify.paymentMode");
    }

    @Override
    public void setTransaction(AccountingTransaction transaction) {
        throw new DomainException("error.accounting.AccountingTransactionDetail.cannot.modify.transaction");
    }

    void delete() {
        super.setTransaction(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public Event getEvent() {
        return getTransaction().getEvent();
    }

}
