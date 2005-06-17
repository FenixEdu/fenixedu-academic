package net.sourceforge.fenixedu.domain.transactions;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */
public abstract class Transaction extends Transaction_Base {

	/**
	 * @return Returns the transactionDate.
	 */
	public Timestamp getTransactionDate() {
        if (this.getTransaction() != null) {
            return new Timestamp(this.getTransaction().getTime());
        }
        return null;
	}

	/**
	 * @param transactionDate
	 *            The transactionDate to set.
	 */
	public void setTransactionDate(Timestamp transactionDate) {
        if (transactionDate != null) {
            this.setTransaction(new Date(transactionDate.getTime()));
        } else {
            this.setTransaction(null);
        }
	}

}
