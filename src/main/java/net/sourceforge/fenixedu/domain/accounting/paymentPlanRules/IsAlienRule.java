package net.sourceforge.fenixedu.domain.accounting.paymentPlanRules;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;

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
