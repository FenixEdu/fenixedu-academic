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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.accessControl.AcademicAuthorizationGroup;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.curriculum.CurricularCourseType;
import org.fenixedu.academic.domain.curriculum.EnrollmentCondition;
import org.fenixedu.academic.domain.curriculum.EnrollmentState;
import org.fenixedu.academic.domain.curriculum.EnrolmentEvaluationContext;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.degreeStructure.EctsConversionTable;
import org.fenixedu.academic.domain.degreeStructure.EctsTableIndex;
import org.fenixedu.academic.domain.enrolment.EnroledEnrolmentWrapper;
import org.fenixedu.academic.domain.enrolment.ExternalDegreeEnrolmentWrapper;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.log.EnrolmentEvaluationLog;
import org.fenixedu.academic.domain.log.EnrolmentLog;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationDataByExecutionYear;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.StudentStatute;
import org.fenixedu.academic.domain.student.curriculum.Curriculum;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CreditsDismissal;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.domain.studentCurriculum.EctsAndWeightProviderRegistry;
import org.fenixedu.academic.domain.studentCurriculum.EnrolmentWrapper;
import org.fenixedu.academic.domain.studentCurriculum.InternalCreditsSourceCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.InternalEnrolmentWrapper;
import org.fenixedu.academic.domain.studentCurriculum.OptionalDismissal;
import org.fenixedu.academic.domain.treasury.ITreasuryBridgeAPI;
import org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.EnrolmentAction;
import org.fenixedu.academic.util.EnrolmentEvaluationState;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dcs-rjao
 *
 *         24/Mar/2003
 */

public class Enrolment extends Enrolment_Base implements IEnrolment {

    static final public Logger logger = LoggerFactory.getLogger(Enrolment.class);

    static final public Comparator<Enrolment> REVERSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_ID = new Comparator<Enrolment>() {
        @Override
        public int compare(final Enrolment o1, final Enrolment o2) {
            return -COMPARATOR_BY_EXECUTION_PERIOD_AND_ID.compare(o1, o2);
        }
    };

    static final public Comparator<Enrolment> COMPARATOR_BY_STUDENT_NUMBER = new Comparator<Enrolment>() {
        @Override
        public int compare(final Enrolment e1, final Enrolment e2) {
            final int s1 = e1.getStudent().getNumber().intValue();
            final int s2 = e2.getStudent().getNumber().intValue();
            return s1 == s2 ? e1.getExternalId().compareTo(e2.getExternalId()) : s1 - s2;
        }
    };

    public static final String SIGNAL_CREATED = "fenixedu.academic.enrolment.created";

    public Enrolment() {
        super();
        super.setIsExtraCurricular(Boolean.FALSE);
        Signal.emit(Enrolment.SIGNAL_CREATED, new DomainObjectEvent<>(this));
    }

    public Enrolment(final StudentCurricularPlan studentCurricularPlan, final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester, final EnrollmentCondition enrolmentCondition, final String createdBy) {
        this();
        initializeAsNew(studentCurricularPlan, curricularCourse, executionSemester, enrolmentCondition, createdBy);
        createCurriculumLineLog(EnrolmentAction.ENROL);
    }

    @Override
    final public boolean isEnrolment() {
        return true;
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    final public boolean isExternalEnrolment() {
        return false;
    }

    @Override
    final public boolean isPropaedeutic() {
        return super.isPropaedeutic();
    }

    @Override
    public boolean isExtraCurricular() {
        return super.isExtraCurricular();
    }

    @Override
    @Deprecated
    public Boolean getIsExtraCurricular() {
        return isExtraCurricular();
    }

    @Override
    @Deprecated
    public void setIsExtraCurricular(final Boolean isExtraCurricular) {
        throw new DomainException("error.org.fenixedu.academic.domain.Enrolment.use.markAsExtraCurricular.method.instead");
    }

    public void markAsExtraCurricular() {
        setCurriculumGroup(getStudentCurricularPlan().getExtraCurriculumGroup());
        super.setIsExtraCurricular(null);
    }

    final public boolean isFinal() {
        return getEnrolmentCondition() == EnrollmentCondition.FINAL;
    }

    final public boolean isInvisible() {
        return getEnrolmentCondition() == EnrollmentCondition.INVISIBLE;
    }

    final public boolean isTemporary() {
        return getEnrolmentCondition() == EnrollmentCondition.TEMPORARY;
    }

    final public boolean isImpossible() {
        return getEnrolmentCondition() == EnrollmentCondition.IMPOSSIBLE;
    }

    /**
     * @deprecated Use hasSpecialSeason() instead
     */
    @Deprecated
    final public boolean isSpecialSeason() {
        return hasSpecialSeason();
    }

    // new student structure methods
    public Enrolment(final StudentCurricularPlan studentCurricularPlan, final CurriculumGroup curriculumGroup,
            final CurricularCourse curricularCourse, final ExecutionSemester executionSemester,
            final EnrollmentCondition enrolmentCondition, final String createdBy) {
        this();
        if (studentCurricularPlan == null || curriculumGroup == null || curricularCourse == null || executionSemester == null
                || enrolmentCondition == null || createdBy == null) {
            throw new DomainException("invalid arguments");
        }
        checkInitConstraints(studentCurricularPlan, curricularCourse, executionSemester);
        // TODO: check this
        // validateDegreeModuleLink(curriculumGroup, curricularCourse);
        initializeAsNew(studentCurricularPlan, curriculumGroup, curricularCourse, executionSemester, enrolmentCondition,
                createdBy);
        createCurriculumLineLog(EnrolmentAction.ENROL);
    }

    protected void checkInitConstraints(final StudentCurricularPlan studentCurricularPlan,
            final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
        if (studentCurricularPlan.getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse, executionSemester) != null) {
            throw new DomainException("error.Enrolment.duplicate.enrolment", curricularCourse.getName());
        }
    }

    protected void initializeAsNew(final StudentCurricularPlan studentCurricularPlan, final CurriculumGroup curriculumGroup,
            final CurricularCourse curricularCourse, final ExecutionSemester executionSemester,
            final EnrollmentCondition enrolmentCondition, final String createdBy) {
        initializeAsNewWithoutEnrolmentEvaluation(studentCurricularPlan, curriculumGroup, curricularCourse, executionSemester,
                enrolmentCondition, createdBy);
        createEnrolmentEvaluationWithoutGrade();
    }

    protected void initializeAsNewWithoutEnrolmentEvaluation(final StudentCurricularPlan studentCurricularPlan,
            final CurriculumGroup curriculumGroup, final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester, final EnrollmentCondition enrolmentCondition, final String createdBy) {
        initializeCommon(studentCurricularPlan, curricularCourse, executionSemester, enrolmentCondition, createdBy);
        setCurriculumGroup(curriculumGroup);
    }

    // end

    protected void initializeAsNew(final StudentCurricularPlan studentCurricularPlan, final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester, final EnrollmentCondition enrolmentCondition, final String createdBy) {
        initializeAsNewWithoutEnrolmentEvaluation(studentCurricularPlan, curricularCourse, executionSemester, enrolmentCondition,
                createdBy);
        createEnrolmentEvaluationWithoutGrade();
    }

