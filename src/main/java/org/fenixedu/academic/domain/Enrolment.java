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
import org.fenixedu.academic.domain.curriculum.EnrollmentCondition;
import org.fenixedu.academic.domain.curriculum.EnrollmentState;
import org.fenixedu.academic.domain.curriculum.EnrolmentEvaluationContext;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.degreeStructure.OptionalCurricularCourse;
import org.fenixedu.academic.domain.enrolment.EnroledEnrolmentWrapper;
import org.fenixedu.academic.domain.enrolment.ExternalDegreeEnrolmentWrapper;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.log.EnrolmentLog;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationDataByExecutionYear;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.StudentStatute;
import org.fenixedu.academic.domain.student.curriculum.Curriculum;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.EctsAndWeightProviderRegistry;
import org.fenixedu.academic.domain.studentCurriculum.InternalCreditsSourceCurriculumGroup;
import org.fenixedu.academic.domain.treasury.ITreasuryBridgeAPI;
import org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory;
import org.fenixedu.academic.service.AcademicPermissionService;
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

    protected Enrolment() {
        super();
        super.setIsExtraCurricular(Boolean.FALSE);
        Signal.emit(Enrolment.SIGNAL_CREATED, new DomainObjectEvent<>(this));
    }

    // TODO: DELETE
