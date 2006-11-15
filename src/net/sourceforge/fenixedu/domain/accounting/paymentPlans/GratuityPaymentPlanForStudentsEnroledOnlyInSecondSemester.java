package net.sourceforge.fenixedu.domain.accounting.paymentPlans;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;

public class GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester extends
	GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester_Base {

    protected GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester() {
	super();
    }

    public GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester(final ExecutionYear executionYear,
	    final ServiceAgreementTemplate serviceAgreementTemplate) {
	this(executionYear, serviceAgreementTemplate, false);
    }

    public GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester(final ExecutionYear executionYear,
	    final ServiceAgreementTemplate serviceAgreementTemplate, final Boolean defaultPlan) {
	this();
	super.init(executionYear, serviceAgreementTemplate, defaultPlan);
    }

    @Override
    public boolean isAppliableFor(StudentCurricularPlan studentCurricularPlan,
	    ExecutionYear executionYear) {
	return getExecutionYear() == executionYear
		&& !studentCurricularPlan.hasAnyEnrolmentForExecutionPeriod(executionYear
			.getFirstExecutionPeriod())
		&& studentCurricularPlan.hasAnyEnrolmentForExecutionPeriod(executionYear
			.getFirstExecutionPeriod().getNextExecutionPeriod());
    }

}
