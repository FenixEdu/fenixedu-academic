/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.domain.transactions.Transaction;

import org.joda.time.YearMonthDay;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixframework.Atomic;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ListPayedInsurancesByDates {

    @Atomic
    public static List<InsuranceTransaction> run(ExecutionYear executionYear, YearMonthDay beginDate, YearMonthDay endDate) {

        List<InsuranceTransaction> insuranceTransactions = new ArrayList<InsuranceTransaction>();

        Collection<Transaction> transactions = Bennu.getInstance().getTransactionsSet();
        for (Transaction transaction : transactions) {
            if (transaction instanceof InsuranceTransaction) {
                InsuranceTransaction insuranceTransaction = (InsuranceTransaction) transaction;

                if (insuranceTransaction.isReimbursed()) {
                    continue;
                }

                if (executionYear != null && !insuranceTransaction.getExecutionYear().equals(executionYear)) {
                    continue;
                }

                if (beginDate != null
                        && insuranceTransaction.getTransactionDateDateTime().isBefore(beginDate.toDateTimeAtMidnight())) {
                    continue;
                }

                if (endDate != null && insuranceTransaction.getTransactionDateDateTime().isAfter(endDate.toDateTimeAtMidnight())) {
                    continue;
                }

                insuranceTransactions.add(insuranceTransaction);
            }
        }

        return insuranceTransactions;

    }

}