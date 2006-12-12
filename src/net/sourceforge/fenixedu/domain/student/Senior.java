/*
 * Created on Dec 10, 2004
 *
 */
package net.sourceforge.fenixedu.domain.student;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 * 
 */
public class Senior extends Senior_Base {

    public Senior() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void setExpectedDegreeTerminationYearMonthDay(YearMonthDay date) {	
	setExpectedDegreeTerminationDateTime(date != null ? date.toDateTimeAtMidnight() : null);	
    }

    public YearMonthDay getExpectedDegreeTerminationYearMonthDay() {
	return getExpectedDegreeTerminationDateTime() != null ? getExpectedDegreeTerminationDateTime()
		.toYearMonthDay() : null;
    }

    public static Senior readByUsername(Person person) {

	Senior senior = null;

	final Registration registration = person.getStudentByType(DegreeType.DEGREE);
	if (registration == null) {
	    return null;
	}

	final StudentCurricularPlan studentCurricularPlan = registration
		.getActiveStudentCurricularPlan();
	if (studentCurricularPlan == null) {
	    return null;
	}

	final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan
		.getDegreeCurricularPlan();
	final Degree degree = degreeCurricularPlan.getDegree();
	senior = registration.getSenior();

	if (senior == null) {
	    final int curricularYear = registration.getCurricularYear();
	    if (curricularYear == degree.getDegreeType().getYears()) {
		senior = new Senior();
		senior.setStudent(registration);
	    } else {
		return null;
	    }
	}

	return senior;
    }
}
