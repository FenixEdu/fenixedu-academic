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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicSemesterCE;
import org.fenixedu.academic.dto.InfoExecutionPeriod;
import org.fenixedu.academic.service.services.commons.ReadExecutionPeriods;
import org.fenixedu.academic.util.Month;
import org.fenixedu.academic.util.PeriodState;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.StringNormalizer;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

/**
 * Created on 11/Fev/2003
 * 
 * @author Jo�o Mota
 * @author jpvl
 * 
 */
public class ExecutionSemester extends ExecutionSemester_Base implements Comparable<ExecutionSemester> {

    public static final Comparator<ExecutionSemester> COMPARATOR_BY_SEMESTER_AND_YEAR = new Comparator<ExecutionSemester>() {

        @Override
        public int compare(final ExecutionSemester o1, final ExecutionSemester o2) {
            final AcademicInterval ai1 = o1.getAcademicInterval();
            final AcademicInterval ai2 = o2.getAcademicInterval();
            final int c = ai1.getStartDateTimeWithoutChronology().compareTo(ai2.getStartDateTimeWithoutChronology());
            return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
        }

    };

    private ExecutionSemester() {
        super();
        setRootDomainObjectForExecutionPeriod(Bennu.getInstance());
    }

    public ExecutionSemester(ExecutionYear executionYear, AcademicInterval academicInterval, String name) {
        this();
        setExecutionYear(executionYear);
        setAcademicInterval(academicInterval);
        setBeginDateYearMonthDay(academicInterval.getBeginYearMonthDayWithoutChronology());
        setEndDateYearMonthDay(academicInterval.getEndYearMonthDayWithoutChronology());
        setName(name);
    }

    private void checkDatesIntersection(YearMonthDay begin, YearMonthDay end) {
        List<InfoExecutionPeriod> infoExecutionPeriods = ReadExecutionPeriods.run();
        if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {

            Collections.sort(infoExecutionPeriods, InfoExecutionPeriod.COMPARATOR_BY_YEAR_AND_SEMESTER);

            for (InfoExecutionPeriod infoExecutionPeriod : infoExecutionPeriods) {
                ExecutionSemester executionSemester = infoExecutionPeriod.getExecutionPeriod();
                YearMonthDay beginDate = executionSemester.getBeginDateYearMonthDay();
                YearMonthDay endDate = executionSemester.getEndDateYearMonthDay();
                if (begin.isAfter(endDate) || end.isBefore(beginDate) || executionSemester.equals(this)) {
                    continue;
                } else {
                    throw new DomainException("error.ExecutionPeriod.intersection.dates");
                }
            }
        }
    }

    public void editPeriod(YearMonthDay begin, YearMonthDay end) throws DomainException {
        if (begin == null || end == null || end.isBefore(begin)) {
            throw new DomainException("error.ExecutionPeriod.invalid.dates");
        }
        checkDatesIntersection(begin, end);
        setBeginDateYearMonthDay(begin);
        setEndDateYearMonthDay(end);
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
        if (executionYear == null) {
            throw new DomainException("error.ExecutionPeriod.empty.executionYear");
        }
        super.setExecutionYear(executionYear);
    }

    public Integer getSemester() {
        return getAcademicInterval().getAcademicSemesterOfAcademicYear();
    }

    public ExecutionSemester getNextExecutionPeriod() {
        AcademicSemesterCE semester = getAcademicInterval().plusSemester(1);
        return semester != null ? ExecutionSemester.getExecutionPeriod(semester) : null;
    }

    public List<Month> getSemesterMonths() {

        int monthStart = getAcademicInterval().getStart().getMonthOfYear();
        int monthEnd = getAcademicInterval().getEnd().getMonthOfYear();

        if (monthStart > monthEnd) {
            monthEnd += 12;
        }

        ArrayList<Month> result = new ArrayList<Month>();

        for (int i = monthStart; i <= monthEnd; i++) {
            result.add(Month.fromInt(i > 12 ? i - 12 : i));
        }

        return result;
    }

