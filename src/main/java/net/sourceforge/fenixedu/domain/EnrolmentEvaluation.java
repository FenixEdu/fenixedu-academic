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

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import net.sourceforge.fenixedu.domain.curriculum.CurriculumValidationEvaluationPhase;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationContext;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.curriculum.GradeFactory;
import net.sourceforge.fenixedu.domain.curriculum.IGrade;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentNotPayedException;
import net.sourceforge.fenixedu.domain.log.EnrolmentEvaluationLog;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.FenixDigestUtils;
import net.sourceforge.fenixedu.util.MarkType;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class EnrolmentEvaluation extends EnrolmentEvaluation_Base implements Comparable {

    public static final Comparator<EnrolmentEvaluation> COMPARATORY_BY_WHEN = new Comparator<EnrolmentEvaluation>() {

        @Override
        public int compare(EnrolmentEvaluation o1, EnrolmentEvaluation o2) {
            return o1.getWhenDateTime().compareTo(o2.getWhenDateTime());
        }

    };

    public static final Comparator<EnrolmentEvaluation> SORT_BY_STUDENT_NUMBER = new Comparator<EnrolmentEvaluation>() {

        @Override
        public int compare(EnrolmentEvaluation o1, EnrolmentEvaluation o2) {
            final Student s1 = o1.getRegistration().getStudent();
            final Student s2 = o2.getRegistration().getStudent();
            return s1.getNumber().compareTo(s2.getNumber());
        }

    };

    public static final Comparator<EnrolmentEvaluation> SORT_SAME_TYPE_GRADE = new Comparator<EnrolmentEvaluation>() {
        @Override
        public int compare(EnrolmentEvaluation o1, EnrolmentEvaluation o2) {
            if (o1.getEnrolmentEvaluationType() != o2.getEnrolmentEvaluationType()) {
                throw new RuntimeException("error.enrolmentEvaluation.different.types");
            }
            if (o1.getEnrolmentEvaluationState().getWeight() == o2.getEnrolmentEvaluationState().getWeight()) {
                return o1.compareMyWhenAlteredDateToAnotherWhenAlteredDate(o2.getWhen());
            }
            return o1.getEnrolmentEvaluationState().getWeight() - o2.getEnrolmentEvaluationState().getWeight();
        }
    };

    public static final Comparator<EnrolmentEvaluation> SORT_BY_GRADE = new Comparator<EnrolmentEvaluation>() {
        @Override
        public int compare(EnrolmentEvaluation o1, EnrolmentEvaluation o2) {
            return o1.getGradeWrapper().compareTo(o2.getGradeWrapper());
        }
    };

    static final public Comparator<EnrolmentEvaluation> COMPARATOR_BY_EXAM_DATE = new Comparator<EnrolmentEvaluation>() {
        @Override
        public int compare(EnrolmentEvaluation o1, EnrolmentEvaluation o2) {
            if (o1.getExamDateYearMonthDay() == null && o2.getExamDateYearMonthDay() == null) {
                return 0;
            }
            if (o1.getExamDateYearMonthDay() == null) {
                return -1;
            }
            if (o2.getExamDateYearMonthDay() == null) {
                return 1;
            }
            return o1.getExamDateYearMonthDay().compareTo(o2.getExamDateYearMonthDay());
        }
    };

    private static final String RECTIFICATION = "RECTIFICAÇÃO";

    private static final String RECTIFIED = "RECTIFICADO";

    public EnrolmentEvaluation() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
        setGrade(Grade.createEmptyGrade());
        setContext(EnrolmentEvaluationContext.MARK_SHEET_EVALUATION);
    }

    public EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationType type) {
        this();
        if (enrolment == null || type == null) {
            throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
        }
        setEnrolment(enrolment);
        setEnrolmentEvaluationType(type);
    }

    private EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationState enrolmentEvaluationState,
            EnrolmentEvaluationType type, Person responsibleFor, Grade grade, Date availableDate, Date examDate) {

        this(enrolment, type);

        if (enrolmentEvaluationState == null || responsibleFor == null) {
            throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
        }
        setEnrolmentEvaluationState(enrolmentEvaluationState);
        setPersonResponsibleForGrade(responsibleFor);
        setGrade(grade);
        setGradeAvailableDate(availableDate);
        setExamDate(examDate);

        generateCheckSum();
    }

    @Override
    public void setExamDateYearMonthDay(YearMonthDay evaluationDateYearMonthDay) {
        if (evaluationDateYearMonthDay != null) {
            final Enrolment enrolment = getEnrolment();
            final Thesis thesis = enrolment.getThesis();
            if (thesis != null) {
                DateTime newDateTime = evaluationDateYearMonthDay.toDateTimeAtMidnight();
                final DateTime dateTime = thesis.getDiscussed();
                if (dateTime != null) {
                    newDateTime = newDateTime.withHourOfDay(dateTime.getHourOfDay());
                    newDateTime = newDateTime.withMinuteOfHour(dateTime.getMinuteOfHour());
                }
                thesis.setDiscussed(newDateTime);
            }
        }
        super.setExamDateYearMonthDay(evaluationDateYearMonthDay);
    }

    protected EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationState enrolmentEvaluationState,
            EnrolmentEvaluationType type, Person responsibleFor, Grade grade, Date availableDate, Date examDate, DateTime when) {
        this(enrolment, enrolmentEvaluationState, type, responsibleFor, grade, availableDate, examDate);
        setWhenDateTime(when);
    }

    protected EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationType enrolmentEvaluationType,
            EnrolmentEvaluationState evaluationState) {
        this(enrolment, enrolmentEvaluationType);
        if (evaluationState == null) {
            throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
        }
        setEnrolmentEvaluationState(evaluationState);
        setWhenDateTime(new DateTime());
    }

    protected EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationType enrolmentEvaluationType,
            EnrolmentEvaluationState evaluationState, Person person) {
        this(enrolment, enrolmentEvaluationType, evaluationState);
        if (person == null) {
            throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
        }
        setPerson(person);
    }

    protected EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationType enrolmentEvaluationType,
            EnrolmentEvaluationState evaluationState, Person person, ExecutionSemester executionSemester) {
        this(enrolment, enrolmentEvaluationType, evaluationState, person);
        if (executionSemester == null) {
            throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
        }
        setExecutionPeriod(executionSemester);
    }

    @Override
    public int compareTo(Object o) {
        EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) o;
        if (this.getEnrolment().getStudentCurricularPlan().getDegreeType().equals(DegreeType.MASTER_DEGREE)) {
            return compareMyWhenAlteredDateToAnotherWhenAlteredDate(enrolmentEvaluation.getWhen());
        }

        if (this.isInCurriculumValidationContextAndIsFinal() && !enrolmentEvaluation.isInCurriculumValidationContextAndIsFinal()) {
            return 1;
        } else if (!this.isInCurriculumValidationContextAndIsFinal()
                && enrolmentEvaluation.isInCurriculumValidationContextAndIsFinal()) {
            return -1;
        } else if (this.isInCurriculumValidationContextAndIsFinal()
                && enrolmentEvaluation.isInCurriculumValidationContextAndIsFinal()) {
            return compareMyWhenAlteredDateToAnotherWhenAlteredDate(enrolmentEvaluation.getWhen());
        } else if (this.getEnrolmentEvaluationType() == enrolmentEvaluation.getEnrolmentEvaluationType()) {
            if ((this.isRectification() && enrolmentEvaluation.isRectification())
                    || (this.isRectified() && enrolmentEvaluation.isRectified())) {
                return compareMyWhenAlteredDateToAnotherWhenAlteredDate(enrolmentEvaluation.getWhen());
            }
            if (this.isRectification()) {
                return 1;
            }
            if (enrolmentEvaluation.isRectification()) {
                return -1;
            }
            return compareByGrade(this.getGrade(), enrolmentEvaluation.getGrade());

        } else {
            return compareByGrade(this.getGrade(), enrolmentEvaluation.getGrade());
        }
    }

    private int compareByGrade(final Grade grade, final Grade otherGrade) {
        EnrollmentState gradeEnrolmentState = getEnrollmentStateByGrade();
        EnrollmentState otherGradeEnrolmentState = getEnrollmentStateByGrade();
        if (gradeEnrolmentState == EnrollmentState.APROVED && otherGradeEnrolmentState == EnrollmentState.APROVED) {
            return grade.compareTo(otherGrade);
        }

        return compareByGradeState(gradeEnrolmentState, otherGradeEnrolmentState);
    }

    private int compareByGradeState(EnrollmentState gradeEnrolmentState, EnrollmentState otherGradeEnrolmentState) {
        if (gradeEnrolmentState == EnrollmentState.APROVED) {
            return 1;
        }
        if (otherGradeEnrolmentState == EnrollmentState.APROVED) {
            return -1;
        }
        if (gradeEnrolmentState == EnrollmentState.NOT_APROVED && otherGradeEnrolmentState == EnrollmentState.NOT_EVALUATED) {
            return 1;
        }
        if (gradeEnrolmentState == EnrollmentState.NOT_EVALUATED && otherGradeEnrolmentState == EnrollmentState.NOT_APROVED) {
            return -1;
        }

        return 0;
    }

    private int compareMyExamDateToAnotherExamDate(Date examDate) {
        if (this.getExamDate() == null) {
            return -1;
        }
        if (examDate == null) {
            return 1;
        }

        return this.getExamDate().compareTo(examDate);

    }

    private int compareMyWhenAlteredDateToAnotherWhenAlteredDate(Date whenAltered) {
        if (this.getWhen() == null) {
            return -1;
        }
        if (whenAltered == null) {
            return 1;
        }

        return this.getWhen().compareTo(whenAltered);

    }

    private int compareMyGradeToAnotherGrade(final Grade otherGrade) {
        return this.getGrade().compareTo(otherGrade);
    }

    private int compareForEqualStates(EnrollmentState myEnrolmentState, EnrollmentState otherEnrolmentState, Grade otherGrade,
            Date otherExamDate) {
        if (myEnrolmentState.equals(EnrollmentState.APROVED)) {
            return compareMyGradeToAnotherGrade(otherGrade);
        }
        return compareMyExamDateToAnotherExamDate(otherExamDate);
    }

    private int compareForNotEqualStates(EnrollmentState myEnrolmentState, EnrollmentState otherEnrolmentState) {
        if (myEnrolmentState.equals(EnrollmentState.APROVED)) {
            return 1;
        } else if (myEnrolmentState.equals(EnrollmentState.NOT_APROVED) && otherEnrolmentState.equals(EnrollmentState.APROVED)) {
            return -1;
        } else if (myEnrolmentState.equals(EnrollmentState.NOT_APROVED)) {
            return 1;
        } else if (myEnrolmentState.equals(EnrollmentState.NOT_EVALUATED)) {
            return -1;
        } else {
            return 0;
        }
    }

    public EnrollmentState getEnrollmentStateByGrade() {
        return getGrade().getEnrolmentState();
    }

    @Override
    public GradeScale getGradeScale() {
        return getGradeScaleChain();
    }

    public GradeScale getGradeScaleChain() {
        return super.getGradeScale() != null ? super.getGradeScale() : getEnrolment().getGradeScaleChain();
    }

    public GradeScale getAssociatedGradeScale() {
        return super.getGradeScale();
    }

    public boolean isNormal() {
        return getEnrolmentEvaluationType() == EnrolmentEvaluationType.NORMAL;
    }

    public boolean isImprovment() {
        return getEnrolmentEvaluationType() == EnrolmentEvaluationType.IMPROVEMENT;
    }

    public boolean isSpecialSeason() {
        return getEnrolmentEvaluationType() == EnrolmentEvaluationType.SPECIAL_SEASON;
    }

    public boolean isNotEvaluated() {
        return getEnrollmentStateByGrade() == EnrollmentState.NOT_EVALUATED;
    }

    public boolean isFlunked() {
        return getEnrollmentStateByGrade() == EnrollmentState.NOT_APROVED;
    }

    public boolean isApproved() {
        return getEnrollmentStateByGrade() == EnrollmentState.APROVED;
    }

    public void edit(Person responsibleFor, Date evaluationDate) {
        if (responsibleFor == null) {
            throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
        }
        setPersonResponsibleForGrade(responsibleFor);
        setExamDate(evaluationDate);
        generateCheckSum();
    }

    public void edit(Person responsibleFor, String gradeValue, Date availableDate, Date examDate, String bookReference,
            String page, String examReference) {
        edit(responsibleFor, gradeValue, availableDate, examDate);
        setBookReference(bookReference);
        setPage(page);
    }

    public void edit(Person responsibleFor, String gradeValue, Date availableDate, Date examDate) {
        edit(responsibleFor, Grade.createGrade(gradeValue, getGradeScale()), availableDate, examDate);
    }

    public void edit(Person responsibleFor, Grade grade, Date availableDate, Date examDate) {
        if (responsibleFor == null) {
            throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
        }

        if (examDate != null) {

            if (!grade.isNotEvaluated()) {
                checkRegistrationState();
            }

            setExamDateYearMonthDay(YearMonthDay.fromDateFields(examDate));

        } else if (grade.isEmpty()) {
            setExamDateYearMonthDay(null);

        } else {
            setExamDateYearMonthDay(YearMonthDay.fromDateFields(availableDate));
        }

        setGrade(grade);
        setGradeAvailableDateYearMonthDay(YearMonthDay.fromDateFields(availableDate));
        setPersonResponsibleForGrade(responsibleFor);

        generateCheckSum();
    }

    private void checkRegistrationState() {

        if (getRegistration().hasAnyActiveState(getExecutionYear()) || getRegistration().isTransited(getExecutionYear())
                || (getRegistration().isConcluded() && isImprovment())) {

            return;
        }

        throw new DomainException("error.EnrolmentEvaluation.registration.with.invalid.state", getRegistration().getNumber()
                .toString());
    }

    private ExecutionYear getExecutionYear() {
        return getExecutionPeriod().getExecutionYear();
    }

    public void confirmSubmission(Person person, String observation) {
        confirmSubmission(EnrolmentEvaluationState.FINAL_OBJ, person, observation);
    }

    public void confirmSubmission(EnrolmentEvaluationState enrolmentEvaluationState, Person person, String observation) {

        if (!isTemporary()) {
            throw new DomainException("EnrolmentEvaluation.cannot.submit.not.temporary");
        }

        if (!hasGrade()) {
            throw new DomainException("EnrolmentEvaluation.cannot.submit.with.empty.grade");
        }

        /*
         * 
         * Due to curriculum validation the exam date is not required
         * 
         * if (!hasExamDateYearMonthDay()) { throw new
         * DomainException("EnrolmentEvaluation.cannot.submit.without.exam.date"
         * ); }
         */

        if (isPayable() && !isPayed()) {
            throw new EnrolmentNotPayedException("EnrolmentEvaluation.cannot.set.grade.on.not.payed.enrolment.evaluation",
                    getEnrolment());
        }

        if (enrolmentEvaluationState == EnrolmentEvaluationState.RECTIFICATION_OBJ && hasRectified()) {
            getRectified().setEnrolmentEvaluationState(EnrolmentEvaluationState.RECTIFIED_OBJ);
        }

        setEnrolmentEvaluationState(enrolmentEvaluationState);
        setPerson(person);
        setObservation(observation);
        setWhenDateTime(new DateTime());

        EnrollmentState newEnrolmentState = EnrollmentState.APROVED;
        if (!this.isImprovment()) {
            if (MarkType.getRepMarks().contains(getGradeValue())) {
                newEnrolmentState = EnrollmentState.NOT_APROVED;
            } else if (MarkType.getNaMarks().contains(getGradeValue())) {
                newEnrolmentState = EnrollmentState.NOT_EVALUATED;
            }
        }

        this.getEnrolment().setEnrollmentState(newEnrolmentState);
    }

    public void canBeDeleted() {
        if (!isTemporary() || hasConfirmedMarkSheet()) {
            throw new DomainException("error.enrolmentEvaluation.isTemporary.or.hasConfirmedMarksheet");
        }
        checkApprovedEnrolmentPayment();
    }

    public boolean hasConfirmedMarkSheet() {
        return hasMarkSheet() && getMarkSheet().isConfirmed();
    }

    public boolean isTemporary() {
        return getEnrolmentEvaluationState() != null
                && getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.TEMPORARY_OBJ);
    }

    public boolean isFinal() {
        return getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.FINAL_OBJ)
                || getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFICATION_OBJ)
                || getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFIED_OBJ);
    }

    public boolean isRectification() {
        return (this.getObservation() != null && this.getObservation().equals(RECTIFICATION))
                || (this.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFICATION_OBJ));
    }

    public boolean isRectified() {
        return (this.getObservation() != null && this.getObservation().equals(RECTIFIED))
                || (this.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFIED_OBJ));
    }

    public void delete() {
        canBeDeleted();
        deleteObject();
    }

    private void deleteObject() {
        checkApprovedEnrolmentPayment();

        setPersonResponsibleForGrade(null);
        setPerson(null);
        setEnrolment(null);
        setMarkSheet(null);
        setRectification(null);
        setRectified(null);
        if (getImprovementOfApprovedEnrolmentEvent() != null) {
            getImprovementOfApprovedEnrolmentEvent().removeImprovementEnrolmentEvaluations(this);
        }
        setExecutionPeriod(null);

        setRootDomainObject(null);

        super.deleteDomainObject();
    }

    private void checkApprovedEnrolmentPayment() {
        if (hasImprovementOfApprovedEnrolmentEvent() && getImprovementOfApprovedEnrolmentEvent().isPayed()) {
            throw new DomainException("error.enrolmentEvaluation.has.been.payed");
        }
    }

    public void removeFromMarkSheet() {
        if (hasConfirmedMarkSheet()) {
            throw new DomainException("error.enrolmentEvaluation.cannot.be.removed.from.markSheet");
        }

        setCheckSum(null);
        setExamDateYearMonthDay(null);
        setGradeAvailableDateYearMonthDay(null);

        setMarkSheet(null);
    }

    public void insertStudentFinalEvaluationForMasterDegree(String gradeValue, Person responsibleFor, Date examDate)
            throws DomainException {

        DegreeCurricularPlan degreeCurricularPlan = getEnrolment().getStudentCurricularPlan().getDegreeCurricularPlan();

        final Grade grade = Grade.createGrade(gradeValue, getGradeScale());
        if (!grade.isEmpty() && degreeCurricularPlan.isGradeValid(grade)) {
            edit(responsibleFor, gradeValue, Calendar.getInstance().getTime(), examDate);
        } else {
            throw new DomainException("error.invalid.grade");
        }
    }

    public void alterStudentEnrolmentEvaluationForMasterDegree(String gradeValue, Person person, Person responsibleFor,
            EnrolmentEvaluationType evaluationType, Date evaluationAvailableDate, Date examDate, String observation)
            throws DomainException {

        Enrolment enrolment = getEnrolment();
        DegreeCurricularPlan degreeCurricularPlan = getEnrolment().getStudentCurricularPlan().getDegreeCurricularPlan();

        final Grade grade = Grade.createGrade(gradeValue, getGradeScale());
        if (grade.isEmpty()) {
            EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation(enrolment, getEnrolmentEvaluationType());
            enrolmentEvaluation.confirmSubmission(EnrolmentEvaluationState.FINAL_OBJ, person, observation);
            enrolment.setEnrollmentState(EnrollmentState.ENROLLED);
        } else {
            if (degreeCurricularPlan.isGradeValid(grade)) {
                EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation(enrolment, evaluationType);
                enrolmentEvaluation.edit(responsibleFor, grade, evaluationAvailableDate, examDate);
                enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ); // temporary
                // hack
                enrolmentEvaluation.confirmSubmission(EnrolmentEvaluationState.FINAL_OBJ, person, observation);
            } else {
                throw new DomainException("error.invalid.grade");
            }
        }
    }

    protected void generateCheckSum() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getExamDateYearMonthDay() != null ? getExamDateYearMonthDay().toString() : "").append(
                getGradeValue());
        stringBuilder.append(getEnrolmentEvaluationType());
        stringBuilder.append(getEnrolment().getStudentCurricularPlan().getRegistration().getNumber());
        setCheckSum(FenixDigestUtils.createDigest(stringBuilder.toString()));
    }

    public IGrade getGradeWrapper() {
        return GradeFactory.getInstance().getGrade(getGradeValue());
    }

    @Override
    public String getGradeValue() {
        return getGrade().getValue();
    }

    @Override
    @Deprecated
    public void setGradeValue(final String grade) {
        setGrade(grade);
    }

    public void setGrade(final String grade) {
        setGrade(Grade.createGrade(grade, getGradeScale()));
    }

    @Override
    public void setGrade(final Grade grade) {

        if (isFinal()) {
            throw new DomainException("EnrolmentEvaluation.cannot.set.grade.final");
        }

        super.setGrade(grade);

        final Enrolment enrolment = getEnrolment();
        if (enrolment != null && enrolment.getCurricularCourse().isDissertation()) {
            final Thesis thesis = enrolment.getThesis();
            if (thesis != null) {
                thesis.setMark(Integer.valueOf(grade.getValue()));
            }
        }

        // TODO remove this once we're sure migration to Grade went OK
        super.setGradeValue(grade.getValue());
    }

    @Deprecated
    public Registration getStudent() {
        return this.getRegistration();
    }

    public Registration getRegistration() {
        return getStudentCurricularPlan().getRegistration();
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return getStudentCurricularPlan().getDegreeCurricularPlan();
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        return getEnrolment().getStudentCurricularPlan();
    }

    public MarkSheet getRectificationMarkSheet() {
        if (this.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFIED_OBJ) && hasRectification()) {
            return getRectification().getMarkSheet();
        } else {
            return null;
        }
    }

    public boolean hasGrade() {
        return !getGrade().isEmpty();
    }

    final public boolean hasExamDateYearMonthDay() {
        return getExamDateYearMonthDay() != null;
    }

    public boolean isPayable() {
        return hasImprovementOfApprovedEnrolmentEvent() && !getImprovementOfApprovedEnrolmentEvent().isCancelled();
    }

    public boolean isPayed() {
        return getImprovementOfApprovedEnrolmentEvent().isPayed();
    }

    @Override
    public ExecutionSemester getExecutionPeriod() {
        if (getEnrolmentEvaluationType() == EnrolmentEvaluationType.IMPROVEMENT) {
            return super.getExecutionPeriod();
        }

        if (getEnrolment() != null) {
            return getEnrolment().getExecutionPeriod();
        }

        return null;
    }

    public boolean isInCurriculumValidationContext() {
        return this.getContext() != null && this.getContext().equals(EnrolmentEvaluationContext.CURRICULUM_VALIDATION_EVALUATION);
    }

    public boolean isInCurriculumValidationContextAndIsFinal() {
        return this.isInCurriculumValidationContext() && this.isFinal();
    }

    private static final String NORMAL_TYPE_FIRST_SEASON_DESCRIPTION =
            "label.curriculum.validation.normal.type.first.season.description";
    private static final String NORMAL_TYPE_SECOND_SEASON_DESCRIPTION =
            "label.curriculum.validation.normal.type.second.season.description";

    public String getEnrolmentEvaluationTypeDescription() {
        if (EnrolmentEvaluationType.NORMAL.equals(this.getEnrolmentEvaluationType())
                && CurriculumValidationEvaluationPhase.FIRST_SEASON.equals(this.getCurriculumValidationEvaluationPhase())) {
            return BundleUtil.getString(Bundle.ACADEMIC, NORMAL_TYPE_FIRST_SEASON_DESCRIPTION);
        } else if (EnrolmentEvaluationType.NORMAL.equals(this.getEnrolmentEvaluationType())
                && CurriculumValidationEvaluationPhase.SECOND_SEASON.equals(this.getCurriculumValidationEvaluationPhase())) {
            return BundleUtil.getString(Bundle.ACADEMIC, NORMAL_TYPE_SECOND_SEASON_DESCRIPTION);
        }

        return this.getEnrolmentEvaluationType().getDescription();
    }

    @Atomic
    public void deleteEnrolmentEvaluationCurriculumValidationContext() {
        if (!getEnrolment().getStudentCurricularPlan().getEvaluationForCurriculumValidationAllowed()) {
            throw new DomainException("error.curriculum.validation.enrolment.evaluatiom.removal.not.allowed");
        }

        Enrolment enrolment = getEnrolment();

        EnrolmentEvaluationLog.logEnrolmentEvaluationDeletion(this);
        deleteObject();

        enrolment.changeStateIfAprovedAndEvaluationsIsEmpty();
    }

    @Deprecated
    public java.util.Date getExamDate() {
        org.joda.time.YearMonthDay ymd = getExamDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setExamDate(java.util.Date date) {
        if (date == null) {
            setExamDateYearMonthDay(null);
        } else {
            setExamDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getGradeAvailableDate() {
        org.joda.time.YearMonthDay ymd = getGradeAvailableDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setGradeAvailableDate(java.util.Date date) {
        if (date == null) {
            setGradeAvailableDateYearMonthDay(null);
        } else {
            setGradeAvailableDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getWhen() {
        org.joda.time.DateTime dt = getWhenDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setWhen(java.util.Date date) {
        if (date == null) {
            setWhenDateTime(null);
        } else {
            setWhenDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public boolean hasGradeAvailableDateYearMonthDay() {
        return getGradeAvailableDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasImprovementOfApprovedEnrolmentEvent() {
        return getImprovementOfApprovedEnrolmentEvent() != null;
    }

    @Deprecated
    public boolean hasEnrolment() {
        return getEnrolment() != null;
    }

    @Deprecated
    public boolean hasRectified() {
        return getRectified() != null;
    }

    @Deprecated
    public boolean hasGradeScale() {
        return getGradeScale() != null;
    }

    @Deprecated
    public boolean hasCurriculumValidationEvaluationPhase() {
        return getCurriculumValidationEvaluationPhase() != null;
    }

    @Deprecated
    public boolean hasMarkSheet() {
        return getMarkSheet() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasObservation() {
        return getObservation() != null;
    }

    @Deprecated
    public boolean hasPersonResponsibleForGrade() {
        return getPersonResponsibleForGrade() != null;
    }

    @Deprecated
    public boolean hasEnrolmentEvaluationType() {
        return getEnrolmentEvaluationType() != null;
    }

    @Deprecated
    public boolean hasGradeValue() {
        return getGradeValue() != null;
    }

    @Deprecated
    public boolean hasWhenDateTime() {
        return getWhenDateTime() != null;
    }

    @Deprecated
    public boolean hasContext() {
        return getContext() != null;
    }

    @Deprecated
    public boolean hasBookReference() {
        return getBookReference() != null;
    }

    @Deprecated
    public boolean hasPage() {
        return getPage() != null;
    }

    @Deprecated
    public boolean hasEnrolmentEvaluationState() {
        return getEnrolmentEvaluationState() != null;
    }

    @Deprecated
    public boolean hasRectification() {
        return getRectification() != null;
    }

    @Deprecated
    public boolean hasCheckSum() {
        return getCheckSum() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
