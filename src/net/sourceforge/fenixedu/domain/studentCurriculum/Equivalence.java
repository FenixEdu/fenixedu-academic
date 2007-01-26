package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;

public class Equivalence extends Equivalence_Base {

    protected Equivalence() {
	super();
    }

    public Equivalence(StudentCurricularPlan studentCurricularPlan,
	    Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments, String grade) {
	this();
	init(studentCurricularPlan, dismissals, enrolments, grade);
    }

    public Equivalence(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
	    Collection<IEnrolment> enrolments, Double credits, String grade) {
	this();
	init(studentCurricularPlan, courseGroup, enrolments, credits, grade);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
	    Collection<IEnrolment> enrolments, Double credits, String grade) {
	initGrade(enrolments, grade);
	super.init(studentCurricularPlan, courseGroup, enrolments, credits);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan,
	    Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments, String grade) {
	initGrade(enrolments, grade);
	super.init(studentCurricularPlan, dismissals, enrolments);
    }

    private void initGrade(Collection<IEnrolment> enrolments, String grade) {
	if ((enrolments == null || enrolments.isEmpty()) && StringUtils.isEmpty(grade)) {
	    throw new DomainException("error.equivalence.must.define.enrolments.and.grade");
	}
	setGivenGrade(grade);
    }
}
