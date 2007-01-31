package net.sourceforge.fenixedu.domain.transactions;

import java.math.BigDecimal;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */
public abstract class Transaction extends Transaction_Base {

    public static Comparator<Transaction> COMPARATOR_BY_TRANSACTION_DATE_TIME = new Comparator<Transaction>() {
	public int compare(Transaction leftTransaction, Transaction rightTransaction) {
	    int comparationResult = leftTransaction.getTransactionDateDateTime().compareTo(
		    rightTransaction.getTransactionDateDateTime());
	    return (comparationResult == 0) ? leftTransaction.getIdInternal().compareTo(
		    rightTransaction.getIdInternal()) : comparationResult;
	}
    };

    public Transaction() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    @Deprecated
    public Double getValue() {
	return getValueBigDecimal().doubleValue();
    }

    @Deprecated
    public void setValue(Double value) {
	setValueBigDecimal(BigDecimal.valueOf(value));
    }

}
