package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DfaRegistrationEvent;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class AccountingEventsCreator {

    @Checked("AcademicPredicates.MANAGE_ACCOUNTING_EVENTS")
    @Atomic
    static public void createInsuranceEvent(final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {
        studentCurricularPlan.getRegistration().getStudent().createInsuranceEvent(studentCurricularPlan, executionYear);
    }

    @Checked("AcademicPredicates.MANAGE_ACCOUNTING_EVENTS")
    @Atomic
    static public void createGratuityEvent(final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {
        studentCurricularPlan.getRegistration().getStudent().createGratuityEvent(studentCurricularPlan, executionYear);
    }

    @Checked("AcademicPredicates.MANAGE_ACCOUNTING_EVENTS")
    @Atomic
    static public void createAdministrativeOfficeFeeAndInsuranceEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear) {
        studentCurricularPlan.getRegistration().getStudent()
                .createAdministrativeOfficeFeeEvent(studentCurricularPlan, executionYear);
    }

    @Checked("AcademicPredicates.MANAGE_ACCOUNTING_EVENTS")
    @Atomic
    static public void createEnrolmentOutOfPeriodEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionSemester executionSemester, final Integer numberOfDelayDays) {
        studentCurricularPlan.getRegistration().getStudent()
                .createEnrolmentOutOfPeriodEvent(studentCurricularPlan, executionSemester, numberOfDelayDays);
    }

    @Checked("AcademicPredicates.MANAGE_ACCOUNTING_EVENTS")
    @Atomic
    static public void createDfaRegistrationEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear) {
        new DfaRegistrationEvent(studentCurricularPlan.getDegree().getAdministrativeOffice(), studentCurricularPlan.getPerson(),
                studentCurricularPlan.getRegistration(), executionYear);
    }

}