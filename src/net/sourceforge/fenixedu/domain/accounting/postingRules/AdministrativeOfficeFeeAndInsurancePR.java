package net.sourceforge.fenixedu.domain.accounting.postingRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AnnualEvent;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.UnitServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class AdministrativeOfficeFeeAndInsurancePR extends AdministrativeOfficeFeeAndInsurancePR_Base {

    protected AdministrativeOfficeFeeAndInsurancePR() {
	super();
    }

    public AdministrativeOfficeFeeAndInsurancePR(DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate) {
	this();
	super.init(EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE, startDate, endDate,
		serviceAgreementTemplate);
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	final AnnualEvent annualEvent = (AnnualEvent) event;
	return getPostingRuleForAdministrativeOfficeFee(annualEvent.getStartDate(),
		annualEvent.getEndDate()).calculateTotalAmountToPay(event, when).add(
		getPostingRuleForInsurance(annualEvent.getStartDate(), annualEvent.getEndDate())
			.calculateTotalAmountToPay(event, when));
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {

	final List<EntryDTO> result = new ArrayList<EntryDTO>();
	final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent = (AdministrativeOfficeFeeAndInsuranceEvent) event;
	final AnnualEvent annualEvent = (AnnualEvent) event;
	if (administrativeOfficeFeeAndInsuranceEvent.hasToPayAdministrativeOfficeFee()) {
	    result.addAll(getPostingRuleForAdministrativeOfficeFee(annualEvent.getStartDate(),
		    annualEvent.getEndDate()).calculateEntries(event, when));
	}
	if (administrativeOfficeFeeAndInsuranceEvent.hasToPayInsurance()) {
	    result.addAll(getPostingRuleForInsurance(annualEvent.getStartDate(),
		    annualEvent.getEndDate()).calculateEntries(event, when));
	}

	return result;
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs,
	    Event event, Account fromAccount, Account toAccount,
	    AccountingTransactionDetailDTO transactionDetail) {

	final Set<AccountingTransaction> result = new HashSet<AccountingTransaction>();
	final Set<Entry> createdEntries = new HashSet<Entry>();
	final AnnualEvent annualEvent = (AnnualEvent) event;
	for (final EntryDTO entryDTO : entryDTOs) {

	    if (entryDTO.getEntryType() == EntryType.INSURANCE_FEE) {

		createdEntries.addAll(getPostingRuleForInsurance(annualEvent.getStartDate(),
			annualEvent.getEndDate()).process(user, Collections.singletonList(entryDTO),
			event, fromAccount, toAccount, transactionDetail));

	    } else if (entryDTO.getEntryType() == EntryType.ADMINISTRATIVE_OFFICE_FEE) {
		createdEntries.addAll(getPostingRuleForAdministrativeOfficeFee(
			annualEvent.getStartDate(), annualEvent.getEndDate()).process(user,
			Collections.singletonList(entryDTO), event, fromAccount, toAccount,
			transactionDetail));
	    } else {
		throw new DomainException(
			"error.accounting.postingRules.AdministrativeOfficeFeeAndInsurancePR.invalid.entry.type");
	    }
	}

	((AdministrativeOfficeFeeAndInsuranceEvent) event).changePaymentCodeState(transactionDetail
		.getWhenRegistered(), transactionDetail.getPaymentMode());

	for (final Entry entry : createdEntries) {
	    result.add(entry.getAccountingTransaction());
	}

	return result;
    }

    private FixedAmountPR getPostingRuleForInsurance(DateTime startDate, DateTime endDate) {
	return (FixedAmountPR) getServiceAgreementTemplateForInsurance().findPostingRuleBy(
		EventType.INSURANCE, startDate, endDate);
    }

    private FixedAmountWithPenaltyFromDatePR getPostingRuleForAdministrativeOfficeFee(
	    DateTime startDate, DateTime endDate) {
	return (FixedAmountWithPenaltyFromDatePR) getServiceAgreementTemplate().findPostingRuleBy(
		EventType.ADMINISTRATIVE_OFFICE_FEE, startDate, endDate);
    }

    public UnitServiceAgreementTemplate getServiceAgreementTemplateForInsurance() {
	return RootDomainObject.getInstance().getInstitutionUnit().getUnitServiceAgreementTemplate();
    }

    @Override
    public boolean isVisible() {
	return false;
    }

    public Money getAdministrativeOfficeFeeAmount(DateTime startDate, DateTime endDate) {
	return getPostingRuleForAdministrativeOfficeFee(startDate, endDate).getFixedAmount();
    }

    public YearMonthDay getAdministrativeOfficeFeePaymentLimitDate(DateTime startDate, DateTime endDate) {
	return getPostingRuleForAdministrativeOfficeFee(startDate, endDate)
		.getWhenToApplyFixedAmountPenalty();
    }

    public Money getAdministrativeOfficeFeePenaltyAmount(DateTime startDate, DateTime endDate) {
	return getPostingRuleForAdministrativeOfficeFee(startDate, endDate).getFixedAmountPenalty();
    }

    public Money getInsuranceAmount(DateTime startDate, DateTime endDate) {
	return getPostingRuleForInsurance(startDate, endDate).getFixedAmount();
    }

}
