package net.sourceforge.fenixedu.domain.accounting.postingRules;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsurancePenaltyExemption;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class AdministrativeOfficeFeePR extends AdministrativeOfficeFeePR_Base {

    protected AdministrativeOfficeFeePR() {
	super();
    }

    public AdministrativeOfficeFeePR(DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount,
	    Money fixedAmountPenalty, YearMonthDay whenToApplyFixedAmountPenalty) {
	this();
	init(EntryType.ADMINISTRATIVE_OFFICE_FEE, EventType.ADMINISTRATIVE_OFFICE_FEE, startDate,
		endDate, serviceAgreementTemplate, fixedAmount, fixedAmountPenalty,
		whenToApplyFixedAmountPenalty);

    }

    @Override
    protected boolean hasPenalty(Event event, DateTime when) {
	return event.hasAnyPenaltyExemptionsFor(AdministrativeOfficeFeeAndInsurancePenaltyExemption.class) ? false
		: super.hasPenalty(event, when);
    }

    @Override
    public AdministrativeOfficeFeePR edit(Money fixedAmount, Money penaltyAmount,
	    YearMonthDay whenToApplyFixedAmountPenalty) {

	deactivate();

	return new AdministrativeOfficeFeePR(new DateTime().minus(1000), null,
		getServiceAgreementTemplate(), fixedAmount, penaltyAmount, whenToApplyFixedAmountPenalty);
    }

}
