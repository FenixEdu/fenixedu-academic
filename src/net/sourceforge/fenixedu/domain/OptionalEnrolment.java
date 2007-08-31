package net.sourceforge.fenixedu.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.enrolment.EnroledOptionalEnrolment;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class OptionalEnrolment extends OptionalEnrolment_Base {

    protected OptionalEnrolment() {
	super();
    }

    public OptionalEnrolment(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
	    CurricularCourse curricularCourse, ExecutionPeriod executionPeriod, EnrollmentCondition enrolmentCondition,
	    String createdBy, OptionalCurricularCourse optionalCurricularCourse) {
	if (studentCurricularPlan == null || curriculumGroup == null || curricularCourse == null || executionPeriod == null
		|| enrolmentCondition == null || createdBy == null || optionalCurricularCourse == null) {
	    throw new DomainException("invalid arguments");
	}
	checkInitConstraints(studentCurricularPlan, curricularCourse, executionPeriod, optionalCurricularCourse);
	// TODO: check this
	// validateDegreeModuleLink(curriculumGroup, curricularCourse);
	initializeAsNew(studentCurricularPlan, curriculumGroup, curricularCourse, executionPeriod, enrolmentCondition, createdBy);
	setOptionalCurricularCourse(optionalCurricularCourse);
    }

    protected void checkInitConstraints(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod, OptionalCurricularCourse optionalCurricularCourse) {
	super.checkInitConstraints(studentCurricularPlan, curricularCourse, executionPeriod);

	final OptionalEnrolment optionalEnrolment = (OptionalEnrolment) studentCurricularPlan.findEnrolmentFor(
		optionalCurricularCourse, executionPeriod);
	if (optionalEnrolment != null && optionalEnrolment.isValid(executionPeriod)) {
	    throw new DomainException("error.OptionalEnrolment.duplicate.enrolment", optionalCurricularCourse.getName());

	}
    }

    @Override
    final public boolean isApproved(final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
	if (executionPeriod == null || getExecutionPeriod().isBeforeOrEquals(executionPeriod)) {
	    return (isCurricularCourseEquivalente(curricularCourse, executionPeriod) || isOptionalCurricularCourseEquivalente(
		    curricularCourse, executionPeriod))
		    && isApproved();
	} else {
	    return false;
	}
    }

    private boolean isCurricularCourseEquivalente(final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
	return getCurricularCourse().isEquivalent(curricularCourse)
		|| hasCurricularCourseEquivalence(getCurricularCourse(), curricularCourse, executionPeriod);
    }

    private boolean isOptionalCurricularCourseEquivalente(final CurricularCourse curricularCourse,
	    final ExecutionPeriod executionPeriod) {
	return getOptionalCurricularCourse().isEquivalent(curricularCourse)
		|| hasCurricularCourseEquivalence(getOptionalCurricularCourse(), curricularCourse, executionPeriod);
    }

    @Override
    final public boolean isEnroledInExecutionPeriod(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
	return this.getExecutionPeriod().equals(executionPeriod)
		&& (this.getCurricularCourse().equals(curricularCourse) || this.getOptionalCurricularCourse().equals(
			curricularCourse));
    }

    @Override
    public boolean isOptional() {
	return true;
    }

    @Override
    public MultiLanguageString getName() {
	ExecutionPeriod executionPeriod = getExecutionPeriod();
	return MultiLanguageString.i18n().add("pt", this.getOptionalCurricularCourse().getName(executionPeriod)).add("en",
		this.getOptionalCurricularCourse().getNameEn(executionPeriod)).finish();
    }

    @Override
    public boolean hasDegreeModule(final DegreeModule degreeModule) {
	return super.hasDegreeModule(degreeModule) || hasOptionalCurricularCourse(degreeModule);
    }

    private boolean hasOptionalCurricularCourse(final DegreeModule degreeModule) {
	return getOptionalCurricularCourse() == degreeModule;
    }

    @Override
    final public void delete() {
	removeOptionalCurricularCourse();
	super.delete();
    }

    @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(ExecutionPeriod executionPeriod) {
	if (isValid(executionPeriod) && isEnroled()) {
	    final Set<IDegreeModuleToEvaluate> result = new HashSet<IDegreeModuleToEvaluate>(1);
	    result.add(new EnroledOptionalEnrolment(this, getOptionalCurricularCourse(), executionPeriod));
	    return result;
	}
	return Collections.emptySet();

    }
}
