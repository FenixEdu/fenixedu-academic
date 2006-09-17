package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class CurriculumLine extends CurriculumLine_Base {
    
    public CurriculumLine() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public boolean isLeaf() {
    	return true;
    }
    
    protected void validateDegreeModuleLink(CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
    	if(!curriculumGroup.getDegreeModule().validate(curricularCourse)) {
    	    throw new DomainException("error.studentCurriculum.curriculumLine.invalid.curriculum.group");
    	}
    }
    
    @Override
    public List<Enrolment> getEnrolments() {
	return Collections.emptyList();
    }
    
    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
        return hasCurriculumGroup() ? getCurriculumGroup().getStudentCurricularPlan() : null; 
    }
    
}
