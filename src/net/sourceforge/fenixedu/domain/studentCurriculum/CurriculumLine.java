package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class CurriculumLine extends CurriculumLine_Base {
    
    public  CurriculumLine() {
        super();
    }
    
    public boolean isLeaf() {
    	return true;
    }
    
    protected void validateDegreeModuleLink(CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
    	if(!curriculumGroup.getDegreeModule().validate(curricularCourse)) {
    		throw new DomainException("invalid curriculum group");
    	}
    }
 
    
}
