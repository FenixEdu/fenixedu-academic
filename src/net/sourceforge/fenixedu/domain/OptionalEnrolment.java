package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class OptionalEnrolment extends OptionalEnrolment_Base {
    
    protected  OptionalEnrolment() {
        super();
    }
    
    public OptionalEnrolment(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
	    CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
            EnrollmentCondition enrolmentCondition, String createdBy, OptionalCurricularCourse optionalCurricularCourse) {
	if (studentCurricularPlan == null || curriculumGroup == null || curricularCourse == null
		|| executionPeriod == null || enrolmentCondition == null || createdBy == null || optionalCurricularCourse == null) {
    		throw new DomainException("invalid arguments");
    	}
	checkInitConstraints(studentCurricularPlan, curricularCourse, executionPeriod);
	//TODO: check this
	//validateDegreeModuleLink(curriculumGroup, curricularCourse);
	initializeAsNew(studentCurricularPlan, curriculumGroup, curricularCourse, executionPeriod,
		enrolmentCondition, createdBy);
	setOptionalCurricularCourse(optionalCurricularCourse);
    }
    

    protected void checkInitConstraints(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse, ExecutionPeriod executionPeriod, OptionalCurricularCourse optionalCurricularCourse) {
        super.checkInitConstraints(studentCurricularPlan, curricularCourse, executionPeriod);
        //TODO check constraint for OptionalCurricularCourse
    }
    
    @Override
    public boolean isAproved(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
	if(executionPeriod == null || this.getExecutionPeriod().isBeforeOrEquals(executionPeriod)) {
            return (this.getCurricularCourse().isEquivalent(curricularCourse) || this.getOptionalCurricularCourse().isEquivalent(curricularCourse)) && this.isApproved();
        } else {
            return false;
        }
    }
    
    @Override
    public boolean isEnroledInExecutionPeriod(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
	return this.getExecutionPeriod().equals(executionPeriod) && 
		(this.getCurricularCourse().equals(curricularCourse) || this.getOptionalCurricularCourse().equals(curricularCourse));
    }
    
    @Override
    public boolean isOptional() {
        return true;
    }
    
    @Override
    public MultiLanguageString getName() {
	MultiLanguageString multiLanguageString = super.getName();
	setNameContent(multiLanguageString, Language.pt, this.getOptionalCurricularCourse().getName());
	setNameContent(multiLanguageString, Language.en, this.getOptionalCurricularCourse().getNameEn());
	return multiLanguageString;
    }
    
    private void setNameContent(MultiLanguageString multiLanguageString, Language language, String content) {
	if (content != null && content.length() > 0) {
	    if(multiLanguageString.getContent(language) != null) {
		multiLanguageString.setContent(language, multiLanguageString.getContent(language) + " (" + content + ")");
	    }
	}
    }
    
    @Override
    public boolean hasDegreeModule(final DegreeModule degreeModule) {
        return super.hasDegreeModule(degreeModule) || hasOptionalCurricularCourse(degreeModule);
    }
    
    private boolean hasOptionalCurricularCourse(final DegreeModule degreeModule) {
	return getOptionalCurricularCourse() == degreeModule;
    }
    
    @Override
    public CurriculumModule findCurriculumModuleFor(final DegreeModule degreeModule) {
	return hasDegreeModule(degreeModule) ? this : null;
    }
}
