package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DfaGratuityEvent extends DfaGratuityEvent_Base {

    protected DfaGratuityEvent() {
	super();
    }

    public DfaGratuityEvent(AdministrativeOffice administrativeOffice, Person person,
	    StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
	
	this();

	checkRulesToCreate(studentCurricularPlan);
	init(administrativeOffice, person, studentCurricularPlan, executionYear);
    }

    private void checkRulesToCreate(StudentCurricularPlan studentCurricularPlan) {
	if (studentCurricularPlan.getDegreeType() != DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.DfaGratuityEvent.invalid.degreeType");
	}
    }

    @Override
    public boolean canApplyExemption() {
	return !isCustomEnrolmentModel();
    }
}
