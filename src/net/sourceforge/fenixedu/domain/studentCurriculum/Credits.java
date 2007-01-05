package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Credits extends Credits_Base {
    
    protected  Credits() {
        super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(this.getClass().getName());
    }
    
    public Credits(StudentCurricularPlan studentCurricularPlan, Collection<GenericPair<CurriculumGroup, CurricularCourse>> dismissals, Collection<Enrolment> enrolments) {
	super();
	init(studentCurricularPlan, dismissals, enrolments);
    }
    
    public Credits(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup, Collection<Enrolment> enrolments, Double credits) {
	super();
	init(studentCurricularPlan, curriculumGroup, enrolments, credits);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup, Collection<Enrolment> enrolments, Double credits) {
	if(studentCurricularPlan == null || curriculumGroup == null || credits == null) {
	    throw new DomainException("error.credits.wrong.arguments");
	}
	checkCurriculumGroup(studentCurricularPlan, curriculumGroup);
	setStudentCurricularPlan(studentCurricularPlan);
	setGivenCredits(credits);
	if(enrolments != null) {
	    getEnrolments().addAll(enrolments);
	}
	new Dismissal(this, curriculumGroup);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, Collection<GenericPair<CurriculumGroup, CurricularCourse>> dismissals, Collection<Enrolment> enrolments) {
	if(studentCurricularPlan == null || dismissals == null || dismissals.isEmpty()) {
	    throw new DomainException("error.credits.wrong.arguments");
	}
	setStudentCurricularPlan(studentCurricularPlan);
	if(enrolments != null) {
	    getEnrolments().addAll(enrolments);
	}

	for (GenericPair<CurriculumGroup, CurricularCourse> pair : dismissals) {
	    checkCurriculumGroup(studentCurricularPlan, pair.getLeft());
	    new Dismissal(this, pair.getLeft(), pair.getRight());
	}
    }
    
    private void checkCurriculumGroup(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup) {
	if(!studentCurricularPlan.getRoot().hasCurriculumModule(curriculumGroup)) {
	    throw new DomainException("error.credits.invalid.studentCurricularPlan.curriculumGroup");
	}
    }
    
}
