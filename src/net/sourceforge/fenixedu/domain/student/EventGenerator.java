package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.AccountingEventsManager;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DfaRegistrationEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;

public class EventGenerator {

    public static void generateNecessaryEvents(StudentCurricularPlan studentCurricularPlan, Person person,
	    ExecutionYear executionYear) {

	final AdministrativeOffice administrativeOffice = AdministrativeOffice
		.readByAdministrativeOfficeType(studentCurricularPlan.getDegree().getDegreeType().getAdministrativeOfficeType());

	switch (studentCurricularPlan.getDegree().getDegreeType()) {

	case BOLONHA_ADVANCED_FORMATION_DIPLOMA:

	    if (!studentCurricularPlan.getDegree().getSigla().equalsIgnoreCase("POSI")) {

		final AccountingEventsManager accountingEventsManager = new AccountingEventsManager();
		final ExecutionYear executionYearToCreateEvents = executionYear != null ? executionYear : ExecutionYear
			.readCurrentExecutionYear();

		accountingEventsManager.createGratuityEvent(studentCurricularPlan, executionYearToCreateEvents, false);

		new DfaRegistrationEvent(administrativeOffice, person, studentCurricularPlan.getRegistration());

		accountingEventsManager.createInsuranceEvent(studentCurricularPlan, executionYearToCreateEvents, false);

	    }
	    break;

	// case BOLONHA_PHD_PROGRAM:
	//
	// new PhDGratuityEvent(administrativeOffice, person,
	// studentCurricularPlan, executionYear);
	// new PhDRegistrationEvent(administrativeOffice, person,
	// studentCurricularPlan
	// .getRegistration());
	//if(!person.hasInsuranceEventOrAdministrativeOfficeFeeInsuranceEventFor
	// (executionYear))
	// {
	// new InsuranceEvent(person, executionYear);
	// }
	// break;

	default:
	    break;

	}

    }
}
