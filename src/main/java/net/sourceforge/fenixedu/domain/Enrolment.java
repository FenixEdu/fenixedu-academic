/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.curriculum.CurriculumValidationEvaluationPhase;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationContext;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsTableIndex;
import net.sourceforge.fenixedu.domain.enrolment.EnroledEnrolmentWrapper;
import net.sourceforge.fenixedu.domain.enrolment.ExternalDegreeEnrolmentWrapper;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.log.EnrolmentEvaluationLog;
import net.sourceforge.fenixedu.domain.log.EnrolmentLog;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.CreditsDismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.EnrolmentWrapper;
import net.sourceforge.fenixedu.domain.studentCurriculum.InternalCreditsSourceCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.InternalEnrolmentWrapper;
import net.sourceforge.fenixedu.domain.studentCurriculum.OptionalDismissal;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.EnrolmentAction;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author dcs-rjao
 * 
 *         24/Mar/2003
 */

public class Enrolment extends Enrolment_Base implements IEnrolment {

    static final public Comparator<Enrolment> REVERSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_ID = new Comparator<Enrolment>() {
        @Override
        public int compare(Enrolment o1, Enrolment o2) {
            return -COMPARATOR_BY_EXECUTION_PERIOD_AND_ID.compare(o1, o2);
        }
    };

    static final public Comparator<Enrolment> COMPARATOR_BY_LATEST_ENROLMENT_EVALUATION = new Comparator<Enrolment>() {
        @Override
        final public int compare(Enrolment o1, Enrolment o2) {
            return EnrolmentEvaluation.COMPARATOR_BY_EXAM_DATE.compare(o1.getLatestEnrolmentEvaluation(),
                    o2.getLatestEnrolmentEvaluation());
        }
    };

