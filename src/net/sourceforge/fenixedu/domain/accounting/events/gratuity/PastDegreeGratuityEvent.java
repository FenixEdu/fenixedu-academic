package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

public class PastDegreeGratuityEvent extends PastDegreeGratuityEvent_Base {

    protected PastDegreeGratuityEvent() {
	super();
    }

    public PastDegreeGratuityEvent(final AdministrativeOffice administrativeOffice, final Person person,
	    final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear,
	    final Money pastDegreeGratuityAmount) {
	this();

	init(administrativeOffice, person, studentCurricularPlan, executionYear, pastDegreeGratuityAmount);

    }

    private void init(AdministrativeOffice administrativeOffice, Person person, StudentCurricularPlan studentCurricularPlan,
	    ExecutionYear executionYear, Money pastDegreeGratuityAmount) {
	super.init(administrativeOffice, person, studentCurricularPlan, executionYear);
	checkParameters(studentCurricularPlan, pastDegreeGratuityAmount);
	super.setPastDegreeGratuityAmount(pastDegreeGratuityAmount);
    }

    private void checkParameters(StudentCurricularPlan studentCurricularPlan, Money pastDegreeGratuityAmount) {
	if (studentCurricularPlan.getDegreeType() != DegreeType.DEGREE) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.PastDegreeGratuityEvent.invalid.degree.type.for.student.curricular.plan");
	}

	if (pastDegreeGratuityAmount == null || pastDegreeGratuityAmount.isZero()) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.PastDegreeGratuityEvent.pastDegreeGratuityAmount.cannot.be.null.and.must.be.greather.than.zero");
	}
	
    }

    @Override
    public void setPastDegreeGratuityAmount(Money pastDegreeGratuityAmount) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.PastDegreeGratuityEvent.cannot.modify.pastDegreeGratuityAmount");
    }

}
