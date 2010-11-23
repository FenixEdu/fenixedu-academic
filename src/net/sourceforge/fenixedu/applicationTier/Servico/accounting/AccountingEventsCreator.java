package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DfaRegistrationEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class AccountingEventsCreator {

    @Checked("RolePredicates.MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    static public void createInsuranceEvent(final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {
	studentCurricularPlan.getRegistration().getStudent().createInsuranceEvent(studentCurricularPlan, executionYear);

    }

    @Checked("RolePredicates.MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    static public void createGratuityEvent(final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {
	studentCurricularPlan.getRegistration().getStudent().createGratuityEvent(studentCurricularPlan, executionYear);
    }

    @Checked("RolePredicates.MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    static public void createAdministrativeOfficeFeeAndInsuranceEvent(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionYear executionYear) {
	studentCurricularPlan.getRegistration().getStudent().createAdministrativeOfficeFeeEvent(studentCurricularPlan,
		executionYear);
    }

    @Checked("RolePredicates.MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    static public void createEnrolmentOutOfPeriodEvent(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionSemester executionSemester, final Integer numberOfDelayDays) {
	studentCurricularPlan.getRegistration().getStudent().createEnrolmentOutOfPeriodEvent(studentCurricularPlan,
		executionSemester, numberOfDelayDays);
    }

    @Checked("RolePredicates.MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    static public void createDfaRegistrationEvent(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionYear executionYear) {
	AdministrativeOffice office = AccessControl.getPerson().getEmployeeAdministrativeOffice();
	new DfaRegistrationEvent(office, studentCurricularPlan.getPerson(), studentCurricularPlan.getRegistration(),
		executionYear);
    }

}