package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota
 * @author jpvl
 * 
 */
public class ExecutionPeriod extends ExecutionPeriod_Base implements Comparable {

    public static final Comparator EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR = new ComparatorChain();
    static {
	final ComparatorChain chain = (ComparatorChain) EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR;
	chain.addComparator(new BeanComparator("executionYear.year"));
	chain.addComparator(new BeanComparator("semester"));
    }

    public ExecutionPeriod() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public int compareTo(Object object) {
	final ExecutionPeriod executionPeriod = (ExecutionPeriod) object;
	final ExecutionYear executionYear = executionPeriod.getExecutionYear();

	if (getExecutionYear() == executionYear) {
	    return getSemester().compareTo(executionPeriod.getSemester());
	} else {
	    return getExecutionYear().compareTo(executionYear);
	}
    }

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
	return !(this.getBeginDateYearMonthDay().isAfter(date) || this.getEndDateYearMonthDay().isBefore(date));
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

    public boolean isAfter(ExecutionPeriod executionPeriod) {
	return this.compareTo(executionPeriod) > 0;
    }

    public boolean isAfterOrEquals(ExecutionPeriod executionPeriod) {
	return this.compareTo(executionPeriod) >= 0;
    }

    public boolean isBefore(ExecutionPeriod executionPeriod) {
	return this.compareTo(executionPeriod) < 0;
    }

    public boolean isBeforeOrEquals(ExecutionPeriod executionPeriod) {
	return this.compareTo(executionPeriod) <= 0;
    }

    public boolean isOneYearAfter(final ExecutionPeriod executionPeriod) {
	final ExecutionPeriod nextExecutionPeriod = executionPeriod.getNextExecutionPeriod();
	return (nextExecutionPeriod == null) ? false : this == nextExecutionPeriod.getNextExecutionPeriod();
    }

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

    public ExecutionCourse getExecutionCourseByInitials(final String courseInitials) {
	for (final ExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
	    if (executionCourse.getSigla().equalsIgnoreCase(courseInitials)) {
		return executionCourse;
	    }
	}
	return null;
    }

    public List<ExecutionCourse> getExecutionCoursesWithNoCurricularCourses() {
	final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
	for (final ExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
	    if (!executionCourse.hasAnyAssociatedCurricularCourses()) {
		result.add(executionCourse);
	    }
	}
	return result;
    }

