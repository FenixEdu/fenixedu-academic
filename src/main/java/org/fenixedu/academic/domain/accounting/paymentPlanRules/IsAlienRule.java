package org.fenixedu.academic.domain.accounting.paymentPlanRules;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationProtocol;

public class IsAlienRule implements PaymentPlanRule {

    IsAlienRule() {
    }

    @Override
    public boolean isEvaluatedInNotSpecificPaymentRules() {
        return false;
    }

    @Override
    public boolean isAppliableFor(final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {
        final Registration registration = studentCurricularPlan.getRegistration();
        final RegistrationProtocol protocol = registration == null ? null : registration.getRegistrationProtocol();
        return protocol != null && protocol.isAlien();
    }

}
