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
/*
 * Attends.java
 *
 * Created on 20 de Outubro de 2002, 14:42
 */

package org.fenixedu.academic.domain;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.GroupEnrolment;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.WeeklyWorkLoad;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Interval;
import org.joda.time.PeriodType;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

/**
 *
 * @author tfc130
 */
public class Attends extends Attends_Base {

    public static enum StudentAttendsStateType {
        ENROLED, NOT_ENROLED, IMPROVEMENT, SPECIAL_SEASON, EXTRAORDINARY_SEASON;
        public String getQualifiedName() {
            return StudentAttendsStateType.class.getSimpleName() + "." + name();
        }
    }

    static {
        getRelationExecutionCourseAttends().addListener(new RelationAdapter<ExecutionCourse, Attends>() {
            @Override
            public void afterAdd(ExecutionCourse executionCourse, Attends attends) {
                if (executionCourse != null && attends != null) {
                    for (Grouping grouping : executionCourse.getGroupings()) {
                        if (grouping.getAutomaticEnrolment() && !grouping.getStudentGroupsSet().isEmpty()) {
                            grouping.addAttends(attends);

                            int groupNumber = 1;
                            final List<StudentGroup> studentGroups = new ArrayList<StudentGroup>(grouping.getStudentGroupsSet());
                            Collections.sort(studentGroups, StudentGroup.COMPARATOR_BY_GROUP_NUMBER);

                            for (final StudentGroup studentGroup : studentGroups) {
                                if (studentGroup.getGroupNumber() > groupNumber) {
                                    break;
                                }
                                groupNumber = studentGroup.getGroupNumber() + 1;
                            }

                            grouping.setGroupMaximumNumber(grouping.getStudentGroupsSet().size() + 1);
                            try {
                                GroupEnrolment.enrole(grouping.getExternalId(), null, groupNumber, new ArrayList<String>(),
                                        attends.getRegistration().getStudent().getPerson().getUsername());
                            } catch (FenixServiceException e) {
                                throw new Error(e);
                            }
                        }
                    }
                }
            }
        });
    }

    public static final Comparator<Attends> COMPARATOR_BY_STUDENT_NUMBER = new Comparator<Attends>() {

        @Override
        public int compare(Attends attends1, Attends attends2) {
            final Integer n1 = attends1.getRegistration().getStudent().getNumber();
            final Integer n2 = attends2.getRegistration().getStudent().getNumber();
            int res = n1.compareTo(n2);
            return res != 0 ? res : DomainObjectUtil.COMPARATOR_BY_ID.compare(attends1, attends2);
        }
    };

    public static final Comparator<Attends> ATTENDS_COMPARATOR = new Comparator<Attends>() {
        @Override
        public int compare(final Attends attends1, final Attends attends2) {
            final ExecutionCourse executionCourse1 = attends1.getExecutionCourse();
            final ExecutionCourse executionCourse2 = attends2.getExecutionCourse();
            if (executionCourse1 == executionCourse2) {
                final Registration registration1 = attends1.getRegistration();
                final Registration registration2 = attends2.getRegistration();
                return registration1.getNumber().compareTo(registration2.getNumber());
            } else {
                final ExecutionSemester executionPeriod1 = executionCourse1.getExecutionPeriod();
                final ExecutionSemester executionPeriod2 = executionCourse2.getExecutionPeriod();
                if (executionPeriod1 == executionPeriod2) {
                    return executionCourse1.getNome().compareTo(executionCourse2.getNome());
                } else {
                    return executionPeriod1.compareTo(executionPeriod2);
                }
            }
        }
    };

    public static final Comparator<Attends> ATTENDS_COMPARATOR_BY_EXECUTION_COURSE_NAME = new Comparator<Attends>() {

        @Override
        public int compare(Attends o1, Attends o2) {
            final ExecutionCourse executionCourse1 = o1.getExecutionCourse();
            final ExecutionCourse executionCourse2 = o2.getExecutionCourse();
            final int c = Collator.getInstance().compare(executionCourse1.getNome(), executionCourse2.getNome());
            return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
        }

    };

