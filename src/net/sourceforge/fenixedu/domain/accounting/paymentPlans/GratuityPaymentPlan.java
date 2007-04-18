package net.sourceforge.fenixedu.domain.accounting.paymentPlans;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
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

}
