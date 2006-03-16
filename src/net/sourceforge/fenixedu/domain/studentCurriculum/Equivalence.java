package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Equivalence extends Equivalence_Base {
    
    public  Equivalence(CurriculumGroup curriculumGroup, Enrolment enrolment) {
    	super();
    	if(curriculumGroup == null || enrolment == null) {
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
    
}