    public Attends() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public Attends(Registration registration, ExecutionCourse executionCourse) {
        this();
        final Student student = registration.getStudent();
        if (student.hasAttends(executionCourse)) {
            throw new DomainException("error.cannot.create.multiple.enrolments.for.student.in.execution.course",
                    executionCourse.getNome(), executionCourse.getExecutionPeriod().getQualifiedName());
        }
        setRegistration(registration);
        setDisciplinaExecucao(executionCourse);
    }

    public void delete() throws DomainException {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        for (; !getWeeklyWorkLoadsSet().isEmpty(); getWeeklyWorkLoadsSet().iterator().next().delete()) {
            ;
        }

        getProjectSubmissionLogsSet().clear();
        getGroupingsSet().clear();
        setAluno(null);
        setDisciplinaExecucao(null);
        setEnrolment(null);

        setRootDomainObject(null);
        deleteDomainObject();
    }

    public Collection<StudentGroup> getAllStudentGroups() {
        return super.getStudentGroupsSet();
    }

    @Override
    public Set<StudentGroup> getStudentGroupsSet() {
        Set<StudentGroup> result = new HashSet<StudentGroup>();
        for (StudentGroup sg : super.getStudentGroupsSet()) {
            if (sg.getValid()) {
                result.add(sg);
            }
        }
        return Collections.unmodifiableSet(result);
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (hasAnyShiftEnrolments()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.attends.cant.delete"));
        }
        if (!getStudentGroupsSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.attends.cant.delete.has.student.groups"));
        }
        if (!getAssociatedMarksSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.attends.cant.delete.has.associated.marks"));
        }
        if (!getProjectSubmissionsSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.attends.cant.delete.has.project.submissions"));
        }
    }

    public boolean isAbleToBeRemoved() {
        try {
            getRegistration().checkIfHasEnrolmentFor(this);
            getRegistration().checkIfHasShiftsFor(this.getExecutionCourse());
        } catch (DomainException e) {
            return false;
        }

        return getDeletionBlockers().isEmpty();
    }