    public ExecutionSemester getPreviousExecutionPeriod() {
        AcademicSemesterCE semester = getAcademicInterval().minusSemester(1);
        return semester != null ? ExecutionSemester.getExecutionPeriod(semester) : null;
    }

    public boolean hasPreviousExecutionPeriod() {
        return getPreviousExecutionPeriod() != null;
    }

    public boolean hasNextExecutionPeriod() {
        return getNextExecutionPeriod() != null;
    }

    @Override
    public int compareTo(ExecutionSemester object) {
        if (object == null) {
            return 1;
        }
        return COMPARATOR_BY_SEMESTER_AND_YEAR.compare(this, object);
    }

    @Override
    public String getQualifiedName() {
        return new StringBuilder().append(this.getName()).append(" ").append(this.getExecutionYear().getYear()).toString();
    }

    public boolean containsDay(final Date day) {
        return containsDay(YearMonthDay.fromDateFields(day));
    }

    public boolean containsDay(final DateTime dateTime) {
        return containsDay(dateTime.toYearMonthDay());
    }

    public boolean containsDay(final YearMonthDay date) {
        return !getBeginDateYearMonthDay().isAfter(date) && !getEndDateYearMonthDay().isBefore(date);
    }

    public DateMidnight getThisMonday() {
        final DateTime beginningOfSemester = getBeginDateYearMonthDay().toDateTimeAtMidnight();
        final DateTime endOfSemester = getEndDateYearMonthDay().toDateTimeAtMidnight();

        if (beginningOfSemester.isAfterNow() || endOfSemester.isBeforeNow()) {
            return null;
        }

        final DateMidnight now = new DateMidnight();
        return now.withField(DateTimeFieldType.dayOfWeek(), 1);
    }

    public Interval getCurrentWeek() {
        final DateMidnight thisMonday = getThisMonday();
        return thisMonday == null ? null : new Interval(thisMonday, thisMonday.plusWeeks(1));
    }

    public Interval getPreviousWeek() {
        final DateMidnight thisMonday = getThisMonday();
        return thisMonday == null ? null : new Interval(thisMonday.minusWeeks(1), thisMonday);
    }

    public boolean isAfter(ExecutionSemester executionSemester) {
        return this.compareTo(executionSemester) > 0;
    }

    public boolean isAfterOrEquals(ExecutionSemester executionSemester) {
        return this.compareTo(executionSemester) >= 0;
    }

    public boolean isBefore(ExecutionSemester executionSemester) {
        return this.compareTo(executionSemester) < 0;
    }

    public boolean isBeforeOrEquals(ExecutionSemester executionSemester) {
        return this.compareTo(executionSemester) <= 0;
    }

    public boolean isOneYearAfter(final ExecutionSemester executionSemester) {
        final ExecutionSemester nextExecutionPeriod = executionSemester.getNextExecutionPeriod();
        return (nextExecutionPeriod == null) ? false : this == nextExecutionPeriod.getNextExecutionPeriod();
    }

    @Override
    public boolean isCurrent() {
        return getState().equals(PeriodState.CURRENT);
    }

    public boolean isClosed() {
        return getState().equals(PeriodState.CLOSED);
    }

    public boolean isNotOpen() {
        return getState().equals(PeriodState.NOT_OPEN);
    }

    public boolean isFor(final Integer semester) {
        return getSemester().equals(semester);
    }

    public boolean isFor(final String year) {
        return getExecutionYear().isFor(year);
    }

    public boolean isForSemesterAndYear(final Integer semester, final String year) {
        return isFor(semester) && isFor(year);
    }

    public boolean isInTimePeriod(final Date begin, final Date end) {
        return isInTimePeriod(YearMonthDay.fromDateFields(begin), YearMonthDay.fromDateFields(end));
    }

