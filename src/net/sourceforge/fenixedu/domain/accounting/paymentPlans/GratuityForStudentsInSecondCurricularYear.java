package net.sourceforge.fenixedu.domain.accounting.paymentPlans;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.HasEnrolmentsForExecutionSemesterPaymentPlanRule;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.PaymentPlanRule;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.PaymentPlanRuleFactory;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.StudentIsInSecondCurricularYearPaymentPlanRule;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;

public class GratuityForStudentsInSecondCurricularYear extends GratuityForStudentsInSecondCurricularYear_Base {

    protected GratuityForStudentsInSecondCurricularYear() {
        super();
    }

    public GratuityForStudentsInSecondCurricularYear(final ExecutionYear executionYear,
            final DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate, final Boolean defaultPlan) {
        this();
        super.init(executionYear, serviceAgreementTemplate, defaultPlan);
    }

    public GratuityForStudentsInSecondCurricularYear(final ExecutionYear executionYear,
            final DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate) {
        this(executionYear, serviceAgreementTemplate, false);
    }

    @Override
    protected Collection<PaymentPlanRule> getSpecificPaymentPlanRules() {
        return Arrays.asList(PaymentPlanRuleFactory.create(StudentIsInSecondCurricularYearPaymentPlanRule.class),

        PaymentPlanRuleFactory.create(HasEnrolmentsForExecutionSemesterPaymentPlanRule.class)

        );
    }

    @Override
    protected Set<Class<? extends GratuityPaymentPlan>> getPaymentPlansWhichHasPrecedence() {
        Set<Class<? extends GratuityPaymentPlan>> plans = new HashSet<Class<? extends GratuityPaymentPlan>>();
        plans.add(FullGratuityPaymentPlan.class);

        return plans;
    }

}
