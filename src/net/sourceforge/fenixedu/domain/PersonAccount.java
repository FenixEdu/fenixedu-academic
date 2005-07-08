/*
 * Created on Jul 22, 2004
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.transactions.IInsuranceTransaction;
import net.sourceforge.fenixedu.domain.transactions.IPaymentTransaction;
import net.sourceforge.fenixedu.domain.transactions.ITransaction;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */
public class PersonAccount extends PersonAccount_Base {

    public PersonAccount() {
    }

    public PersonAccount(IPerson person) {
        setPerson(person);
        setBalance(new Double(0));

    }

    public List getPaymentTransactions() {

        List result = new ArrayList<IPaymentTransaction>();
        for (ITransaction transaction : this.getTransactions()) {
            if (transaction instanceof IPaymentTransaction) {
                result.add((IPaymentTransaction) transaction);
            }
        }

        return result;
    }
	
	 public List getInsuranceTransactions() {

	        List result = new ArrayList<IInsuranceTransaction>();
	        for (ITransaction transaction : this.getTransactions()) {
	            if (transaction instanceof IInsuranceTransaction) {
	                result.add((IInsuranceTransaction) transaction);
	            }
	        }
	        return result;
	    }

}
