package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.SelectedDismissal;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Equivalence extends Equivalence_Base {

    protected Equivalence() {
	super();
    }

    public Equivalence(StudentCurricularPlan studentCurricularPlan,
	    Collection<SelectedDismissal> dismissals, Collection<IEnrolment> enrolments, String grade) {
	this();
	init(studentCurricularPlan, dismissals, enrolments, grade);
    }

    public Equivalence(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
	    Collection<IEnrolment> enrolments, Double credits, String grade) {
	this();
	init(studentCurricularPlan, curriculumGroup, enrolments, credits, grade);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
	    Collection<IEnrolment> enrolments, Double credits, String grade) {
	initGrade(enrolments, grade);
	super.init(studentCurricularPlan, curriculumGroup, enrolments, credits);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan,
	    Collection<SelectedDismissal> dismissals, Collection<IEnrolment> enrolments, String grade) {
	initGrade(enrolments, grade);
	super.init(studentCurricularPlan, dismissals, enrolments);
    }

    private void initGrade(Collection<IEnrolment> enrolments, String grade) {
	if ((enrolments == null || enrolments.isEmpty()) && grade == null) {
	    throw new DomainException("error.equivalence.wrong.arguments");
	}
	setGivenGrade(grade);
    }
}
