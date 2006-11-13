package net.sourceforge.fenixedu.domain.accounting.paymentPlans;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public abstract class GratuityPaymentPlan extends GratuityPaymentPlan_Base {

    protected GratuityPaymentPlan() {
	super();
    }

    abstract public boolean isAppliableFor(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionYear executionYear);

}
