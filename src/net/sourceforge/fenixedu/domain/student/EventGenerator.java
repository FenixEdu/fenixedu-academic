package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DfaRegistrationEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.DfaGratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;

public class EventGenerator {

    public static void generateNecessaryEvents(StudentCurricularPlan studentCurricularPlan, Person person,
	    ExecutionYear executionYear) {

	final AdministrativeOffice administrativeOffice = AdministrativeOffice
		.readByAdministrativeOfficeType(studentCurricularPlan.getDegree().getDegreeType().getAdministrativeOfficeType());

	switch (studentCurricularPlan.getDegree().getDegreeType()) {

	case BOLONHA_ADVANCED_FORMATION_DIPLOMA:

	    if (!studentCurricularPlan.getDegree().getSigla().equalsIgnoreCase("POSI")) {
		new DfaGratuityEvent(administrativeOffice, person, studentCurricularPlan, ExecutionYear
			.readCurrentExecutionYear());
		new DfaRegistrationEvent(administrativeOffice, person, studentCurricularPlan.getRegistration());
		if (studentCurricularPlan.getRegistration().hasToPayGratuityOrInsurance()
			&& !studentCurricularPlan.getPerson().hasInsuranceEventOrAdministrativeOfficeFeeInsuranceEventFor(
				executionYear)) {
		    new InsuranceEvent(studentCurricularPlan.getPerson(), executionYear);
		}
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
