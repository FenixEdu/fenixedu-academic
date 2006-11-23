package net.sourceforge.fenixedu.domain.transactions;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonAccount;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class InsuranceTransaction extends InsuranceTransaction_Base {

    public InsuranceTransaction() {
	super();
	setOjbConcreteClass(getClass().getName());

    }

    public InsuranceTransaction(Double value, Timestamp transactionDate, String remarks,
	    PaymentType paymentType, TransactionType transactionType, Boolean wasInternalBalance,
	    Person responsiblePerson, PersonAccount personAccount, GuideEntry guideEntry,
	    ExecutionYear executionYear, Registration registration) {
	this();
	setValue(value);
	setTransactionDate(transactionDate);
	setRemarks(remarks);
	setPaymentType(paymentType);
	setTransactionType(transactionType);
	setWasInternalBalance(wasInternalBalance);
	setResponsiblePerson(responsiblePerson);
	setPersonAccount(personAccount);
	setExecutionYear(executionYear);
	setGuideEntry(guideEntry);
	setStudent(registration);
    }

    public InsuranceTransaction(final BigDecimal value, final DateTime whenRegistered,
	    final PaymentType paymentType, final Person responsiblePerson,
	    final PersonAccount personAccount, final ExecutionYear executionYear,
	    final Registration registration) {
	this();
	setValueBigDecimal(value);
	setTransactionDateDateTime(whenRegistered);
	setPaymentType(paymentType);
	setTransactionType(TransactionType.INSURANCE_PAYMENT);
	setWasInternalBalance(false);
	setResponsiblePerson(responsiblePerson);
	setPersonAccount(personAccount);
	setExecutionYear(executionYear);
	setStudent(registration);
    }

    public boolean isReimbursed() {
	if (!hasGuideEntry() || getGuideEntry().getReimbursementGuideEntriesCount() == 0) {
	    return false;
	} else {
	    for (final ReimbursementGuideEntry reimbursementGuideEntry : getGuideEntry()
		    .getReimbursementGuideEntries()) {
		if (reimbursementGuideEntry.getReimbursementGuide()
			.getActiveReimbursementGuideSituation().getReimbursementGuideState().equals(
				ReimbursementGuideState.PAYED)) {
		    return true;
		}
	    }

	    return false;
	}
    }

}