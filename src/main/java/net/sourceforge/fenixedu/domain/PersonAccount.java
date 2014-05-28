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
/*
 * Created on Jul 22, 2004
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentTransaction;
import net.sourceforge.fenixedu.domain.transactions.Transaction;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */
public class PersonAccount extends PersonAccount_Base {

    public PersonAccount() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public PersonAccount(Person person) {
        this();
        setPerson(person);
        setBalance(new Double(0));

    }

    public List getPaymentTransactions() {

        List result = new ArrayList<PaymentTransaction>();
        for (Transaction transaction : this.getTransactions()) {
            if (transaction instanceof PaymentTransaction) {
                result.add(transaction);
            }
        }

        return result;
    }

    public List getInsuranceTransactions() {

        List result = new ArrayList<InsuranceTransaction>();
        for (Transaction transaction : this.getTransactions()) {
            if (transaction instanceof InsuranceTransaction) {
                result.add(transaction);
            }
        }
        return result;
    }

    public void delete() {

        if (getTransactionsSet().size() > 0) {
            throw new DomainException("error.person.cannot.be.deleted");
        }

        setRootDomainObject(null);
        setPerson(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.transactions.Transaction> getTransactions() {
        return getTransactionsSet();
    }

    @Deprecated
    public boolean hasAnyTransactions() {
        return !getTransactionsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasBalance() {
        return getBalance() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