    private void initializeCommon(final StudentCurricularPlan studentCurricularPlan, final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester, final EnrollmentCondition enrolmentCondition, final String createdBy) {
        setCurricularCourse(curricularCourse);
        setWeigth(studentCurricularPlan.isBolonhaDegree() ? curricularCourse.getEctsCredits(executionSemester) : curricularCourse
                .getWeigth());
        setEnrollmentState(EnrollmentState.ENROLLED);
        setExecutionPeriod(executionSemester);
        setEvaluationSeason(EvaluationConfiguration.getInstance().getDefaultEvaluationSeason());
        setCreatedBy(createdBy);
        setCreationDateDateTime(new DateTime());
        setEnrolmentCondition(enrolmentCondition);
        createAttend(studentCurricularPlan.getRegistration(), curricularCourse, executionSemester);
        super.setIsExtraCurricular(Boolean.FALSE);
    }

    protected void initializeAsNewWithoutEnrolmentEvaluation(final StudentCurricularPlan studentCurricularPlan,
            final CurricularCourse curricularCourse, final ExecutionSemester executionSemester,
            final EnrollmentCondition enrolmentCondition, final String createdBy) {
        initializeCommon(studentCurricularPlan, curricularCourse, executionSemester, enrolmentCondition, createdBy);
        setStudentCurricularPlan(studentCurricularPlan);
    }

    @Override
    public void delete() {
        checkRulesToDelete();
        createCurriculumLineLog(EnrolmentAction.UNENROL);
        deleteInformation();
        setEvaluationSeason(null);

        super.delete();
    }

    protected void deleteInformation() {

        final Registration registration = getRegistration();

        getStudentCurricularPlan().setIsFirstTimeToNull();
        setExecutionPeriod(null);
        setStudentCurricularPlan(null);
        setDegreeModule(null);
        setCurriculumGroup(null);
        getNotNeedToEnrollCurricularCoursesSet().clear();

        Iterator<Attends> attendsIter = getAttendsSet().iterator();
        while (attendsIter.hasNext()) {
            Attends attends = attendsIter.next();

            attendsIter.remove();
            attends.setEnrolment(null);

            if (attends.getAssociatedMarksSet().isEmpty() && attends.getStudentGroupsSet().isEmpty()) {
                boolean hasShiftEnrolment = false;
                for (Shift shift : attends.getExecutionCourse().getAssociatedShifts()) {
                    if (shift.getStudentsSet().contains(registration)) {
                        hasShiftEnrolment = true;
                        break;
                    }
                }

                if (!hasShiftEnrolment) {
                    attends.delete();
                }
            }
        }

        Iterator<EnrolmentEvaluation> evalsIter = getEvaluationsSet().iterator();
        while (evalsIter.hasNext()) {
            EnrolmentEvaluation eval = evalsIter.next();
            evalsIter.remove();
            eval.delete();
        }
    }

    protected void checkRulesToDelete() {
        if (!getExtraExamRequestsSet().isEmpty()) {
            throw new DomainException("error.Enrolment.has.ExtraExamRequests");
        }
        if (!getEnrolmentWrappersSet().isEmpty()) {
            throw new DomainException("error.Enrolment.is.origin.in.some.Equivalence");
        }
        if (!getCourseLoadRequestsSet().isEmpty()) {
            throw new DomainException("error.Enrolment.has.CourseLoadRequests");
        }
        if (!getProgramCertificateRequestsSet().isEmpty()) {
            throw new DomainException("error.Enrolment.has.ProgramCertificateRequests");
        }
    }

    final public Collection<Enrolment> getBrothers() {
        final Collection<Enrolment> result = new HashSet<>();

        result.addAll(getStudentCurricularPlan().getEnrolments(getCurricularCourse()));
        result.remove(this);

        return result;
    }

    final public Optional<EnrolmentEvaluation> getEnrolmentEvaluationBySeasonAndState(final EnrolmentEvaluationState state,
            final EvaluationSeason season) {

        final Supplier<Stream<EnrolmentEvaluation>> supplier =
                () -> getEnrolmentEvaluationBySeason(season).filter(e -> e.getEnrolmentEvaluationState().equals(state));

        // performance: avoid count
        if (logger.isDebugEnabled() && supplier.get().count() > 1) {

            // just to be precocious
            logger.debug("Multiple Evaluations for pair Season<->STATE! [REG {}] [SCP {}] [{}] [{}] [{}]",
                    getRegistration().getNumber(), getStudentCurricularPlan().getName(), print("").toString().replace("/n", ""),
                    season == null ? "" : season.getName().getContent(), state == null ? "" : state.toString());

        }

        return supplier.get().findAny();
    }

    final public Stream<EnrolmentEvaluation> getEnrolmentEvaluationBySeason(final EvaluationSeason season) {
        return getEvaluationsSet().stream().filter(e -> e.getEvaluationSeason().equals(season));
    }

    public boolean isEvaluatedInSeason(final EvaluationSeason season, final ExecutionSemester semester) {
        return getEnrolmentEvaluation(season, semester, Boolean.TRUE).isPresent();
    }

    public boolean isEnroledInSeason(final EvaluationSeason season, final ExecutionSemester semester) {
        return getTemporaryEvaluation(season, semester).isPresent();
    }

    final public Optional<EnrolmentEvaluation> getEnrolmentEvaluation(final EvaluationSeason season,
            final ExecutionSemester semester, final Boolean assertFinal) {

        final Supplier<Stream<EnrolmentEvaluation>> supplier = () -> getEnrolmentEvaluationBySeason(season).filter(evaluation -> {

            if (evaluation.isAnnuled()) {
                return false;
            }

            if (evaluation.getEvaluationSeason().isImprovement() && evaluation.getExecutionPeriod() != semester) {
                return false;
            }

            if (assertFinal != null) {

                if (assertFinal && !evaluation.isFinal()) {
                    return false;
                }

                // testing isFinal is insuficient, other states are final
                if (!assertFinal && !evaluation.isTemporary()) {
                    return false;
                }
            }

            return true;
        });

        // performance: avoid count
        if (logger.isDebugEnabled() && supplier.get().count() > 1) {

            // just to be precocious
            logger.debug("Multiple Evaluations for pair Season<->SEMESTER! [REG {}] [SCP {}] [{}] [{}] [{}]",
                    getRegistration().getNumber(), getStudentCurricularPlan().getName(), print("").toString().replace("/n", ""),
                    season == null ? "" : season.getName().getContent(), semester == null ? "" : semester.getQualifiedName());
        }

        return supplier.get().findAny();
    }

    protected void createEnrolmentEvaluationWithoutGrade() {
        boolean existing = getEnrolmentEvaluationBySeason(EvaluationConfiguration.getInstance().getDefaultEvaluationSeason())
                .filter(e -> e.getGrade().equals(null)).findAny().isPresent();
        if (!existing) {
            EnrolmentEvaluation evaluation = new EnrolmentEvaluation(this,
                    EvaluationConfiguration.getInstance().getDefaultEvaluationSeason(), EnrolmentEvaluationState.TEMPORARY_OBJ);
            evaluation.setWhenDateTime(new DateTime());
            addEvaluations(evaluation);
        }
    }

    private void createAttend(final Registration registration, final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester) {

        final List<ExecutionCourse> executionCourses = curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester);

        ExecutionCourse executionCourse = null;
        if (executionCourses.size() > 1) {
            final Iterator<ExecutionCourse> iterator = executionCourses.iterator();
            while (iterator.hasNext()) {
                final ExecutionCourse each = iterator.next();
                executionCourse = each;
            }
        } else if (executionCourses.size() == 1) {
            executionCourse = executionCourses.iterator().next();
        }

