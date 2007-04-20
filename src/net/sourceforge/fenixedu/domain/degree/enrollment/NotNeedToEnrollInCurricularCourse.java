package net.sourceforge.fenixedu.domain.degree.enrollment;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * @author David Santos in Jun 17, 2004
 */

public class NotNeedToEnrollInCurricularCourse extends NotNeedToEnrollInCurricularCourse_Base {

    public NotNeedToEnrollInCurricularCourse() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public NotNeedToEnrollInCurricularCourse(CurricularCourse curricularCourse,
	    StudentCurricularPlan studentCurricularPlan) {
	this();
	setCurricularCourse(curricularCourse);
	setStudentCurricularPlan(studentCurricularPlan);
    }

    public void delete() {
	removeStudentCurricularPlan();
	removeCurricularCourse();
	removeRootDomainObject();
	deleteDomainObject();
    }

    public Double getEctsCredits() {
	if (isDueToEquivalence()) {
	    return Double.valueOf(0d);
    	}
	
	return getCurricularCourse().getEctsCredits();
    }

    private boolean isDueToEquivalence() {
	for (final CurricularCourseEquivalence curricularCourseEquivalence : getCurricularCourse().getCurricularCourseEquivalencesSet()) {
	    if (curricularCourseEquivalence.isSatisfied(getRegistration())) {
		return true;
	    }
	}
	
	return false;
    }

    public Registration getRegistration() {
	return getStudentCurricularPlan().getRegistration();
    }
    
}
