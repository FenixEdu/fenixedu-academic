/*
 * Created on Aug 2, 2004
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.List;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface IPersonAccount extends IDomainObject {
    public abstract Double getBalance();

    public abstract void setBalance(Double balance);

    public abstract IPerson getPerson();

    public abstract void setPerson(IPerson person);

    public abstract List getTransactions();

    public abstract void setTransactions(List transactions);
}