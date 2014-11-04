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
package org.fenixedu.academic.domain;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.personnelSection.contracts.PersonContractSituation;
import org.fenixedu.academic.domain.personnelSection.contracts.PersonProfessionalData;
import org.fenixedu.academic.domain.personnelSection.contracts.PersonProfessionalExemption;
import org.fenixedu.academic.domain.personnelSection.contracts.ProfessionalCategory;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.predicate.AccessControl;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.PeriodType;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class TeacherCredits extends TeacherCredits_Base {

    public TeacherCredits(Teacher teacher, TeacherCreditsState teacherCreditsState) throws ParseException {
        super();
        setTeacher(teacher);
        setTeacherCreditsState(teacherCreditsState);
        setRootDomainObject(Bennu.getInstance());
        saveTeacherCredits();
    }

    public static TeacherCredits readTeacherCredits(ExecutionSemester executionSemester, Teacher teacher) {
        for (TeacherCredits teacherCredit : teacher.getTeacherCreditsSet()) {
            if (teacherCredit.getTeacherCreditsState().getExecutionSemester().equals(executionSemester)) {
                return teacherCredit;
            }
        }
        return null;
    }

    @Atomic
    public static void closeAllTeacherCredits(ExecutionSemester executionSemester) throws ParseException {
        Collection<Teacher> teachers = Bennu.getInstance().getTeachersSet();
        TeacherCreditsState teacherCreditsState = TeacherCreditsState.getTeacherCreditsState(executionSemester);
        if (teacherCreditsState == null) {
            teacherCreditsState = new TeacherCreditsState(executionSemester);
        }
        for (Teacher teacher : teachers) {
            closeTeacherCredits(teacher, teacherCreditsState);
        }
        teacherCreditsState.setCloseState();
    }

    @Atomic
    public static void closeTeacherCredits(Teacher teacher, TeacherCreditsState teacherCreditsState) throws ParseException {
        TeacherCredits teacherCredits = readTeacherCredits(teacherCreditsState.getExecutionSemester(), teacher);
        if (teacherCredits == null) {
            new TeacherCredits(teacher, teacherCreditsState);
        } else if (teacherCredits.getTeacherCreditsState().isOpenState()) {
            teacherCredits.saveTeacherCredits();
        }
    }

    @Atomic
    public static void openAllTeacherCredits(ExecutionSemester executionSemester) throws ParseException {
        TeacherCreditsState teacherCreditsState = TeacherCreditsState.getTeacherCreditsState(executionSemester);
        teacherCreditsState.setOpenState();
    }

    @Atomic
    public void editTeacherCredits(ExecutionSemester executionSemester) throws ParseException {
        saveTeacherCredits();
    }

    private void saveTeacherCredits() throws ParseException {
        Teacher teacher = getTeacher();
        ExecutionSemester executionSemester = getTeacherCreditsState().getExecutionSemester();
        setProfessionalCategory(ProfessionalCategory.getCategoryByPeriod(teacher, executionSemester));
        double managementCredits = calculateManagementFunctionsCredits(teacher, executionSemester);
        double serviceExemptionsCredits = calculateServiceExemptionCredits(teacher, executionSemester);
        double thesesCredits = calculateThesesCredits(teacher, executionSemester);
        double mandatoryLessonHours = calculateMandatoryLessonHours(teacher, executionSemester);
        TeacherService teacherService = TeacherService.getTeacherServiceByExecutionPeriod(teacher, executionSemester);
        setTeacherService(teacherService);
        setThesesCredits(new BigDecimal(thesesCredits));
        setBalanceOfCredits(new BigDecimal(
                calculateBalanceOfCreditsUntil(teacher, executionSemester.getPreviousExecutionPeriod())));
        setMandatoryLessonHours(new BigDecimal(mandatoryLessonHours));
        setManagementCredits(new BigDecimal(managementCredits));
        setServiceExemptionCredits(new BigDecimal(serviceExemptionsCredits));

        double totalCredits = 0;
        if (!ProfessionalCategory.isMonitor(getTeacher(), executionSemester)) {
            totalCredits =
                    getTeachingDegreeCredits().doubleValue() + getMasterDegreeCredits().doubleValue()
                            + getTfcAdviseCredits().doubleValue() + thesesCredits + getOtherCredits().doubleValue()
                            + managementCredits + serviceExemptionsCredits;
        }
        setTotalCredits(new BigDecimal(totalCredits));

        addTeacherCreditsDocument(new TeacherCreditsDocument(teacher, executionSemester, teacherService));
        setBasicOperations();
    }

    private void setBasicOperations() {
        setPerson(AccessControl.getPerson());
        setLastModifiedDate(new DateTime());
    }

    private void setTeacherService(TeacherService teacherService) throws ParseException {
        if (teacherService != null) {
            setTeachingDegreeCredits(new BigDecimal(teacherService.getTeachingDegreeCredits()));
            setSupportLessonHours(new BigDecimal(teacherService.getSupportLessonHours()));
            setMasterDegreeCredits(new BigDecimal(teacherService.getMasterDegreeServiceCredits()));
            setTfcAdviseCredits(new BigDecimal(teacherService.getTeacherAdviseServiceCredits()));
            setOtherCredits(new BigDecimal(teacherService.getOtherServiceCredits()));
            setInstitutionWorkingHours(new BigDecimal(teacherService.getInstitutionWorkingHours()));
            setPastServiceCredits(new BigDecimal(teacherService.getPastServiceCredits()));
        } else {
            setTeachingDegreeCredits(new BigDecimal(0));
            setSupportLessonHours(new BigDecimal(0));
            setMasterDegreeCredits(new BigDecimal(0));
            setTfcAdviseCredits(new BigDecimal(0));
            setOtherCredits(new BigDecimal(0));
            setInstitutionWorkingHours(new BigDecimal(0));
            setPastServiceCredits(new BigDecimal(0));
        }
    }

    public TeacherCreditsDocument getLastTeacherCreditsDocument() {
        TeacherCreditsDocument lastTeacherCreditsDocument = null;
        for (TeacherCreditsDocument teacherCreditsDocument : getTeacherCreditsDocumentSet()) {
            if (lastTeacherCreditsDocument == null
                    || lastTeacherCreditsDocument.getUploadTime().isBefore(teacherCreditsDocument.getUploadTime())) {
                lastTeacherCreditsDocument = teacherCreditsDocument;
            }
        }
        return lastTeacherCreditsDocument;
    }

    public static double calculateThesesCredits(Teacher teacher, ExecutionSemester executionSemester) {
        return round(teacher.getPerson().getThesisEvaluationParticipants(executionSemester).stream()
                .mapToDouble(p -> p.getParticipationCredits()).sum());
    }

    public static double calculateManagementFunctionsCredits(Teacher teacher, ExecutionSemester executionSemester) {
        double totalCredits = 0.0;
        for (PersonFunction personFunction : (Collection<PersonFunction>) teacher.getPerson().getParentAccountabilities(
                AccountabilityTypeEnum.MANAGEMENT_FUNCTION, PersonFunction.class)) {
            if (personFunction.belongsToPeriod(executionSemester.getBeginDateYearMonthDay(),
                    executionSemester.getEndDateYearMonthDay())
                    && !personFunction.getFunction().isVirtual()) {
                totalCredits = (personFunction.getCredits() != null) ? totalCredits + personFunction.getCredits() : totalCredits;
            }
        }
        return round(totalCredits);
    }

    private static Double round(double n) {
        return Math.round((n * 100.0)) / 100.0;
    }

    public static Double calculateMandatoryLessonHours(Teacher teacher, ExecutionSemester executionSemester) {
        PersonContractSituation teacherContractSituation = null;
        Interval semesterInterval =
                new Interval(executionSemester.getBeginDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay(),
                        executionSemester.getEndDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay());
        if (PersonProfessionalData.isTeacherActiveForSemester(teacher, executionSemester)) {
            teacherContractSituation = PersonContractSituation.getDominantTeacherContractSituation(teacher, semesterInterval);
            PersonContractSituation personContractSituation =
                    PersonContractSituation.getDominantTeacherServiceExemption(teacher, executionSemester);
            if (personContractSituation != null && !personContractSituation.countForCredits(semesterInterval)) {
                teacherContractSituation = personContractSituation;
            }
        } else if (teacher.hasTeacherAuthorization(executionSemester.getAcademicInterval())) {
            TeacherService teacherService = TeacherService.getTeacherServiceByExecutionPeriod(teacher, executionSemester);
            return teacherService == null ? 0 : teacherService.getTeachingDegreeHours();
        }
        return teacherContractSituation == null ? 0 : teacherContractSituation.getWeeklyLessonHours(semesterInterval);
    }

    public static double calculateBalanceOfCreditsUntil(Teacher teacher, ExecutionSemester executionSemester)
            throws ParseException {
        double balanceCredits = 0.0;
        ExecutionSemester firstExecutionPeriod = ExecutionSemester.readStartExecutionSemesterForCredits();
        TeacherService firstTeacherService = TeacherService.getTeacherServiceByExecutionPeriod(teacher, firstExecutionPeriod);
        if (firstTeacherService != null) {
            balanceCredits = firstTeacherService.getPastServiceCredits();
        }

        if (executionSemester != null && executionSemester.isAfter(firstExecutionPeriod)) {
            balanceCredits =
                    sumCreditsBetweenPeriods(teacher, firstExecutionPeriod.getNextExecutionPeriod(), executionSemester,
                            balanceCredits);
        }
        return balanceCredits;
    }

    private static double sumCreditsBetweenPeriods(Teacher teacher, ExecutionSemester startPeriod,
            ExecutionSemester endExecutionPeriod, double totalCredits) throws ParseException {
        ExecutionSemester lastExecutionSemester = ExecutionSemester.readLastExecutionSemesterForCredits();

        ExecutionSemester executionPeriodAfterEnd = endExecutionPeriod.getNextExecutionPeriod();
        while (startPeriod != executionPeriodAfterEnd && endExecutionPeriod.isBeforeOrEquals(lastExecutionSemester)) {
            TeacherCredits teacherCredits = readTeacherCredits(startPeriod, teacher);
            if (teacherCredits != null && teacherCredits.getTeacherCreditsState().isCloseState()) {
                totalCredits += teacherCredits.getTotalCredits().subtract(teacherCredits.getMandatoryLessonHours()).doubleValue();
            } else if (!ProfessionalCategory.isMonitor(teacher, startPeriod)) {
                final ExecutionSemester executionSemester = startPeriod;
                TeacherService teacherService = TeacherService.getTeacherServiceByExecutionPeriod(teacher, executionSemester);
                if (teacherService != null) {
                    totalCredits += teacherService.getCredits();
                }
                totalCredits += calculateThesesCredits(teacher, startPeriod);
                totalCredits += calculateManagementFunctionsCredits(teacher, startPeriod);
                totalCredits += calculateServiceExemptionCredits(teacher, startPeriod);
                totalCredits -= calculateMandatoryLessonHours(teacher, startPeriod);
            }
            startPeriod = startPeriod.getNextExecutionPeriod();
        }
        return totalCredits;
    }

    public static double calculateServiceExemptionCredits(Teacher teacher, ExecutionSemester executionSemester) {
        Set<PersonContractSituation> personProfessionalExemptions =
                PersonContractSituation.getValidTeacherServiceExemptions(teacher, executionSemester);
        Interval semesterInterval =
                new Interval(executionSemester.getBeginDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay(),
                        executionSemester.getEndDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay());
        int lessonsDays = semesterInterval.toPeriod(PeriodType.days()).getDays();

        List<Interval> notYetOverlapedIntervals = new ArrayList<Interval>();
        List<Interval> newIntervals = new ArrayList<Interval>();
        notYetOverlapedIntervals.add(semesterInterval);

        Double mandatoryLessonHours = calculateMandatoryLessonHours(teacher, executionSemester);
        Double maxSneHours = mandatoryLessonHours;
        TeacherService teacherService = TeacherService.getTeacherServiceByExecutionPeriod(teacher, executionSemester);
        if (teacherService != null && teacherService.getReductionService() != null) {
            maxSneHours = Math.max(0, (mandatoryLessonHours - teacherService.getReductionServiceCredits().doubleValue()));
        }

        for (PersonContractSituation personContractSituation : personProfessionalExemptions) {
            LocalDate exemptionEnd =
                    personContractSituation.getServiceExemptionEndDate() == null ? semesterInterval.getEnd().toLocalDate() : personContractSituation
                            .getServiceExemptionEndDate();

            Interval exemptionInterval =
                    new Interval(personContractSituation.getBeginDate().toDateTimeAtStartOfDay(),
                            exemptionEnd.toDateTimeAtStartOfDay());

            PersonProfessionalExemption personProfessionalExemption = personContractSituation.getPersonProfessionalExemption();
            if (personContractSituation.countForCredits(semesterInterval)) {
                if (personProfessionalExemption != null) {
                    exemptionEnd =
                            personProfessionalExemption.getEndDate() == null ? semesterInterval.getEnd().toLocalDate() : personProfessionalExemption
                                    .getEndDate();
                    exemptionInterval =
                            new Interval(personProfessionalExemption.getBeginDate().toDateTimeAtStartOfDay(),
                                    exemptionEnd.toDateTimeAtStartOfDay());
                    if (personProfessionalExemption.getIsSabaticalOrEquivalent()) {
                        if (isSabbaticalForSemester(teacher, exemptionInterval, semesterInterval)) {
                            return maxSneHours;
                        } else {
                            continue;
                        }
                    }
                }
                for (Interval notYetOverlapedInterval : notYetOverlapedIntervals) {
                    Interval overlapInterval = exemptionInterval.overlap(notYetOverlapedInterval);
                    if (overlapInterval != null) {
                        newIntervals.addAll(getNotOverlapedIntervals(overlapInterval, notYetOverlapedInterval));
                    } else {
                        newIntervals.add(notYetOverlapedInterval);
                    }
                }
                notYetOverlapedIntervals.clear();
                notYetOverlapedIntervals.addAll(newIntervals);
                newIntervals.clear();
            }
        }

        int notOverlapedDays = 0;
        for (Interval interval : notYetOverlapedIntervals) {
            notOverlapedDays += interval.toPeriod(PeriodType.days()).getDays();
        }
        int overlapedDays = lessonsDays - notOverlapedDays;
        Double overlapedPercentage = round(Double.valueOf(overlapedDays) / Double.valueOf(lessonsDays));
        return round(overlapedPercentage * maxSneHours);
    }

    private static List<Interval> getNotOverlapedIntervals(Interval overlapInterval, Interval notYetOverlapedInterval) {
        List<Interval> intervals = new ArrayList<Interval>();
        LocalDate overlapIntervalStart = overlapInterval.getStart().toLocalDate();
        LocalDate overlapIntervalEnd = overlapInterval.getEnd().toLocalDate();
        LocalDate notYetOverlapedIntervalStart = notYetOverlapedInterval.getStart().toLocalDate();
        LocalDate notYetOverlapedIntervalEnd = notYetOverlapedInterval.getEnd().toLocalDate();

        if (overlapIntervalStart.equals(notYetOverlapedIntervalStart) && !overlapIntervalEnd.equals(notYetOverlapedIntervalEnd)) {
            intervals.add(new Interval(overlapInterval.getEnd().plusDays(1), notYetOverlapedInterval.getEnd()));

        } else if (!overlapIntervalStart.equals(notYetOverlapedIntervalStart)
                && overlapIntervalEnd.equals(notYetOverlapedIntervalEnd)) {
            intervals.add(new Interval(notYetOverlapedInterval.getStart(), overlapInterval.getStart().minusDays(1)));

        } else if (!overlapIntervalStart.equals(notYetOverlapedIntervalStart)
                && !overlapIntervalEnd.equals(notYetOverlapedIntervalEnd)) {
            intervals.add(new Interval(notYetOverlapedInterval.getStart(), overlapInterval.getStart().minusDays(1)));
            intervals.add(new Interval(overlapInterval.getEnd().plusDays(1), notYetOverlapedInterval.getEnd()));
        }

        return intervals;
    }

    private static boolean isSabbaticalForSemester(Teacher teacher, Interval exemptionInterval, Interval semesterPeriod) {
        double overlapPercentageThisSemester =
                calculateLessonsIntervalAndExemptionOverlapPercentage(semesterPeriod, exemptionInterval);
        if (overlapPercentageThisSemester == 1) {
            return true;
        }
        if (semesterPeriod.contains(exemptionInterval.getStart())) {
            return overlapPercentageThisSemester >= 0.5;
        }
        ExecutionSemester firstExecutionPeriod = ExecutionSemester.readByDateTime(exemptionInterval.getStart());
        Interval firstExecutionPeriodInterval =
                new Interval(firstExecutionPeriod.getBeginDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay(),
                        firstExecutionPeriod.getEndDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay());
        double overlapPercentageFirstSemester =
                calculateLessonsIntervalAndExemptionOverlapPercentage(firstExecutionPeriodInterval, exemptionInterval);
        return overlapPercentageFirstSemester < 0.5;
    }

    private static double calculateLessonsIntervalAndExemptionOverlapPercentage(Interval lessonsInterval,
            Interval exemptionInterval) {
        if (lessonsInterval != null) {
            Interval overlapInterval = lessonsInterval.overlap(exemptionInterval);
            if (overlapInterval != null) {
                int intersectedDays = overlapInterval.toPeriod(PeriodType.days()).getDays();
                return round(Double.valueOf(intersectedDays)
                        / Double.valueOf(lessonsInterval.toPeriod(PeriodType.days()).getDays()));
            }
        }
        return 0.0;
    }

    public static SortedSet<SupportLesson> getSupportLessonsOrderedByStartTimeAndWeekDay(Professorship professorship) {
        final SortedSet<SupportLesson> supportLessons =
                new TreeSet<SupportLesson>(SupportLesson.SUPPORT_LESSON_COMPARATOR_BY_HOURS_AND_WEEK_DAY);
        supportLessons.addAll(professorship.getSupportLessonsSet());
        return supportLessons;
    }

    public static List<Teacher> getAllTeachersFromUnit(Unit unit, YearMonthDay begin, YearMonthDay end) {
        List<Teacher> teachers = new ArrayList<Teacher>();
        List<Employee> employees = Employee.getAllWorkingEmployees(unit, begin, end);
        for (Employee employee : employees) {
            Teacher teacher = employee.getPerson().getTeacher();
            if (teacher != null
                    && PersonProfessionalData.hasAnyTeacherContractSituation(teacher, begin.toLocalDate(), end.toLocalDate())) {
                teachers.add(teacher);
            }
        }
        return teachers;
    }

}
