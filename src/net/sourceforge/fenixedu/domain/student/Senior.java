/*
 * Created on Dec 10, 2004
 *
 */
package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 * 
 */
public class Senior extends Senior_Base {

    public Senior(Registration student) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setStudent(student);
    }

    public void setExpectedDegreeTerminationYearMonthDay(YearMonthDay date) {	
	setExpectedDegreeTerminationDateTime(date != null ? date.toDateTimeAtMidnight() : null);	
    }

    public YearMonthDay getExpectedDegreeTerminationYearMonthDay() {
	return getExpectedDegreeTerminationDateTime() != null ? getExpectedDegreeTerminationDateTime()
		.toYearMonthDay() : null;
    }

    @Override
    public void setStudent(Registration student) {
	if(student == null) {
	    throw new DomainException("error.senior.empty.senior");
	}
	super.setStudent(student);
    }
    
    @Override
    public void setExpectedDegreeAverageGrade(Integer expectedDegreeAverageGrade) {
	setLastModificationDateDateTime(new DateTime());
	super.setExpectedDegreeAverageGrade(expectedDegreeAverageGrade);
    }

    @Override
    public void setExpectedDegreeTerminationDateTime(DateTime expectedDegreeTerminationDateTime) {
	setLastModificationDateDateTime(new DateTime());
	super.setExpectedDegreeTerminationDateTime(expectedDegreeTerminationDateTime);
    }

    @Override
    public void setExtracurricularActivities(String extracurricularActivities) {
	setLastModificationDateDateTime(new DateTime());
	super.setExtracurricularActivities(extracurricularActivities);
    }

    @Override
    public void setInformaticsSkills(String informaticsSkills) {
	setLastModificationDateDateTime(new DateTime());
	super.setInformaticsSkills(informaticsSkills);
    }

    @Override
    public void setLanguageSkills(String languageSkills) {
	setLastModificationDateDateTime(new DateTime());
	super.setLanguageSkills(languageSkills);
    }

    @Override
    public void setProfessionalExperience(String professionalExperience) {
	setLastModificationDateDateTime(new DateTime());
	super.setProfessionalExperience(professionalExperience);
    }

    @Override
    public void setProfessionalInterests(String professionalInterests) {
	setLastModificationDateDateTime(new DateTime());
	super.setProfessionalInterests(professionalInterests);
    }

    @Override
    public void setSpecialtyField(String specialtyField) {
	setLastModificationDateDateTime(new DateTime());
	super.setSpecialtyField(specialtyField);
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
		senior = new Senior(registration);		
	    } else {
		return null;
	    }
	}

	return senior;
    }  
}
