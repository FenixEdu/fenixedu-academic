package net.sourceforge.fenixedu.domain.accounting.postingRules;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class InsurancePR extends InsurancePR_Base {

    protected InsurancePR() {
	super();
    }

    public InsurancePR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount) {
	this();
	init(EntryType.INSURANCE_FEE, EventType.INSURANCE, startDate, endDate, serviceAgreementTemplate, fixedAmount);
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {

	if (event instanceof InsuranceEvent) {
	    InsuranceEvent insuranceEvent = (InsuranceEvent) event;
	    if (insuranceEvent.hasInsuranceExemption()) {
		return Money.ZERO;
	    }
	} else if (event instanceof AdministrativeOfficeFeeAndInsuranceEvent) {
	    final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent = (AdministrativeOfficeFeeAndInsuranceEvent) event;
	    if (administrativeOfficeFeeAndInsuranceEvent.hasInsuranceExemption()) {
		return Money.ZERO;
	    }
	}

	return getFixedAmount();
    }

    @Checked("PostingRulePredicates.editPredicate")
    @Override
    public FixedAmountPR edit(final Money fixedAmount) {
	deactivate();
	return new InsurancePR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), fixedAmount);
    }

}
