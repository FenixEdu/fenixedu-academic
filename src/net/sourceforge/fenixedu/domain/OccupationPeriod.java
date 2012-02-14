/*
 * Created on 14/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.CalendarUtil;

import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;

/**
 * @author Ana e Ricardo
 * 
 */
public class OccupationPeriod extends OccupationPeriod_Base {

    private OccupationPeriod() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public OccupationPeriod(Date startDate, Date endDate) {
	this();
	if (startDate == null || endDate == null || startDate.after(endDate)) {
	    throw new DomainException("error.occupationPeriod.invalid.dates");
	}
	this.setStart(startDate);
	this.setEnd(endDate);
    }

    public OccupationPeriod(YearMonthDay startDate, YearMonthDay endDate) {
	this();
	if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
	    throw new DomainException("error.occupationPeriod.invalid.dates");
	}
	setStartYearMonthDay(startDate);
	setEndYearMonthDay(endDate);
    }

    public OccupationPeriod(final YearMonthDay... yearMonthDays) {
	this(null, yearMonthDays);
    }

    protected OccupationPeriod(final OccupationPeriod previous, final YearMonthDay... yearMonthDays) {
	this();
	final int l = yearMonthDays.length;
	if (yearMonthDays == null || l < 2) {
	    throw new DomainException("error.occupationPeriod.invalid.dates");
	}

	final YearMonthDay start = yearMonthDays[0];
	final YearMonthDay end = yearMonthDays[1];
	setStartYearMonthDay(start);
	setEndYearMonthDay(end);

	if (l > 2) {
	    final YearMonthDay[] nextYearMonthDays = new YearMonthDay[l - 2];
	    System.arraycopy(yearMonthDays, 2, nextYearMonthDays, 0, l - 2);
	    new OccupationPeriod(this, nextYearMonthDays);
	}

	setPreviousPeriod(previous);
    }

    public void setNextPeriodWithoutChecks(OccupationPeriod nextPeriod) {
	if (nextPeriod != null && !nextPeriod.getStartYearMonthDay().isAfter(getEndYearMonthDay())) {
	    throw new DomainException("error.occupationPeriod.invalid.nextPeriod");
	}
	super.setNextPeriod(nextPeriod);
    }

    public void setPreviousPeriodWithoutChecks(OccupationPeriod previousPeriod) {
	if (previousPeriod != null && !previousPeriod.getEndYearMonthDay().isBefore(getStartYearMonthDay())) {
	    throw new DomainException("error.occupationPeriod.invalid.previousPeriod");
	}
	super.setPreviousPeriod(previousPeriod);
    }

    @Override
    public void setNextPeriod(OccupationPeriod nextPeriod) {
	if (!allNestedPeriodsAreEmpty()) {
	    throw new DomainException("error.occupationPeriod.previous.periods.not.empty");
	}
	if (nextPeriod != null && !nextPeriod.getStartYearMonthDay().isAfter(getEndYearMonthDay())) {
	    throw new DomainException("error.occupationPeriod.invalid.nextPeriod");
	}
	super.setNextPeriod(nextPeriod);
    }

    @Override
    public void setPreviousPeriod(OccupationPeriod previousPeriod) {
	if (!allNestedPeriodsAreEmpty()) {
	    throw new DomainException("error.occupationPeriod.next.periods.not.empty");
	}
	if (previousPeriod != null && !previousPeriod.getEndYearMonthDay().isBefore(getStartYearMonthDay())) {
	    throw new DomainException("error.occupationPeriod.invalid.previousPeriod");
	}
	super.setPreviousPeriod(previousPeriod);
    }

    public Calendar getStartDate() {
	if (this.getStart() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getStart());
	    return result;
	}
	return null;
    }

    public void setEndDate(Calendar calendar) {
	if (calendar != null) {
	    this.setEnd(calendar.getTime());
	} else {
	    this.setEnd(null);
	}
    }

    public Calendar getEndDate() {
	if (this.getEnd() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getEnd());
	    return result;
	}
	return null;
    }

    public void setStartDate(Calendar calendar) {
	if (calendar != null) {
	    this.setStart(calendar.getTime());
	} else {
	    this.setStart(null);
	}
    }

    public Calendar getEndDateOfComposite() {
	Calendar end = this.getEndDate();
	OccupationPeriod period = this.getNextPeriod();
	while (period != null) {
	    end = period.getEndDate();
	    period = period.getNextPeriod();
	}
	return end;
    }

    private boolean intersectPeriods(final Calendar start, final Calendar end) {
	return CalendarUtil.intersectDates(start, end, getStartDate(), getEndDate());
    }

    private boolean intersectPeriods(YearMonthDay start, YearMonthDay end) {
	return !getStartYearMonthDay().isAfter(end) && !getEndYearMonthDay().isBefore(start);
    }

    private boolean containsDay(YearMonthDay day) {
	return intersectPeriods(day, day);
    }

    public void delete() {
	if (allNestedPeriodsAreEmpty()) {
	    OccupationPeriod first = getFirstOccupationPeriodOfNestedPeriods();
	    first.deleteAllNestedPeriods();
	}
    }

    private void deleteAllNestedPeriods() {
	OccupationPeriod nextPeriod = getNextPeriod();

	super.setNextPeriod(null);
	super.setPreviousPeriod(null);
	removeRootDomainObject();
	deleteDomainObject();

	if (nextPeriod != null) {
	    nextPeriod.delete();
	}
    }

    public boolean allNestedPeriodsAreEmpty() {
	OccupationPeriod firstOccupationPeriod = getFirstOccupationPeriodOfNestedPeriods();
	if (!firstOccupationPeriod.isEmpty()) {
	    return false;
	}
	while (firstOccupationPeriod.getNextPeriod() != null) {
	    if (!firstOccupationPeriod.getNextPeriod().isEmpty()) {
		return false;
	    }
	    firstOccupationPeriod = firstOccupationPeriod.getNextPeriod();
	}
	return true;
    }

    private boolean isEmpty() {
	return getLessons().isEmpty() && getExecutionDegreesForExamsSpecialSeason().isEmpty()
		&& getExecutionDegreesForExamsFirstSemester().isEmpty() && getExecutionDegreesForExamsSecondSemester().isEmpty()
		&& getExecutionDegreesForLessonsFirstSemester().isEmpty()
		&& getExecutionDegreesForLessonsSecondSemester().isEmpty()
		&& getExecutionDegreesForGradeSubmissionNormalSeasonFirstSemester().isEmpty()
		&& getExecutionDegreesForGradeSubmissionNormalSeasonSecondSemester().isEmpty()
		&& getExecutionDegreesForGradeSubmissionSpecialSeason().isEmpty();
    }

    public OccupationPeriod getLastOccupationPeriodOfNestedPeriods() {
	OccupationPeriod occupationPeriod = this;
	while (occupationPeriod.getNextPeriod() != null) {
	    occupationPeriod = occupationPeriod.getNextPeriod();
	}
	return occupationPeriod;
    }

    private OccupationPeriod getFirstOccupationPeriodOfNestedPeriods() {
	OccupationPeriod occupationPeriod = this;
	while (occupationPeriod.getPreviousPeriod() != null) {
	    occupationPeriod = occupationPeriod.getPreviousPeriod();
	}
	return occupationPeriod;
    }

    public static OccupationPeriod readByDates(Date startDate, Date endDate) {
	for (OccupationPeriod occupationPeriod : RootDomainObject.getInstance().getOccupationPeriods()) {
	    if (occupationPeriod.getNextPeriod() == null && occupationPeriod.getPreviousPeriod() == null
		    && DateFormatUtil.equalDates("yyyy-MM-dd", occupationPeriod.getStart(), startDate)
		    && DateFormatUtil.equalDates("yyyy-MM-dd", occupationPeriod.getEnd(), endDate)) {
		return occupationPeriod;
	    }
	}
	return null;
    }

    /**
     * 
     * @param startDate
     * @param endDate
     * @param startDatePart2
     * @param endDatePart2
     * @return
     */
    public static OccupationPeriod getOccupationPeriod(final Calendar startDate, final Calendar endDate,
	    final Calendar startDatePart2, final Calendar endDatePart2) {
	OccupationPeriod occupationPeriod = OccupationPeriod.readOccupationPeriod(YearMonthDay.fromCalendarFields(startDate),
		YearMonthDay.fromCalendarFields(endDate), YearMonthDay.fromCalendarFields(startDatePart2),
		YearMonthDay.fromCalendarFields(endDatePart2));
	if (occupationPeriod == null) {
	    final OccupationPeriod next = startDatePart2 == null ? null : new OccupationPeriod(startDatePart2.getTime(),
		    endDatePart2.getTime());
	    occupationPeriod = new OccupationPeriod(startDate.getTime(), endDate.getTime());
	    occupationPeriod.setNextPeriod(next);
	}
	return occupationPeriod;
    }

    public static OccupationPeriod getOccupationPeriod(final YearMonthDay startDate, final YearMonthDay endDate,
	    final YearMonthDay startDatePart2, final YearMonthDay endDatePart2) {
	OccupationPeriod occupationPeriod = OccupationPeriod.readOccupationPeriod(startDate, endDate, startDatePart2,
		endDatePart2);
	if (occupationPeriod == null) {
	    final OccupationPeriod next = startDatePart2 == null ? null : new OccupationPeriod(startDatePart2, endDatePart2);
	    occupationPeriod = new OccupationPeriod(startDate, endDate);
	    occupationPeriod.setNextPeriod(next);
	}
	return occupationPeriod;
    }

    /**
     * Created because semantics of readOccupationPeriod is not well defined but
     * we can't touch it because they're afraid of consequences.
     * 
     * @param startDate
     * @param endDate
     * @param startDatePart2
     * @param endDatePart2
     * @return
     */
    public static OccupationPeriod getEqualOccupationPeriod(final YearMonthDay startDate, final YearMonthDay endDate,
	    final YearMonthDay startDatePart2, final YearMonthDay endDatePart2) {
	OccupationPeriod occupationPeriod = OccupationPeriod.readEqualOccupationPeriod(startDate, endDate, startDatePart2,
		endDatePart2);
	if (occupationPeriod == null) {
	    final OccupationPeriod next = startDatePart2 == null ? null : new OccupationPeriod(startDatePart2, endDatePart2);
	    occupationPeriod = new OccupationPeriod(startDate, endDate);
	    occupationPeriod.setNextPeriod(next);
	}
	return occupationPeriod;
    }

    public static OccupationPeriod readOccupationPeriod(YearMonthDay start, YearMonthDay end) {
	for (final OccupationPeriod occupationPeriod : RootDomainObject.getInstance().getOccupationPeriodsSet()) {
	    if (occupationPeriod.getNextPeriod() == null && occupationPeriod.getPreviousPeriod() == null
		    && occupationPeriod.getStartYearMonthDay().equals(start) && occupationPeriod.getEndYearMonthDay().equals(end)) {
		return occupationPeriod;
	    }
	}
	return null;
    }

    public static OccupationPeriod readEqualOccupationPeriod(YearMonthDay start, YearMonthDay end, final YearMonthDay startPart2,
	    final YearMonthDay endPart2) {
	for (final OccupationPeriod occupationPeriod : RootDomainObject.getInstance().getOccupationPeriodsSet()) {
	    if (occupationPeriod.isEqualTo(start, end, startPart2, endPart2)) {
		return occupationPeriod;
	    }
	}
	return null;
    }

    public static OccupationPeriod readOccupationPeriod(YearMonthDay start, YearMonthDay end, final YearMonthDay startPart2,
	    final YearMonthDay endPart2) {
	for (final OccupationPeriod occupationPeriod : RootDomainObject.getInstance().getOccupationPeriodsSet()) {
	    if (occupationPeriod.getNextPeriod() == null
		    && occupationPeriod.getPreviousPeriod() == null
		    && occupationPeriod.getStartYearMonthDay().equals(start)
		    && occupationPeriod.getEndYearMonthDay().equals(end)
		    && ((!occupationPeriod.hasNextPeriod() && startPart2 == null) || (occupationPeriod.getNextPeriod()
			    .getStartYearMonthDay().equals(startPart2) && occupationPeriod.getNextPeriod().getEndYearMonthDay()
			    .equals(endPart2)))) {
		return occupationPeriod;
	    }
	}
	return null;
    }

    public static OccupationPeriod readOccupationPeriod(final ExecutionCourse executionCourse, final YearMonthDay start,
	    final YearMonthDay end) {
	OccupationPeriod result = null;
	boolean ok = true;
	for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
	    final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
	    for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
		if (executionCourse.getExecutionYear() == executionDegree.getExecutionYear()) {
		    final OccupationPeriod occupationPeriod = executionDegree.getPeriodLessons(executionCourse
			    .getExecutionPeriod());
		    if (result == null) {
			result = occupationPeriod;
		    } else if (result != occupationPeriod) {
			ok = false;
		    }
		}
	    }
	}
	if (ok && result != null) {
	    return result;
	}
	for (final OccupationPeriod occupationPeriod : RootDomainObject.getInstance().getOccupationPeriodsSet()) {
	    if (occupationPeriod.getNextPeriod() == null && occupationPeriod.getPreviousPeriod() == null
		    && occupationPeriod.getStartYearMonthDay().equals(start) && occupationPeriod.getEndYearMonthDay().equals(end)) {
		return occupationPeriod;
	    }
	}
	return null;
    }

    public boolean nestedOccupationPeriodsIntersectDates(Calendar start, Calendar end) {
	OccupationPeriod firstOccupationPeriod = this;
	while (firstOccupationPeriod != null) {
	    if (firstOccupationPeriod.intersectPeriods(start, end)) {
		return true;
	    }
	    firstOccupationPeriod = firstOccupationPeriod.getNextPeriod();
	}
	return false;
    }

    public boolean nestedOccupationPeriodsIntersectDates(YearMonthDay start, YearMonthDay end) {
	OccupationPeriod firstOccupationPeriod = this;
	while (firstOccupationPeriod != null) {
	    if (firstOccupationPeriod.intersectPeriods(start, end)) {
		return true;
	    }
	    firstOccupationPeriod = firstOccupationPeriod.getNextPeriod();
	}
	return false;
    }

    public boolean nestedOccupationPeriodsContainsDay(YearMonthDay day) {
	OccupationPeriod firstOccupationPeriod = this;
	while (firstOccupationPeriod != null) {
	    if (firstOccupationPeriod.containsDay(day)) {
		return true;
	    }
	    firstOccupationPeriod = firstOccupationPeriod.getNextPeriod();
	}
	return false;
    }

    public boolean isGreater(OccupationPeriod period) {
	int periodDays = Days.daysBetween(period.getStartYearMonthDay(), period.getEndYearMonthDay()).getDays();
	int thisDays = Days.daysBetween(getStartYearMonthDay(), getEndYearMonthDay()).getDays();
	return thisDays > periodDays;
    }

    public boolean isEqualTo(OccupationPeriod period) {
	if (hasNextPeriod() && period.hasNextPeriod()) {
	    return isEqualTo(period.getStartYearMonthDay(), period.getEndYearMonthDay(), period.getNextPeriod()
		    .getStartYearMonthDay(), period.getNextPeriod().getEndYearMonthDay());
	}
	return getStartYearMonthDay().equals(period.getStartYearMonthDay())
		&& getEndYearMonthDay().equals(period.getEndYearMonthDay());
    }

    public boolean isEqualTo(YearMonthDay start, YearMonthDay end, final YearMonthDay startPart2, final YearMonthDay endPart2) {
	final boolean eqStart = getStartYearMonthDay().equals(start);
	final boolean eqEnd = getEndYearMonthDay().equals(end);
	final boolean eqNextPeriod = hasNextPeriod() ? (getNextPeriod().getStartYearMonthDay().equals(startPart2)
		&& getNextPeriod().getEndYearMonthDay().equals(endPart2) ? true : false) : true;
	return eqStart && eqEnd && eqNextPeriod;
    }

    public YearMonthDay getEndYearMonthDayWithNextPeriods() {
	return hasNextPeriod() ? getNextPeriod().getEndYearMonthDayWithNextPeriods() : getEndYearMonthDay();
    }

    public String asString() {
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	return String.format("[%s,%s]", sdf.format(getStartDate().getTime()), sdf.format(getEndDate().getTime()));
    }

    public Interval getIntervalWithNextPeriods() {
	return new Interval(getStartYearMonthDay().toLocalDate().toDateTimeAtStartOfDay(), getEndYearMonthDayWithNextPeriods()
		.toLocalDate().toDateTimeAtStartOfDay());
    }

    public static OccupationPeriod getOccupationPeriod(final YearMonthDay[] yearMonthDays) {
	for (final OccupationPeriod occupationPeriod : RootDomainObject.getInstance().getOccupationPeriodsSet()) {
	    if (occupationPeriod.matches(yearMonthDays)) {
		return occupationPeriod;
	    }
	}
	return null;
    }

    public boolean matches(final YearMonthDay[] yearMonthDays) {
	if (yearMonthDays == null || yearMonthDays.length < 2) {
	    return false;
	}
	final YearMonthDay start = yearMonthDays[0];
	final YearMonthDay end = yearMonthDays[1];
	if (start == null || !start.equals(getStartYearMonthDay())
		|| end == null || !end.equals(getEndYearMonthDay())) {
	    return false;
	}
	if (yearMonthDays.length > 2) {
	    final int l = yearMonthDays.length;
	    final YearMonthDay[] nextYearMonthDays = new YearMonthDay[l - 2];
	    System.arraycopy(yearMonthDays, 2, nextYearMonthDays, 0, l - 2);
	    return hasNextPeriod() && getNextPeriod().matches(nextYearMonthDays);
	}
	return !hasNextPeriod();
    }

    public YearMonthDay[] toYearMonthDays() {
	if (hasNextPeriod()) {
	    final YearMonthDay[] nextValue = getNextPeriod().toYearMonthDays();
	    final int l = nextValue.length;
	    final YearMonthDay[] result = new YearMonthDay[l + 2];
	    result[0] = getStartYearMonthDay();
	    result[1] = getEndYearMonthDay();
	    System.arraycopy(nextValue, 0, result, 2, l);
	    return result;
	} else {
	    return new YearMonthDay[] { getStartYearMonthDay(), getEndYearMonthDay() };
	}
    }

}
