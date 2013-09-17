package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DfaRegistrationEvent;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import pt.ist.fenixframework.Atomic;

public class AccountingEventsCreator {

    @Atomic
    static public void createInsuranceEvent(final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {
        check(AcademicPredicates.MANAGE_ACCOUNTING_EVENTS);
        studentCurricularPlan.getRegistration().getStudent().createInsuranceEvent(studentCurricularPlan, executionYear);
    }

    @Atomic
    static public void createGratuityEvent(final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {
        check(AcademicPredicates.MANAGE_ACCOUNTING_EVENTS);
        studentCurricularPlan.getRegistration().getStudent().createGratuityEvent(studentCurricularPlan, executionYear);
    }

    @Atomic
    static public void createAdministrativeOfficeFeeAndInsuranceEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear) {
        check(AcademicPredicates.MANAGE_ACCOUNTING_EVENTS);
        studentCurricularPlan.getRegistration().getStudent()
                .createAdministrativeOfficeFeeEvent(studentCurricularPlan, executionYear);
    }

    @Atomic
    static public void createEnrolmentOutOfPeriodEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionSemester executionSemester, final Integer numberOfDelayDays) {
        check(AcademicPredicates.MANAGE_ACCOUNTING_EVENTS);
        studentCurricularPlan.getRegistration().getStudent()
                .createEnrolmentOutOfPeriodEvent(studentCurricularPlan, executionSemester, numberOfDelayDays);
    }

    @Atomic
    static public void createDfaRegistrationEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear) {
        check(AcademicPredicates.MANAGE_ACCOUNTING_EVENTS);
        new DfaRegistrationEvent(studentCurricularPlan.getDegree().getAdministrativeOffice(), studentCurricularPlan.getPerson(),
                studentCurricularPlan.getRegistration(), executionYear);
    }

}