    public boolean hasAnyShiftEnrolments() {
        for (Shift shift : this.getExecutionCourse().getAssociatedShifts()) {
            if (shift.getStudentsSet().contains(this.getRegistration())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAllShiftEnrolments() {
        if (this.getExecutionCourse().getAssociatedShifts().size() == 0) {
            return true;
        }

        Set<ShiftType> shiftTypes = this.getExecutionCourse().getShiftTypes();
        Set<ShiftType> foundShiftTypes = new HashSet<ShiftType>();

        for (Shift shift : this.getExecutionCourse().getAssociatedShifts()) {
            if (shift.getStudentsSet().contains(this.getRegistration())) {
                foundShiftTypes.addAll(shift.getTypes());
            }
        }

        return foundShiftTypes.size() == shiftTypes.size();
    }

    public FinalMark getFinalMark() {
        for (Mark mark : getAssociatedMarksSet()) {
            if (mark instanceof FinalMark) {
                return (FinalMark) mark;
            }
        }
        return null;
    }

    public Mark getMarkByEvaluation(Evaluation evaluation) {
        for (final Mark mark : getAssociatedMarksSet()) {
            if (mark.getEvaluation().equals(evaluation)) {
                return mark;
            }
        }
        return null;
    }

    public List<Mark> getAssociatedMarksOrderedByEvaluationDate() {
        final List<Evaluation> orderedEvaluations = getExecutionCourse().getOrderedAssociatedEvaluations();
        final List<Mark> orderedMarks = new ArrayList<Mark>(orderedEvaluations.size());
        for (int i = 0; i < orderedEvaluations.size(); i++) {
            orderedMarks.add(null);
        }
        for (final Mark mark : getAssociatedMarksSet()) {
            final Evaluation evaluation = mark.getEvaluation();
            orderedMarks.set(orderedEvaluations.indexOf(evaluation), mark);
        }
        return orderedMarks;
    }

    public WeeklyWorkLoad createWeeklyWorkLoad(final Integer contact, final Integer autonomousStudy, final Integer other) {

        if (contact.intValue() < 0 || autonomousStudy.intValue() < 0 || other.intValue() < 0) {
            throw new DomainException("weekly.work.load.creation.invalid.data");
        }

        if (getEnrolment() == null) {
            throw new DomainException("weekly.work.load.creation.requires.enrolment");
        }

        final int currentWeekOffset = calculateCurrentWeekOffset();
        if (currentWeekOffset < 1
                || new YearMonthDay(getEndOfExamsPeriod()).plusDays(Lesson.NUMBER_OF_DAYS_IN_WEEK).isBefore(new YearMonthDay())) {
            throw new DomainException("outside.weekly.work.load.response.period");
        }

        final int previousWeekOffset = currentWeekOffset - 1;

        final WeeklyWorkLoad lastExistentWeeklyWorkLoad =
                getWeeklyWorkLoadsSet().isEmpty() ? null : Collections.max(getWeeklyWorkLoadsSet());
        if (lastExistentWeeklyWorkLoad != null && lastExistentWeeklyWorkLoad.getWeekOffset().intValue() == previousWeekOffset) {
            throw new DomainException("weekly.work.load.for.previous.week.already.exists");
        }

        return new WeeklyWorkLoad(this, Integer.valueOf(previousWeekOffset), contact, autonomousStudy, other);
    }

    public Interval getWeeklyWorkLoadInterval() {
        final DateTime beginningOfSemester = new DateTime(getBegginingOfLessonPeriod());
        final DateTime firstMonday = beginningOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1);
        final DateTime endOfSemester = new DateTime(getEndOfExamsPeriod());
        final DateTime nextLastMonday = endOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1).plusWeeks(1);
        return new Interval(firstMonday, nextLastMonday);
    }

    public WeeklyWorkLoad getWeeklyWorkLoadOfPreviousWeek() {
        final int currentWeekOffset = calculateCurrentWeekOffset();
        if (currentWeekOffset < 1
                || new YearMonthDay(getEndOfExamsPeriod()).plusDays(Lesson.NUMBER_OF_DAYS_IN_WEEK).isBefore(new YearMonthDay())) {
            throw new DomainException("outside.weekly.work.load.response.period");
        }
        final int previousWeekOffset = currentWeekOffset - 1;
        for (final WeeklyWorkLoad weeklyWorkLoad : getWeeklyWorkLoadsSet()) {
            if (weeklyWorkLoad.getWeekOffset().intValue() == previousWeekOffset) {
                return weeklyWorkLoad;
            }
        }
        return null;
    }

    public Interval getCurrentWeek() {
        final DateMidnight beginningOfSemester = new DateMidnight(getBegginingOfLessonPeriod());
        final DateMidnight firstMonday = beginningOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1);
        final int currentWeek = calculateCurrentWeekOffset();
        final DateMidnight start = firstMonday.plusWeeks(currentWeek);
        return new Interval(start, start.plusWeeks(1));
    }

    public Interval getPreviousWeek() {
        final DateMidnight thisMonday = new DateMidnight().withField(DateTimeFieldType.dayOfWeek(), 1);
        final DateMidnight previousMonday = thisMonday.minusWeeks(1);
        return new Interval(previousMonday, thisMonday);
    }

    public Interval getResponseWeek() {
        final DateMidnight beginningOfSemester = new DateMidnight(getBegginingOfLessonPeriod());
        final DateMidnight firstMonday = beginningOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1);
        final DateMidnight secondMonday = firstMonday.plusWeeks(1);

