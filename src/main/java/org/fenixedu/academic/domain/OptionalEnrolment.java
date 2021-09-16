/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import org.fenixedu.academic.domain.curriculum.EnrollmentCondition;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.degreeStructure.OptionalCurricularCourse;
import org.fenixedu.academic.domain.enrolment.EnroledOptionalEnrolment;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.log.OptionalEnrolmentLog;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.util.EnrolmentAction;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.fenixedu.commons.i18n.LocalizedString;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class OptionalEnrolment extends OptionalEnrolment_Base {

    public static final String SIGNAL_CREATED = "fenixedu.academic.optional.enrolment.created";

    protected OptionalEnrolment() {
        super();
    }

    public OptionalEnrolment(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
            CurricularCourse curricularCourse, ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition,
            String createdBy, OptionalCurricularCourse optionalCurricularCourse) {

        if (studentCurricularPlan == null || curriculumGroup == null || curricularCourse == null || executionSemester == null
                || enrolmentCondition == null || createdBy == null || optionalCurricularCourse == null) {
            throw new DomainException("invalid arguments");
        }
        checkInitConstraints(studentCurricularPlan, curricularCourse, executionSemester, optionalCurricularCourse);
        // TODO: check this
        // validateDegreeModuleLink(curriculumGroup, curricularCourse);
        initializeAsNew(studentCurricularPlan, curriculumGroup, curricularCourse, executionSemester, enrolmentCondition,
                createdBy);
        setOptionalCurricularCourse(optionalCurricularCourse);
        createCurriculumLineLog(EnrolmentAction.ENROL);
        Signal.emit(SIGNAL_CREATED, new DomainObjectEvent<OptionalEnrolment>(this));
    }

    protected void checkInitConstraints(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
            ExecutionSemester executionSemester, OptionalCurricularCourse optionalCurricularCourse) {
        super.checkInitConstraints(studentCurricularPlan, curricularCourse, executionSemester);

        final OptionalEnrolment optionalEnrolment =
                (OptionalEnrolment) studentCurricularPlan.findEnrolmentFor(optionalCurricularCourse, executionSemester);
        if (optionalEnrolment != null && optionalEnrolment.isValid(executionSemester)) {
            throw new DomainException("error.OptionalEnrolment.duplicate.enrolment", optionalCurricularCourse.getName());

        }
    }

    @Override
    protected void createCurriculumLineLog(final EnrolmentAction action) {
        new OptionalEnrolmentLog(action, getRegistration(), getCurricularCourse(), getOptionalCurricularCourse(),
                getExecutionPeriod(), getCurrentUser());
    }

    @Override
    final public boolean isApproved(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
        if (executionSemester == null || getExecutionPeriod().isBeforeOrEquals(executionSemester)) {
            return isApproved() && hasCurricularCourseOrOptionalCurricularCourse(curricularCourse, executionSemester);
        } else {
            return false;
        }
    }

    private boolean hasCurricularCourseOrOptionalCurricularCourse(final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester) {
        return hasCurricularCourse(getCurricularCourse(), curricularCourse, executionSemester)
                || hasCurricularCourse(getOptionalCurricularCourse(), curricularCourse, executionSemester);
    }

    @Override
    final public boolean isEnroledInExecutionPeriod(CurricularCourse curricularCourse, ExecutionSemester executionSemester) {
        return this.getExecutionPeriod().equals(executionSemester)
                && (this.getCurricularCourse().equals(curricularCourse) || this.getOptionalCurricularCourse().equals(
                        curricularCourse));
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    @Override
    public LocalizedString getName() {
        final ExecutionSemester executionSemester = getExecutionPeriod();
        return new LocalizedString().with(org.fenixedu.academic.util.LocaleUtils.PT,
                this.getOptionalCurricularCourse().getName(executionSemester)).with(org.fenixedu.academic.util.LocaleUtils.EN,
                this.getOptionalCurricularCourse().getNameEn(executionSemester));
    }

    @Override
    public LocalizedString getPresentationName() {

        final String namePt =
                String.format("%s (%s)", getOptionalCurricularCourse().getName(getExecutionPeriod()), getCurricularCourse()
                        .getName(getExecutionPeriod()));

        final String nameEn =
                String.format("%s (%s)", getOptionalCurricularCourse().getNameEn(getExecutionPeriod()), getCurricularCourse()
                        .getNameEn(getExecutionPeriod()));

        return new LocalizedString().with(org.fenixedu.academic.util.LocaleUtils.PT, namePt).with(org.fenixedu.academic.util.LocaleUtils.EN, nameEn);
    }

    @Override
    public boolean hasDegreeModule(final DegreeModule degreeModule) {
        return super.hasDegreeModule(degreeModule) || hasOptionalCurricularCourse(degreeModule);
    }

    private boolean hasOptionalCurricularCourse(final DegreeModule degreeModule) {
        return getOptionalCurricularCourse() == degreeModule;
    }

    @Override
    protected void deleteInformation() {
        super.deleteInformation();
        setOptionalCurricularCourse(null);
    }

    @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(ExecutionSemester executionSemester) {
        if (isValid(executionSemester) && isEnroled()) {
            final Set<IDegreeModuleToEvaluate> result = new HashSet<IDegreeModuleToEvaluate>(1);
            result.add(new EnroledOptionalEnrolment(this, getOptionalCurricularCourse(), executionSemester));
            return result;
        }
        return Collections.emptySet();

    }

    /**
     * 
     * After create new OptionalEnrolment, must delete Enrolment (to delete
     * Enrolment disconnect at least: ProgramCertificateRequests,
     * CourseLoadRequests, ExamDateCertificateRequests)
     * 
     * @param enrolment
     * @param curriculumGroup
     *            : new CurriculumGroup for OptionalEnrolment
     * @param optionalCurricularCourse
     *            : choosed OptionalCurricularCourse
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
        optionalEnrolment.setEvaluationSeason(enrolment.getEvaluationSeason());
        optionalEnrolment.setCreatedBy(Authenticate.getUser().getUsername());
        optionalEnrolment.setCreationDateDateTime(enrolment.getCreationDateDateTime());
        optionalEnrolment.setEnrolmentCondition(enrolment.getEnrolmentCondition());
        optionalEnrolment.setCurriculumGroup(curriculumGroup);
        optionalEnrolment.setOptionalCurricularCourse(optionalCurricularCourse);

        optionalEnrolment.getEvaluationsSet().addAll(enrolment.getEvaluationsSet());
        optionalEnrolment.getProgramCertificateRequestsSet().addAll(enrolment.getProgramCertificateRequestsSet());
        optionalEnrolment.getCourseLoadRequestsSet().addAll(enrolment.getCourseLoadRequestsSet());
        optionalEnrolment.getExtraExamRequestsSet().addAll(enrolment.getExtraExamRequestsSet());
        optionalEnrolment.getEnrolmentWrappersSet().addAll(enrolment.getEnrolmentWrappersSet());
        optionalEnrolment.getThesesSet().addAll(enrolment.getThesesSet());
        optionalEnrolment.getExamDateCertificateRequestsSet().addAll(enrolment.getExamDateCertificateRequestsSet());
        changeAttends(enrolment, optionalEnrolment);
        optionalEnrolment.createCurriculumLineLog(EnrolmentAction.ENROL);

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