    static final public Comparator<Enrolment> COMPARATOR_BY_LATEST_ENROLMENT_EVALUATION_AND_ID = new Comparator<Enrolment>() {
        @Override
        final public int compare(Enrolment o1, Enrolment o2) {
            final ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(Enrolment.COMPARATOR_BY_LATEST_ENROLMENT_EVALUATION);
            comparatorChain.addComparator(DomainObjectUtil.COMPARATOR_BY_ID);

            return comparatorChain.compare(o1, o2);
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

    public Enrolment() {
        super();
        setRootDomainObject(Bennu.getInstance());
        super.setIsExtraCurricular(Boolean.FALSE);
    }

    public Enrolment(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
            ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition, String createdBy) {
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
        return (isBoxStructure() && super.isPropaedeutic()) || (!isBolonhaDegree() && getCurricularCourse().isPropaedeutic());
    }

    @Override
    public boolean isExtraCurricular() {
        if (!isBoxStructure()) {
            return super.getIsExtraCurricular() != null && super.getIsExtraCurricular();
        }

        return super.isExtraCurricular();
    }

    @Override
    @Deprecated
    public Boolean getIsExtraCurricular() {
        return isExtraCurricular();
    }

    @Override
    @Deprecated
    public void setIsExtraCurricular(Boolean isExtraCurricular) {
        if (isBoxStructure()) {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.Enrolment.use.markAsExtraCurricular.method.instead");
        }

        super.setIsExtraCurricular(isExtraCurricular);
    }

    public void markAsExtraCurricular() {
        if (isBoxStructure()) {
            setCurriculumGroup(getStudentCurricularPlan().getExtraCurriculumGroup());
            super.setIsExtraCurricular(null);
        } else {
            super.setIsExtraCurricular(true);
        }
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

    final public boolean isSpecialSeason() {
        return hasSpecialSeason();
    }

    // new student structure methods
    public Enrolment(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
            CurricularCourse curricularCourse, ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition,
            String createdBy) {
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

    protected void checkInitConstraints(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
            ExecutionSemester executionSemester) {
        if (studentCurricularPlan.getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse, executionSemester) != null) {
            throw new DomainException("error.Enrolment.duplicate.enrolment", curricularCourse.getName());
        }
    }

    protected void initializeAsNew(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
            CurricularCourse curricularCourse, ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition,
            String createdBy) {
        initializeAsNewWithoutEnrolmentEvaluation(studentCurricularPlan, curriculumGroup, curricularCourse, executionSemester,
                enrolmentCondition, createdBy);
        createEnrolmentEvaluationWithoutGrade();
    }

    protected void initializeAsNewWithoutEnrolmentEvaluation(StudentCurricularPlan studentCurricularPlan,
            CurriculumGroup curriculumGroup, CurricularCourse curricularCourse, ExecutionSemester executionSemester,
            EnrollmentCondition enrolmentCondition, String createdBy) {
        initializeCommon(studentCurricularPlan, curricularCourse, executionSemester, enrolmentCondition, createdBy);
        setCurriculumGroup(curriculumGroup);
    }

    // end

    protected void initializeAsNew(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
            ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition, String createdBy) {
        initializeAsNewWithoutEnrolmentEvaluation(studentCurricularPlan, curricularCourse, executionSemester, enrolmentCondition,
                createdBy);
        createEnrolmentEvaluationWithoutGrade();
    }

    private void initializeCommon(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
            ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition, String createdBy) {
        setCurricularCourse(curricularCourse);
        setWeigth(studentCurricularPlan.isBolonhaDegree() ? curricularCourse.getEctsCredits(executionSemester) : curricularCourse
                .getWeigth());
        setEnrollmentState(EnrollmentState.ENROLLED);
        setExecutionPeriod(executionSemester);
        setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
        setCreatedBy(createdBy);
        setCreationDateDateTime(new DateTime());
        setEnrolmentCondition(enrolmentCondition);
        createAttend(studentCurricularPlan.getRegistration(), curricularCourse, executionSemester);
        super.setIsExtraCurricular(Boolean.FALSE);
    }

    protected void initializeAsNewWithoutEnrolmentEvaluation(StudentCurricularPlan studentCurricularPlan,
            CurricularCourse curricularCourse, ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition,
            String createdBy) {
        initializeCommon(studentCurricularPlan, curricularCourse, executionSemester, enrolmentCondition, createdBy);
        setStudentCurricularPlan(studentCurricularPlan);
    }

    final public void unEnroll() throws DomainException {

        for (EnrolmentEvaluation eval : getEvaluationsSet()) {

            if (eval.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.NORMAL)
                    && eval.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.TEMPORARY_OBJ)
                    && eval.getGrade().isEmpty()) {
                continue;
            } else {
                throw new DomainException("error.enrolment.cant.unenroll");
            }
        }

        delete();
    }

    @Override
    public void delete() {
        checkRulesToDelete();
        createCurriculumLineLog(EnrolmentAction.UNENROL);
        deleteInformation();
        super.delete();
    }

    protected void deleteInformation() {

        final Iterator<Thesis> theses = getThesesSet().iterator();
        while (theses.hasNext()) {
            final Thesis thesis = theses.next();
            if (!thesis.canBeDeleted()) {
                throw new DomainException("error.Enrolment.cannot.delete.thesis");
            }
            thesis.delete();
        }

        final Registration registration = getRegistration();

        getStudentCurricularPlan().setIsFirstTimeToNull();
        setExecutionPeriod(null);
        setStudentCurricularPlan(null);
        setDegreeModule(null);
        setCurriculumGroup(null);
        getNotNeedToEnrollCurricularCourses().clear();

        Iterator<Attends> attendsIter = getAttendsSet().iterator();
        while (attendsIter.hasNext()) {
            Attends attends = attendsIter.next();

            attendsIter.remove();
            attends.setEnrolment(null);

            if (!attends.hasAnyAssociatedMarks() && !attends.hasAnyStudentGroups()) {
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

        Iterator<CreditsInAnySecundaryArea> creditsInAnysecundaryAreaIterator = getCreditsInAnySecundaryAreasSet().iterator();

        while (creditsInAnysecundaryAreaIterator.hasNext()) {
            CreditsInAnySecundaryArea credits = creditsInAnysecundaryAreaIterator.next();
            creditsInAnysecundaryAreaIterator.remove();
            credits.delete();
        }

        Iterator<CreditsInScientificArea> creditsInScientificAreaIterator = getCreditsInScientificAreasSet().iterator();

        while (creditsInScientificAreaIterator.hasNext()) {
            CreditsInScientificArea credits = creditsInScientificAreaIterator.next();
            creditsInScientificAreaIterator.remove();
            credits.delete();
        }
    }

    protected void checkRulesToDelete() {
        if (hasAnyExtraExamRequests()) {
            throw new DomainException("error.Enrolment.has.ExtraExamRequests");
        }
        if (hasAnyEnrolmentWrappers()) {
            throw new DomainException("error.Enrolment.is.origin.in.some.Equivalence");
        }
        if (hasAnyCourseLoadRequests()) {
            throw new DomainException("error.Enrolment.has.CourseLoadRequests");
        }
        if (hasAnyProgramCertificateRequests()) {
            throw new DomainException("error.Enrolment.has.ProgramCertificateRequests");
        }
    }

    final public Collection<Enrolment> getBrothers() {
        final Collection<Enrolment> result = new HashSet<Enrolment>();

        result.addAll(getStudentCurricularPlan().getEnrolments(getCurricularCourse()));
        result.remove(this);

        return result;
    }

    final public EnrolmentEvaluation getEnrolmentEvaluationByEnrolmentEvaluationTypeAndGrade(
            final EnrolmentEvaluationType evaluationType, final Grade grade) {

        return (EnrolmentEvaluation) CollectionUtils.find(getEvaluationsSet(), new Predicate() {

            @Override
            final public boolean evaluate(Object o) {
                EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) o;
                Grade evaluationGrade = enrolmentEvaluation.getGrade();

                return enrolmentEvaluation.getEnrolmentEvaluationType().equals(evaluationType) && evaluationGrade.equals(grade);
            }

        });
    }

    final public EnrolmentEvaluation getEnrolmentEvaluationByEnrolmentEvaluationStateAndType(
            final EnrolmentEvaluationState state, final EnrolmentEvaluationType type) {
        return (EnrolmentEvaluation) CollectionUtils.find(getEvaluationsSet(), new Predicate() {

            @Override
            final public boolean evaluate(Object o) {
                EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) o;
                return (enrolmentEvaluation.getEnrolmentEvaluationState().equals(state) && enrolmentEvaluation
                        .getEnrolmentEvaluationType().equals(type));
            }

        });
    }

    public EnrolmentEvaluation getEnrolmentEvaluation(pt.utl.ist.fenix.tools.predicates.Predicate<EnrolmentEvaluation> predicate) {
        for (EnrolmentEvaluation enrolmentEvaluation : getEvaluations()) {
            if (predicate.eval(enrolmentEvaluation)) {
                return enrolmentEvaluation;
            }
        }
        return null;
    }

    final public List<EnrolmentEvaluation> getEnrolmentEvaluationsByEnrolmentEvaluationState(
            final EnrolmentEvaluationState evaluationState) {
        List<EnrolmentEvaluation> result = new ArrayList<EnrolmentEvaluation>();
        for (EnrolmentEvaluation evaluation : getEvaluationsSet()) {
            if (evaluation.getEnrolmentEvaluationState().equals(evaluationState)) {
                result.add(evaluation);
            }
        }
        return result;
    }

    final public List<EnrolmentEvaluation> getEnrolmentEvaluationsByEnrolmentEvaluationType(
            final EnrolmentEvaluationType evaluationType) {
        List<EnrolmentEvaluation> result = new ArrayList<EnrolmentEvaluation>();
        for (EnrolmentEvaluation evaluation : getEvaluationsSet()) {
            if (evaluation.getEnrolmentEvaluationType().equals(evaluationType)) {
                result.add(evaluation);
            }
        }
        return result;
    }

    final public List<EnrolmentEvaluation> getEnrolmentEvaluationByEnrolmentEvaluationTypeAndPhase(
            final EnrolmentEvaluationType evaluationType, final CurriculumValidationEvaluationPhase evaluationPhase) {
        List<EnrolmentEvaluation> result = new ArrayList<EnrolmentEvaluation>();
        for (EnrolmentEvaluation evaluation : getEvaluationsSet()) {
            if (evaluationType.equals(evaluation.getEnrolmentEvaluationType())
                    && (evaluationPhase == null || evaluationPhase.equals(evaluation.getCurriculumValidationEvaluationPhase()))) {
                result.add(evaluation);
            }
        }
        return result;
    }

    protected void createEnrolmentEvaluationWithoutGrade() {

        EnrolmentEvaluation enrolmentEvaluation =
                getEnrolmentEvaluationByEnrolmentEvaluationTypeAndGrade(EnrolmentEvaluationType.NORMAL, null);

        if (enrolmentEvaluation == null) {
            enrolmentEvaluation =
                    new EnrolmentEvaluation(this, EnrolmentEvaluationType.NORMAL, EnrolmentEvaluationState.TEMPORARY_OBJ);
            enrolmentEvaluation.setWhenDateTime(new DateTime());

            addEvaluations(enrolmentEvaluation);
        }
    }

    private void createAttend(Registration registration, CurricularCourse curricularCourse, ExecutionSemester executionSemester) {

        final List<ExecutionCourse> executionCourses = curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester);

        ExecutionCourse executionCourse = null;
        if (executionCourses.size() > 1) {
            final Iterator<ExecutionCourse> iterator = executionCourses.iterator();
            while (iterator.hasNext()) {
                final ExecutionCourse each = iterator.next();
                if (!each.hasAnyExecutionCourseProperties()) {
                    executionCourse = each;
                }
            }
        } else if (executionCourses.size() == 1) {
            executionCourse = executionCourses.iterator().next();
        }

        if (executionCourse != null) {
            final Attends attend = executionCourse.getAttendsByStudent(registration.getStudent());
            if (attend == null) {
                addAttends(new Attends(registration, executionCourse));
            } else if (!attend.hasEnrolment()) {
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

    final public EnrolmentEvaluation createEnrolmentEvaluationForImprovement(final Person person,
            final ExecutionSemester executionSemester) {
        final EnrolmentEvaluation enrolmentEvaluation =
                new EnrolmentEvaluation(this, EnrolmentEvaluationType.IMPROVEMENT, EnrolmentEvaluationState.TEMPORARY_OBJ,
                        person, executionSemester);
        createAttendForImprovement(executionSemester);
        return enrolmentEvaluation;
    }

    private void createAttendForImprovement(final ExecutionSemester executionSemester) {
        final Registration registration = getRegistration();

        // FIXME this is completly wrong, there may be more than one execution
        // course for this curricular course
        ExecutionCourse currentExecutionCourse =
                (ExecutionCourse) CollectionUtils.find(getCurricularCourse().getAssociatedExecutionCourses(), new Predicate() {

                    @Override
                    final public boolean evaluate(Object arg0) {
                        ExecutionCourse executionCourse = (ExecutionCourse) arg0;
                        if (executionCourse.getExecutionPeriod().equals(executionSemester)
                                && executionCourse.getEntryPhase().equals(EntryPhase.FIRST_PHASE)) {
                            return true;
                        }
                        return false;
                    }

                });

        if (currentExecutionCourse != null) {
            Collection attends = currentExecutionCourse.getAttends();
            Attends attend = (Attends) CollectionUtils.find(attends, new Predicate() {

                @Override
                public boolean evaluate(Object arg0) {
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

    final public void unEnrollImprovement(final ExecutionSemester executionSemester) throws DomainException {
        final EnrolmentEvaluation temporaryImprovement = getImprovementEvaluation();
        if (temporaryImprovement == null) {
            throw new DomainException("error.enrolment.cant.unenroll.improvement");
        }

        temporaryImprovement.delete();
        Attends attends = getAttendsFor(executionSemester);
        if (attends != null) {
            attends.delete();
        }
    }

    final public boolean isImprovementForExecutionCourse(ExecutionCourse executionCourse) {
        return getCurricularCourse().getAssociatedExecutionCoursesSet().contains(executionCourse)
                && getExecutionPeriod() != executionCourse.getExecutionPeriod();
    }

    final public boolean isImprovingInExecutionPeriodFollowingApproval(final ExecutionSemester improvementExecutionPeriod) {
        final DegreeModule degreeModule = getDegreeModule();
        if (hasImprovement() || !isApproved() || !degreeModule.hasAnyParentContexts(improvementExecutionPeriod)) {
            throw new DomainException("Enrolment.is.not.in.improvement.conditions");
        }

        final ExecutionSemester enrolmentExecutionPeriod = getExecutionPeriod();
        if (improvementExecutionPeriod.isBeforeOrEquals(enrolmentExecutionPeriod)) {
            throw new DomainException("Enrolment.cannot.improve.enrolment.prior.to.its.execution.period");
        }

        ExecutionSemester enrolmentNextExecutionPeriod = enrolmentExecutionPeriod.getNextExecutionPeriod();
        if (improvementExecutionPeriod == enrolmentNextExecutionPeriod) {
            return true;
        }

        for (ExecutionSemester executionSemester = enrolmentNextExecutionPeriod; executionSemester != null
                && executionSemester != improvementExecutionPeriod; executionSemester =
                executionSemester.getNextExecutionPeriod()) {
            if (degreeModule.hasAnyParentContexts(executionSemester)) {
                return false;
            }
        }

        return true;
    }

    final public EnrolmentEvaluation createSpecialSeasonEvaluation(final Person person) {
        if (getEnrolmentEvaluationType() != EnrolmentEvaluationType.SPECIAL_SEASON && !isApproved()) {
            setEnrolmentEvaluationType(EnrolmentEvaluationType.SPECIAL_SEASON);
            setEnrollmentState(EnrollmentState.ENROLLED);

            if (person == null) {
                return new EnrolmentEvaluation(this, EnrolmentEvaluationType.SPECIAL_SEASON,
                        EnrolmentEvaluationState.TEMPORARY_OBJ);
            }

            return new EnrolmentEvaluation(this, EnrolmentEvaluationType.SPECIAL_SEASON, EnrolmentEvaluationState.TEMPORARY_OBJ,
                    person);
        } else {
            throw new DomainException("error.invalid.enrolment.state");
        }
    }

    final public void deleteSpecialSeasonEvaluation() {
        if (getEnrolmentEvaluationType() == EnrolmentEvaluationType.SPECIAL_SEASON && hasSpecialSeason()) {
            setEnrolmentCondition(EnrollmentCondition.FINAL);
            setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
            EnrolmentEvaluation enrolmentEvaluation =
                    getEnrolmentEvaluationByEnrolmentEvaluationStateAndType(EnrolmentEvaluationState.TEMPORARY_OBJ,
                            EnrolmentEvaluationType.SPECIAL_SEASON);
            if (enrolmentEvaluation != null) {
                enrolmentEvaluation.delete();
            }

            EnrolmentEvaluation normalEnrolmentEvaluation =
                    getEnrolmentEvaluationByEnrolmentEvaluationStateAndType(EnrolmentEvaluationState.FINAL_OBJ,
                            EnrolmentEvaluationType.NORMAL);
            if (normalEnrolmentEvaluation != null) {
                setEnrollmentState(normalEnrolmentEvaluation.getEnrollmentStateByGrade());
            }
        } else {
            throw new DomainException("error.invalid.enrolment.state");
        }
    }

    final public List<EnrolmentEvaluation> getAllFinalEnrolmentEvaluations() {
        final List<EnrolmentEvaluation> result = new ArrayList<EnrolmentEvaluation>();

        for (final EnrolmentEvaluation enrolmentEvaluation : getEvaluationsSet()) {
            if (enrolmentEvaluation.isFinal()) {
                result.add(enrolmentEvaluation);
            }
        }

        return result;
    }

    private boolean hasEnrolmentEvaluationByType(final EnrolmentEvaluationType enrolmentEvaluationType) {
        for (final EnrolmentEvaluation enrolmentEvaluation : getEvaluationsSet()) {
            if (enrolmentEvaluation.getEnrolmentEvaluationType().equals(enrolmentEvaluationType)) {
                return true;
            }
        }
        return false;
    }

    final public boolean hasImprovement() {
        return hasEnrolmentEvaluationByType(EnrolmentEvaluationType.IMPROVEMENT);
    }

    final public boolean hasImprovementFor(ExecutionSemester executionSemester) {
        for (EnrolmentEvaluation enrolmentEvaluation : this.getEvaluationsSet()) {
            if (enrolmentEvaluation.isImprovment() && enrolmentEvaluation.hasExecutionPeriod()
                    && enrolmentEvaluation.getExecutionPeriod().equals(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    final public boolean hasSpecialSeason() {
        return hasEnrolmentEvaluationByType(EnrolmentEvaluationType.SPECIAL_SEASON);
    }

    final public boolean hasSpecialSeasonInExecutionYear() {
        for (final Enrolment enrolment : getBrothers()) {
            if (enrolment.getExecutionYear() == getExecutionYear() && enrolment.hasSpecialSeason()) {
                return true;
            }
        }

        return hasSpecialSeason();
    }

    final public boolean isNotEvaluated() {
        final EnrolmentEvaluation latestEnrolmentEvaluation = getLatestEnrolmentEvaluation();
        return latestEnrolmentEvaluation == null || latestEnrolmentEvaluation.isNotEvaluated();
    }

    final public boolean isFlunked() {
        final EnrolmentEvaluation latestEnrolmentEvaluation = getLatestEnrolmentEvaluation();
        return latestEnrolmentEvaluation != null && latestEnrolmentEvaluation.isFlunked();
    }

    @Override
    final public boolean isApproved() {
        if (isAnnulled()) {
            return false;
        }

        final EnrolmentEvaluation latestEnrolmentEvaluation = getLatestEnrolmentEvaluation();
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
    public final ConclusionValue isConcluded(ExecutionYear executionYear) {
        return ConclusionValue.create(isAproved(executionYear));
    }

    @Override
    public YearMonthDay calculateConclusionDate() {
        if (!isApproved()) {
            throw new DomainException("error.Enrolment.not.approved");
        }

        EnrolmentEvaluation exceptImprovements = getLatestEnrolmentEvaluationExceptImprovements();
        if (exceptImprovements == null || exceptImprovements.getExamDateYearMonthDay() == null) {
            return getLatestEnrolmentEvaluation().getExamDateYearMonthDay();
        } else {
            return exceptImprovements.getExamDateYearMonthDay();
        }
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
        final EnrolmentEvaluation enrolmentEvaluation = getLatestEnrolmentEvaluation();
        return enrolmentEvaluation == null ? Grade.createEmptyGrade() : enrolmentEvaluation.getGrade();
    }

    @Override
    final public String getGradeValue() {
        return getGrade().getValue();
    }

    @Override
    public Grade getEctsGrade(StudentCurricularPlan scp, DateTime processingDate) {
        Grade grade = getGrade();
        if (getEnrolmentWrappersSet().size() > 0) {
            Set<Dismissal> dismissals = new HashSet<Dismissal>();
            for (EnrolmentWrapper wrapper : getEnrolmentWrappersSet()) {
                if (wrapper.getCredits().getStudentCurricularPlan().isBolonhaDegree()) {
                    if (!wrapper.getCredits().getStudentCurricularPlan().equals(scp)) {
                        continue;
                    }
                }
                for (Dismissal dismissal : wrapper.getCredits().getDismissalsSet()) {
                    dismissals.add(dismissal);
                }
            }
            if (dismissals.size() == 1) {
                Dismissal dismissal = dismissals.iterator().next();
                if (dismissal instanceof OptionalDismissal || dismissal instanceof CreditsDismissal
                        || dismissal.getCurricularCourse().isOptionalCurricularCourse()) {
                    return EctsTableIndex.convertGradeToEcts(scp.getDegree(), dismissal, grade, processingDate);
                } else {
                    CurricularCourse curricularCourse = dismissal.getCurricularCourse();
                    return EctsTableIndex.convertGradeToEcts(curricularCourse, dismissal, grade, processingDate);
                }
            } else if (dismissals.size() > 1) {
                // if more than one exists we can't base the conversion on the
                // origin, so step up to the degree, on a context based on one
                // of the sources.
                for (Dismissal dismissal : dismissals) {
                    if (dismissal.getParentCycleCurriculumGroup() != null) {
                        return EctsTableIndex.convertGradeToEcts(scp.getDegree(), dismissal, grade, processingDate);
                    }
                }
            }
        }
        return EctsTableIndex.convertGradeToEcts(getCurricularCourse(), this, grade, processingDate);
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
    public BigDecimal getWeigthTimesGrade() {
        return getGrade().isNumeric() ? getWeigthForCurriculum().multiply(getGrade().getNumericValue()) : null;
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
    final public void setDegreeModule(DegreeModule degreeModule) {
        super.setDegreeModule(degreeModule);
        resetIsFirstTimeEnrolment();
    }

    @Override
    final public void setEnrollmentState(EnrollmentState enrollmentState) {
        super.setEnrollmentState(enrollmentState);
        resetIsFirstTimeEnrolment();
    }

    @Override
    final public void setExecutionPeriod(ExecutionSemester executionSemester) {
        super.setExecutionPeriod(executionSemester);
        resetIsFirstTimeEnrolment();
    }

    @Override
    final public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        super.setStudentCurricularPlan(studentCurricularPlan);
        resetIsFirstTimeEnrolment();
    }

    final public int getNumberOfTotalEnrolmentsInThisCourse() {
        return this.getStudentCurricularPlan().countEnrolmentsByCurricularCourse(this.getCurricularCourse());
    }

    final public int getNumberOfTotalEnrolmentsInThisCourse(ExecutionSemester untilExecutionPeriod) {
        return this.getStudentCurricularPlan()
                .countEnrolmentsByCurricularCourse(this.getCurricularCourse(), untilExecutionPeriod);
    }

    @Override
    protected void createCurriculumLineLog(final EnrolmentAction action) {
        new EnrolmentLog(action, getRegistration(), getCurricularCourse(), getExecutionPeriod(), getCurrentUser());
    }

    @Override
    public StringBuilder print(String tabs) {
        final StringBuilder builder = new StringBuilder();
        builder.append(tabs);
        builder.append("[E ").append(getDegreeModule().getName()).append(" ").append(isApproved()).append(" ]\n");
        return builder;
    }

    final public EnrolmentEvaluation addNewEnrolmentEvaluation(EnrolmentEvaluationState enrolmentEvaluationState,
            EnrolmentEvaluationType enrolmentEvaluationType, Person responsibleFor, String gradeValue, Date availableDate,
            Date examDate, ExecutionSemester executionSemester, final GradeScale gradeScale) {

        final Grade grade = Grade.createGrade(gradeValue, gradeScale != null ? gradeScale : getGradeScale());

        final EnrolmentEvaluation enrolmentEvaluation =
                new EnrolmentEvaluation(this, enrolmentEvaluationState, enrolmentEvaluationType, responsibleFor, grade,
                        availableDate, examDate, new DateTime());
        if (enrolmentEvaluationType == EnrolmentEvaluationType.IMPROVEMENT) {
            enrolmentEvaluation.setExecutionPeriod(executionSemester);
        }
        return enrolmentEvaluation;
    }

    final public EnrolmentEvaluation addNewEnrolmentEvaluation(EnrolmentEvaluationState enrolmentEvaluationState,
            EnrolmentEvaluationType enrolmentEvaluationType, CurriculumValidationEvaluationPhase phase, Person responsibleFor,
            String gradeValue, Date availableDate, Date examDate, ExecutionSemester executionSemester, String bookReference,
            String page, GradeScale gradeScale) {

        EnrolmentEvaluation enrolmentEvaluation =
                addNewEnrolmentEvaluation(enrolmentEvaluationState, enrolmentEvaluationType, responsibleFor, gradeValue,
                        availableDate, examDate, executionSemester, gradeScale);
        enrolmentEvaluation.setBookReference(bookReference);
        enrolmentEvaluation.setPage(page);
        enrolmentEvaluation.setContext(EnrolmentEvaluationContext.CURRICULUM_VALIDATION_EVALUATION);
        enrolmentEvaluation.setCurriculumValidationEvaluationPhase(phase);
        enrolmentEvaluation.setGradeScale(gradeScale);

        EnrolmentEvaluationLog.logEnrolmentEvaluationCreation(enrolmentEvaluation);

        return enrolmentEvaluation;
    }

    final public boolean hasAssociatedMarkSheet(MarkSheetType markSheetType) {
        for (final EnrolmentEvaluation enrolmentEvaluation : this.getEvaluationsSet()) {
            if (enrolmentEvaluation.hasMarkSheet()
                    && enrolmentEvaluation.getEnrolmentEvaluationType() == markSheetType.getEnrolmentEvaluationType()) {
                return true;
            }
        }
        return false;
    }

    final public boolean hasAssociatedMarkSheetOrFinalGrade() {
        for (final EnrolmentEvaluation enrolmentEvaluation : getEvaluationsSet()) {
            if (enrolmentEvaluation.hasMarkSheet() || enrolmentEvaluation.isFinal()) {
                return true;
            }
        }
        return false;
    }

    final public boolean hasAssociatedMarkSheetOrFinalGrade(MarkSheetType markSheetType) {
        for (final EnrolmentEvaluation enrolmentEvaluation : this.getEvaluationsSet()) {
            if (enrolmentEvaluation.getEnrolmentEvaluationType() == markSheetType.getEnrolmentEvaluationType()
                    && (enrolmentEvaluation.hasMarkSheet() || enrolmentEvaluation.isFinal())) {
                return true;
            }
        }
        return false;
    }

    final public List<EnrolmentEvaluation> getConfirmedEvaluations(MarkSheetType markSheetType) {
        List<EnrolmentEvaluation> evaluations = new ArrayList<EnrolmentEvaluation>();
        for (EnrolmentEvaluation evaluation : this.getEvaluationsSet()) {
            if (evaluation.hasMarkSheet() && evaluation.getMarkSheet().getMarkSheetType() == markSheetType
                    && evaluation.getMarkSheet().isConfirmed()) {

                evaluations.add(evaluation);
            }
        }
        Collections.sort(evaluations, EnrolmentEvaluation.COMPARATORY_BY_WHEN);
        return evaluations;
    }

    final public Attends getAttendsByExecutionCourse(ExecutionCourse executionCourse) {
        for (final Attends attends : this.getAttendsSet()) {
            if (attends.isFor(executionCourse)) {
                return attends;
            }
        }
        return null;
    }

    final public boolean hasAttendsFor(ExecutionSemester executionSemester) {
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
        for (final Attends attend : getAttends()) {
            if (attend.getExecutionCourse().getExecutionPeriod() == executionSemester) {
                return attend.getExecutionCourse();
            }
        }

        return null;
    }

    @Deprecated
    final public EnrolmentEvaluation getFinalEnrolmentEvaluation() {
        return getLatestEnrolmentEvaluation();
    }

    final public EnrolmentEvaluation getLatestEnrolmentEvaluation() {
        //FIXME: different behaviour based on administrative office type is not acceptable, workflows must converge
        return (getStudentCurricularPlan().getDegreeType().getAdministrativeOfficeType() == AdministrativeOfficeType.DEGREE ? getLatestEnrolmentEvalution(getAllFinalEnrolmentEvaluations()) : getLatestEnrolmentEvalution(getEvaluationsSet()));
    }

    final public EnrolmentEvaluation getLatestEnrolmentEvaluationExceptImprovements() {
        final Collection<EnrolmentEvaluation> toInspect = new HashSet<EnrolmentEvaluation>();

        for (final EnrolmentEvaluation enrolmentEvaluation : getEvaluationsSet()) {
            if (!enrolmentEvaluation.isImprovment()) {
                toInspect.add(enrolmentEvaluation);
            }
        }

        return getLatestEnrolmentEvalution(toInspect);
    }

    @SuppressWarnings("unchecked")
    private EnrolmentEvaluation getLatestEnrolmentEvalution(Collection<EnrolmentEvaluation> enrolmentEvaluations) {
        return ((enrolmentEvaluations == null || enrolmentEvaluations.isEmpty()) ? null : Collections
                .<EnrolmentEvaluation> max(enrolmentEvaluations));
    }

    public EnrolmentEvaluation getLatestEnrolmentEvaluationFromAnyType() {
        return getLatestEnrolmentEvalution(getEvaluationsSet());
    }

    final public EnrolmentEvaluation getLatestEnrolmentEvaluationBy(EnrolmentEvaluationType evaluationType) {
        return getLatestEnrolmentEvalution(getEnrolmentEvaluationsByEnrolmentEvaluationType(evaluationType));
    }

    final public EnrolmentEvaluation getLatestEnrolmentEvaluationByTypeAndPhase(EnrolmentEvaluationType evaluationType,
            CurriculumValidationEvaluationPhase phase) {
        return getLatestEnrolmentEvalution(getEnrolmentEvaluationByEnrolmentEvaluationTypeAndPhase(evaluationType, phase));
    }

    final public EnrolmentEvaluation getLatestFinalNormalEnrolmentEvaluation() {
        return getLatestEnrolmentEvalution(getFinalEnrolmentEvaluationsByEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL));
    }

    final public EnrolmentEvaluation getLatestFinalNormalEnrolmentEvaluationFirstSeason() {
        return getLatestEnrolmentEvalution(getFinalEnrolmentEvaluationsByEnrolmentEvaluationTypeAndPhase(
                EnrolmentEvaluationType.NORMAL, CurriculumValidationEvaluationPhase.FIRST_SEASON));
    }

    final public EnrolmentEvaluation getLatestFinalNormalEnrolmentEvaluationSecondSeason() {
        return getLatestEnrolmentEvalution(getFinalEnrolmentEvaluationsByEnrolmentEvaluationTypeAndPhase(
                EnrolmentEvaluationType.NORMAL, CurriculumValidationEvaluationPhase.SECOND_SEASON));
    }

    final public boolean hasNormalEvaluationSecondSeason() {
        return getLatestEnrolmentEvalution(getFinalEnrolmentEvaluationsByEnrolmentEvaluationTypeAndPhase(
                EnrolmentEvaluationType.NORMAL, CurriculumValidationEvaluationPhase.SECOND_SEASON)) != null;
    }

    final public EnrolmentEvaluation getLatestFinalSpecialSeasonEnrolmentEvaluation() {
        return getLatestEnrolmentEvalution(getFinalEnrolmentEvaluationsByEnrolmentEvaluationType(EnrolmentEvaluationType.SPECIAL_SEASON));
    }

    final public EnrolmentEvaluation getLatestFinalImprovementEnrolmentEvaluation() {
        return getLatestEnrolmentEvalution(getFinalEnrolmentEvaluationsByEnrolmentEvaluationType(EnrolmentEvaluationType.IMPROVEMENT));
    }

    final private List<EnrolmentEvaluation> getFinalEnrolmentEvaluationsByEnrolmentEvaluationType(
            final EnrolmentEvaluationType evaluationType) {
        List<EnrolmentEvaluation> result = new ArrayList<EnrolmentEvaluation>();
        for (EnrolmentEvaluation evaluation : getEvaluationsSet()) {
            if (evaluation.isFinal() && evaluation.getEnrolmentEvaluationType().equals(evaluationType)) {
                result.add(evaluation);
            }
        }
        return result;
    }

    final public List<EnrolmentEvaluation> getFinalEnrolmentEvaluationsByEnrolmentEvaluationTypeAndPhase(
            final EnrolmentEvaluationType evaluationType, CurriculumValidationEvaluationPhase phase) {
        List<EnrolmentEvaluation> result = new ArrayList<EnrolmentEvaluation>();
        for (EnrolmentEvaluation evaluation : getEvaluationsSet()) {
            if (evaluation.isFinal() && evaluation.getEnrolmentEvaluationType().equals(evaluationType)
                    && phase.equals(evaluation.getCurriculumValidationEvaluationPhase())) {
                result.add(evaluation);
            }
        }
        return result;
    }

    final public EnrolmentEvaluation getImprovementEvaluation() {
        final EnrolmentEvaluation latestImprovementEnrolmentEvaluation =
                getLatestEnrolmentEvaluationBy(EnrolmentEvaluationType.IMPROVEMENT);

        if (latestImprovementEnrolmentEvaluation != null
                && latestImprovementEnrolmentEvaluation.getEnrolmentEvaluationState().equals(
                        EnrolmentEvaluationState.TEMPORARY_OBJ)) {
            return latestImprovementEnrolmentEvaluation;
        }

        return null;
    }

    final public EnrolmentEvaluation getLatestEquivalenceEnrolmentEvaluation() {
        return getLatestEnrolmentEvaluationBy(EnrolmentEvaluationType.EQUIVALENCE);
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
        return hasCurriculumGroup() ? getCurriculumGroup().getStudentCurricularPlan() : super.getStudentCurricularPlan();
    }

    @Override
    public boolean isEnroledInExecutionPeriod(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
        return isValid(executionSemester) && this.getCurricularCourse().equals(curricularCourse);
    }

    @Override
    public boolean isValid(final ExecutionSemester executionSemester) {
        return getExecutionPeriod() == executionSemester
                || (getCurricularCourse().isAnual() && getExecutionPeriod().getExecutionYear() == executionSemester
                        .getExecutionYear());
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
        return isEnroledInExecutionPeriod(curricularCourse, executionSemester) && isEnroled();
    }

    final public Collection<ExecutionCourse> getExecutionCourses() {
        return this.getCurricularCourse().getAssociatedExecutionCourses();
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

    final public static class DeleteEnrolmentExecutor implements FactoryExecutor {

        private final Enrolment enrolment;

        public DeleteEnrolmentExecutor(Enrolment enrolment) {
            super();
            this.enrolment = enrolment;
        }

        @Override
        public Object execute() {
            enrolment.delete();
            return null;
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

    static final public BigDecimal LEIC_WEIGHT_BEFORE_0607_EXCEPT_TFC = BigDecimal.valueOf(4.0d);

    static final BigDecimal LMAC_AND_LCI_WEIGHT_FACTOR = BigDecimal.valueOf(0.25d);

    static final BigDecimal LMAC_WEIGHT_BEFORE_0607_EXCEPT_LMAC_AND_LCI_DEGREE_MODULES = BigDecimal.valueOf(7.5d);

    @Override
    final public Double getWeigth() {
        return isExtraCurricular() || isPropaedeutic() ? Double.valueOf(0) : getWeigthForCurriculum().doubleValue();
    }

    @Override
    final public BigDecimal getWeigthForCurriculum() {
        if (!isBolonhaDegree()) {

            final DegreeCurricularPlan dcpOfStudent = getDegreeCurricularPlanOfStudent();
            if (dcpOfStudent.getDegreeType().isDegree()) {

                final Degree leicPb = Degree.readBySigla("LEIC-pB");
                if (isStudentFromDegree(leicPb, dcpOfStudent)) {
                    return getBaseWeigth();
                }

                if (isExecutionYearEnrolmentAfterOrEqualsExecutionYear0607()) {
                    return getEctsCreditsForCurriculum();
                }

                final Degree lmacPb = Degree.readBySigla("LMAC-pB");
                final DegreeCurricularPlan dcpOfDegreeModule = getDegreeCurricularPlanOfDegreeModule();
                if (isDegreeModuleFromDegree(lmacPb, dcpOfDegreeModule)) {
                    return getBaseWeigth().multiply(LMAC_AND_LCI_WEIGHT_FACTOR);
                }

                final Degree lciPb = Degree.readBySigla("LCI-pB");
                if (isDegreeModuleFromDegree(lciPb, dcpOfDegreeModule)) {
                    return getBaseWeigth().multiply(LMAC_AND_LCI_WEIGHT_FACTOR);
                }

                if (isStudentFromDegree(lmacPb, dcpOfStudent)) {
                    return LMAC_WEIGHT_BEFORE_0607_EXCEPT_LMAC_AND_LCI_DEGREE_MODULES;
                }

            }
        }

        return getBaseWeigth();
    }

    private BigDecimal getBaseWeigth() {
        final Double d;
        if (super.getWeigth() == null || super.getWeigth() == 0d) {
            final CurricularCourse curricularCourse = getCurricularCourse();
            d = curricularCourse == null ? null : curricularCourse.getWeigth();
        } else {
            d = super.getWeigth();
        }
        return d == null ? BigDecimal.ZERO : BigDecimal.valueOf(d);
    }

    private boolean isExecutionYearEnrolmentAfterOrEqualsExecutionYear0607() {
        final ExecutionYear executionYear = getExecutionPeriod().getExecutionYear();
        final ExecutionYear executionYear0607 = ExecutionYear.readExecutionYearByName("2006/2007");
        return executionYear.isAfterOrEquals(executionYear0607);
    }

    private boolean isStudentFromDegree(final Degree degree, final DegreeCurricularPlan degreeCurricularPlanOfStudent) {
        return degree.getDegreeCurricularPlansSet().contains(degreeCurricularPlanOfStudent);
    }

    private boolean isDegreeModuleFromDegree(final Degree degree, DegreeCurricularPlan degreeCurricularPlanOfDegreeModule) {
        return degree.getDegreeCurricularPlansSet().contains(degreeCurricularPlanOfDegreeModule);
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
        return isExtraCurricular() || isPropaedeutic() ? Double.valueOf(0d) : getEctsCreditsForCurriculum().doubleValue();
    }

    @Override
    final public BigDecimal getEctsCreditsForCurriculum() {
        return BigDecimal.valueOf(getCurricularCourse().getEctsCredits(getExecutionPeriod()));
    }

    @Override
    final public Double getAprovedEctsCredits() {
        return isApproved() ? getEctsCredits() : Double.valueOf(0d);
    }

    @Override
    final public Double getCreditsConcluded(ExecutionYear executionYear) {
        return executionYear == null || getExecutionYear().isBeforeOrEquals(executionYear) ? getAprovedEctsCredits() : 0d;
    }

    @Override
    final public Double getEnroledEctsCredits(final ExecutionSemester executionSemester) {
        return isValid(executionSemester) && isEnroled() ? getEctsCredits() : Double.valueOf(0d);
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

    final public boolean isImprovementEnroled() {
        return isEnrolmentStateApproved() && getImprovementEvaluation() != null;
    }

    final public boolean canBeImproved() {
        return isEnrolmentStateApproved() && !hasImprovement();
    }

    final public boolean isSpecialSeasonEnroled(final ExecutionYear executionYear) {
        return isSpecialSeason() && getExecutionPeriod().getExecutionYear() == executionYear
                && getTempSpecialSeasonEvaluation() != null;
    }

    final public boolean isSpecialSeasonEnroled(ExecutionSemester executionSemester) {
        return isSpecialSeason() && isValid(executionSemester) && getTempSpecialSeasonEvaluation() != null;
    }

    private EnrolmentEvaluation getTempSpecialSeasonEvaluation() {
        final EnrolmentEvaluation latestSpecialSeasonEnrolmentEvaluation =
                getLatestEnrolmentEvaluationBy(EnrolmentEvaluationType.SPECIAL_SEASON);

        if (latestSpecialSeasonEnrolmentEvaluation != null
                && latestSpecialSeasonEnrolmentEvaluation.getEnrolmentEvaluationState().equals(
                        EnrolmentEvaluationState.TEMPORARY_OBJ)) {
            return latestSpecialSeasonEnrolmentEvaluation;
        }

        return null;
    }

    final public boolean canBeSpecialSeasonEnroled(ExecutionYear executionYear) {
        return getEnrolmentEvaluationType() != EnrolmentEvaluationType.SPECIAL_SEASON
                && getExecutionPeriod().getExecutionYear() == executionYear && !isApproved();
    }

    final public boolean canBeSpecialSeasonEnroled(ExecutionSemester executionSemester) {
        return getEnrolmentEvaluationType() != EnrolmentEvaluationType.SPECIAL_SEASON
                && getExecutionPeriod() == executionSemester && !isApproved();
    }

    @Override
    final public Collection<Enrolment> getSpecialSeasonEnrolments(final ExecutionYear executionYear) {
        if (isSpecialSeason() && getExecutionPeriod().getExecutionYear().equals(executionYear)) {
            return Collections.singleton(this);
        }
        return Collections.emptySet();
    }

    @Override
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

    /**
     * {@inheritDoc}
     * 
     * <p>
     * This method assumes that each Student has at most one non evaluated Thesis and no more that two Thesis.
     */
    @Override
    final public Thesis getThesis() {
        Collection<Thesis> theses = getTheses();

        switch (theses.size()) {
        case 0:
            return null;
        case 1:
            return theses.iterator().next();
        default:
            SortedSet<Thesis> sortedTheses = new TreeSet<Thesis>(new Comparator<Thesis>() {
                @Override
                public int compare(Thesis o1, Thesis o2) {
                    return o2.getCreation().compareTo(o1.getCreation());
                }
            });

            sortedTheses.addAll(theses);
            return sortedTheses.iterator().next();
        }
    }

    final public boolean isBefore(final Enrolment enrolment) {
        return getExecutionPeriod().isBefore(enrolment.getExecutionPeriod());
    }

    final public Proposal getDissertationProposal() {
        final Registration registration = getRegistration();
        final Proposal proposal = getDissertationProposal(registration, getExecutionYear());
        if (proposal != null) {
            return proposal;
        }
        final Student student = registration.getStudent();
        for (final Registration otherRegistration : student.getRegistrationsSet()) {
            final Proposal otherProposal = getDissertationProposal(otherRegistration, getExecutionYear());
            if (otherProposal != null) {
                return otherProposal;
            }
        }
        return null;
    }

    public static Proposal getDissertationProposal(final Registration registration, final ExecutionYear executionYear) {
        final Proposal proposal = registration.getDissertationProposal(executionYear);
        if (proposal != null) {
            return proposal;
        }
        final ExecutionYear previousExecutionYear = executionYear.getPreviousExecutionYear();
        return previousExecutionYear == null ? null : getDissertationProposal(registration, previousExecutionYear);
    }

    public Thesis getPreviousYearThesis() {
        ExecutionYear executionYear = getExecutionYear().getPreviousExecutionYear();
        Enrolment enrolment = getStudent().getDissertationEnrolment(null, executionYear);
        if (enrolment != null && enrolment.getThesis() != null) {
            return enrolment.getThesis();
        }
        return null;
    }

    public Thesis getPossibleThesis() {
        Thesis thesis = getThesis();
        return (thesis == null /*&& getDissertationProposal() == null */) ? getPreviousYearThesis() : thesis;
    }

    //
    public MultiLanguageString getPossibleDissertationTitle() {
        Thesis thesis = getThesis();
        if (thesis == null) {
            if (getDissertationProposal() == null) {
                thesis = getPreviousYearThesis();
            } else {
                return new MultiLanguageString(getDissertationProposal().getTitle());
            }
        }
        return thesis == null ? new MultiLanguageString("-") : thesis.getTitle();
    }

    @Override
    final public Unit getAcademicUnit() {
        return Bennu.getInstance().getInstitutionUnit();
    }

    @Override
    final public String getCode() {
        if (hasDegreeModule()) {
            return getDegreeModule().getCode();
        }
        return null;
    }

    public boolean hasAnyAssociatedMarkSheetOrFinalGrade() {
        for (final EnrolmentEvaluation enrolmentEvaluation : getEvaluationsSet()) {
            if (enrolmentEvaluation.hasMarkSheet() || enrolmentEvaluation.isFinal()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasEnrolment(ExecutionSemester executionSemester) {
        return isValid(executionSemester);
    }

    @Override
    public boolean hasEnrolment(ExecutionYear executionYear) {
        return isValid(executionYear);
    }

    @Override
    public boolean isEnroledInSpecialSeason(final ExecutionSemester executionSemester) {
        return isValid(executionSemester) && hasSpecialSeason();
    }

    @Override
    public boolean isEnroledInSpecialSeason(ExecutionYear executionYear) {
        return isValid(executionYear) && hasSpecialSeason();
    }

    @Override
    public int getNumberOfAllApprovedEnrolments(ExecutionSemester executionSemester) {
        return isValid(executionSemester) && isApproved() ? 1 : 0;
    }

    public boolean canBeSubmittedForOldMarkSheet(EnrolmentEvaluationType enrolmentEvaluationType) {
        if (enrolmentEvaluationType == EnrolmentEvaluationType.NORMAL && !hasAnyEvaluations()) {
            return true;
        }

        for (EnrolmentEvaluation enrolmentEvaluation : getEvaluations()) {
            if (enrolmentEvaluation.getEnrolmentEvaluationType() == enrolmentEvaluationType
                    && !enrolmentEvaluation.hasMarkSheet()
                    && (enrolmentEvaluation.isTemporary() || (enrolmentEvaluation.isNotEvaluated() && enrolmentEvaluation
                            .getExamDateYearMonthDay() == null))) {
                return true;
            }
        }

        return false;
    }

    public boolean isSourceOfAnyCreditsInCurriculum() {
        for (final InternalEnrolmentWrapper enrolmentWrapper : getEnrolmentWrappers()) {
            if (enrolmentWrapper.getCredits().hasAnyDismissalInCurriculum()) {
                return true;
            }
        }
        return false;
    }

    static public Enrolment getEnrolmentWithLastExecutionPeriod(List<Enrolment> enrolments) {
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
        enrolment.setEnrolmentEvaluationType(optionalEnrolment.getEnrolmentEvaluationType());
        enrolment.setCreatedBy(Authenticate.getUser().getUsername());
        enrolment.setCreationDateDateTime(optionalEnrolment.getCreationDateDateTime());
        enrolment.setEnrolmentCondition(optionalEnrolment.getEnrolmentCondition());
        enrolment.setCurriculumGroup(curriculumGroup);

        enrolment.getEvaluations().addAll(optionalEnrolment.getEvaluations());
        enrolment.getProgramCertificateRequests().addAll(optionalEnrolment.getProgramCertificateRequests());
        enrolment.getCourseLoadRequests().addAll(optionalEnrolment.getCourseLoadRequests());
        enrolment.getExtraExamRequests().addAll(optionalEnrolment.getExtraExamRequests());
        enrolment.getEnrolmentWrappers().addAll(optionalEnrolment.getEnrolmentWrappers());
        enrolment.getTheses().addAll(optionalEnrolment.getTheses());
        enrolment.getExamDateCertificateRequests().addAll(optionalEnrolment.getExamDateCertificateRequests());
        changeAttends(optionalEnrolment, enrolment);
        enrolment.createCurriculumLineLog(EnrolmentAction.ENROL);

        return enrolment;
    }

    @Override
    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
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
            for (final Attends attend : from.getAttends()) {
                oldRegistration.changeShifts(attend, newRegistration);
                attend.setRegistration(newRegistration);
            }
        }
        to.getAttends().addAll(from.getAttends());
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

    void changeStateIfAprovedAndEvaluationsIsEmpty() {
        if (!getStudentCurricularPlan().getEvaluationForCurriculumValidationAllowed()) {
            throw new DomainException("error.curriculum.validation.enrolment.evaluatiom.removal.not.allowed");
        }

        if (getEnrollmentState().equals(EnrollmentState.APROVED) && !hasAnyEvaluations()) {
            setEnrollmentState(EnrollmentState.ENROLLED);
        }
    }

    @Atomic
    public void markAsTemporaryEnrolled() {
        if (!getStudentCurricularPlan().getEvaluationForCurriculumValidationAllowed()) {
            throw new DomainException("error.curriculum.validation.enrolment.evaluatiom.removal.not.allowed");
        }

        if (!hasAnyEvaluations()) {
            setEnrollmentState(EnrollmentState.ENROLLED);
        }
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

    @ConsistencyPredicate
    public boolean checkThesisMultiplicity() {
        return this.getThesesSet().size() <= 2;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.StandaloneEnrolmentCertificateRequest> getStandaloneEnrolmentRequests() {
        return getStandaloneEnrolmentRequestsSet();
    }

    @Deprecated
    public boolean hasAnyStandaloneEnrolmentRequests() {
        return !getStandaloneEnrolmentRequestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.thesis.Thesis> getTheses() {
        return getThesesSet();
    }

    @Deprecated
    public boolean hasAnyTheses() {
        return !getThesesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.ExtraExamRequest> getExtraExamRequests() {
        return getExtraExamRequestsSet();
    }

    @Deprecated
    public boolean hasAnyExtraExamRequests() {
        return !getExtraExamRequestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExtraCurricularCertificateRequest> getExtraCurricularRequests() {
        return getExtraCurricularRequestsSet();
    }

    @Deprecated
    public boolean hasAnyExtraCurricularRequests() {
        return !getExtraCurricularRequestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ProgramCertificateRequest> getProgramCertificateRequests() {
        return getProgramCertificateRequestsSet();
    }

    @Deprecated
    public boolean hasAnyProgramCertificateRequests() {
        return !getProgramCertificateRequestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Attends> getAttends() {
        return getAttendsSet();
    }

    @Deprecated
    public boolean hasAnyAttends() {
        return !getAttendsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.studentCurriculum.InternalEnrolmentWrapper> getEnrolmentWrappers() {
        return getEnrolmentWrappersSet();
    }

    @Deprecated
    public boolean hasAnyEnrolmentWrappers() {
        return !getEnrolmentWrappersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse> getNotNeedToEnrollCurricularCourses() {
        return getNotNeedToEnrollCurricularCoursesSet();
    }

    @Deprecated
    public boolean hasAnyNotNeedToEnrollCurricularCourses() {
        return !getNotNeedToEnrollCurricularCoursesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.curriculum.ConclusionProcessVersion> getConclusionProcessVersions() {
        return getConclusionProcessVersionsSet();
    }

    @Deprecated
    public boolean hasAnyConclusionProcessVersions() {
        return !getConclusionProcessVersionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CreditsInAnySecundaryArea> getCreditsInAnySecundaryAreas() {
        return getCreditsInAnySecundaryAreasSet();
    }

    @Deprecated
    public boolean hasAnyCreditsInAnySecundaryAreas() {
        return !getCreditsInAnySecundaryAreasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EnrolmentEvaluation> getEvaluations() {
        return getEvaluationsSet();
    }

    @Deprecated
    public boolean hasAnyEvaluations() {
        return !getEvaluationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CourseLoadRequest> getCourseLoadRequests() {
        return getCourseLoadRequestsSet();
    }

    @Deprecated
    public boolean hasAnyCourseLoadRequests() {
        return !getCourseLoadRequestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CreditsInScientificArea> getCreditsInScientificAreas() {
        return getCreditsInScientificAreasSet();
    }

    @Deprecated
    public boolean hasAnyCreditsInScientificAreas() {
        return !getCreditsInScientificAreasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExamDateCertificateRequest> getExamDateCertificateRequests() {
        return getExamDateCertificateRequestsSet();
    }

    @Deprecated
    public boolean hasAnyExamDateCertificateRequests() {
        return !getExamDateCertificateRequestsSet().isEmpty();
    }

    @Deprecated
    public boolean hasIsFirstTime() {
        return getIsFirstTime() != null;
    }

    @Deprecated
    public boolean hasEnrollmentState() {
        return getEnrollmentState() != null;
    }

    @Deprecated
    public boolean hasIsExtraCurricular() {
        return getIsExtraCurricular() != null;
    }

    @Deprecated
    public boolean hasWeigth() {
        return getWeigth() != null;
    }

    @Override
    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasStudentCurricularPlan() {
        return getStudentCurricularPlan() != null;
    }

    @Deprecated
    public boolean hasEnrolmentEvaluationType() {
        return getEnrolmentEvaluationType() != null;
    }

    @Deprecated
    public boolean hasEnrolmentCondition() {
        return getEnrolmentCondition() != null;
    }

}
