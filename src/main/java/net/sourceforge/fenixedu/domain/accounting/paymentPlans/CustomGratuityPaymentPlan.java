package net.sourceforge.fenixedu.domain.accounting.paymentPlans;

import java.util.Collection;
import java.util.Collections;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreement;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.PaymentPlanRule;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreements.DegreeCurricularPlanServiceAgreement;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class CustomGratuityPaymentPlan extends CustomGratuityPaymentPlan_Base {

    static {
        getRelationServiceAgreementServiceAgreementPaymentPlan().addListener(
                new RelationAdapter<ServiceAgreement, ServiceAgreementPaymentPlan>() {
                    @Override
                    public void beforeAdd(ServiceAgreement serviceAgreement, ServiceAgreementPaymentPlan paymentPlanToAdd) {

                        if (paymentPlanToAdd != null) {
                            if (paymentPlanToAdd.isCustomGratuityPaymentPlan()
                                    && serviceAgreement.hasCustomGratuityPaymentPlan(paymentPlanToAdd.getExecutionYear())) {
                                throw new DomainException(
                                        "error.domain.accounting.ServiceAgreement.already.has.a.customGratuity.payment.plan.for.execution.year");
                            }

                        }
                    }
                });

        getRelationGratuityPaymentPlanGratuityEventWithPaymentPlan().addListener(
                new RelationAdapter<PaymentPlan, GratuityEventWithPaymentPlan>() {
                    @Override
                    public void beforeAdd(PaymentPlan paymentPlan, GratuityEventWithPaymentPlan event) {
                        if (paymentPlan != null && event != null) {
                            if (paymentPlan.isCustomGratuityPaymentPlan() && paymentPlan.hasAnyGratuityEventsWithPaymentPlan()) {
                                throw new DomainException("error.domain.accounting.PaymentPlan.already.has.gratuityEvent");
                            }
                        }
                    }
                });
    }

    private CustomGratuityPaymentPlan() {
        super();
    }

    public CustomGratuityPaymentPlan(final ExecutionYear executionYear,
            final DegreeCurricularPlanServiceAgreement serviceAgreement) {
        this();
        super.init(executionYear, serviceAgreement, Boolean.FALSE);
    }

    @Override
    public boolean isGratuityPaymentPlan() {
        return true;
    }

    @Override
    public boolean isCustomGratuityPaymentPlan() {
        return true;
    }

    @Override
    public void delete() {
        while (hasAnyInstallments()) {
            getInstallments().iterator().next().delete();
        }
        getGratuityEventsWithPaymentPlan().clear();
        removeParameters();
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Override
    protected Collection<PaymentPlanRule> getSpecificPaymentPlanRules() {
        return Collections.emptyList();
    }
}
