package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Equivalence extends Equivalence_Base {

    public Equivalence(CurriculumGroup curriculumGroup, Enrolment enrolment) {
	super();
	if (curriculumGroup == null || enrolment == null) {
	    throw new DomainException("invalid arguments");
	}
	validateDegreeModuleLink(curriculumGroup, enrolment.getCurricularCourse());
	setCurriculumGroup(curriculumGroup);
	addEnrolments(enrolment);
    }

    @Override
    public void delete() {
	getEnrolments().clear();
	super.delete();
    }

    @Override
    public StringBuilder print(String tabs) {
	// TODO Auto-generated method stub
	return null;
    }
    
    @Override
    public boolean isAproved(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
        if(allEnrolmentsBeforeExecutionPeriod(executionPeriod)) {
            return this.getCurricularCourse().isEquivalent(curricularCourse);
        } else {
            return false;
        }
    }
    
    private boolean allEnrolmentsBeforeExecutionPeriod(ExecutionPeriod executionPeriod) {
	for (Enrolment enrolment : this.getEnrolmentsSet()) {
	    if(enrolment.getExecutionPeriod().isAfter(executionPeriod)) {
		return false;
	    }
	}
	return true;
    }
    
    public CurricularCourse getCurricularCourse() {
	return (CurricularCourse) getDegreeModule();
    }
}
