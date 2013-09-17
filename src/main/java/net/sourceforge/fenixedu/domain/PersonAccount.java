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

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */
public class PersonAccount extends PersonAccount_Base {

    public PersonAccount() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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
    public boolean hasRootDomainObject() {
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