    public boolean isInTimePeriod(final YearMonthDay begin, final YearMonthDay end) {
        return getBeginDateYearMonthDay().isBefore(end) && getEndDateYearMonthDay().isAfter(begin);
    }

    public boolean isInTimePeriod(final LocalDate begin, final LocalDate end) {
        return getBeginDateYearMonthDay().isBefore(end) && getEndDateYearMonthDay().isAfter(begin);
    }

    public boolean isFirstOfYear() {
        return getExecutionYear().getFirstExecutionPeriod() == this;
    }

    public ExecutionCourse getExecutionCourseByInitials(final String courseInitials) {
        for (final ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
            if (executionCourse.getSigla().equalsIgnoreCase(courseInitials)) {
                return executionCourse;
            }
        }
        return null;
    }

    public List<ExecutionCourse> getExecutionCoursesWithNoCurricularCourses() {
        final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        for (final ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
            if (executionCourse.getAssociatedCurricularCoursesSet().isEmpty()) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    public List<ExecutionCourse> getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(
            final DegreeCurricularPlan degreeCurricularPlan, final CurricularYear curricularYear, final String name) {

        final String normalizedName = (name != null) ? StringNormalizer.normalize(name).replaceAll("%", ".*") : null;
        final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();

        for (final ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
            final String executionCourseName = StringNormalizer.normalize(executionCourse.getNome());
            if (normalizedName != null && executionCourseName.matches(normalizedName)) {
                if (executionCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(curricularYear, degreeCurricularPlan)) {
                    result.add(executionCourse);
                }
            }
        }
        return result;
    }

    public Collection<MarkSheet> getWebMarkSheetsNotPrinted() {
        final Collection<MarkSheet> markSheets = new HashSet<MarkSheet>();
        for (final MarkSheet sheet : getMarkSheetsSet()) {
            if (sheet.getSubmittedByTeacher() && !sheet.getPrinted()) {
                markSheets.add(sheet);
            }
        }
        return markSheets;
    }

    public Collection<MarkSheet> getWebMarkSheetsNotPrinted(Person person, DegreeCurricularPlan dcp) {
        final Collection<MarkSheet> markSheets = new HashSet<MarkSheet>();
        for (final MarkSheet sheet : getMarkSheetsSet()) {
            if (sheet.getSubmittedByTeacher() && !sheet.getPrinted()) {
                if ((dcp == null || sheet.isFor(dcp)) && sheet.getCurricularCourse().hasAnyExecutionDegreeFor(getExecutionYear())) {
                    ExecutionDegree executionDegree =
                            sheet.getCurricularCourse().getExecutionDegreeFor(getExecutionYear().getAcademicInterval());
                    if (AcademicAccessRule
                            .getDegreesAccessibleToFunction(AcademicOperationType.MANAGE_MARKSHEETS, person.getUser())
                            .collect(Collectors.toSet()).contains(executionDegree.getDegree())) {
                        markSheets.add(sheet);
                    }
                }
            }
        }
        return markSheets;
    }

    public Collection<ExecutionCourse> getExecutionCoursesWithDegreeGradesToSubmit(final DegreeCurricularPlan degreeCurricularPlan) {
        final Collection<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        for (final ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
            if (executionCourse.hasAnyDegreeGradeToSubmit(this, degreeCurricularPlan)) {
                executionCourses.add(executionCourse);
            }
        }
        return executionCourses;
    }

    public Collection<MarkSheet> getMarkSheetsToConfirm(final DegreeCurricularPlan degreeCurricularPlan) {
        final Collection<MarkSheet> markSheets = new ArrayList<MarkSheet>();
        for (final MarkSheet markSheet : this.getMarkSheetsSet()) {
            if ((degreeCurricularPlan == null || markSheet.getCurricularCourse().getDegreeCurricularPlan()
                    .equals(degreeCurricularPlan))
                    && markSheet.isNotConfirmed()) {
                markSheets.add(markSheet);
            }
        }
        return markSheets;
    }

    public List<Attends> getAttendsByDegreeCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan) {
        final List<Attends> attendsList = new ArrayList<Attends>();
        for (final ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
            for (final Attends attends : executionCourse.getAttendsSet()) {
                if (attends.getEnrolment() != null
                        && attends.getEnrolment().getDegreeCurricularPlanOfStudent().equals(degreeCurricularPlan)) {
                    attendsList.add(attends);
                }
            }
        }
        return attendsList;
    }

    public List<Enrolment> getEnrolmentsWithAttendsByDegreeCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan) {
        final List<Enrolment> enrolmentsList = new ArrayList<Enrolment>();
        for (final ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
            for (final Attends attends : executionCourse.getAttendsSet()) {
                if (attends.getEnrolment() != null
                        && attends.getEnrolment().getDegreeCurricularPlanOfStudent().equals(degreeCurricularPlan)) {
                    enrolmentsList.add(attends.getEnrolment());
                }
            }
        }
        return enrolmentsList;
    }

