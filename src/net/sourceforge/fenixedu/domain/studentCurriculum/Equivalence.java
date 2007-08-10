package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Equivalence extends Equivalence_Base {

    protected Equivalence() {
	super();
	setGrade(Grade.createEmptyGrade());
    }

    public Equivalence(StudentCurricularPlan studentCurricularPlan,
	    Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments, Grade grade, ExecutionPeriod executionPeriod) {
	this();
	init(studentCurricularPlan, dismissals, enrolments, grade, executionPeriod);
    }

    public Equivalence(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
	    Collection<IEnrolment> enrolments, Double credits, Grade grade, ExecutionPeriod executionPeriod) {
	this();
	init(studentCurricularPlan, courseGroup, enrolments, credits, grade, executionPeriod);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
	    Collection<IEnrolment> enrolments, Double credits, Grade grade, ExecutionPeriod executionPeriod) {
	initGrade(enrolments, grade);
	super.init(studentCurricularPlan, courseGroup, enrolments, credits, executionPeriod);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan,
	    Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments, Grade grade, ExecutionPeriod executionPeriod) {
	initGrade(enrolments, grade);
	super.init(studentCurricularPlan, dismissals, enrolments, executionPeriod);
    }

    private void initGrade(Collection<IEnrolment> enrolments, Grade grade) {
	if ((enrolments == null || enrolments.isEmpty()) && grade.isEmpty()) {
	    throw new DomainException("error.equivalence.must.define.enrolments.and.grade");
	}
	setGrade(grade);
    }
    
    @Override
    public String getGivenGrade() {
        return getGrade() != null ? getGrade().getValue() : null;
    }
    
    @Override
    public boolean isEquivalence() {
        return true;
    }
}
