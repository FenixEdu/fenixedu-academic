package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class OptionalEnrolment extends OptionalEnrolment_Base {

    protected OptionalEnrolment() {
	super();
    }

    public OptionalEnrolment(StudentCurricularPlan studentCurricularPlan,
	    CurriculumGroup curriculumGroup, CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod, EnrollmentCondition enrolmentCondition, String createdBy,
	    OptionalCurricularCourse optionalCurricularCourse) {
	if (studentCurricularPlan == null || curriculumGroup == null || curricularCourse == null
		|| executionPeriod == null || enrolmentCondition == null || createdBy == null
		|| optionalCurricularCourse == null) {
	    throw new DomainException("invalid arguments");
	}
	checkInitConstraints(studentCurricularPlan, curricularCourse, executionPeriod,
		optionalCurricularCourse);
	// TODO: check this
	// validateDegreeModuleLink(curriculumGroup, curricularCourse);
	initializeAsNew(studentCurricularPlan, curriculumGroup, curricularCourse, executionPeriod,
		enrolmentCondition, createdBy);
	setOptionalCurricularCourse(optionalCurricularCourse);
    }

    protected void checkInitConstraints(StudentCurricularPlan studentCurricularPlan,
	    CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
	    OptionalCurricularCourse optionalCurricularCourse) {
	super.checkInitConstraints(studentCurricularPlan, curricularCourse, executionPeriod);

	final OptionalEnrolment optionalEnrolment = (OptionalEnrolment) studentCurricularPlan
		.findEnrolmentFor(optionalCurricularCourse, executionPeriod);
	if (optionalEnrolment != null && optionalEnrolment.isValid(executionPeriod)) {
	    throw new DomainException(
		    "error.OptionalEnrolment.duplicate.enrolment",
		    optionalCurricularCourse.getName());

	}
    }

    @Override
    public boolean isApproved(final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
	if (executionPeriod == null || getExecutionPeriod().isBeforeOrEquals(executionPeriod)) {
	    return (isCurricularCourseEquivalente(curricularCourse, executionPeriod) 
		    || isOptionalCurricularCourseEquivalente(curricularCourse, executionPeriod))
		    && isApproved();
	} else {
	    return false;
	}
    }
    
    private boolean isCurricularCourseEquivalente(final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
	return getCurricularCourse().isEquivalent(curricularCourse) || hasCurricularCourseEquivalence(getCurricularCourse(), curricularCourse, executionPeriod);
    }
    
    private boolean isOptionalCurricularCourseEquivalente(final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
	return getOptionalCurricularCourse().isEquivalent(curricularCourse) || hasCurricularCourseEquivalence(getOptionalCurricularCourse(), curricularCourse, executionPeriod);
    }
    
    @Override
    public boolean isEnroledInExecutionPeriod(CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod) {
	return this.getExecutionPeriod().equals(executionPeriod)
		&& (this.getCurricularCourse().equals(curricularCourse) || this
			.getOptionalCurricularCourse().equals(curricularCourse));
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

    private void setNameContent(MultiLanguageString multiLanguageString, Language language,
	    String content) {
	if (content != null && content.length() > 0) {
	    if (multiLanguageString.getContent(language) != null) {
		multiLanguageString.setContent(language, multiLanguageString.getContent(language) + " ("
			+ content + ")");
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
    public void delete() {
	removeOptionalCurricularCourse();
	super.delete();
    }
}
