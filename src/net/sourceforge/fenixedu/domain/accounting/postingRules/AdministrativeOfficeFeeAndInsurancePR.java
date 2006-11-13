package net.sourceforge.fenixedu.domain.accounting.postingRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
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
    public Money calculateTotalAmountToPay(Event event, DateTime when) {
	return getPostingRuleForAdministrativeOfficeFee(when).calculateTotalAmountToPay(event, when)
		.add(getPostingRuleForInsurance(when).calculateTotalAmountToPay(event, when));
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {

	final List<EntryDTO> result = new ArrayList<EntryDTO>();
	final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent = (AdministrativeOfficeFeeAndInsuranceEvent) event;

	if (!administrativeOfficeFeeAndInsuranceEvent.hasPayedAdministrativeOfficeFee()) {
	    result.addAll(getPostingRuleForAdministrativeOfficeFee(when).calculateEntries(event, when));
	}
	if (administrativeOfficeFeeAndInsuranceEvent.needsToPayInsurance()) {
	    result.addAll(getPostingRuleForInsurance(when).calculateEntries(event, when));
	}

	return result;
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs,
	    PaymentMode paymentMode, DateTime whenRegistered, Event event, Account fromAccount,
	    Account toAccount) {

	final Set<AccountingTransaction> result = new HashSet<AccountingTransaction>();
	final Set<Entry> createdEntries = new HashSet<Entry>();
	for (final EntryDTO entryDTO : entryDTOs) {
	    if (entryDTO.getEntryType() == EntryType.INSURANCE_FEE) {
		createdEntries.addAll(getPostingRuleForInsurance(whenRegistered).process(user,
			Collections.singletonList(entryDTO), paymentMode, whenRegistered, event,
			fromAccount, toAccount));
	    } else if (entryDTO.getEntryType() == EntryType.ADMINISTRATIVE_OFFICE_FEE) {
		createdEntries.addAll(getPostingRuleForAdministrativeOfficeFee(whenRegistered).process(
			user, Collections.singletonList(entryDTO), paymentMode, whenRegistered, event,
			fromAccount, toAccount));
	    } else {
		throw new DomainException(
			"error.accounting.postingRules.AdministrativeOfficeFeeAndInsurancePR.invalid.entry.type");
	    }
	}

	for (final Entry entry : createdEntries) {
	    result.add(entry.getAccountingTransaction());
	}

	return result;
    }

    private FixedAmountPR getPostingRuleForInsurance(DateTime whenRegistered) {
	return (FixedAmountPR) getServiceAgreementTemplateForInsurance()
		.findPostingRuleByEventTypeAndDate(EventType.INSURANCE, whenRegistered);
    }

    private FixedAmountWithPenaltyFromDatePR getPostingRuleForAdministrativeOfficeFee(
	    DateTime whenRegistered) {
	return (FixedAmountWithPenaltyFromDatePR) getServiceAgreementTemplate()
		.findPostingRuleByEventTypeAndDate(EventType.ADMINISTRATIVE_OFFICE_FEE, whenRegistered);
    }

    public UnitServiceAgreementTemplate getServiceAgreementTemplateForInsurance() {
	return RootDomainObject.getInstance().getInstitutionUnit().getUnitServiceAgreementTemplate();
    }

    @Override
    public boolean isVisible() {
	return false;
    }

    public Money getAdministrativeOfficeFeeAmount() {
	return getPostingRuleForAdministrativeOfficeFee(new DateTime()).getFixedAmount();
    }

    public YearMonthDay getAdministrativeOfficeFeePaymentLimitDate() {
	return getPostingRuleForAdministrativeOfficeFee(new DateTime())
		.getWhenToApplyFixedAmountPenalty();
    }

    public Money getAdministrativeOfficeFeePenaltyAmount() {
	return getPostingRuleForAdministrativeOfficeFee(new DateTime()).getFixedAmountPenalty();
    }

    public Money getInsuranceAmount() {
	return getPostingRuleForInsurance(new DateTime()).getFixedAmount();
    }

}