//    public Enrolment(final StudentCurricularPlan studentCurricularPlan, final CurricularCourse curricularCourse,
//            final ExecutionInterval executionInterval, final EnrollmentCondition enrolmentCondition, final String createdBy) {
//        this();
//        initializeAsNew(studentCurricularPlan, curricularCourse, executionInterval, enrolmentCondition, createdBy);
//        createCurriculumLineLog(EnrolmentAction.ENROL);
//    }

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

    @Deprecated
    final public boolean isInvisible() {
        return getEnrolmentCondition() == EnrollmentCondition.INVISIBLE;
    }

    final public boolean isTemporary() {
        return getEnrolmentCondition() == EnrollmentCondition.TEMPORARY;
    }

    @Deprecated
    final public boolean isImpossible() {
        return getEnrolmentCondition() == EnrollmentCondition.IMPOSSIBLE;
    }

    // new student structure methods
    public Enrolment(final StudentCurricularPlan studentCurricularPlan, final CurriculumGroup curriculumGroup,
            final CurricularCourse curricularCourse, final ExecutionInterval executionInterval,
            final EnrollmentCondition enrolmentCondition, final String createdBy) {
        this();
        if (studentCurricularPlan == null || curriculumGroup == null || curricularCourse == null || executionInterval == null
                || enrolmentCondition == null || createdBy == null) {
            throw new DomainException("invalid arguments");
        }
        checkInitConstraints(studentCurricularPlan, curricularCourse, executionInterval);
        // TODO: check this
        // validateDegreeModuleLink(curriculumGroup, curricularCourse);
        initializeAsNew(studentCurricularPlan, curriculumGroup, curricularCourse, executionInterval, enrolmentCondition,
                createdBy);
        createCurriculumLineLog(EnrolmentAction.ENROL);
    }

    protected void checkInitConstraints(final StudentCurricularPlan studentCurricularPlan,
            final CurricularCourse curricularCourse, final ExecutionInterval executionInterval) {
        if (studentCurricularPlan.getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse, executionInterval) != null) {
            throw new DomainException("error.Enrolment.duplicate.enrolment", curricularCourse.getName());
        }
    }

    protected void initializeAsNew(final StudentCurricularPlan studentCurricularPlan, final CurriculumGroup curriculumGroup,
            final CurricularCourse curricularCourse, final ExecutionInterval executionInterval,
            final EnrollmentCondition enrolmentCondition, final String createdBy) {
        initializeAsNewWithoutEnrolmentEvaluation(studentCurricularPlan, curriculumGroup, curricularCourse, executionInterval,
                enrolmentCondition, createdBy);
        createEnrolmentEvaluationWithoutGrade();
    }

    protected void initializeAsNewWithoutEnrolmentEvaluation(final StudentCurricularPlan studentCurricularPlan,
            final CurriculumGroup curriculumGroup, final CurricularCourse curricularCourse,
            final ExecutionInterval executionInterval, final EnrollmentCondition enrolmentCondition, final String createdBy) {
        initializeCommon(studentCurricularPlan, curricularCourse, executionInterval, enrolmentCondition, createdBy);
        setCurriculumGroup(curriculumGroup);
    }

    // end

    protected void initializeAsNew(final StudentCurricularPlan studentCurricularPlan, final CurricularCourse curricularCourse,
            final ExecutionInterval executionInterval, final EnrollmentCondition enrolmentCondition, final String createdBy) {
        initializeAsNewWithoutEnrolmentEvaluation(studentCurricularPlan, curricularCourse, executionInterval, enrolmentCondition,
                createdBy);
        createEnrolmentEvaluationWithoutGrade();
    }

    private void initializeCommon(final StudentCurricularPlan studentCurricularPlan, final CurricularCourse curricularCourse,
            final ExecutionInterval executionInterval, final EnrollmentCondition enrolmentCondition, final String createdBy) {
        setCurricularCourse(curricularCourse);
        setWeigth(curricularCourse.getEctsCredits(executionInterval));
        setEnrollmentState(EnrollmentState.ENROLLED);
        setExecutionPeriod(executionInterval);
        setEvaluationSeason(EvaluationConfiguration.getInstance().getDefaultEvaluationSeason());
        setCreatedBy(createdBy);
        setCreationDateDateTime(new DateTime());
        setEnrolmentCondition(enrolmentCondition);
        createAttend(studentCurricularPlan.getRegistration(), curricularCourse, executionInterval);
        super.setIsExtraCurricular(Boolean.FALSE);
    }

    protected void initializeAsNewWithoutEnrolmentEvaluation(final StudentCurricularPlan studentCurricularPlan,
            final CurricularCourse curricularCourse, final ExecutionInterval executionInterval,
            final EnrollmentCondition enrolmentCondition, final String createdBy) {
        initializeCommon(studentCurricularPlan, curricularCourse, executionInterval, enrolmentCondition, createdBy);
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

        setExecutionPeriod(null);
        setStudentCurricularPlan(null);
        setDegreeModule(null);
        setCurriculumGroup(null);

        Iterator<Attends> attendsIter = getAttendsSet().iterator();
        while (attendsIter.hasNext()) {
            Attends attends = attendsIter.next();

            attendsIter.remove();
            attends.setEnrolment(null);

            if (attends.getAssociatedMarksSet().isEmpty()) {
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
        if (!getEnrolmentWrappersSet().isEmpty()) {
            throw new DomainException("error.Enrolment.is.origin.in.some.Equivalence");
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

    public boolean isEvaluatedInSeason(final EvaluationSeason season, final ExecutionInterval interval) {
        return getEnrolmentEvaluation(season, interval, Boolean.TRUE).isPresent();
    }

    public boolean isEnroledInSeason(final EvaluationSeason season, final ExecutionInterval interval) {
        return getTemporaryEvaluation(season, interval).isPresent();
    }

    public Optional<EnrolmentEvaluation> getEnrolmentEvaluation(final EvaluationSeason season, final ExecutionInterval interval,
            final Boolean assertFinal) {

        final Supplier<Stream<EnrolmentEvaluation>> supplier = () -> getEnrolmentEvaluationBySeason(season).filter(evaluation -> {

            if (evaluation.isAnnuled()) {
                return false;
            }

            if (evaluation.getExecutionInterval() != null && interval != null && evaluation.getExecutionInterval() != interval) {
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
                    season == null ? "" : season.getName().getContent(), interval == null ? "" : interval.getQualifiedName());
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
            final ExecutionInterval executionInterval) {

        final List<ExecutionCourse> executionCourses = curricularCourse.getExecutionCoursesByExecutionPeriod(executionInterval);

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
                        executionCourse.getNome(), executionCourse.getExecutionInterval().getQualifiedName());
            }
        }
    }

    final public void createAttends(final Registration registration, final ExecutionCourse executionCourse) {
        final Attends attendsFor = this.getAttendsFor(executionCourse.getExecutionInterval());
        if (attendsFor != null) {
            try {
                attendsFor.delete();
            } catch (DomainException e) {
                throw new DomainException("error.attends.cant.change.attends");
            }
        }

        this.addAttends(new Attends(registration, executionCourse));
    }

    public EnrolmentEvaluation createTemporaryEvaluationForImprovement(final Person person,
            final EvaluationSeason evaluationSeason, final ExecutionInterval executionInterval) {

        getPredicateImprovement().fill(evaluationSeason, executionInterval, EnrolmentEvaluationContext.MARK_SHEET_EVALUATION)
                .test(this);

        final EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation(this, evaluationSeason,
                EnrolmentEvaluationState.TEMPORARY_OBJ, person, executionInterval);
        createAttendForImprovement(executionInterval);
        RegistrationDataByExecutionYear.getOrCreateRegistrationDataByYear(getRegistration(),
                executionInterval.getExecutionYear());

        Signal.emit(ITreasuryBridgeAPI.IMPROVEMENT_ENROLMENT, new DomainObjectEvent<>(enrolmentEvaluation));

        return enrolmentEvaluation;
    }

    private void createAttendForImprovement(final ExecutionInterval executionInterval) {
        final Registration registration = getRegistration();

        // FIXME this is completly wrong, there may be more than one execution
        // course for this curricular course
        ExecutionCourse currentExecutionCourse =
                (ExecutionCourse) CollectionUtils.find(getCurricularCourse().getAssociatedExecutionCoursesSet(), new Predicate() {

                    @Override
                    final public boolean evaluate(final Object arg0) {
                        ExecutionCourse executionCourse = (ExecutionCourse) arg0;
                        if (executionCourse.getExecutionInterval() == executionInterval) {
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
            final ExecutionInterval improvementInterval) throws DomainException {

        final EnrolmentEvaluation temporaryImprovement = getTemporaryEvaluation(season, improvementInterval).orElse(null);
        if (temporaryImprovement == null || !temporaryImprovement.isTemporary()) {
            throw new DomainException("error.enrolment.cant.unenroll");
        }

        TreasuryBridgeAPIFactory.implementation().improvementUnrenrolment(temporaryImprovement);

        temporaryImprovement.delete();

        // improvement may be on the same semester, in which case we don't want to
        // remove the Attends
        if (getExecutionInterval() != improvementInterval) {
            final Attends attends = getAttendsFor(improvementInterval);
            if (attends != null) {
                attends.delete();
            }
        }
    }

    final public boolean isImprovementForExecutionCourse(final ExecutionCourse executionCourse) {
        return getExecutionInterval() != executionCourse.getExecutionInterval()
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

            if (enrolment.isEvaluatedInSeason(getEvaluationSeason(), getExecutionInterval())) {
                throw new DomainException("error.EvaluationSeason.enrolment.evaluated.in.this.season",
                        enrolment.getName().getContent(), getEvaluationSeason().getName().getContent());
            }

            if (getContext() == EnrolmentEvaluationContext.MARK_SHEET_EVALUATION) {
                if (enrolment.isEnroledInSeason(getEvaluationSeason(), getExecutionInterval())) {
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
            final ExecutionInterval improvementInterval = getExecutionInterval();

            final ExecutionInterval enrolmentInterval = enrolment.getExecutionInterval();
            if (improvementInterval.isBeforeOrEquals(enrolmentInterval)) {
                throw new DomainException("error.EnrolmentEvaluation.improvement.semester.must.be.after.or.equal.enrolment",
                        enrolment.getName().getContent());
            }

            if (!enrolment.isApproved()) {
                throw new DomainException(
                        "curricularRules.ruleExecutors.ImprovementOfApprovedEnrolmentExecutor.degree.module.hasnt.been.approved",
                        enrolment.getName().getContent());
            }

            getPredicateSeason().fill(getEvaluationSeason(), improvementInterval, getContext()).test(enrolment);

            final DegreeModule degreeModule = enrolment.getDegreeModule();
            if (!degreeModule.hasAnyParentContexts(improvementInterval)) {
                throw new DomainException(
                        "curricularRules.ruleExecutors.ImprovementOfApprovedEnrolmentExecutor.degree.module.has.no.context.in.present.execution.period",
                        enrolment.getName().getContent(), improvementInterval.getQualifiedName());
            }

            // ImprovingInExecutionPeriodFollowingApproval
            final ExecutionInterval nextInterval = enrolmentInterval.getNext();
            if (improvementInterval == nextInterval) {
                return true;
            }

            // ImprovingInExecutionPeriodFollowingApproval
            if (!isOneYearAfter(improvementInterval, enrolmentInterval)) {

                for (ExecutionInterval iter = nextInterval; iter != null && iter != improvementInterval; iter = iter.getNext()) {
                    if (degreeModule.hasAnyParentContexts(iter)) {
                        throw new DomainException(
                                "curricularRules.ruleExecutors.ImprovementOfApprovedEnrolmentExecutor.is.not.improving.in.execution.period.following.approval");
                    }
                }
            }

            return true;
        }

    };

    public static boolean isOneYearAfter(final ExecutionInterval improvementInterval, final ExecutionInterval enrolmentInterval) {
        final ExecutionInterval nextExecutionPeriod = enrolmentInterval.getNext();
        return nextExecutionPeriod == null ? false : improvementInterval == nextExecutionPeriod.getNext();
    }

    static abstract public class EnrolmentPredicate implements java.util.function.Predicate<Enrolment> {

        private EvaluationSeason evaluationSeason;
        private ExecutionInterval executionInterval;
        private EnrolmentEvaluationContext context;

        public EvaluationSeason getEvaluationSeason() {
            return evaluationSeason;
        }

        public ExecutionInterval getExecutionInterval() {
            return executionInterval;
        }

        public EnrolmentEvaluationContext getContext() {
            return context;
        }

        public EnrolmentPredicate fill(final EvaluationSeason season, final ExecutionInterval interval,
                final EnrolmentEvaluationContext context) {
            this.evaluationSeason = season;
            this.executionInterval = interval;
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
            final ExecutionInterval specialSeasonInterval = getExecutionInterval();

            final ExecutionInterval enrolmentInterval = enrolment.getExecutionInterval();
            if (specialSeasonInterval != enrolmentInterval) {
                throw new DomainException("error.EnrolmentEvaluation.special.season.semester.must.be",
                        enrolment.getName().getContent());
            }

            if (enrolment.isApproved()) {
                throw new DomainException(
                        "curricularRules.ruleExecutors.EnrolmentInSpecialSeasonEvaluationExecutor.degree.module.has.been.approved",
                        enrolment.getName().getContent());
            }

            getPredicateSeason().fill(getEvaluationSeason(), getExecutionInterval(), getContext()).test(enrolment);

            if (enrolment.hasSpecialSeasonInExecutionYear(enrolmentInterval.getExecutionYear())) {
                throw new DomainException(
                        "curricularRules.ruleExecutors.EnrolmentInSpecialSeasonEvaluationExecutor.already.enroled.in.special.season",
                        enrolment.getName().getContent(), enrolment.getExecutionYear().getYear());
            }

            // checkPermissions
            final StudentCurricularPlan scp = enrolment.getStudentCurricularPlan();
            final Registration registration = scp.getRegistration();
            if (!isSpecialSeasonGrantedByStatute(registration)) {
                throw new DomainException("error.special.season.not.granted");
            }

            final boolean isServices =
                    AcademicAuthorizationGroup.get(AcademicOperationType.STUDENT_ENROLMENTS).isMember(Authenticate.getUser())
                            || AcademicPermissionService.hasAccess("ACADEMIC_OFFICE_ENROLMENTS", Authenticate.getUser());
            return considerThisEnrolmentNormalEnrolments(enrolment)
                    || considerThisEnrolmentPropaedeuticEnrolments(enrolment, isServices)
                    || considerThisEnrolmentExtraCurricularEnrolments(enrolment, isServices)
                    || considerThisEnrolmentStandaloneEnrolments(enrolment, isServices);
        }

        private boolean isSpecialSeasonGrantedByStatute(final Registration registration) {
            final Student student = registration.getStudent();

            for (StudentStatute statute : student.getStudentStatutesSet()) {
                if (!statute.getType().getSpecialSeasonGranted() && !statute.hasSeniorStatuteForRegistration(registration)) {
                    continue;
                }
                if (!statute.isValidInExecutionInterval(getExecutionInterval())) {
                    continue;
                }

                return true;
            }

            return false;
        }

        private boolean considerThisEnrolmentNormalEnrolments(final Enrolment enrolment) {
            if (!enrolment.isExtraCurricular() && !enrolment.isPropaedeutic() && !enrolment.isStandalone()) {
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

        final ExecutionInterval executionInterval = getExecutionInterval();

        getPredicateSpecialSeason().fill(evaluationSeason, executionInterval, EnrolmentEvaluationContext.MARK_SHEET_EVALUATION)
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

        final EnrolmentEvaluation temporarySpecialSeason = getTemporaryEvaluation(season, getExecutionInterval()).orElse(null);
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
     * @deprecated Please use Enrolment.hasImprovementFor(ExecutionSemester) where
     *             possible
     */
    @Deprecated
    final public boolean hasImprovement() {
        return getEvaluationsSet().stream().filter(i -> i.getEvaluationSeason().isImprovement()).findAny().isPresent();
    }

    public boolean hasImprovementFor(final ExecutionInterval interval) {
        for (EnrolmentEvaluation enrolmentEvaluation : this.getEvaluationsSet()) {
            if (enrolmentEvaluation.getEvaluationSeason().isImprovement()) {
                final ExecutionInterval evalPeriod = enrolmentEvaluation.getExecutionInterval();
                if (evalPeriod != null && evalPeriod == interval) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasSpecialSeasonFor(final ExecutionInterval interval) {
        return this.getEvaluationsSet().stream().filter(ee -> ee.getEvaluationSeason().isSpecial())
                .anyMatch(ee -> ee.getExecutionInterval() == interval);
    }

    @Deprecated
    final public boolean hasSpecialSeason() {
        for (final EnrolmentEvaluation evaluation : getEvaluationsSet()) {
            final EvaluationSeason season = evaluation.getEvaluationSeason();

            if (season.isSpecial() && isEnroledInSeason(season, getExecutionInterval())) {
                return true;
            }
        }

        return false;
    }

    @Deprecated
    final public boolean hasSpecialSeasonInExecutionYear(final ExecutionYear executionYear) {
        for (final Enrolment enrolment : getBrothers()) {
            if (enrolment.getExecutionYear() == executionYear && enrolment.hasSpecialSeason()) {
                return true;
            }
        }

        return hasSpecialSeason();
    }

    final public boolean isFlunked() {
        if (isAnnulled()) {
            return true;
        }

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
    public boolean isApproved(final CurricularCourse curricularCourse, final ExecutionInterval executionInterval) {
        if (executionInterval == null || getExecutionInterval().isBeforeOrEquals(executionInterval)) {
            return isApproved() && hasCurricularCourse(getCurricularCourse(), curricularCourse, executionInterval);
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

    @Override
    final public String getGradeValue() {
        return getGrade().getValue();
    }

    @Override
    final public Integer getFinalGrade() {
        return getGrade().getIntegerValue();
    }

    @Override
    final public boolean isEnroled() {
        return this.getEnrollmentState() == EnrollmentState.ENROLLED;
    }

    final public boolean isAnnulled() {
        return this.getEnrollmentState() == EnrollmentState.ANNULED;
    }

    final public int getNumberOfTotalEnrolmentsInThisCourse(final ExecutionInterval untilExecutionInterval) {
        return this.getStudentCurricularPlan().countEnrolmentsByCurricularCourse(this.getCurricularCourse(),
                untilExecutionInterval);
    }

    @Override
    protected void createCurriculumLineLog(final EnrolmentAction action) {
        new EnrolmentLog(action, getRegistration(), getCurricularCourse(), getExecutionInterval(), getCurrentUser());
    }

    @Override
    public StringBuilder print(final String tabs) {
        final StringBuilder builder = new StringBuilder();
        builder.append(tabs);
        builder.append("[E ").append(getDegreeModule().getName()).append(" ").append(isApproved()).append(" ]\n");
        return builder;
    }

    final public Attends getAttendsByExecutionCourse(final ExecutionCourse executionCourse) {
        for (final Attends attends : this.getAttendsSet()) {
            if (attends.isFor(executionCourse)) {
                return attends;
            }
        }
        return null;
    }

    public Attends getAttendsFor(final ExecutionInterval interval) {
        Attends result = null;

        for (final Attends attends : getAttendsSet()) {
            if (attends.isFor(interval)) {
                if (result == null) {
                    result = attends;
                } else {
                    throw new DomainException("Enrolment.found.two.attends.for.same.execution.period");
                }
            }
        }

        return result;
    }

    final public ExecutionCourse getExecutionCourseFor(final ExecutionInterval executionInterval) {
        for (final Attends attend : getAttendsSet()) {
            if (attend.getExecutionCourse().getExecutionInterval() == executionInterval) {
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
            final ExecutionInterval interval) {
        return getEnrolmentEvaluation(season, interval, Boolean.FALSE);
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
            final ExecutionInterval executionInterval) {
        return isValid(executionInterval) && this.getCurricularCourse().equals(curricularCourse);
    }

    @Override
    public boolean isValid(final ExecutionInterval executionInterval) {
        return getExecutionInterval() == executionInterval || getCurricularCourse().isAnual()
                && getExecutionInterval().getExecutionYear() == executionInterval.getExecutionYear();
    }

    public boolean isValid(final ExecutionYear executionYear) {
        for (final ExecutionInterval executionInterval : executionYear.getChildIntervals()) {
            if (isValid(executionInterval)) {
                return true;
            }
        }
        return false;
    }

    @Override
    final public boolean hasEnrolmentWithEnroledState(final CurricularCourse curricularCourse,
            final ExecutionInterval executionInterval) {
        return isEnroled() && isEnroledInExecutionPeriod(curricularCourse, executionInterval);
    }

    final public Collection<ExecutionCourse> getExecutionCourses() {
        return this.getCurricularCourse().getAssociatedExecutionCoursesSet();
    }

    final public boolean isEnrolmentTypeNormal() {
        return !getCurricularCourse().isOptionalCurricularCourse() && !isExtraCurricular() && !isOptional();
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

    @Override
    public Set<CurriculumLine> getCurriculumLinesForCurriculum(final StudentCurricularPlan studentCurricularPlan) {
        return studentCurricularPlan.getCreditsSet().stream()
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

        return BigDecimal.valueOf(getCurricularCourse().getEctsCredits(getExecutionInterval()));
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
    final public Double getEnroledEctsCredits(final ExecutionInterval executionInterval) {
        return isValid(executionInterval) && isEnroled() ? getEctsCredits() : Double.valueOf(0d);
    }

    @Override
    final public Double getEnroledEctsCredits(final ExecutionYear executionYear) {
        return isValid(executionYear) && isEnroled() ? getEctsCredits() : Double.valueOf(0d);
    }

    @Override
    final public Enrolment findEnrolmentFor(final CurricularCourse curricularCourse, final ExecutionInterval executionInterval) {
        return isEnroledInExecutionPeriod(curricularCourse, executionInterval) ? this : null;
    }

    @Override
    final public Enrolment getApprovedEnrolment(final CurricularCourse curricularCourse) {
        return isApproved(curricularCourse) ? this : null;
    }

    @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(final ExecutionInterval executionInterval) {
        if (isValid(executionInterval) && isEnroled()) {
            if (isFromExternalDegree()) {
                return Collections
                        .<IDegreeModuleToEvaluate> singleton(new ExternalDegreeEnrolmentWrapper(this, executionInterval));
            } else {
                return Collections.<IDegreeModuleToEvaluate> singleton(new EnroledEnrolmentWrapper(this, executionInterval));
            }
        }
        return Collections.emptySet();
    }

    private boolean isFromExternalDegree() {
        return getDegreeModule().getParentDegreeCurricularPlan() != getDegreeCurricularPlanOfDegreeModule();
    }

    @Override
    final public double getAccumulatedEctsCredits(final ExecutionInterval executionInterval) {
        return !parentAllowAccumulatedEctsCredits() ? 0d : getStudentCurricularPlan().getAccumulatedEctsCredits(executionInterval,
                getCurricularCourse());
    }

    @Override
    final public String getDescription() {
        return getStudentCurricularPlan().getDegree().getPresentationName(getExecutionYear()) + " > " + getName().getContent();
    }

    final public boolean isBefore(final Enrolment enrolment) {
        return getExecutionInterval().isBefore(enrolment.getExecutionInterval());
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
    public boolean hasEnrolment(final ExecutionInterval executionInterval) {
        return isValid(executionInterval);
    }

    @Override
    public boolean hasEnrolment(final ExecutionYear executionYear) {
        return isValid(executionYear);
    }

    @Override
    public boolean isEnroledInSpecialSeason(final ExecutionInterval executionInterval) {
        return isValid(executionInterval) && hasSpecialSeason();
    }

    @Override
    public boolean isEnroledInSpecialSeason(final ExecutionYear executionYear) {
        return isValid(executionYear) && hasSpecialSeason();
    }

    @Override
    public int getNumberOfAllApprovedEnrolments(final ExecutionInterval executionInterval) {
        return isValid(executionInterval) && isApproved() ? 1 : 0;
    }

    /**
     *
     * After create new Enrolment, must delete OptionalEnrolment (to delete
     * OptionalEnrolment disconnect at least: ProgramCertificateRequests,
     * CourseLoadRequests, ExamDateCertificateRequests)
     *
     * @param optionalEnrolment
     * @param curriculumGroup : new CurriculumGroup for Enrolment
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
        enrolment.getEnrolmentWrappersSet().addAll(optionalEnrolment.getEnrolmentWrappersSet());
        changeAttends(optionalEnrolment, enrolment);
        enrolment.createCurriculumLineLog(EnrolmentAction.ENROL);

        return enrolment;
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
        if (isAnnulled()) {
            final Grade finalGrade = getGrade();
            setEnrollmentState(finalGrade.isEmpty() ? EnrollmentState.ENROLLED : finalGrade.getEnrolmentState());
            setAnnulmentDate(null);
        }
    }

    /**
     * @deprecated use {@link #getExecutionInterval()} instead.
     */
    @Deprecated
    @Override
    public ExecutionInterval getExecutionPeriod() {
        return getExecutionInterval();
    }

    @Override
    public ExecutionInterval getExecutionInterval() {
        return super.getExecutionPeriod();
    }

    @Override
    public void setDegreeModule(DegreeModule degreeModule) {
        if (degreeModule instanceof OptionalCurricularCourse) {
            throw new DomainException("error.Enrolment.optional.curricular.course.cannot.be.set.as.degree.module");
        }

        super.setDegreeModule(degreeModule);
    }
}