    public OccupationPeriod getLessonsPeriod() {
        OccupationPeriod lessonsPeriod = null;

        Collection<ExecutionDegree> degrees = getExecutionYear().getExecutionDegreesMatching(DegreeType::isPreBolonhaDegree);
        degrees.addAll(getExecutionYear().getExecutionDegreesMatching(DegreeType::isBolonhaDegree));
        degrees.addAll(getExecutionYear().getExecutionDegreesMatching(DegreeType::isIntegratedMasterDegree));
        degrees.addAll(getExecutionYear().getExecutionDegreesMatching(DegreeType::isBolonhaMasterDegree));

        for (ExecutionDegree executionDegree : degrees) {
            if (getSemester() == 1) {
                OccupationPeriod lessonsPeriodFirstSemester = executionDegree.getPeriodLessonsFirstSemester();
                lessonsPeriod =
                        (lessonsPeriod == null || lessonsPeriodFirstSemester.isGreater(lessonsPeriod)) ? lessonsPeriodFirstSemester : lessonsPeriod;
            } else {
                OccupationPeriod lessonsPeriodSecondSemester = executionDegree.getPeriodLessonsSecondSemester();
                lessonsPeriod =
                        (lessonsPeriod == null || lessonsPeriodSecondSemester.isGreater(lessonsPeriod)) ? lessonsPeriodSecondSemester : lessonsPeriod;
            }
        }

        return lessonsPeriod;
    }

    public String getYear() {
        return getExecutionYear().getYear();
    }