        if (executionCourse != null) {
            final Attends attend = executionCourse.getAttendsByStudent(registration.getStudent());
            if (attend == null) {
                addAttends(new Attends(registration, executionCourse));
            } else if (attend.getEnrolment() == null) {
                attend.setRegistration(registration);
                addAttends(attend);
            } else {
                throw new DomainException("error.cannot.create.multiple.enrolments.for.student.in.execution.course",
                        executionCourse.getNome(), executionCourse.getExecutionPeriod().getQualifiedName());
            }
        }
    }

    final public void createAttends(final Registration registration, final ExecutionCourse executionCourse) {
        final Attends attendsFor = this.getAttendsFor(executionCourse.getExecutionPeriod());
        if (attendsFor != null) {
            try {
                attendsFor.delete();
            } catch (DomainException e) {
                throw new DomainException("error.attends.cant.change.attends");
            }
        }

        this.addAttends(new Attends(registration, executionCourse));
    }

    final public EnrolmentEvaluation createTemporaryEvaluationForImprovement(final Person person,
            final EvaluationSeason evaluationSeason, final ExecutionSemester executionSemester) {

        getPredicateImprovement().fill(evaluationSeason, executionSemester, EnrolmentEvaluationContext.MARK_SHEET_EVALUATION)
                .test(this);

        final EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation(this, evaluationSeason,
                EnrolmentEvaluationState.TEMPORARY_OBJ, person, executionSemester);
        createAttendForImprovement(executionSemester);
        RegistrationDataByExecutionYear.getOrCreateRegistrationDataByYear(getRegistration(),
                executionSemester.getExecutionYear());

        Signal.emit(ITreasuryBridgeAPI.IMPROVEMENT_ENROLMENT, new DomainObjectEvent<>(enrolmentEvaluation));

        return enrolmentEvaluation;
    }

    private void createAttendForImprovement(final ExecutionSemester executionSemester) {
        final Registration registration = getRegistration();

        // FIXME this is completly wrong, there may be more than one execution
        // course for this curricular course
        ExecutionCourse currentExecutionCourse =
                (ExecutionCourse) CollectionUtils.find(getCurricularCourse().getAssociatedExecutionCoursesSet(), new Predicate() {

                    @Override
                    final public boolean evaluate(final Object arg0) {
                        ExecutionCourse executionCourse = (ExecutionCourse) arg0;
                        if (executionCourse.getExecutionPeriod() == executionSemester
                                && executionCourse.getEntryPhase() == EntryPhase.FIRST_PHASE) {
                            return true;
                        }
                        return false;
                    }

                });

        if (currentExecutionCourse != null) {
            Collection attends = currentExecutionCourse.getAttendsSet();
            Attends attend = (Attends) CollectionUtils.find(attends, new Predicate() {

                @Override
                public boolean evaluate(final Object arg0) {
                    Attends attend = (Attends) arg0;
                    if (attend.getRegistration().equals(registration)) {
                        return true;
                    }
                    return false;
                }

            });

            if (attend == null) {
                attend = getStudent().getAttends(currentExecutionCourse);

                if (attend != null) {
                    attend.setRegistration(registration);
                } else {
                    attend = new Attends(registration, currentExecutionCourse);
                }
            }
            attend.setEnrolment(this);
        }
    }

    final public void deleteTemporaryEvaluationForImprovement(final EvaluationSeason season,
            final ExecutionSemester improvementSemester) throws DomainException {

        final EnrolmentEvaluation temporaryImprovement = getTemporaryEvaluation(season, improvementSemester).orElse(null);
        if (temporaryImprovement == null || !temporaryImprovement.isTemporary()) {
            throw new DomainException("error.enrolment.cant.unenroll");
        }

        TreasuryBridgeAPIFactory.implementation().improvementUnrenrolment(temporaryImprovement);

        temporaryImprovement.delete();

        // improvement may be on the same semester, in which case we don't want to remove the Attends
        if (getExecutionPeriod() != improvementSemester) {
            final Attends attends = getAttendsFor(improvementSemester);
            if (attends != null) {
                attends.delete();
            }
        }
    }

    final public boolean isImprovementForExecutionCourse(final ExecutionCourse executionCourse) {
        return getExecutionPeriod() != executionCourse.getExecutionPeriod()
                && getCurricularCourse().getAssociatedExecutionCoursesSet().contains(executionCourse);
    }

    public static EnrolmentPredicate getPredicateSeason() {
        return PREDICATE_SEASON.get();
    }

    public static void setPredicateSeason(final Supplier<EnrolmentPredicate> input) {
        if (input != null && input.get() != null) {
            PREDICATE_SEASON = input;
        } else {
            logger.error("Could not set PREDICATE_SEASON to null");
        }
    }

    static private Supplier<EnrolmentPredicate> PREDICATE_SEASON = () -> new EnrolmentPredicate() {

        @Override
        public boolean test(final Enrolment enrolment) {

            if (enrolment.isEvaluatedInSeason(getEvaluationSeason(), getExecutionSemester())) {
                throw new DomainException("error.EvaluationSeason.enrolment.evaluated.in.this.season",
                        enrolment.getName().getContent(), getEvaluationSeason().getName().getContent());
            }

            if (getContext() == EnrolmentEvaluationContext.MARK_SHEET_EVALUATION) {
                if (enrolment.isEnroledInSeason(getEvaluationSeason(), getExecutionSemester())) {
                    throw new DomainException("error.EvaluationSeason.already.enroled.in.this.season",
                            enrolment.getName().getContent(), getEvaluationSeason().getName().getContent());
                }

                if (enrolment.isApproved() && !getEvaluationSeason().isImprovement()) {
                    throw new DomainException("error.EvaluationSeason.evaluation.already.approved",
                            enrolment.getName().getContent(), getEvaluationSeason().getName().getContent());
                }
            }

            return true;
        }

    };

    public static EnrolmentPredicate getPredicateImprovement() {
        return PREDICATE_IMPROVEMENT.get();
    }

    public static void setPredicateImprovement(final Supplier<EnrolmentPredicate> input) {
        if (input != null && input.get() != null) {
            PREDICATE_IMPROVEMENT = input;
        } else {
            logger.error("Could not set PREDICATE_IMPROVEMENT to null");
        }
    }

    static private Supplier<EnrolmentPredicate> PREDICATE_IMPROVEMENT = () -> new EnrolmentPredicate() {

        @Override
        public boolean test(final Enrolment enrolment) {
            final ExecutionSemester improvementSemester = getExecutionSemester();

            final ExecutionSemester enrolmentSemester = enrolment.getExecutionPeriod();
            if (improvementSemester.isBeforeOrEquals(enrolmentSemester)) {
                throw new DomainException("error.EnrolmentEvaluation.improvement.semester.must.be.after.or.equal.enrolment",
                        enrolment.getName().getContent());
            }

            if (!enrolment.isApproved()) {
                throw new DomainException(
                        "curricularRules.ruleExecutors.ImprovementOfApprovedEnrolmentExecutor.degree.module.hasnt.been.approved",
                        enrolment.getName().getContent());
            }

            getPredicateSeason().fill(getEvaluationSeason(), improvementSemester, getContext()).test(enrolment);

            final DegreeModule degreeModule = enrolment.getDegreeModule();
            if (!degreeModule.hasAnyParentContexts(improvementSemester)) {
                throw new DomainException(
                        "curricularRules.ruleExecutors.ImprovementOfApprovedEnrolmentExecutor.degree.module.has.no.context.in.present.execution.period",
                        enrolment.getName().getContent(), improvementSemester.getQualifiedName());
            }

            // ImprovingInExecutionPeriodFollowingApproval
            final ExecutionSemester nextSemester = enrolmentSemester.getNextExecutionPeriod();
            if (improvementSemester == nextSemester) {
                return true;
            }

            // ImprovingInExecutionPeriodFollowingApproval
            if (!improvementSemester.isOneYearAfter(enrolmentSemester)) {

                for (ExecutionSemester iter = nextSemester; iter != null && iter != improvementSemester; iter =
                        iter.getNextExecutionPeriod()) {
                    if (degreeModule.hasAnyParentContexts(iter)) {
                        throw new DomainException(
                                "curricularRules.ruleExecutors.ImprovementOfApprovedEnrolmentExecutor.is.not.improving.in.execution.period.following.approval");
                    }
                }
            }

            return true;
        }

    };

    static abstract public class EnrolmentPredicate implements java.util.function.Predicate<Enrolment> {

        private EvaluationSeason evaluationSeason;
        private ExecutionSemester executionSemester;
        private EnrolmentEvaluationContext context;

        public EvaluationSeason getEvaluationSeason() {
            return evaluationSeason;
        }

        public ExecutionSemester getExecutionSemester() {
            return executionSemester;
        }

        public EnrolmentEvaluationContext getContext() {
            return context;
        }

        public EnrolmentPredicate fill(final EvaluationSeason season, final ExecutionSemester semester,
                final EnrolmentEvaluationContext context) {
            this.evaluationSeason = season;
            this.executionSemester = semester;
            this.context = context;
            return this;
        }

        public boolean testExceptionless(final Enrolment input) {

            try {
                return test(input);

            } catch (final Throwable t) {
                return false;
            }
        }
    }

    public static EnrolmentPredicate getPredicateSpecialSeason() {
        return PREDICATE_SPECIAL_SEASON.get();
    }

    public static void setPredicateSpecialSeason(final Supplier<EnrolmentPredicate> input) {
        if (input != null && input.get() != null) {
            PREDICATE_SPECIAL_SEASON = input;
        } else {
            logger.error("Could not set PREDICATE_SPECIAL_SEASON to null");
        }
    }

    static private Supplier<EnrolmentPredicate> PREDICATE_SPECIAL_SEASON = () -> new EnrolmentPredicate() {

        @Override
        public boolean test(final Enrolment enrolment) {
            final ExecutionSemester specialSeasonSemester = getExecutionSemester();

            final ExecutionSemester enrolmentSemester = enrolment.getExecutionPeriod();
            if (specialSeasonSemester != enrolmentSemester) {
                throw new DomainException("error.EnrolmentEvaluation.special.season.semester.must.be",
                        enrolment.getName().getContent());
            }

            if (enrolment.isApproved()) {
                throw new DomainException(
                        "curricularRules.ruleExecutors.EnrolmentInSpecialSeasonEvaluationExecutor.degree.module.has.been.approved",
                        enrolment.getName().getContent());
            }

            getPredicateSeason().fill(getEvaluationSeason(), getExecutionSemester(), getContext()).test(enrolment);

            if (enrolment.hasSpecialSeasonInExecutionYear(enrolmentSemester.getExecutionYear())) {
                throw new DomainException(
                        "curricularRules.ruleExecutors.EnrolmentInSpecialSeasonEvaluationExecutor.already.enroled.in.special.season",
                        enrolment.getName().getContent(), enrolment.getExecutionYear().getYear());
            }

            // checkPermissions
            final StudentCurricularPlan scp = enrolment.getStudentCurricularPlan();
            final Registration registration = scp.getRegistration();
            if (!isSpecialSeasonGrantedByStatute(registration)
                    && !registration.getStudent().isSenior(getExecutionSemester().getExecutionYear())) {
                throw new DomainException("error.special.season.not.granted");
            }

            final boolean isServices =
                    AcademicAuthorizationGroup.get(AcademicOperationType.STUDENT_ENROLMENTS).isMember(Authenticate.getUser());
            return considerThisEnrolmentNormalEnrolments(enrolment)
                    || considerThisEnrolmentPropaedeuticEnrolments(enrolment, isServices)
                    || considerThisEnrolmentExtraCurricularEnrolments(enrolment, isServices)
                    || considerThisEnrolmentStandaloneEnrolments(enrolment, isServices);
        }

        private boolean isSpecialSeasonGrantedByStatute(final Registration registration) {
            final Student student = registration.getStudent();

            for (StudentStatute statute : student.getStudentStatutesSet()) {
                if (!statute.getType().isSpecialSeasonGranted() && !statute.hasSeniorStatuteForRegistration(registration)) {
                    continue;
                }
                if (!statute.isValidInExecutionPeriod(getExecutionSemester())) {
                    continue;
                }

                return true;
            }

            return false;
        }

        private boolean considerThisEnrolmentNormalEnrolments(final Enrolment enrolment) {
            if (enrolment.isBolonhaDegree() && !enrolment.isExtraCurricular() && !enrolment.isPropaedeutic()
                    && !enrolment.isStandalone()) {
                if (enrolment.getParentCycleCurriculumGroup().isConclusionProcessed()) {
                    return false;
                }
            }
            return !enrolment.parentCurriculumGroupIsNoCourseGroupCurriculumGroup() || enrolment.isPropaedeutic();
        }

        private boolean considerThisEnrolmentPropaedeuticEnrolments(final Enrolment enrolment, final boolean isServices) {
            return enrolment.isPropaedeutic() && isServices;
        }

        private boolean considerThisEnrolmentExtraCurricularEnrolments(final Enrolment enrolment, final boolean isServices) {
            return enrolment.isExtraCurricular() && isServices;
        }

        private boolean considerThisEnrolmentStandaloneEnrolments(final Enrolment enrolment, final boolean isServices) {
            return enrolment.isStandalone() && isServices;
        }

    };

    final public EnrolmentEvaluation createTemporaryEvaluationForSpecialSeason(final Person person,
            final EvaluationSeason evaluationSeason) {

        final ExecutionSemester executionSemester = getExecutionPeriod();

        getPredicateSpecialSeason().fill(evaluationSeason, executionSemester, EnrolmentEvaluationContext.MARK_SHEET_EVALUATION)
                .test(this);

        if (FenixEduAcademicConfiguration.getConfiguration().getEnrolmentsInSpecialSeasonEvaluationsInduceEnrolmentVariables()) {
            setEvaluationSeason(evaluationSeason);
            setEnrollmentState(EnrollmentState.ENROLLED);
        }

        final EnrolmentEvaluation enrolmentEvaluation =
                new EnrolmentEvaluation(this, evaluationSeason, EnrolmentEvaluationState.TEMPORARY_OBJ, person);

        return enrolmentEvaluation;
    }

    final public void deleteTemporaryEvaluationForSpecialSeason(final EvaluationSeason season) throws DomainException {

        final EnrolmentEvaluation temporarySpecialSeason = getTemporaryEvaluation(season, getExecutionPeriod()).orElse(null);
        if (temporarySpecialSeason == null || !temporarySpecialSeason.isTemporary()) {
            throw new DomainException("error.enrolment.cant.unenroll");
        }

        temporarySpecialSeason.delete();

        if (FenixEduAcademicConfiguration.getConfiguration().getEnrolmentsInSpecialSeasonEvaluationsInduceEnrolmentVariables()) {
            setEvaluationSeason(EvaluationConfiguration.getInstance().getDefaultEvaluationSeason());
            setEnrolmentCondition(EnrollmentCondition.FINAL);
            getEnrolmentEvaluationBySeasonAndState(EnrolmentEvaluationState.FINAL_OBJ, season)
                    .ifPresent(e -> setEnrollmentState(e.getEnrollmentStateByGrade()));
        }
    }

    final public List<EnrolmentEvaluation> getAllFinalEnrolmentEvaluations() {
        final List<EnrolmentEvaluation> result = new ArrayList<>();

        for (final EnrolmentEvaluation enrolmentEvaluation : getEvaluationsSet()) {
            if (enrolmentEvaluation.isFinal()) {
                result.add(enrolmentEvaluation);
            }
        }

        return result;
    }

    /**
     * @deprecated Please use Enrolment.hasImprovementFor(ExecutionSemester) where possible
     */
    @Deprecated
    final public boolean hasImprovement() {
        return getEvaluationsSet().stream().filter(i -> i.getEvaluationSeason().isImprovement()).findAny().isPresent();
    }

    final public boolean hasImprovementFor(final ExecutionSemester executionSemester) {
        for (EnrolmentEvaluation enrolmentEvaluation : this.getEvaluationsSet()) {
            if (enrolmentEvaluation.getEvaluationSeason().isImprovement()) {
                final ExecutionSemester evalPeriod = enrolmentEvaluation.getExecutionPeriod();
                if (evalPeriod != null && evalPeriod == executionSemester) {
                    return true;
                }
            }
        }
        return false;
    }

    final public boolean hasSpecialSeason() {
        for (final EnrolmentEvaluation evaluation : getEvaluationsSet()) {
            final EvaluationSeason season = evaluation.getEvaluationSeason();

            if (season.isSpecial() && isEnroledInSeason(season, getExecutionPeriod())) {
                return true;
            }
        }

        return false;
    }

    final public boolean hasSpecialSeasonInExecutionYear(final ExecutionYear executionYear) {
        for (final Enrolment enrolment : getBrothers()) {
            if (enrolment.getExecutionYear() == executionYear && enrolment.hasSpecialSeason()) {
                return true;
            }
        }

        return hasSpecialSeason();
    }

    final public boolean isNotEvaluated() {
        final EnrolmentEvaluation latestEnrolmentEvaluation = getFinalEnrolmentEvaluation();
        return latestEnrolmentEvaluation == null || latestEnrolmentEvaluation.isNotEvaluated();
    }

    final public boolean isFlunked() {
        final EnrolmentEvaluation latestEnrolmentEvaluation = getFinalEnrolmentEvaluation();
        return latestEnrolmentEvaluation != null && latestEnrolmentEvaluation.isFlunked();
    }

    @Override
    final public boolean isApproved() {
        if (isAnnulled()) {
            return false;
        }

        final EnrolmentEvaluation latestEnrolmentEvaluation = getFinalEnrolmentEvaluation();
        return latestEnrolmentEvaluation != null && latestEnrolmentEvaluation.isApproved();
    }

    final public boolean isAproved(final ExecutionYear executionYear) {
        return (executionYear == null || getExecutionYear().isBeforeOrEquals(executionYear)) && isApproved();
    }

    @Override
    public boolean isApproved(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
        if (executionSemester == null || getExecutionPeriod().isBeforeOrEquals(executionSemester)) {
            return isApproved() && hasCurricularCourse(getCurricularCourse(), curricularCourse, executionSemester);
        } else {
            return false;
        }
    }

    @Override
    public final ConclusionValue isConcluded(final ExecutionYear executionYear) {
        return ConclusionValue.create(isAproved(executionYear));
    }

    @Override
    public YearMonthDay calculateConclusionDate() {
        if (!isApproved()) {
            throw new DomainException("error.Enrolment.not.approved");
        }
        return EvaluationConfiguration.getInstance().getEnrolmentEvaluationForConclusionDate(this)
                .map(EnrolmentEvaluation::getExamDateYearMonthDay).orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    final public Curriculum getCurriculum(final DateTime when, final ExecutionYear year) {
        if (wasCreated(when) && (year == null || getExecutionYear().isBefore(year)) && isApproved() && !isPropaedeutic()
                && !isExtraCurricular()) {
            return new Curriculum(this, year, Collections.singleton((ICurriculumEntry) this), Collections.EMPTY_SET,
                    Collections.singleton((ICurriculumEntry) this));
        }

        return Curriculum.createEmpty(this, year);
    }

    @Override
    final public Grade getGrade() {
        final EnrolmentEvaluation enrolmentEvaluation = getFinalEnrolmentEvaluation();
        return enrolmentEvaluation == null ? Grade.createEmptyGrade() : enrolmentEvaluation.getGrade();
    }

    private EctsConversionTable getEctsConversionTable() {
        final EnrolmentEvaluation enrolmentEvaluation = getFinalEnrolmentEvaluation();
        return enrolmentEvaluation == null ? null : enrolmentEvaluation.getEctsConversionTable();
    }

    private Grade getNormalizedEctsGrade() {
        final EnrolmentEvaluation enrolmentEvaluation = getFinalEnrolmentEvaluation();
        return enrolmentEvaluation == null ? null : enrolmentEvaluation.getNormalizedEctsGrade();
    }

    @Override
    final public String getGradeValue() {
        return getGrade().getValue();
    }

    @Override
    public Grade getEctsGrade(final StudentCurricularPlan scp, final DateTime processingDate) {
        final Grade normalizedEctsGrade = getNormalizedEctsGrade();
        return normalizedEctsGrade == null ? calculateNormalizedEctsGrade(scp, processingDate) : normalizedEctsGrade;
    }

    @Override
    public EctsConversionTable getEctsConversionTable(final StudentCurricularPlan scp, final DateTime processingDate) {
        final EctsConversionTable table = getEctsConversionTable();
        return table == null ? calculateEctsConversionTable(scp, processingDate, getGrade()) : table;
    }

    private Grade calculateNormalizedEctsGrade(final StudentCurricularPlan scp, final DateTime processingDate) {
        final Grade grade = getGrade();
        final EctsConversionTable table = getEctsConversionTable();
        final EctsConversionTable tableForCalculation =
                table == null ? calculateEctsConversionTable(scp, processingDate, grade) : table;
        return tableForCalculation.convert(grade);
    }

    private EctsConversionTable calculateEctsConversionTable(final StudentCurricularPlan scp, final DateTime processingDate,
            final Grade grade) {
        final Set<InternalEnrolmentWrapper> wrappers = getEnrolmentWrappersSet();
        if (wrappers.size() > 0) {
            final Set<Dismissal> dismissals = new HashSet<Dismissal>();
            for (final EnrolmentWrapper wrapper : wrappers) {
                if (wrapper.getCredits().getStudentCurricularPlan().isBolonhaDegree()) {
                    if (!wrapper.getCredits().getStudentCurricularPlan().equals(scp)) {
                        continue;
                    }
                }
                for (final Dismissal dismissal : wrapper.getCredits().getDismissalsSet()) {
                    dismissals.add(dismissal);
                }
            }
            if (dismissals.size() == 1) {
                Dismissal dismissal = dismissals.iterator().next();
                if (dismissal instanceof OptionalDismissal || dismissal instanceof CreditsDismissal
                        || dismissal.getCurricularCourse().isOptionalCurricularCourse()) {
                    return EctsTableIndex.getEctsConversionTable(scp.getDegree(), dismissal, grade, processingDate);
                } else {
                    CurricularCourse curricularCourse = dismissal.getCurricularCourse();
                    return EctsTableIndex.getEctsConversionTable(curricularCourse, dismissal, grade, processingDate);
                }
            } else if (dismissals.size() > 1) {
                // if more than one exists we can't base the conversion on the
                // origin, so step up to the degree, on a context based on one
                // of the sources.
                for (Dismissal dismissal : dismissals) {
                    if (dismissal.getParentCycleCurriculumGroup() != null) {
                        return EctsTableIndex.getEctsConversionTable(scp.getDegree(), dismissal, grade, processingDate);
                    }
                }
            }
        }
        return EctsTableIndex.getEctsConversionTable(getCurricularCourse(), this, grade, processingDate);
    }

    @Override
    final public Integer getFinalGrade() {
        return getGrade().getIntegerValue();
    }

    public GradeScale getGradeScaleChain() {
        return getCurricularCourse().getGradeScaleChain();
    }

    public GradeScale getGradeScale() {
        return this.getGradeScaleChain();
    }

    @Override
    final public boolean isEnroled() {
        return this.getEnrollmentState() == EnrollmentState.ENROLLED;
    }

    @Deprecated
    final public boolean isEnrolmentStateApproved() {
        return this.getEnrollmentState() == EnrollmentState.APROVED;
    }

    @Deprecated
    final public boolean isEnrolmentStateNotApproved() {
        return this.getEnrollmentState() == EnrollmentState.NOT_APROVED;
    }

    @Deprecated
    final public boolean isEnrolmentStateNotEvaluated() {
        return this.getEnrollmentState() == EnrollmentState.NOT_EVALUATED;
    }

    final public boolean isAnnulled() {
        return this.getEnrollmentState() == EnrollmentState.ANNULED;
    }

    final public boolean isTemporarilyEnroled() {
        return this.getEnrollmentState() == EnrollmentState.TEMPORARILY_ENROLLED;
    }

    final public boolean isEvaluated() {
        return isEnrolmentStateApproved() || isEnrolmentStateNotApproved();
    }

    final public boolean isActive() {
        return !isAnnulled() && !isTemporarilyEnroled();
    }

    final public Boolean isFirstTime() {
        if (getIsFirstTime() == null) {
            resetIsFirstTimeEnrolment();
        }
        return getIsFirstTime();
    }

    final public void resetIsFirstTimeEnrolment() {
        if (getStudentCurricularPlan() != null && getCurricularCourse() != null && getExecutionPeriod() != null
                && getEnrollmentState() != null) {
            getStudentCurricularPlan().resetIsFirstTimeEnrolmentForCurricularCourse(getCurricularCourse());
        } else {
            setIsFirstTime(Boolean.FALSE);
        }
    }

    @Override
    final public void setDegreeModule(final DegreeModule degreeModule) {
        super.setDegreeModule(degreeModule);
        resetIsFirstTimeEnrolment();
    }

    @Override
    final public void setEnrollmentState(final EnrollmentState enrollmentState) {
        super.setEnrollmentState(enrollmentState);
        resetIsFirstTimeEnrolment();
    }

    @Override
    final public void setExecutionPeriod(final ExecutionSemester executionSemester) {
        super.setExecutionPeriod(executionSemester);
        resetIsFirstTimeEnrolment();
    }

    @Override
    final public void setStudentCurricularPlan(final StudentCurricularPlan studentCurricularPlan) {
        super.setStudentCurricularPlan(studentCurricularPlan);
        resetIsFirstTimeEnrolment();
    }

    final public int getNumberOfTotalEnrolmentsInThisCourse() {
        return this.getStudentCurricularPlan().countEnrolmentsByCurricularCourse(this.getCurricularCourse());
    }

    final public int getNumberOfTotalEnrolmentsInThisCourse(final ExecutionSemester untilExecutionPeriod) {
        return this.getStudentCurricularPlan().countEnrolmentsByCurricularCourse(this.getCurricularCourse(),
                untilExecutionPeriod);
    }

    @Override
    protected void createCurriculumLineLog(final EnrolmentAction action) {
        new EnrolmentLog(action, getRegistration(), getCurricularCourse(), getExecutionPeriod(), getCurrentUser());
    }

    @Override
    public StringBuilder print(final String tabs) {
        final StringBuilder builder = new StringBuilder();
        builder.append(tabs);
        builder.append("[E ").append(getDegreeModule().getName()).append(" ").append(isApproved()).append(" ]\n");
        return builder;
    }

    final public EnrolmentEvaluation addNewEnrolmentEvaluation(final EnrolmentEvaluationState enrolmentEvaluationState,
            final EvaluationSeason evaluationSeason, final Person responsibleFor, final String gradeValue,
            final Date availableDate, final Date examDate, final ExecutionSemester executionSemester,
            final GradeScale gradeScale) {

        final Grade grade = Grade.createGrade(gradeValue, gradeScale != null ? gradeScale : getGradeScale());

        final EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation(this, enrolmentEvaluationState, evaluationSeason,
                responsibleFor, grade, availableDate, examDate, new DateTime());
        if (evaluationSeason.isImprovement()) {
            enrolmentEvaluation.setExecutionPeriod(executionSemester);
        }
        return enrolmentEvaluation;
    }

    final public EnrolmentEvaluation addNewEnrolmentEvaluation(final EnrolmentEvaluationState enrolmentEvaluationState,
            final EvaluationSeason evaluationSeason, final Person responsibleFor, final String gradeValue,
            final Date availableDate, final Date examDate, final ExecutionSemester executionSemester, final String bookReference,
            final String page, final GradeScale gradeScale) {

        EnrolmentEvaluation enrolmentEvaluation = addNewEnrolmentEvaluation(enrolmentEvaluationState, evaluationSeason,
                responsibleFor, gradeValue, availableDate, examDate, executionSemester, gradeScale);
        enrolmentEvaluation.setBookReference(bookReference);
        enrolmentEvaluation.setPage(page);
        enrolmentEvaluation.setContext(EnrolmentEvaluationContext.CURRICULUM_VALIDATION_EVALUATION);
        enrolmentEvaluation.setGradeScale(gradeScale);

        EnrolmentEvaluationLog.logEnrolmentEvaluationCreation(enrolmentEvaluation);

        return enrolmentEvaluation;
    }

    final public Attends getAttendsByExecutionCourse(final ExecutionCourse executionCourse) {
        for (final Attends attends : this.getAttendsSet()) {
            if (attends.isFor(executionCourse)) {
                return attends;
            }
        }
        return null;
    }

    final public boolean hasAttendsFor(final ExecutionSemester executionSemester) {
        for (final Attends attends : this.getAttendsSet()) {
            if (attends.isFor(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    final public Attends getAttendsFor(final ExecutionSemester executionSemester) {
        Attends result = null;

        for (final Attends attends : getAttendsSet()) {
            if (attends.isFor(executionSemester)) {
                if (result == null) {
                    result = attends;
                } else {
                    throw new DomainException("Enrolment.found.two.attends.for.same.execution.period");
                }
            }
        }

        return result;
    }

    final public ExecutionCourse getExecutionCourseFor(final ExecutionSemester executionSemester) {
        for (final Attends attend : getAttendsSet()) {
            if (attend.getExecutionCourse().getExecutionPeriod() == executionSemester) {
                return attend.getExecutionCourse();
            }
        }

        return null;
    }

    public EnrolmentEvaluation getLatestEnrolmentEvaluationBySeason(final EvaluationSeason season) {
        return EvaluationConfiguration.getInstance().getCurrentEnrolmentEvaluation(this, season).orElse(null);
    }

    final public EnrolmentEvaluation getFinalEnrolmentEvaluation() {
        return EvaluationConfiguration.getInstance().getFinalEnrolmentEvaluation(this).orElse(null);
    }

    public Optional<EnrolmentEvaluation> getFinalEnrolmentEvaluationBySeason(final EvaluationSeason season) {
        return EvaluationConfiguration.getInstance().getFinalEnrolmentEvaluation(this, season);
    }

    private Optional<EnrolmentEvaluation> getTemporaryEvaluation(final EvaluationSeason season,
            final ExecutionSemester semester) {
        return getEnrolmentEvaluation(season, semester, Boolean.FALSE);
    }

    @Override
    final public List<Enrolment> getEnrolments() {
        return Collections.singletonList(this);
    }

    @Override
    final public boolean hasAnyEnrolments() {
        return true;
    }

    @Override
    final public StudentCurricularPlan getStudentCurricularPlan() {
        return getCurriculumGroup() != null ? getCurriculumGroup().getStudentCurricularPlan() : super.getStudentCurricularPlan();
    }

    @Override
    public boolean isEnroledInExecutionPeriod(final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester) {
        return isValid(executionSemester) && this.getCurricularCourse().equals(curricularCourse);
    }

    @Override
    public boolean isValid(final ExecutionSemester executionSemester) {
        return getExecutionPeriod() == executionSemester || getCurricularCourse().isAnual()
                && getExecutionPeriod().getExecutionYear() == executionSemester.getExecutionYear();
    }

    public boolean isValid(final ExecutionYear executionYear) {
        for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
            if (isValid(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    @Override
    final public boolean hasEnrolmentWithEnroledState(final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester) {
        return isEnroled() && isEnroledInExecutionPeriod(curricularCourse, executionSemester);
    }

    final public Collection<ExecutionCourse> getExecutionCourses() {
        return this.getCurricularCourse().getAssociatedExecutionCoursesSet();
    }

    final public boolean isEnrolmentTypeNormal() {
        return getCurricularCourse().getType() == CurricularCourseType.NORMAL_COURSE && !isExtraCurricular() && !isOptional();
    }

    @Override
    final public String getEnrolmentTypeName() {
        if (isExtraCurricular()) {
            return "EXTRA_CURRICULAR_ENROLMENT";
        } else if (isOptional()) {
            return "ENROLMENT_IN_OPTIONAL_DEGREE_MODULE";
        } else {
            return "COMPULSORY_ENROLMENT";
        }
    }

    static public int countEvaluated(final List<Enrolment> enrolments) {
        int result = 0;

        for (final Enrolment enrolment : enrolments) {
            if (enrolment.isEvaluated()) {
                result++;
            }
        }

        return result;
    }

    static public int countApproved(final List<Enrolment> enrolments) {
        int result = 0;

        for (final Enrolment enrolment : enrolments) {
            if (enrolment.isEnrolmentStateApproved()) {
                result++;
            }
        }

        return result;
    }

    @Override
    public Set<CurriculumLine> getCurriculumLinesForCurriculum() {
        return getRegistration().getLastStudentCurricularPlan().getCreditsSet().stream()
                .filter(c -> c.getEnrolmentsSet().stream().anyMatch(ew -> ew.getIEnrolment() == this))
                .flatMap(c -> c.getDismissalsSet().stream()).collect(Collectors.toSet());
    }

    @Override
    final public Double getWeigth() {

        final Function<ICurriculumEntry, BigDecimal> provider = EctsAndWeightProviderRegistry.getWeightProvider(Enrolment.class);
        if (provider != null) {
            BigDecimal providedValue = provider.apply(this);
            return providedValue != null ? providedValue.doubleValue() : null;
        }

        return isExtraCurricular() || isPropaedeutic() ? Double.valueOf(0) : getWeigthForCurriculum().doubleValue();
    }

    @Override
    final public BigDecimal getWeigthForCurriculum() {

        final Function<ICurriculumEntry, BigDecimal> provider =
                EctsAndWeightProviderRegistry.getWeightForCurriculumProvider(Enrolment.class);
        if (provider != null) {
            return provider.apply(this);
        }

        final Double d;
        if (super.getWeigth() == null || super.getWeigth() == 0d) {
            final CurricularCourse curricularCourse = getCurricularCourse();
            d = curricularCourse == null ? null : curricularCourse.getWeigth();
        } else {
            d = super.getWeigth();
        }
        return d == null ? BigDecimal.ZERO : BigDecimal.valueOf(d);
    }

    /**
     * Just for Master Degrees legacy code
     *
     * @return
     */
    @Deprecated
    final public double getCredits() {
        return getEctsCredits();
    }

    @Override
    final public Double getEctsCredits() {

        final Function<ICurriculumEntry, BigDecimal> provider = EctsAndWeightProviderRegistry.getEctsProvider(Enrolment.class);
        if (provider != null) {
            final BigDecimal providedValue = provider.apply(this);
            return providedValue != null ? providedValue.doubleValue() : null;
        }

        return getEctsCreditsForCurriculum().doubleValue();
    }

    @Override
    final public BigDecimal getEctsCreditsForCurriculum() {

        final Function<ICurriculumEntry, BigDecimal> provider =
                EctsAndWeightProviderRegistry.getEctsForCurriculumProvider(Enrolment.class);
        if (provider != null) {
            return provider.apply(this);
        }

        return BigDecimal.valueOf(getCurricularCourse().getEctsCredits(getExecutionPeriod()));
    }

    @Override
    final public Double getAprovedEctsCredits() {
        return isApproved() ? getEctsCredits() : Double.valueOf(0d);
    }

    @Override
    final public Double getCreditsConcluded(final ExecutionYear executionYear) {
        return executionYear == null || getExecutionYear().isBeforeOrEquals(executionYear) ? getAprovedEctsCredits() : 0d;
    }

    @Override
    final public Double getEnroledEctsCredits(final ExecutionSemester executionSemester) {
        return isValid(executionSemester) && isEnroled() ? getEctsCredits() : Double.valueOf(0d);
    }

    @Override
    final public Double getEnroledEctsCredits(final ExecutionYear executionYear) {
        return isValid(executionYear) && isEnroled() ? getEctsCredits() : Double.valueOf(0d);
    }

    @Override
    final public Enrolment findEnrolmentFor(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
        return isEnroledInExecutionPeriod(curricularCourse, executionSemester) ? this : null;
    }

    @Override
    final public Enrolment getApprovedEnrolment(final CurricularCourse curricularCourse) {
        return isApproved(curricularCourse) ? this : null;
    }

    @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(final ExecutionSemester executionSemester) {
        if (isValid(executionSemester) && isEnroled()) {
            if (isFromExternalDegree()) {
                return Collections
                        .<IDegreeModuleToEvaluate> singleton(new ExternalDegreeEnrolmentWrapper(this, executionSemester));
            } else {
                return Collections.<IDegreeModuleToEvaluate> singleton(new EnroledEnrolmentWrapper(this, executionSemester));
            }
        }
        return Collections.emptySet();
    }

    private boolean isFromExternalDegree() {
        return getDegreeModule().getParentDegreeCurricularPlan() != getDegreeCurricularPlanOfDegreeModule();
    }

    @Override
    final public double getAccumulatedEctsCredits(final ExecutionSemester executionSemester) {
        return isBolonhaDegree() && !parentAllowAccumulatedEctsCredits() ? 0d : getStudentCurricularPlan()
                .getAccumulatedEctsCredits(executionSemester, getCurricularCourse());
    }

    final public boolean isSpecialSeasonEnroled(final ExecutionYear executionYear) {
        return isSpecialSeason() && getExecutionPeriod().getExecutionYear() == executionYear
                && getTempSpecialSeasonEvaluation() != null;
    }

    final public boolean isSpecialSeasonEnroled(final ExecutionSemester executionSemester) {
        return isSpecialSeason() && isValid(executionSemester) && getTempSpecialSeasonEvaluation() != null;
    }

    private EnrolmentEvaluation getTempSpecialSeasonEvaluation() {
        return getEnrolmentEvaluationBySeasonAndState(EnrolmentEvaluationState.TEMPORARY_OBJ,
                EvaluationSeason.readSpecialSeason()).orElse(null);
    }

    final public boolean canBeSpecialSeasonEnroled(final ExecutionYear executionYear) {
        return !getEvaluationSeason().isSpecial() && getExecutionPeriod().getExecutionYear() == executionYear && !isApproved();
    }

    final public boolean canBeSpecialSeasonEnroled(final ExecutionSemester executionSemester) {
        return !getEvaluationSeason().isSpecial() && getExecutionPeriod() == executionSemester && !isApproved();
    }

    final public Collection<Enrolment> getSpecialSeasonEnrolments(final ExecutionYear executionYear) {
        return getExecutionPeriod().getExecutionYear() == executionYear && isSpecialSeason() ? Collections
                .singleton(this) : Collections.emptySet();
    }

    final public Collection<Enrolment> getSpecialSeasonEnrolments(final ExecutionSemester executionSemester) {
        if (isSpecialSeason() && isValid(executionSemester)) {
            return Collections.singleton(this);
        }
        return Collections.emptySet();
    }

    @Override
    final public String getDescription() {
        return getStudentCurricularPlan().getDegree().getPresentationName(getExecutionYear()) + " > " + getName().getContent();
    }

    final public boolean isBefore(final Enrolment enrolment) {
        return getExecutionPeriod().isBefore(enrolment.getExecutionPeriod());
    }

    @Override
    final public Unit getAcademicUnit() {
        return Bennu.getInstance().getInstitutionUnit();
    }

    @Override
    final public String getCode() {
        if (getDegreeModule() != null) {
            return getDegreeModule().getCode();
        }
        return null;
    }

    @Override
    public boolean hasEnrolment(final ExecutionSemester executionSemester) {
        return isValid(executionSemester);
    }

    @Override
    public boolean hasEnrolment(final ExecutionYear executionYear) {
        return isValid(executionYear);
    }

    @Override
    public boolean isEnroledInSpecialSeason(final ExecutionSemester executionSemester) {
        return isValid(executionSemester) && hasSpecialSeason();
    }

    @Override
    public boolean isEnroledInSpecialSeason(final ExecutionYear executionYear) {
        return isValid(executionYear) && hasSpecialSeason();
    }

    @Override
    public int getNumberOfAllApprovedEnrolments(final ExecutionSemester executionSemester) {
        return isValid(executionSemester) && isApproved() ? 1 : 0;
    }

    public boolean isSourceOfAnyCreditsInCurriculum() {
        for (final InternalEnrolmentWrapper enrolmentWrapper : getEnrolmentWrappersSet()) {
            if (enrolmentWrapper.getCredits().hasAnyDismissalInCurriculum()) {
                return true;
            }
        }
        return false;
    }

    static public Enrolment getEnrolmentWithLastExecutionPeriod(final List<Enrolment> enrolments) {
        Collections.sort(enrolments, Enrolment.REVERSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_ID);
        return enrolments.iterator().next();
    }

    /**
     *
     * After create new Enrolment, must delete OptionalEnrolment (to delete
     * OptionalEnrolment disconnect at least: ProgramCertificateRequests,
     * CourseLoadRequests, ExamDateCertificateRequests)
     *
     * @param optionalEnrolment
     * @param curriculumGroup
     *            : new CurriculumGroup for Enrolment
     * @return Enrolment
     */
    static Enrolment createBasedOn(final OptionalEnrolment optionalEnrolment, final CurriculumGroup curriculumGroup) {
        checkParameters(optionalEnrolment, curriculumGroup);

        final Enrolment enrolment = new Enrolment();
        enrolment.setCurricularCourse(optionalEnrolment.getCurricularCourse());
        enrolment.setWeigth(optionalEnrolment.getWeigth());
        enrolment.setEnrollmentState(optionalEnrolment.getEnrollmentState());
        enrolment.setExecutionPeriod(optionalEnrolment.getExecutionPeriod());
        enrolment.setEvaluationSeason(optionalEnrolment.getEvaluationSeason());
        enrolment.setCreatedBy(Authenticate.getUser().getUsername());
        enrolment.setCreationDateDateTime(optionalEnrolment.getCreationDateDateTime());
        enrolment.setEnrolmentCondition(optionalEnrolment.getEnrolmentCondition());
        enrolment.setCurriculumGroup(curriculumGroup);

        enrolment.getEvaluationsSet().addAll(optionalEnrolment.getEvaluationsSet());
        enrolment.getProgramCertificateRequestsSet().addAll(optionalEnrolment.getProgramCertificateRequestsSet());
        enrolment.getCourseLoadRequestsSet().addAll(optionalEnrolment.getCourseLoadRequestsSet());
        enrolment.getExtraExamRequestsSet().addAll(optionalEnrolment.getExtraExamRequestsSet());
        enrolment.getEnrolmentWrappersSet().addAll(optionalEnrolment.getEnrolmentWrappersSet());
        enrolment.getExamDateCertificateRequestsSet().addAll(optionalEnrolment.getExamDateCertificateRequestsSet());
        changeAttends(optionalEnrolment, enrolment);
        enrolment.createCurriculumLineLog(EnrolmentAction.ENROL);

        return enrolment;
    }

    @Override
    public void setCurriculumGroup(final CurriculumGroup curriculumGroup) {
        super.setCurriculumGroup(curriculumGroup);
        // Enrolment "isFirstTime" optimization needs to be re-computed, for all
        // sibling enrolments.
        if (curriculumGroup != null) {
            curriculumGroup.getStudentCurricularPlan().setIsFirstTimeToNull();
        }
    }

    static protected void changeAttends(final Enrolment from, final Enrolment to) {
        final Registration oldRegistration = from.getRegistration();
        final Registration newRegistration = to.getRegistration();

        if (oldRegistration != newRegistration) {
            for (final Attends attend : from.getAttendsSet()) {
                oldRegistration.changeShifts(attend, newRegistration);
                attend.setRegistration(newRegistration);
            }
        }
        to.getAttendsSet().addAll(from.getAttendsSet());
    }

    static private void checkParameters(final OptionalEnrolment optionalEnrolment, final CurriculumGroup curriculumGroup) {
        if (optionalEnrolment == null) {
            throw new DomainException("error.Enrolment.invalid.optionalEnrolment");
        }
        if (curriculumGroup == null) {
            throw new DomainException("error.Enrolment.invalid.curriculumGroup");
        }
    }

    @Override
    public boolean isAnual() {
        final CurricularCourse curricularCourse = getCurricularCourse();
        return curricularCourse != null && curricularCourse.isAnual();
    }

    public boolean hasAnyNonTemporaryEvaluations() {
        for (EnrolmentEvaluation evaluation : this.getEvaluationsSet()) {
            if (!EnrolmentEvaluationState.TEMPORARY_OBJ.equals(evaluation.getEnrolmentEvaluationState())) {
                return true;
            }
        }

        return false;
    }

    public boolean canBeUsedAsCreditsSource() {
        return !isInvisible() && isApproved() && !parentIsInternalCreditsGroup();
    }

    private boolean parentIsInternalCreditsGroup() {
        return getCurriculumGroup() instanceof InternalCreditsSourceCurriculumGroup;
    }

    public boolean isDissertation() {
        return getCurricularCourse().isDissertation();
    }

    @Override
    public String getModuleTypeName() {
        return BundleUtil.getString(Bundle.ENUMERATION, this.getClass().getName());
    }

    public void annul() {
        setEnrollmentState(EnrollmentState.ANNULED);
        setAnnulmentDate(new DateTime());
    }

    public void activate() {
        if (!isActive()) {
            final Grade finalGrade = getGrade();
            setEnrollmentState(finalGrade.isEmpty() ? EnrollmentState.ENROLLED : finalGrade.getEnrolmentState());
            setAnnulmentDate(null);
        }
    }

}
