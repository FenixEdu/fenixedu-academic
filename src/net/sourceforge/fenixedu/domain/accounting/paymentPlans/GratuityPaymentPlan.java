package net.sourceforge.fenixedu.domain.accounting.paymentPlans;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;

public class GratuityPaymentPlan extends GratuityPaymentPlan_Base {

    protected GratuityPaymentPlan() {
	super();
    }

    public GratuityPaymentPlan(final ExecutionYear executionYear,
	    final DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate) {
	this(executionYear, serviceAgreementTemplate, false);
    }

    public GratuityPaymentPlan(final ExecutionYear executionYear,
	    final DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate,
	    final Boolean defaultPlan) {
	this();
	super.init(executionYear, serviceAgreementTemplate, defaultPlan);
    }

    public boolean isAppliableFor(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionYear executionYear) {
	return false;
    }

    @Override
    protected boolean isToApplyPenalty(Event event, Installment installment) {
	return !((GratuityEventWithPaymentPlan) event).hasPenaltyExemptionFor(installment);
    }

}