    public void delete() {
        if (!getAssociatedExecutionCoursesSet().isEmpty()) {
            throw new Error("cannot.delete.execution.period.because.execution.courses.exist");
        }
        if (!getEnrolmentsSet().isEmpty()) {
            throw new Error("cannot.delete.execution.period.because.enrolments.exist");
        }
        if (!getTeachersWithIncompleteEvaluationWorkGroupSet().isEmpty()) {
            throw new DomainException("error.executionPeriod.cannotDeleteExecutionPeriodUsedInAccessControl");
        }
        super.setExecutionYear(null);
        setRootDomainObjectForExecutionPeriod(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public int getNumberOfProfessorships(CurricularCourse curricularCourse) {
        int count = 0;
        for (ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionPeriod(this)) {
            count += executionCourse.getProfessorshipsSet().size();
        }
        return count;
    }

    public EnrolmentPeriod getEnrolmentPeriod(final Class<? extends EnrolmentPeriod> clazz,
            final DegreeCurricularPlan degreeCurricularPlan) {
        for (final EnrolmentPeriod enrolmentPeriod : getEnrolmentPeriodSet()) {
            if (enrolmentPeriod.getClass().equals(clazz) && enrolmentPeriod.getDegreeCurricularPlan() == degreeCurricularPlan) {
                return enrolmentPeriod;
            }
        }

        return null;
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------

    public static ExecutionSemester getExecutionPeriod(AcademicSemesterCE entry) {
        if (entry != null) {
            entry = (AcademicSemesterCE) entry.getOriginalTemplateEntry();
            for (final ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
                if (executionSemester.getAcademicInterval().getAcademicCalendarEntry().equals(entry)) {
                    return executionSemester;
                }
            }
        }
        return null;
    }

    private static transient ExecutionSemester currentExecutionPeriod = null;

    private static transient ExecutionSemester markSheetManagmentExecutionPeriod = null;

    static transient private ExecutionSemester firstBolonhaExecutionPeriod = null;

    static transient private ExecutionSemester firstBolonhaTransitionExecutionPeriod = null;

    static transient private ExecutionSemester firstEnrolmentsExecutionPeriod = null;

    public static ExecutionSemester readActualExecutionSemester() {
        if (currentExecutionPeriod == null || currentExecutionPeriod.getRootDomainObject() != Bennu.getInstance()
                || !currentExecutionPeriod.isCurrent()) {
            for (final ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
                if (executionSemester.isCurrent()) {
                    currentExecutionPeriod = executionSemester;
                    break;
                }
            }
        }
        return currentExecutionPeriod;
    }

    static private ExecutionSemester readFromProperties(ExecutionSemester executionSemester, String yearString,
            String semesterString) {
        if (executionSemester == null || executionSemester.getRootDomainObject() != Bennu.getInstance()) {

            if (yearString == null || yearString.length() == 0 || semesterString == null || semesterString.length() == 0) {
                executionSemester = null;
            } else {
                executionSemester = readBySemesterAndExecutionYear(Integer.valueOf(semesterString), yearString);
            }
        }

        return executionSemester;
    }

    public static ExecutionSemester readMarkSheetManagmentExecutionPeriod() {
        markSheetManagmentExecutionPeriod =
                readFromProperties(markSheetManagmentExecutionPeriod, FenixEduAcademicConfiguration.getConfiguration()
                        .getYearForFromMarkSheetManagment(), FenixEduAcademicConfiguration.getConfiguration()
                        .getSemesterForFromMarkSheetManagment());
        return markSheetManagmentExecutionPeriod;
    }

    static public ExecutionSemester readFirstBolonhaExecutionPeriod() {
        firstBolonhaExecutionPeriod =
                readFromProperties(firstBolonhaExecutionPeriod, FenixEduAcademicConfiguration.getConfiguration()
                        .getStartYearForBolonhaDegrees(), FenixEduAcademicConfiguration.getConfiguration()
                        .getStartSemesterForBolonhaDegrees());
        return firstBolonhaExecutionPeriod;
    }

    static public ExecutionSemester readFirstBolonhaTransitionExecutionPeriod() {
        firstBolonhaTransitionExecutionPeriod =
                readFromProperties(firstBolonhaTransitionExecutionPeriod, FenixEduAcademicConfiguration.getConfiguration()
                        .getStartYearForBolonhaTransition(), FenixEduAcademicConfiguration.getConfiguration()
                        .getStartSemesterForBolonhaTransition());
        return firstBolonhaTransitionExecutionPeriod;
    }

    static public ExecutionSemester readFirstEnrolmentsExecutionPeriod() {
        firstEnrolmentsExecutionPeriod =
                readFromProperties(firstEnrolmentsExecutionPeriod, FenixEduAcademicConfiguration.getConfiguration()
                        .getYearForFromEnrolments(), FenixEduAcademicConfiguration.getConfiguration()
                        .getSemesterForFromEnrolments());
        return firstEnrolmentsExecutionPeriod;
    }

    public static ExecutionSemester readFirstExecutionSemester() {
        final Set<ExecutionSemester> exeutionPeriods = Bennu.getInstance().getExecutionPeriodsSet();
        return exeutionPeriods.isEmpty() ? null : Collections.min(exeutionPeriods);
    }

    public static ExecutionSemester readLastExecutionSemester() {
        final Set<ExecutionSemester> exeutionPeriods = Bennu.getInstance().getExecutionPeriodsSet();
        final int size = exeutionPeriods.size();
        return size == 0 ? null : size == 1 ? exeutionPeriods.iterator().next() : Collections.max(exeutionPeriods);
    }

    public static List<ExecutionSemester> readNotClosedExecutionPeriods() {
        final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
        for (final ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
            if (!executionSemester.isClosed()) {
                result.add(executionSemester);
            }
        }
        return result;
    }

    public static List<ExecutionSemester> readNotOpenExecutionPeriods() {
        final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
        for (final ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
            if (!executionSemester.isNotOpen()) {
                result.add(executionSemester);
            }
        }
        return result;
    }

    public static List<ExecutionSemester> readPublicExecutionPeriods() {
        final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
        for (final ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
            if (!executionSemester.isNotOpen()) {
                result.add(executionSemester);
            }
        }
        return result;
    }

    public static List<ExecutionSemester> readNotClosedPublicExecutionPeriods() {
        final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
        for (final ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
            if (!executionSemester.isClosed() && !executionSemester.isNotOpen()) {
                result.add(executionSemester);
            }
        }
        return result;
    }

    public static List<ExecutionSemester> readExecutionPeriodsInTimePeriod(final LocalDate beginDate, final LocalDate endDate) {
        final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
        for (final ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
            if (executionSemester.isInTimePeriod(beginDate, endDate)) {
                result.add(executionSemester);
            }
        }
        return result;
    }

    @Deprecated
    public static List<ExecutionSemester> readExecutionPeriodsInTimePeriod(final Date beginDate, final Date endDate) {
        final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
        for (final ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
            if (executionSemester.isInTimePeriod(beginDate, endDate)) {
                result.add(executionSemester);
            }
        }
        return result;
    }

    public static List<ExecutionSemester> readExecutionPeriod(final YearMonthDay beginDate, final YearMonthDay endDate) {
        final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
        for (final ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
            if (executionSemester.getBeginDateYearMonthDay().isEqual(beginDate)
                    && executionSemester.getEndDateYearMonthDay().isEqual(endDate)) {
                result.add(executionSemester);
            }
        }
        return result;
    }

    public static ExecutionSemester readByNameAndExecutionYear(final String name, final String year) {
        for (final ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
            if (executionSemester.getName().equals(name) && executionSemester.isFor(year)) {
                return executionSemester;
            }
        }
        return null;
    }

    public static ExecutionSemester readBySemesterAndExecutionYear(final Integer semester, final String year) {
        for (final ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
            if (executionSemester.isForSemesterAndYear(semester, year)) {
                return executionSemester;
            }
        }
        return null;
    }

    public static ExecutionSemester readByDateTime(final DateTime dateTime) {
        final YearMonthDay yearMonthDay = dateTime.toYearMonthDay();
        return readByYearMonthDay(yearMonthDay);
    }

    public static ExecutionSemester readByYearMonthDay(final YearMonthDay yearMonthDay) {
        for (final ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
            if (executionSemester.containsDay(yearMonthDay)) {
                return executionSemester;
            }
        }
        return null;
    }

    public Stream<TeacherAuthorization> getTeacherAuthorizationStream() {
        return getTeacherAuthorizationSet().stream();
    }

    public Stream<TeacherAuthorization> getRevokedTeacherAuthorizationStream() {
        return getRevokedTeacherAuthorizationSet().stream();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends ExecutionInterval> E convert(final Class<E> input) {
        E result = null;

        if (ExecutionYear.class.equals(input)) {
            result = (E) getExecutionYear();
        } else if (ExecutionSemester.class.equals(input)) {
            result = (E) this;
        }

        return result;
    }
}
