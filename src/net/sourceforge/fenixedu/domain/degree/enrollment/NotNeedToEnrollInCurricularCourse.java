package net.sourceforge.fenixedu.domain.degree.enrollment;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEnrolment;
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

    public NotNeedToEnrollInCurricularCourse(CurricularCourse curricularCourse, StudentCurricularPlan studentCurricularPlan) {
	this();
	setCurricularCourse(curricularCourse);
	setStudentCurricularPlan(studentCurricularPlan);
    }

    public void delete() {
	removeStudentCurricularPlan();
	removeCurricularCourse();
	removeRootDomainObject();
	getEnrolments().clear();
	getExternalEnrolments().clear();
	super.deleteDomainObject();
    }

    public Double getEctsCredits() {
	if (isDueToEquivalence()) {
	    return Double.valueOf(0d);
	}
	return getCurricularCourse().getEctsCredits();
    }

    private boolean isDueToEquivalence() {
	return isDueToOtherEnrolmentEquivalence() || isDueToGlobalEquivalence();
    }

    private boolean isDueToGlobalEquivalence() {
	for (final CurricularCourseEquivalence curricularCourseEquivalence : getCurricularCourse()
		.getCurricularCourseEquivalencesSet()) {
	    if (curricularCourseEquivalence.isSatisfied(getRegistration())) {
		return true;
	    }
	}

	return false;
    }

    private boolean isDueToOtherEnrolmentEquivalence() {
	for (final Enrolment enrolment : getEnrolmentsSet()) {
	    if (getRegistration().hasEnrolments(enrolment)) {
		return true;
	    }
	}

	return false;
    }

    public Registration getRegistration() {
	return getStudentCurricularPlan().getRegistration();
    }

    public Collection<IEnrolment> getIEnrolments() {
	Set<IEnrolment> res = new HashSet<IEnrolment>(getEnrolmentsSet());
	res.addAll(getExternalEnrolmentsSet());
	return res;
    }

}
