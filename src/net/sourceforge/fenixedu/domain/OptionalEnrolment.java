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
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.EnrolmentAction;
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
	    return isApproved() && hasCurricularCourseOrOptionalCurricularCourse(curricularCourse, executionPeriod);
	} else {
	    return false;
	}
    }

    private boolean hasCurricularCourseOrOptionalCurricularCourse(final CurricularCourse curricularCourse,
	    final ExecutionPeriod executionPeriod) {
	return hasCurricularCourse(getCurricularCourse(), curricularCourse, executionPeriod)
		|| hasCurricularCourse(getOptionalCurricularCourse(), curricularCourse, executionPeriod);
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

    /**
     * 
     * After create new OptionalEnrolment, must delete Enrolment
     * (to delete Enrolment remove: ProgramCertificateRequests, CourseLoadRequests, ExamDateCertificateRequests)
     * @param enrolment 
     * @param curriculumGroup: new CurriculumGroup for OptionalEnrolment
     * @param optionalCurricularCourse: choosed OptionalCurricularCourse
     * @return OptionalEnrolment
     */
    static OptionalEnrolment createBasedOn(final Enrolment enrolment, final CurriculumGroup curriculumGroup,
	    final OptionalCurricularCourse optionalCurricularCourse) {
	checkParameters(enrolment, curriculumGroup, optionalCurricularCourse);

	final OptionalEnrolment optionalEnrolment = new OptionalEnrolment();
	optionalEnrolment.setCurricularCourse(enrolment.getCurricularCourse());
	optionalEnrolment.setWeigth(enrolment.getWeigth());
	optionalEnrolment.setEnrollmentState(enrolment.getEnrollmentState());
	optionalEnrolment.setExecutionPeriod(enrolment.getExecutionPeriod());
	optionalEnrolment.setEnrolmentEvaluationType(enrolment.getEnrolmentEvaluationType());
	optionalEnrolment.setCreatedBy(AccessControl.getUserView().getUtilizador());
	optionalEnrolment.setCreationDateDateTime(enrolment.getCreationDateDateTime());
	optionalEnrolment.setEnrolmentCondition(enrolment.getEnrolmentCondition());
	optionalEnrolment.setCurriculumGroup(curriculumGroup);
	optionalEnrolment.setOptionalCurricularCourse(optionalCurricularCourse);

	optionalEnrolment.getEvaluations().addAll(enrolment.getEvaluations());
	optionalEnrolment.getProgramCertificateRequests().addAll(enrolment.getProgramCertificateRequests());
	optionalEnrolment.getCourseLoadRequests().addAll(enrolment.getCourseLoadRequests());
	optionalEnrolment.getExtraExamRequests().addAll(enrolment.getExtraExamRequests());
	optionalEnrolment.getEnrolmentWrappers().addAll(enrolment.getEnrolmentWrappers());
	optionalEnrolment.getTheses().addAll(enrolment.getTheses());
	optionalEnrolment.getExamDateCertificateRequests().addAll(enrolment.getExamDateCertificateRequests());
	optionalEnrolment.getAttends().addAll(enrolment.getAttends());
	optionalEnrolment.createEnrolmentLog(EnrolmentAction.ENROL);

	return optionalEnrolment;
    }

    private static void checkParameters(final Enrolment enrolment, final CurriculumGroup curriculumGroup,
	    final OptionalCurricularCourse optionalCurricularCourse) {
	if (enrolment == null || enrolment.isOptional()) {
	    throw new DomainException("error.OptionalEnrolment.invalid.enrolment");
	}
	if (curriculumGroup == null) {
	    throw new DomainException("error.OptionalEnrolment.invalid.curriculumGroup");
	}
	if (optionalCurricularCourse == null) {
	    throw new DomainException("error.OptionalEnrolment.invalid.optional.curricularCourse");
	}
    }
}
