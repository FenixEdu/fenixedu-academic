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

import org.fenixedu.academic.domain.accounting.events.gratuity.EnrolmentGratuityEvent;
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
import org.fenixedu.academic.domain.student.curriculum.Curriculum;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.InternalCreditsSourceCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.InternalEnrolmentWrapper;
import org.fenixedu.academic.domain.thesis.Thesis;
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
import pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate;

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
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

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
        Signal.emit(Enrolment.SIGNAL_CREATED, new DomainObjectEvent<Enrolment>(this));
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
    public void setIsExtraCurricular(Boolean isExtraCurricular) {
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

    final public boolean isSpecialSeason() {
        return hasSpecialSeason();
    }

    final public boolean isExtraordinarySeason() {
        return hasExtraordinarySeason();
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
        setEvaluationSeason(EvaluationConfiguration.getInstance().getDefaultEvaluationSeason());
        setCreatedBy(createdBy);
        setCreationDateDateTime(new DateTime());
        setEnrolmentCondition(enrolmentCondition);
        createAttend(studentCurricularPlan.getRegistration(), curricularCourse, executionSemester);
        super.setIsExtraCurricular(Boolean.FALSE);
        final RegistrationDataByExecutionYear dataByExecutionYear = studentCurricularPlan.getRegistration().getRegistrationDataByExecutionYearSet().stream()
                .filter(data -> data.getExecutionYear() == executionSemester.getExecutionYear())
                .findAny().orElse(null);
        if (dataByExecutionYear != null) {
            dataByExecutionYear.checkEnrolmentsConformToSettings();
        }
    }

    protected void initializeAsNewWithoutEnrolmentEvaluation(StudentCurricularPlan studentCurricularPlan,
            CurricularCourse curricularCourse, ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition,
            String createdBy) {
        initializeCommon(studentCurricularPlan, curricularCourse, executionSemester, enrolmentCondition, createdBy);
        setStudentCurricularPlan(studentCurricularPlan);
    }

    @Override
    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        checkRulesToDelete();
        createCurriculumLineLog(EnrolmentAction.UNENROL);
        deleteInformation();
        setEvaluationSeason(null);
        if (getEvent() != null) {
            setEvent(null);
        }
        super.delete();
    }

    protected void deleteInformation() {

        final Iterator<Thesis> theses = getThesesSet().iterator();
        while (theses.hasNext()) {
            final Thesis thesis = theses.next();
            if (!thesis.isDeletable()) {
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
        final Collection<Enrolment> result = new HashSet<Enrolment>();

        result.addAll(getStudentCurricularPlan().getEnrolments(getCurricularCourse()));
        result.remove(this);

        return result;
    }

    final public Optional<EnrolmentEvaluation> getEnrolmentEvaluationBySeasonAndState(final EnrolmentEvaluationState state,
            final EvaluationSeason season) {
        return getEnrolmentEvaluationBySeason(season).filter(e -> e.getEnrolmentEvaluationState().equals(state)).findAny();
    }

    final public Stream<EnrolmentEvaluation> getEnrolmentEvaluationBySeason(EvaluationSeason season) {
        return getEvaluationsSet().stream().filter(e -> e.getEvaluationSeason().equals(season));
    }

    protected void createEnrolmentEvaluationWithoutGrade() {
        boolean existing =
                getEnrolmentEvaluationBySeason(EvaluationConfiguration.getInstance().getDefaultEvaluationSeason())
                        .filter(e -> e.getGrade().equals(null)).findAny().isPresent();
        if (!existing) {
            EnrolmentEvaluation evaluation =
                    new EnrolmentEvaluation(this, EvaluationConfiguration.getInstance().getDefaultEvaluationSeason(),
                            EnrolmentEvaluationState.TEMPORARY_OBJ);
            evaluation.setWhenDateTime(new DateTime());
            addEvaluations(evaluation);
        }
    }

    private void createAttend(Registration registration, CurricularCourse curricularCourse, ExecutionSemester executionSemester) {

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

    final public EnrolmentEvaluation createEnrolmentEvaluationForImprovement(final Person person,
            final ExecutionSemester executionSemester) {
        final EnrolmentEvaluation enrolmentEvaluation =
                new EnrolmentEvaluation(this, EvaluationSeason.readImprovementSeason(), EnrolmentEvaluationState.TEMPORARY_OBJ,
                        person, executionSemester);
        createAttendForImprovement(executionSemester);
        return enrolmentEvaluation;
    }

    private void createAttendForImprovement(final ExecutionSemester executionSemester) {
        final Registration registration = getRegistration();

        // FIXME this is completly wrong, there may be more than one execution
        // course for this curricular course

        ExecutionCourse currentExecutionCourse = getCurricularCourse().getAssociatedExecutionCoursesSet().stream()
                .filter(executionCourse -> executionCourse.getExecutionPeriod() == executionSemester)
                .filter(executionCourse -> executionCourse.getEntryPhase() == EntryPhase.FIRST_PHASE)
                .findFirst().orElse(null);

        if (currentExecutionCourse != null) {
            Attends attend = currentExecutionCourse.getAttendsSet().stream()
                    .filter(attend1 -> attend1.getRegistration().equals(registration))
                    .findFirst().orElseGet(() -> getStudent().getAttends(currentExecutionCourse));

            if (attend != null) {
                attend.setRegistration(registration);
            }
            else {
                attend = new Attends(registration, currentExecutionCourse);
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
        //the improvement can be in the same execution semester and in that case we don't want to delete the attends, only when there is an improvement in another semester and consequently another attends
        if (attends != null && getExecutionPeriod() != executionSemester) {
            attends.delete();
        }
    }

    final public boolean isImprovementForExecutionCourse(ExecutionCourse executionCourse) {
        return getExecutionPeriod() != executionCourse.getExecutionPeriod()
        		&& getCurricularCourse().getAssociatedExecutionCoursesSet().contains(executionCourse);
    }

    final public boolean isImprovingInExecutionPeriodFollowingApproval(final ExecutionSemester improvementExecutionPeriod) {
        final DegreeModule degreeModule = getDegreeModule();
        if (hasImprovement() || !isApproved() || !degreeModule.hasAnyParentContexts(improvementExecutionPeriod)) {
            throw new DomainException("Enrolment.is.not.in.improvement.conditions");
        }

        final ExecutionSemester enrolmentExecutionPeriod = getExecutionPeriod();
        if (improvementExecutionPeriod.isBefore(enrolmentExecutionPeriod)) {
            throw new DomainException("Enrolment.cannot.improve.enrolment.prior.to.its.execution.period");
        }

        ExecutionSemester enrolmentNextExecutionPeriod = enrolmentExecutionPeriod.getNextExecutionPeriod();
        if (improvementExecutionPeriod == enrolmentExecutionPeriod || improvementExecutionPeriod == enrolmentNextExecutionPeriod) {
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
        if (!getEvaluationSeason().isSpecial() && !isApproved()) {
            setEvaluationSeason(EvaluationSeason.readSpecialSeason());
            setEnrollmentState(EnrollmentState.ENROLLED);

            if (person == null) {
                return new EnrolmentEvaluation(this, EvaluationSeason.readSpecialSeason(), EnrolmentEvaluationState.TEMPORARY_OBJ);
            }

            return new EnrolmentEvaluation(this, EvaluationSeason.readSpecialSeason(), EnrolmentEvaluationState.TEMPORARY_OBJ,
                    person);
        } else {
            throw new DomainException("error.invalid.enrolment.state");
        }
    }

    final public void deleteSpecialSeasonEvaluation() {
        if (getEvaluationSeason().isSpecial() && hasSpecialSeason()) {
            setEnrolmentCondition(EnrollmentCondition.FINAL);
            setEvaluationSeason(EvaluationConfiguration.getInstance().getDefaultEvaluationSeason());
            getEnrolmentEvaluationBySeasonAndState(EnrolmentEvaluationState.TEMPORARY_OBJ, EvaluationSeason.readSpecialSeason())
                    .ifPresent(
                            ee -> {
                                if (ee.getSpecialSeasonEnrolmentEvent() != null) {
                                    ee.getSpecialSeasonEnrolmentEvent()
                                            .cancel(Authenticate.getUser().getPerson(),
                                                    BundleUtil.getString(Bundle.ACADEMIC,
                                                            "enrolmentEvaluation.cancel.event.disenrolled"));
                                    ee.setEnrolmentEvaluationState(EnrolmentEvaluationState.ANNULED_OBJ);
                                } else {
                                    ee.delete();
                                }
                            });

            EnrolmentEvaluation finalEnrolmentEvaluation = getFinalEnrolmentEvaluation();
            if (finalEnrolmentEvaluation != null) {                
                setEnrollmentState(finalEnrolmentEvaluation.getEnrollmentStateByGrade());
            }
        } else {
            throw new DomainException("error.invalid.enrolment.state");
        }
    }

    final public EnrolmentEvaluation createExtraordinarySeasonEvaluation(final Person person) {
        if (!getEvaluationSeason().isExtraordinary() && !isApproved()) {
            setEvaluationSeason(EvaluationSeason.readExtraordinarySeason());
            setEnrollmentState(EnrollmentState.ENROLLED);

            if (person == null) {
                return new EnrolmentEvaluation(this, EvaluationSeason.readExtraordinarySeason(), EnrolmentEvaluationState.TEMPORARY_OBJ);
            }

            return new EnrolmentEvaluation(this, EvaluationSeason.readExtraordinarySeason(), EnrolmentEvaluationState.TEMPORARY_OBJ,
                    person);
        } else {
            throw new DomainException("error.invalid.enrolment.state");
        }
    }

    final public void deleteExtraordinarySeasonEvaluation() {
        if (getEvaluationSeason().isExtraordinary() && hasExtraordinarySeason()) {
            setEnrolmentCondition(EnrollmentCondition.FINAL);
            setEvaluationSeason(EvaluationConfiguration.getInstance().getDefaultEvaluationSeason());
            getEnrolmentEvaluationBySeasonAndState(EnrolmentEvaluationState.TEMPORARY_OBJ, EvaluationSeason.readExtraordinarySeason())
                    .ifPresent(
                            ee -> {
                                /* TODO is this the right method to use? */
                                if (ee.getEnrolmentEvaluationEvent() != null) {
                                    ee.getEnrolmentEvaluationEvent()
                                            .cancel(Authenticate.getUser().getPerson(),
                                                    BundleUtil.getString(Bundle.ACADEMIC,
                                                            "enrolmentEvaluation.cancel.event.disenrolled"));
                                    ee.setEnrolmentEvaluationState(EnrolmentEvaluationState.ANNULED_OBJ);
                                } else {
                                    ee.delete();
                                }
                            });

            EnrolmentEvaluation finalEnrolmentEvaluation = getFinalEnrolmentEvaluation();
            if (finalEnrolmentEvaluation != null) {
                setEnrollmentState(finalEnrolmentEvaluation.getEnrollmentStateByGrade());
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

    final public boolean hasImprovement() {
        return getEnrolmentEvaluationBySeason(EvaluationSeason.readImprovementSeason()).findAny().isPresent();
    }

    final public boolean hasImprovementFor(ExecutionSemester executionSemester) {
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
        return getEnrolmentEvaluationBySeason(EvaluationSeason.readSpecialSeason()).anyMatch(
                ee -> !ee.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.ANNULED_OBJ));
    }

    final public boolean hasExtraordinarySeason() {
        return getEnrolmentEvaluationBySeason(EvaluationSeason.readExtraordinarySeason()).anyMatch(
                ee -> !ee.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.ANNULED_OBJ));
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
    public final ConclusionValue isConcluded(ExecutionYear executionYear) {
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

    private Grade calculateNormalizedEctsGrade(final StudentCurricularPlan scp, final DateTime processingDate) {
        return calculateEctsConversionTable(scp, processingDate).convert(getGrade());
    }

    private EctsConversionTable calculateEctsConversionTable(StudentCurricularPlan scp, DateTime processingDate) {
        return EctsTableIndex.getEctsConversionTable(getCurricularCourse(), this, processingDate);
    }

    @Override
    public Grade getEctsGrade(final StudentCurricularPlan scp, final DateTime processingDate) {
        final EnrolmentEvaluation enrolmentEvaluation = getFinalEnrolmentEvaluation();
        final Grade normalizedEctsGrade = enrolmentEvaluation == null ? null : enrolmentEvaluation.getNormalizedEctsGrade();
        return normalizedEctsGrade == null ? calculateNormalizedEctsGrade(scp, processingDate) : normalizedEctsGrade;
    }

    public void setEctsGrade(Grade grade) {
        final EnrolmentEvaluation enrolmentEvaluation = getFinalEnrolmentEvaluation();
        if (enrolmentEvaluation != null) {
            enrolmentEvaluation.setNormalizedEctsGrade(grade);
        }
    }

    public void setEctsConversionTable(EctsConversionTable table) {
        final EnrolmentEvaluation enrolmentEvaluation = getFinalEnrolmentEvaluation();
        if (enrolmentEvaluation != null) {
            enrolmentEvaluation.setEctsConversionTable(table);
        }
    }

    @Override
    public EctsConversionTable getEctsConversionTable(final StudentCurricularPlan scp, final DateTime processingDate) {
        final EnrolmentEvaluation enrolmentEvaluation = getFinalEnrolmentEvaluation();
        final EctsConversionTable ectsConversionTable = enrolmentEvaluation == null ? null : enrolmentEvaluation.getEctsConversionTable();
        return ectsConversionTable == null ? calculateEctsConversionTable(scp, processingDate) : ectsConversionTable;
    }

    @Override
    final public String getGradeValue() {
        return getGrade().getValue();
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
            EvaluationSeason evaluationSeason, Person responsibleFor, String gradeValue, Date availableDate, Date examDate,
            ExecutionSemester executionSemester, final GradeScale gradeScale) {

        final Grade grade = Grade.createGrade(gradeValue, gradeScale != null ? gradeScale : getGradeScale());

        final EnrolmentEvaluation enrolmentEvaluation =
                new EnrolmentEvaluation(this, enrolmentEvaluationState, evaluationSeason, responsibleFor, grade, availableDate,
                        examDate, new DateTime());
        if (evaluationSeason.isImprovement()) {
            enrolmentEvaluation.setExecutionPeriod(executionSemester);
        }
        return enrolmentEvaluation;
    }

    final public EnrolmentEvaluation addNewEnrolmentEvaluation(EnrolmentEvaluationState enrolmentEvaluationState,
            EvaluationSeason evaluationSeason, Person responsibleFor, String gradeValue, Date availableDate, Date examDate,
            ExecutionSemester executionSemester, String bookReference, String page, GradeScale gradeScale) {

        EnrolmentEvaluation enrolmentEvaluation =
                addNewEnrolmentEvaluation(enrolmentEvaluationState, evaluationSeason, responsibleFor, gradeValue, availableDate,
                        examDate, executionSemester, gradeScale);
        enrolmentEvaluation.setBookReference(bookReference);
        enrolmentEvaluation.setPage(page);
        enrolmentEvaluation.setContext(EnrolmentEvaluationContext.CURRICULUM_VALIDATION_EVALUATION);
        enrolmentEvaluation.setGradeScale(gradeScale);

        EnrolmentEvaluationLog.logEnrolmentEvaluationCreation(enrolmentEvaluation);

        return enrolmentEvaluation;
    }

    final public boolean hasAssociatedMarkSheet(EvaluationSeason season) {
        for (final EnrolmentEvaluation enrolmentEvaluation : this.getEvaluationsSet()) {
            if (enrolmentEvaluation.getMarkSheet() != null && enrolmentEvaluation.getEvaluationSeason().equals(season)) {
                return true;
            }
        }
        return false;
    }

    final public boolean hasAssociatedMarkSheetOrFinalGrade() {
        for (final EnrolmentEvaluation enrolmentEvaluation : getEvaluationsSet()) {
            if (enrolmentEvaluation.getMarkSheet() != null || enrolmentEvaluation.isFinal()) {
                return true;
            }
        }
        return false;
    }

    final public boolean hasAssociatedMarkSheetOrFinalGrade(EvaluationSeason season) {
        for (final EnrolmentEvaluation enrolmentEvaluation : this.getEvaluationsSet()) {
            if (enrolmentEvaluation.getEvaluationSeason().equals(season)
                    && (enrolmentEvaluation.getMarkSheet() != null || enrolmentEvaluation.isFinal())) {
                return true;
            }
        }
        return false;
    }

    final public List<EnrolmentEvaluation> getConfirmedEvaluations(EvaluationSeason season) {
        List<EnrolmentEvaluation> evaluations = new ArrayList<EnrolmentEvaluation>();
        for (EnrolmentEvaluation evaluation : this.getEvaluationsSet()) {
            if (evaluation.getMarkSheet() != null && evaluation.getMarkSheet().getEvaluationSeason().equals(season)
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
        for (final Attends attend : getAttendsSet()) {
            if (attend.getExecutionCourse().getExecutionPeriod() == executionSemester) {
                return attend.getExecutionCourse();
            }
        }

        return null;
    }

    public EnrolmentEvaluation getLatestEnrolmentEvaluationBySeason(EvaluationSeason season) {
        return EvaluationConfiguration.getInstance().getCurrentEnrolmentEvaluation(this, season).orElse(null);
    }

    final public EnrolmentEvaluation getFinalEnrolmentEvaluation() {
        return EvaluationConfiguration.getInstance().getFinalEnrolmentEvaluation(this).orElse(null);
    }

    public Optional<EnrolmentEvaluation> getFinalEnrolmentEvaluationBySeason(EvaluationSeason season) {
        return EvaluationConfiguration.getInstance().getFinalEnrolmentEvaluation(this, season);
    }

    final public EnrolmentEvaluation getImprovementEvaluation() {
        return getEnrolmentEvaluationBySeasonAndState(EnrolmentEvaluationState.TEMPORARY_OBJ,
                EvaluationSeason.readImprovementSeason()).orElse(null);
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
    public boolean isEnroledInExecutionPeriod(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
        return this.getCurricularCourse() == curricularCourse && isValid(executionSemester);
    }

    @Override
    public boolean isValid(final ExecutionSemester executionSemester) {
        return getExecutionPeriod() == executionSemester
                || (getExecutionPeriod().getExecutionYear() == executionSemester.getExecutionYear()
                	&& getCurricularCourse().isAnual());
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
    final public Double getWeigth() {
        return isExtraCurricular() || isPropaedeutic() ? Double.valueOf(0) : getWeigthForCurriculum().doubleValue();
    }

    @Override
    final public BigDecimal getWeigthForCurriculum() {
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

    final public boolean isExtraordinarySeasonEnroled(ExecutionSemester executionSemester) {
        return isExtraordinarySeason() && isValid(executionSemester) && getTempExtraordinarySeasonEvaluation() != null;
    }

    private EnrolmentEvaluation getTempSpecialSeasonEvaluation() {
        return getEnrolmentEvaluationBySeasonAndState(EnrolmentEvaluationState.TEMPORARY_OBJ,
                EvaluationSeason.readSpecialSeason()).orElse(null);
    }

    private EnrolmentEvaluation getTempExtraordinarySeasonEvaluation() {
        return getEnrolmentEvaluationBySeasonAndState(EnrolmentEvaluationState.TEMPORARY_OBJ,
                EvaluationSeason.readExtraordinarySeason()).orElse(null);
    }

    final public boolean canBeSpecialSeasonEnroled(ExecutionYear executionYear) {
        return !getEvaluationSeason().isSpecial() && getExecutionPeriod().getExecutionYear() == executionYear && !isApproved();
    }

    final public boolean canBeSpecialSeasonEnroled(ExecutionSemester executionSemester) {
        return !getEvaluationSeason().isSpecial() && getExecutionPeriod() == executionSemester && !isApproved();
    }

    final public boolean canBeExtraordinarySeasonEnroled(ExecutionSemester executionSemester) {
        // TODO keep isApproved?
        return !getEvaluationSeason().isExtraordinary() && getExecutionPeriod() == executionSemester && !isApproved();
    }

    @Override
    final public Collection<Enrolment> getSpecialSeasonEnrolments(final ExecutionYear executionYear) {
        return getExecutionPeriod().getExecutionYear() == executionYear && isSpecialSeason() ?
            Collections.singleton(this) : Collections.emptySet();
    }

    @Override
    final public Collection<Enrolment> getSpecialSeasonEnrolments(final ExecutionSemester executionSemester) {
        if (isSpecialSeason() && isValid(executionSemester)) {
            return Collections.singleton(this);
        }
        return Collections.emptySet();
    }

    @Override
    final public Collection<Enrolment> getExtraordinarySeasonEnrolments(final ExecutionYear executionYear) {
        return getExecutionPeriod().getExecutionYear() == executionYear && isExtraordinarySeason() ?
                Collections.singleton(this) : Collections.emptySet();
    }

    @Override
    final public Collection<Enrolment> getExtraordinarySeasonEnrolments(final ExecutionSemester executionSemester) {
        if (isExtraordinarySeason() && isValid(executionSemester)) {
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
        Collection<Thesis> theses = getThesesSet();

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

    public boolean hasAnyAssociatedMarkSheetOrFinalGrade() {
        for (final EnrolmentEvaluation enrolmentEvaluation : getEvaluationsSet()) {
            if (enrolmentEvaluation.getMarkSheet() != null || enrolmentEvaluation.isFinal()) {
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

    public boolean canBeSubmittedForOldMarkSheet(EvaluationSeason season) {
        if (season.isNormal() && getEvaluationsSet().isEmpty()) {
            return true;
        }

        for (EnrolmentEvaluation enrolmentEvaluation : getEvaluationsSet()) {
            if (enrolmentEvaluation.getEvaluationSeason().equals(season)
                    && enrolmentEvaluation.getMarkSheet() == null
                    && (enrolmentEvaluation.isTemporary() || (enrolmentEvaluation.isNotEvaluated() && enrolmentEvaluation
                            .getExamDateYearMonthDay() == null))) {
                return true;
            }
        }

        return false;
    }

    public boolean isSourceOfAnyCreditsInCurriculum() {
        for (final InternalEnrolmentWrapper enrolmentWrapper : getEnrolmentWrappersSet()) {
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
        enrolment.getThesesSet().addAll(optionalEnrolment.getThesesSet());
        enrolment.getExamDateCertificateRequestsSet().addAll(optionalEnrolment.getExamDateCertificateRequestsSet());
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

    @ConsistencyPredicate
    public boolean checkThesisMultiplicity() {
        return this.getThesesSet().size() <= 2;
    }

    public void annul() {
        setEnrollmentState(EnrollmentState.ANNULED);
    }

    public void activate() {
        if (!isActive()) {
            final Grade finalGrade = getGrade();
            setEnrollmentState(finalGrade.isEmpty() ? EnrollmentState.ENROLLED : finalGrade.getEnrolmentState());
        }
    }

    public Optional<EnrolmentGratuityEvent> getGratuityEvent() {
        return Optional.ofNullable(super.getEvent());
    }
}
