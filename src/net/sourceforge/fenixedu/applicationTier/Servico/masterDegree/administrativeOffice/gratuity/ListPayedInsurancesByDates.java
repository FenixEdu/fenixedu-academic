/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.domain.transactions.Transaction;

import org.joda.time.YearMonthDay;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ListPayedInsurancesByDates extends Service {

    public List<InsuranceTransaction> run(ExecutionYear executionYear, YearMonthDay beginDate,
	    YearMonthDay endDate) {

	List<InsuranceTransaction> insuranceTransactions = new ArrayList<InsuranceTransaction>();

	List<Transaction> transactions = rootDomainObject.getTransactions();
	for (Transaction transaction : transactions) {
	    if (transaction instanceof InsuranceTransaction) {
		InsuranceTransaction insuranceTransaction = (InsuranceTransaction) transaction;

		if (insuranceTransaction.isReimbursed()) {
		    continue;
		}

		if (executionYear != null
			&& !insuranceTransaction.getExecutionYear().equals(executionYear)) {
		    continue;
		}

		if (beginDate != null
			&& insuranceTransaction.getTransactionDateDateTime().isBefore(
				beginDate.toDateTimeAtMidnight())) {
		    continue;
		}

		if (endDate != null
			&& insuranceTransaction.getTransactionDateDateTime().isAfter(
				endDate.toDateTimeAtMidnight())) {
		    continue;
		}

		insuranceTransactions.add(insuranceTransaction);
	    }
	}

	return insuranceTransactions;

    }

}
