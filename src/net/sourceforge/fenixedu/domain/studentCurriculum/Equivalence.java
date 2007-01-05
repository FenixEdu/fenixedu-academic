package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Equivalence extends Equivalence_Base {

    protected Equivalence() {
    }

    public Equivalence(StudentCurricularPlan studentCurricularPlan,
	    Collection<GenericPair<CurriculumGroup, CurricularCourse>> dismissals,
	    Collection<Enrolment> enrolments, String grade) {
	super();
	init(studentCurricularPlan, dismissals, enrolments, grade);
    }

    public Equivalence(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
	    Collection<Enrolment> enrolments, Double credits, String grade) {
	super();
	init(studentCurricularPlan, curriculumGroup, enrolments, credits, grade);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
	    Collection<Enrolment> enrolments, Double credits, String grade) {
	initGrade(enrolments, grade);
	super.init(studentCurricularPlan, curriculumGroup, enrolments, credits);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan,
	    Collection<GenericPair<CurriculumGroup, CurricularCourse>> dismissals,
	    Collection<Enrolment> enrolments, String grade) {
	initGrade(enrolments, grade);
	super.init(studentCurricularPlan, dismissals, enrolments);
    }

    private void initGrade(Collection<Enrolment> enrolments, String grade) {
	if ((enrolments == null || enrolments.isEmpty()) && grade == null) {
	    throw new DomainException("error.equivalence.wrong.arguments");
	}
	setGivenGrade(grade);
    }
}
