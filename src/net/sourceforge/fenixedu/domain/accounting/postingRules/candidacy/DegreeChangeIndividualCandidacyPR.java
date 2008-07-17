package net.sourceforge.fenixedu.domain.accounting.postingRules.candidacy;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.DegreeChangeIndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class DegreeChangeIndividualCandidacyPR extends DegreeChangeIndividualCandidacyPR_Base {

    private DegreeChangeIndividualCandidacyPR() {
	super();
    }

    public DegreeChangeIndividualCandidacyPR(final DateTime start, final DateTime end,
	    final ServiceAgreementTemplate serviceAgreementTemplate, final Money amountForInstitutionStudent,
	    final Money amountForExternalStudent) {
	this();
	super.init(EntryType.DEGREE_CHANGE_INDIVIDUAL_CANDICAY_FEE, EventType.DEGREE_CHANGE_INDIVIDUAL_CANDICAY, start, end,
		serviceAgreementTemplate);
	checkParameters(amountForInstitutionStudent, amountForExternalStudent);
	super.setAmountForInstitutionStudent(amountForInstitutionStudent);
	super.setAmountForExternalStudent(amountForExternalStudent);
    }

    private void checkParameters(final Money amountForInstitutionStudent, final Money amountForExternalStudent) {
	if (amountForInstitutionStudent == null) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacyPR.invalid.amountForInstitutionStudent");
	}
	if (amountForExternalStudent == null) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacyPR.invalid.amountForExternalStudent");
	}
    }

    @Override
    public void setAmountForInstitutionStudent(final Money amountForInstitutionStudent) {
	throw new DomainException("error.DegreeChangeIndividualCandidacyPR.cannot.modify.amountForInstitutionStudent");
    }

    @Override
    public void setAmountForExternalStudent(final Money amountForExternalStudent) {
	throw new DomainException("error.DegreeChangeIndividualCandidacyPR.cannot.modify.amountForExternalStudent");
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
	final Money amountToPay = calculateTotalAmountToPay(event, when);
	return Collections.singletonList(new EntryDTO(getEntryType(), event, amountToPay, Money.ZERO, amountToPay, event
		.getDescriptionForEntryType(getEntryType()), amountToPay));
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	final CandidacyPrecedentDegreeInformation information = ((DegreeChangeIndividualCandidacyEvent) event)
		.getIndividualCandidacy().getPrecedentDegreeInformation();
	return information.isExternal() && !belongsToInstitutionGroup(information.getInstitution()) ? getAmountForExternalStudent()
		: getAmountForInstitutionStudent();
    }

    private boolean belongsToInstitutionGroup(final Unit unit) {
	for (final Unit parent : getRootDomainObject().getInstitutionUnit().getParentUnits()) {
	    if (parent.hasSubUnit(unit)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs, Event event, Account fromAccount,
	    Account toAccount, AccountingTransactionDetailDTO transactionDetail) {
	if (entryDTOs.size() != 1) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacyPR.invalid.number.of.entryDTOs");
	}

	final EntryDTO entryDTO = entryDTOs.get(0);
	checkIfCanAddAmount(entryDTO.getAmountToPay(), event, transactionDetail.getWhenRegistered());

	return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount, entryDTO.getEntryType(),
		entryDTO.getAmountToPay(), transactionDetail));
    }

    private void checkIfCanAddAmount(final Money amountToPay, final Event event, final DateTime when) {
	if (amountToPay.compareTo(calculateTotalAmountToPay(event, when)) < 0) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.DegreeChangeIndividualCandidacyPR.amount.being.payed.must.match.amount.to.pay", event
			    .getDescriptionForEntryType(getEntryType()));
	}
    }

    @Checked("PostingRulePredicates.editPredicate")
    public DegreeChangeIndividualCandidacyPR edit(final Money amountForInstitutionStudent, final Money amountForExternalStudent) {
	deactivate();
	return new DegreeChangeIndividualCandidacyPR(new DateTime(), null, getServiceAgreementTemplate(),
		amountForInstitutionStudent, amountForExternalStudent);
    }
}
