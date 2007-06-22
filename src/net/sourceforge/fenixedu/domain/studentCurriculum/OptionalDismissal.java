package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class OptionalDismissal extends OptionalDismissal_Base {
    
    protected  OptionalDismissal() {
        super();
    }
    
    public OptionalDismissal(Credits credits, CurriculumGroup curriculumGroup, OptionalCurricularCourse optionalCurricularCourse, Double ectsCredits) {
	init(credits, curriculumGroup, optionalCurricularCourse, ectsCredits);
    }
    
    protected void init(Credits credits, CurriculumGroup curriculumGroup, OptionalCurricularCourse optionalCurricularCourse, Double ectsCredits) {
	init(credits, curriculumGroup, optionalCurricularCourse);
	
	setEctsCredits(ectsCredits);
    }
    
    @Override
    public void setDegreeModule(final DegreeModule degreeModule) {
	if (degreeModule != null && !(degreeModule instanceof OptionalCurricularCourse)) {
	    throw new DomainException(
		    "error.optionalDismissal.DegreeModuleCanOnlyBeOptionalCurricularCourse");
	}
	super.setDegreeModule(degreeModule);
    }
    
    @Override
    public Double getEctsCredits() {
	if(super.getEctsCredits() != null) {
	    return super.getEctsCredits();
	} else {
	    return getEnrolmentsEcts();
	}
        
    }
        
}
