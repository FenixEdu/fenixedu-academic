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
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.joda.time.DateTime;

public class AccountingTransactionDetail extends AccountingTransactionDetail_Base {

    public static final String SIGNAL_TRANSACTION_DETAIL_INIT = AccountingTransactionDetail.class.getName() + ".init";

    protected AccountingTransactionDetail() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
        super.setWhenProcessed(new DateTime());
    }

    public AccountingTransactionDetail(final DateTime whenRegistered, final PaymentMethod paymentMethod, String paymentReference, String comments) {
        this();
        init(whenRegistered, paymentMethod, paymentReference, comments);
    }

    private void checkParameters(DateTime whenRegistered, PaymentMethod paymentMethod) {

        if (whenRegistered == null) {
            throw new DomainException("error.accounting.AccountingTransactionDetail.whenRegistered.cannot.be.null");
        }
        
        if (whenRegistered.isAfter(new DateTime())) {
            throw new DomainException("error.accounting.AccountingTransactionDetail.whenRegistered.cannot.be.after.now");
        }

        if (paymentMethod == null) {
            throw new DomainException("error.accounting.AccountingTransactionDetail.paymentMethod.cannot.be.null");
        }
    }

    protected void init(DateTime whenRegistered, PaymentMethod paymentMethod, final String paymentReference, final String
            comments) {

        checkParameters(whenRegistered, paymentMethod);

        super.setWhenRegistered(whenRegistered);
        super.setPaymentMethod(paymentMethod);
        super.setPaymentReference(paymentReference);
        super.setComments(comments);

        Signal.emit(SIGNAL_TRANSACTION_DETAIL_INIT, new DomainObjectEvent<>(this));
    }

    @Override
    public void setWhenRegistered(DateTime whenRegistered) {
        throw new DomainException("error.accounting.AccountingTransactionDetail.cannot.modify.whenRegistered");
    }

    @Override
    public void setTransaction(AccountingTransaction transaction) {
        throw new DomainException("error.accounting.AccountingTransactionDetail.cannot.modify.transaction");
    }

    void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        super.setTransaction(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public Event getEvent() {
        return getTransaction().getEvent();
    }

}