    public List<ExecutionCourse> getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(
	    final DegreeCurricularPlan degreeCurricularPlan, final CurricularYear curricularYear, final String name) {

	final String normalizedName = (name != null) ? StringNormalizer.normalize(name).toLowerCase().replaceAll("%", ".*")
		: null;
	final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();

	for (final ExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
	    final String executionCourseName = StringNormalizer.normalize(executionCourse.getNome()).toLowerCase();
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
	for (final MarkSheet sheet : getMarkSheets()) {
	    if (sheet.getSubmittedByTeacher() && !sheet.getPrinted()) {
		markSheets.add(sheet);
	    }
	}
	return markSheets;
    }

    public Collection<MarkSheet> getWebMarkSheetsNotPrintedByAdministraticeOfficeAndCampus(
	    AdministrativeOffice administrativeOffice, Campus campus) {
	final Collection<MarkSheet> markSheets = new HashSet<MarkSheet>();
	for (final MarkSheet sheet : getMarkSheets()) {
	    if (sheet.getSubmittedByTeacher() && !sheet.getPrinted()) {
		if (sheet.getAdministrativeOfficeType() == administrativeOffice.getAdministrativeOfficeType()
			&& sheet.getCurricularCourse().getDegreeCurricularPlan().getExecutionDegreeByYearAndCampus(
				getExecutionYear(), campus) != null) {
		    markSheets.add(sheet);
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
	    if ((degreeCurricularPlan == null || markSheet.getCurricularCourse().getDegreeCurricularPlan().equals(
		    degreeCurricularPlan))
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
		if (attends.hasEnrolment()
			&& attends.getEnrolment().getDegreeCurricularPlanOfStudent().equals(degreeCurricularPlan)) {
		    attendsList.add(attends);
		}
	    }
	}
	return attendsList;
    }

    public void checkValidCreditsPeriod(RoleType roleType) {
	if (roleType != RoleType.SCIENTIFIC_COUNCIL) {
	    Interval validCreditsPerid = getValidCreditsPeriod(roleType);
	    if (validCreditsPerid == null) {
		throw new DomainException("message.invalid.credits.period2");
	    } else if (!validCreditsPerid.containsNow()) {
		throw new DomainException("message.invalid.credits.period", validCreditsPerid.getStart().toString(
			"dd-MM-yyy HH:mm"), validCreditsPerid.getEnd().toString("dd-MM-yyy HH:mm"));
	    }
	}
    }

    public Interval getValidCreditsPeriod(RoleType roleType) {
	switch (roleType) {
	case DEPARTMENT_MEMBER:
	    if (getTeacherCreditsPeriodBegin() != null && getTeacherCreditsPeriodEnd() != null) {
		return new Interval(getTeacherCreditsPeriodBegin(), getTeacherCreditsPeriodEnd());
	    } else {
		return null;
	    }
	case DEPARTMENT_ADMINISTRATIVE_OFFICE:
	    if (getDepartmentAdmOfficeCreditsPeriodBegin() != null && getDepartmentAdmOfficeCreditsPeriodEnd() != null) {
		return new Interval(getDepartmentAdmOfficeCreditsPeriodBegin(), getDepartmentAdmOfficeCreditsPeriodEnd());
	    } else {
		return null;
	    }
	default:
	    throw new DomainException("invalid.role.type");
	}
    }

    public OccupationPeriod getLessonsPeriod() {

	Collection<ExecutionDegree> degrees = getExecutionYear().getExecutionDegreesByType(DegreeType.DEGREE);
	if (degrees.isEmpty()) {
	    degrees.addAll(getExecutionYear().getExecutionDegreesByType(DegreeType.BOLONHA_DEGREE));
	}
	if (degrees.isEmpty()) {
	    degrees.addAll(getExecutionYear().getExecutionDegreesByType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE));
	}
	if (degrees.isEmpty()) {
	    degrees.addAll(getExecutionYear().getExecutionDegreesByType(DegreeType.BOLONHA_MASTER_DEGREE));
	}

	for (ExecutionDegree executionDegree : degrees) {
	    if (getSemester() == 1) {
		return executionDegree.getPeriodLessonsFirstSemester();
	    } else {
		return executionDegree.getPeriodLessonsSecondSemester();
	    }
	}

	return null;
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
	removeExecutionYear();
	removeRootDomainObject();
	removePreviousExecutionPeriod();
	removeNextExecutionPeriod();
	deleteDomainObject();
    }

    public int getNumberOfProfessorships(CurricularCourse curricularCourse) {
	int count = 0;
	for (ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionPeriod(this)) {
	    count += executionCourse.getProfessorshipsCount();
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

    private static transient ExecutionPeriod currentExecutionPeriod = null;

    public static ExecutionPeriod readActualExecutionPeriod() {
	if (currentExecutionPeriod == null || currentExecutionPeriod.getRootDomainObject() != RootDomainObject.getInstance()
		|| !currentExecutionPeriod.isCurrent()) {
	    for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance().getExecutionPeriodsSet()) {
		if (executionPeriod.isCurrent()) {
		    currentExecutionPeriod = executionPeriod;
		    break;
		}
	    }
	}
	return currentExecutionPeriod;
    }

    public static ExecutionPeriod readFirstExecutionPeriod() {
	final Set<ExecutionPeriod> exeutionPeriods = RootDomainObject.getInstance().getExecutionPeriodsSet();
	return exeutionPeriods.isEmpty() ? null : Collections.min(exeutionPeriods);
    }

    public static ExecutionPeriod readLastExecutionPeriod() {
	final Set<ExecutionPeriod> exeutionPeriods = RootDomainObject.getInstance().getExecutionPeriodsSet();
	final int size = exeutionPeriods.size();
	return size == 0 ? null : size == 1 ? exeutionPeriods.iterator().next() : Collections.max(exeutionPeriods);
    }

    public static List<ExecutionPeriod> readNotClosedExecutionPeriods() {
	final List<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();
	for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance().getExecutionPeriodsSet()) {
	    if (!executionPeriod.isClosed()) {
		result.add(executionPeriod);
	    }
	}
	return result;
    }

    public static List<ExecutionPeriod> readPublicExecutionPeriods() {
	final List<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();
	for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance().getExecutionPeriodsSet()) {
	    if (!executionPeriod.isNotOpen()) {
		result.add(executionPeriod);
	    }
	}
	return result;
    }

    public static List<ExecutionPeriod> readNotClosedPublicExecutionPeriods() {
	final List<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();
	for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance().getExecutionPeriodsSet()) {
	    if (!executionPeriod.isClosed() && !executionPeriod.isNotOpen()) {
		result.add(executionPeriod);
	    }
	}
	return result;
    }

    public static List<ExecutionPeriod> readExecutionPeriodsInTimePeriod(final Date beginDate, final Date endDate) {
	final List<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();
	for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance().getExecutionPeriodsSet()) {
	    if (executionPeriod.isInTimePeriod(beginDate, endDate)) {
		result.add(executionPeriod);
	    }
	}
	return result;
    }

    public static ExecutionPeriod readByNameAndExecutionYear(final String name, final String year) {
	for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance().getExecutionPeriodsSet()) {
	    if (executionPeriod.getName().equals(name) && executionPeriod.isFor(year)) {
		return executionPeriod;
	    }
	}
	return null;
    }

    public static ExecutionPeriod readBySemesterAndExecutionYear(final Integer semester, final String year) {
	for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance().getExecutionPeriodsSet()) {
	    if (executionPeriod.isForSemesterAndYear(semester, year)) {
		return executionPeriod;
	    }
	}
	return null;
    }

    public static ExecutionPeriod readByDateTime(final DateTime dateTime) {
	final YearMonthDay yearMonthDay = dateTime.toYearMonthDay();
	for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance().getExecutionPeriodsSet()) {
	    if (executionPeriod.containsDay(yearMonthDay)) {
		return executionPeriod;
	    }
	}
	return null;
    }

}