        final DateMidnight endOfSemester = new DateMidnight(getEndOfExamsPeriod());
        final DateMidnight lastMonday = endOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1);
        final DateMidnight endOfResponsePeriod = lastMonday.plusWeeks(2);

        return (secondMonday.isEqualNow() || secondMonday.isBeforeNow()) && endOfResponsePeriod.isAfterNow() ? getPreviousWeek() : null;
    }

    public int getCalculatePreviousWeek() {
        return calculateCurrentWeekOffset();
    }

    public int calculateCurrentWeekOffset() {
        final DateMidnight beginningOfLessonPeriod = new DateMidnight(getBegginingOfLessonPeriod());
        final DateMidnight firstMonday = beginningOfLessonPeriod.withField(DateTimeFieldType.dayOfWeek(), 1);
        final DateMidnight thisMonday = new DateMidnight().withField(DateTimeFieldType.dayOfWeek(), 1);

        final Interval interval = new Interval(firstMonday, thisMonday);

        return interval.toPeriod(PeriodType.weeks()).getWeeks();
    }

    public Set<WeeklyWorkLoad> getSortedWeeklyWorkLoads() {
        return new TreeSet<WeeklyWorkLoad>(getWeeklyWorkLoadsSet());
    }

    public int getWeeklyWorkLoadContact() {
        int result = 0;
        for (final WeeklyWorkLoad weeklyWorkLoad : getWeeklyWorkLoadsSet()) {
            final int contact = weeklyWorkLoad.getContact() != null ? weeklyWorkLoad.getContact() : 0;
            result += contact;
        }
        return result;
    }

    public int getWeeklyWorkLoadAutonomousStudy() {
        int result = 0;
        for (final WeeklyWorkLoad weeklyWorkLoad : getWeeklyWorkLoadsSet()) {
            final int contact = weeklyWorkLoad.getAutonomousStudy() != null ? weeklyWorkLoad.getAutonomousStudy() : 0;
            result += contact;
        }
        return result;
    }

    public int getWeeklyWorkLoadOther() {
        int result = 0;
        for (final WeeklyWorkLoad weeklyWorkLoad : getWeeklyWorkLoadsSet()) {
            final int contact = weeklyWorkLoad.getOther() != null ? weeklyWorkLoad.getOther() : 0;
            result += contact;
        }
        return result;
    }

    public int getWeeklyWorkLoadTotal() {
        int result = 0;
        for (final WeeklyWorkLoad weeklyWorkLoad : getWeeklyWorkLoadsSet()) {
            final int contact = weeklyWorkLoad.getTotal();
            result += contact;
        }
        return result;
    }

    public Date getBegginingOfLessonPeriod() {
        final ExecutionSemester executionSemester = getExecutionCourse().getExecutionPeriod();
        final StudentCurricularPlan studentCurricularPlan = getEnrolment().getStudentCurricularPlan();
        final ExecutionDegree executionDegree =
                studentCurricularPlan.getDegreeCurricularPlan().getExecutionDegreeByYear(executionSemester.getExecutionYear());
        if (executionSemester.getSemester().intValue() == 1) {
            return executionDegree.getPeriodLessonsFirstSemester().getStart();
        } else if (executionSemester.getSemester().intValue() == 2) {
            return executionDegree.getPeriodLessonsSecondSemester().getStart();
        } else {
            throw new DomainException("unsupported.execution.period.semester");
        }
    }

    public Date getEndOfExamsPeriod() {
        final ExecutionSemester executionSemester = getExecutionCourse().getExecutionPeriod();
        final StudentCurricularPlan studentCurricularPlan = getEnrolment().getStudentCurricularPlan();
        final ExecutionDegree executionDegree =
                studentCurricularPlan.getDegreeCurricularPlan().getExecutionDegreeByYear(executionSemester.getExecutionYear());
        if (executionSemester.getSemester().intValue() == 1) {
            return executionDegree.getPeriodExamsFirstSemester().getEnd();
        } else if (executionSemester.getSemester().intValue() == 2) {
            return executionDegree.getPeriodExamsSecondSemester().getEnd();
        } else {
            throw new DomainException("unsupported.execution.period.semester");
        }
    }

    public boolean isFor(final ExecutionSemester executionSemester) {
        return getExecutionCourse().getExecutionPeriod() == executionSemester;
    }

    public boolean isFor(final ExecutionCourse executionCourse) {
        return getExecutionCourse() == executionCourse;
    }

    public boolean isFor(final ExecutionYear executionYear) {
        return getExecutionCourse().getExecutionYear() == executionYear;
    }

    public boolean isFor(final Shift shift) {
        return isFor(shift.getExecutionCourse());
    }

    public boolean isFor(final Student student) {
        return getRegistration().getStudent().equals(student);
    }

    public boolean isFor(final Registration registration) {
        return getRegistration().equals(registration);
    }

    @Override
    @Deprecated
    public Registration getAluno() {
        return getRegistration();
    }

    public Registration getRegistration() {
        return super.getAluno();
    }

    @Override
    @Deprecated
    public void setAluno(Registration registration) {
        setRegistration(registration);
    }

    public boolean hasRegistration() {
        return super.getAluno() != null;
    }

    public void setRegistration(final Registration registration) {
        if (hasRegistration() && registration != null && getRegistration() != registration) {
            getRegistration().changeShifts(this, registration);
        }
        super.setAluno(registration);
    }

    @Override
    @Deprecated
    public ExecutionCourse getDisciplinaExecucao() {
        return getExecutionCourse();
    }

    public ExecutionCourse getExecutionCourse() {
        return super.getDisciplinaExecucao();
    }

    public ExecutionSemester getExecutionPeriod() {
        return getExecutionCourse().getExecutionPeriod();
    }

    public ExecutionYear getExecutionYear() {
        return getExecutionPeriod().getExecutionYear();
    }

    public void removeShifts() {
        for (final Shift shift : getRegistration().getShiftsSet()) {
            if (shift.getExecutionCourse() == getExecutionCourse()) {
                getRegistration().removeShifts(shift);
            }
        }
    }

    public boolean hasAnyAssociatedMarkSheetOrFinalGrade() {
        return getEnrolment().hasAnyAssociatedMarkSheetOrFinalGrade();
    }

    public StudentCurricularPlan getStudentCurricularPlanFromAttends() {
        final Enrolment enrolment = getEnrolment();
        return enrolment == null ? getRegistration().getLastStudentCurricularPlan() : enrolment.getStudentCurricularPlan();
    }

    public StudentAttendsStateType getAttendsStateType() {
        if (getEnrolment() == null) {
            return StudentAttendsStateType.NOT_ENROLED;
        }

        if (!getEnrolment().getExecutionPeriod().equals(getExecutionPeriod()) && getEnrolment().hasImprovement()) {
            return StudentAttendsStateType.IMPROVEMENT;
        }

        if (getEnrolment().isValid(getExecutionPeriod())) {
            if(getEnrolment().hasExtraordinarySeason()) {
                return StudentAttendsStateType.EXTRAORDINARY_SEASON;
            }
            if (getEnrolment().hasSpecialSeason()) {
                return StudentAttendsStateType.SPECIAL_SEASON;
            }
            return StudentAttendsStateType.ENROLED;
        }

        return null;
    }

    public StudentGroup getStudentGroupByGrouping(final Grouping grouping) {
        for (StudentGroup studentGroup : getStudentGroupsSet()) {
            if (studentGroup.getGrouping().equals(grouping)) {
                return studentGroup;
            }
        }
        return null;
    }

    public boolean hasExecutionCourseTo(final DegreeCurricularPlan degreeCurricularPlan) {
        for (final CurricularCourse curricularCourse : getExecutionCourse().getAssociatedCurricularCoursesSet()) {
            if (degreeCurricularPlan.hasDegreeModule(curricularCourse)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasExecutionCourseTo(final StudentCurricularPlan studentCurricularPlan) {
        return hasExecutionCourseTo(studentCurricularPlan.getDegreeCurricularPlan());
    }

    boolean canMove(final StudentCurricularPlan from, final StudentCurricularPlan to) {
        if (getEnrolment() != null) {
            return !from.hasEnrolments(getEnrolment()) && to.hasEnrolments(getEnrolment());
        }
        return !getExecutionPeriod().isBefore(to.getStartExecutionPeriod());
    }

    @Atomic
    public void deleteShiftEnrolments() {
        final Registration registration = getRegistration();
        final ExecutionCourse executionCourse = getExecutionCourse();
        for (final Shift shift : executionCourse.getAssociatedShifts()) {
            shift.removeStudents(registration);
        }
    }

}
