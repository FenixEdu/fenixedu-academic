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
	    throw new DomainException("error.optionalDismissal.DegreeModuleCanOnlyBeOptionalCurricularCourse");
	}
	super.setDegreeModule(degreeModule);
    }
    
    @Override
    public Double getEctsCredits() {
	return hasEctsCredits() ? super.getEctsCredits() : getEnrolmentsEcts();
    }
    
    private boolean hasEctsCredits() {
	return super.getEctsCredits() != null;
    }

    @Override
    public StringBuilder print(String tabs) {
	final StringBuilder builder = new StringBuilder();
	builder.append(tabs);
	builder.append("[OD ").append(hasDegreeModule() ? getDegreeModule().getName() :  "").append(" ]\n");
	return builder;
    }
    
}
