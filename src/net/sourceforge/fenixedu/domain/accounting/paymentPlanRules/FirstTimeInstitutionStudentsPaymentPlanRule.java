package net.sourceforge.fenixedu.domain.accounting.paymentPlanRules;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

public class FirstTimeInstitutionStudentsPaymentPlanRule implements PaymentPlanRule {

    FirstTimeInstitutionStudentsPaymentPlanRule() {
    }

    @Override
    public boolean isEvaluatedInNotSpecificPaymentRules() {
        return true;
    }

    @Override
    public boolean isAppliableFor(final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {

        if (studentCurricularPlan.getRegistration().getStartExecutionYear() != executionYear) {
            return false;
        }

        final Student student = studentCurricularPlan.getRegistration().getStudent();
        return hasOnlyOneValidRegistration(student, studentCurricularPlan.getRegistration());
    }

    private boolean hasOnlyOneValidRegistration(final Student student, final Registration current) {

        if (student.getRegistrationsCount() > 1) {

            for (final Registration registration : student.getRegistrations()) {

                if (registration != current) {

                    if (registration.getActiveStateType() != RegistrationStateType.CANCELED) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
