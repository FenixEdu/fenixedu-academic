package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ReadAssiduousnessWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Attributes;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;
import net.sourceforge.fenixedu.util.IntervalUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Partial;

public class Leave extends Leave_Base {

    public static final Comparator<Leave> COMPARATORY_BY_DATE = new BeanComparator("date");

    public Leave(Assiduousness assiduousness, DateTime date, Duration dateDuration, JustificationMotive justificationMotive,
	    WorkWeek aplicableWeekDays, String notes, DateTime lastModificationDate, Employee modifiedBy, Integer oracleSequence) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setJustificationMotive(justificationMotive);
	setAplicableWeekDays(aplicableWeekDays);
	setAssiduousness(assiduousness);
	setDate(date);
	setNotes(notes);
	setDuration(dateDuration);
	setLastModifiedDate(lastModificationDate);
	setModifiedBy(modifiedBy);
	setOracleSequence(oracleSequence);
    }

    public Leave(Assiduousness assiduousness, DateTime date, Duration dateDuration, JustificationMotive justificationMotive,
	    WorkWeek aplicableWeekDays, String notes, LocalDate referenceDate, DateTime lastModificationDate, Employee modifiedBy) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setJustificationMotive(justificationMotive);
	setAplicableWeekDays(aplicableWeekDays);
	setAssiduousness(assiduousness);
	setDate(date);
	setNotes(notes);
	setDuration(dateDuration);
	setLastModifiedDate(lastModificationDate);
	setModifiedBy(modifiedBy);
	setOracleSequence(0);
	correctAssiduousnessClosedMonth(null);
	if (justificationMotive.getHasReferenceDate() && referenceDate == null) {
	    referenceDate = date.toLocalDate();
	}
	setReferenceDate(referenceDate);
    }

    public void modify(DateTime date, Duration dateDuration, JustificationMotive justificationMotive, WorkWeek aplicableWeekDays,
	    String notes, LocalDate referenceDate, Employee modifiedBy) {
	ClosedMonth closedMonth = ClosedMonth.getClosedMonthForBalance(date.toLocalDate());
	Interval oldInterval = getTotalInterval();
	if (justificationMotive.getHasReferenceDate() && referenceDate == null) {
	    referenceDate = date.toLocalDate();
	}
	if (closedMonth != null && closedMonth.getClosedForBalance()
		&& (getLastModifiedDate() == null || getLastModifiedDate().isBefore(closedMonth.getClosedForBalanceDate()))) {
	    anulate(modifiedBy);
	    Leave leave = new Leave(getAssiduousness(), date, dateDuration, justificationMotive, aplicableWeekDays, notes,
		    referenceDate, new DateTime(), modifiedBy);
	    leave.correctAssiduousnessClosedMonth(oldInterval);
	} else {
	    setDate(date);
	    setJustificationMotive(justificationMotive);
	    setAplicableWeekDays(aplicableWeekDays);
	    setNotes(notes);
	    setDuration(dateDuration);
	    setLastModifiedDate(new DateTime());
	    setModifiedBy(modifiedBy);
	    setOracleSequence(0);
	    setReferenceDate(referenceDate);
	    correctAssiduousnessClosedMonth(oldInterval);
	}
    }

    @Override
    public void anulate(Employee modifiedBy) {
	super.anulate(modifiedBy);
	correctAssiduousnessClosedMonth(null);
    }

    private void correctAssiduousnessClosedMonth(Interval oldInterval) {
	ClosedMonth correctionClosedMonth = ClosedMonth.getNextClosedMonth();
	Boolean correctNext = false;
	LocalDate date = getDate().toLocalDate();

	do {
	    correctNext = false;
	    ClosedMonth closedMonth = ClosedMonth.getClosedMonthForBalance(date);
	    if (closedMonth != null && closedMonth.getClosedForBalance()) {
		List<AssiduousnessClosedMonth> oldAssiduousnessClosedMonths = closedMonth
			.getAssiduousnessClosedMonths(getAssiduousness());
		for (AssiduousnessClosedMonth oldAssiduousnessClosedMonth : oldAssiduousnessClosedMonths) {
		    if (oldAssiduousnessClosedMonth != null) {
			EmployeeWorkSheet employeeWorkSheet = ReadAssiduousnessWorkSheet.run(getAssiduousness().getEmployee(),
				oldAssiduousnessClosedMonth.getBeginDate(), oldAssiduousnessClosedMonth.getEndDate());

			AssiduousnessClosedMonth newAssiduousnessClosedMonth = oldAssiduousnessClosedMonth;

			if (!oldAssiduousnessClosedMonth.hasEqualValues(employeeWorkSheet)) {
			    correctNext = true;
			    if (oldAssiduousnessClosedMonth.getIsCorrection()
				    && (!oldAssiduousnessClosedMonth.getCorrectedOnClosedMonth().getClosedYearMonth()
					    .isBefore(correctionClosedMonth.getClosedYearMonth()))) {
				oldAssiduousnessClosedMonth.correct(employeeWorkSheet);
			    } else {
				newAssiduousnessClosedMonth = new AssiduousnessClosedMonth(employeeWorkSheet,
					correctionClosedMonth, oldAssiduousnessClosedMonth);
			    }
			}

			List<ClosedMonthJustification> closedMonthJustifications = oldAssiduousnessClosedMonth
				.getClosedMonthJustifications();
			for (ClosedMonthJustification closedMonthJustification : closedMonthJustifications) {
			    if (!closedMonthJustification.hasEqualValues(employeeWorkSheet)) {
				correctNext = true;
				if (closedMonthJustification.getIsCorrection()
					&& (!closedMonthJustification.getCorrectedOnClosedMonth().getClosedYearMonth()
						.isBefore(correctionClosedMonth.getClosedYearMonth()))) {
				    closedMonthJustification.correct(employeeWorkSheet);
				} else {
				    new ClosedMonthJustification(employeeWorkSheet, correctionClosedMonth,
					    newAssiduousnessClosedMonth, closedMonthJustification.getJustificationMotive());
				}
			    }
			}

			Set<JustificationMotive> notCorrectedJustifications = new HashSet<JustificationMotive>();
			notCorrectedJustifications.addAll(getNotCorrected(employeeWorkSheet.getJustificationsDuration(),
				closedMonthJustifications));
			for (JustificationMotive justificationMotive : notCorrectedJustifications) {
			    new ClosedMonthJustification(employeeWorkSheet, correctionClosedMonth, oldAssiduousnessClosedMonth,
				    justificationMotive);
			}

			List<Interval> leavesIntervals = null;
			if (oldInterval != null) {
			    leavesIntervals = IntervalUtils.mergeIntervalLists(getTotalInterval(), oldInterval);
			} else {
			    leavesIntervals = new ArrayList<Interval>();
			    leavesIntervals.add(getTotalInterval());
			}
			Interval monthInterval = new Interval(
				oldAssiduousnessClosedMonth.getBeginDate().toDateTimeAtStartOfDay(), oldAssiduousnessClosedMonth
					.getEndDate().plusDays(1).toDateTimeAtStartOfDay());

			for (Interval interval : leavesIntervals) {
			    Interval overlapsInterval = interval.overlap(monthInterval);
			    correctAssiduousnessClosedDay(correctionClosedMonth, oldAssiduousnessClosedMonth, employeeWorkSheet,
				    newAssiduousnessClosedMonth, overlapsInterval);
			}

		    }
		}
	    }
	    date = date.plusMonths(1);
	} while (correctNext);
    }

    private void correctAssiduousnessClosedDay(ClosedMonth correctionClosedMonth,
	    AssiduousnessClosedMonth oldAssiduousnessClosedMonth, EmployeeWorkSheet employeeWorkSheet,
	    AssiduousnessClosedMonth newAssiduousnessClosedMonth, Interval overlapsInterval) {
	if (overlapsInterval != null) {
	    List<AssiduousnessExtraWork> assiduousnessExtraWorks = oldAssiduousnessClosedMonth.getAssiduousnessExtraWorks();
	    for (AssiduousnessExtraWork assiduousnessExtraWork : assiduousnessExtraWorks) {
		if (!assiduousnessExtraWork.hasEqualValues(employeeWorkSheet)) {
		    if (assiduousnessExtraWork.getIsCorrection()
			    && (!assiduousnessExtraWork.getCorrectedOnClosedMonth().getClosedYearMonth()
				    .isBefore(correctionClosedMonth.getClosedYearMonth()))) {
			assiduousnessExtraWork.correct(employeeWorkSheet);
		    } else {
			new AssiduousnessExtraWork(employeeWorkSheet, correctionClosedMonth, newAssiduousnessClosedMonth,
				assiduousnessExtraWork.getWorkScheduleType());
		    }
		}
	    }
	    Set<WorkScheduleType> notCorrected = new HashSet<WorkScheduleType>();
	    notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtra125Map(), assiduousnessExtraWorks));
	    notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtra150Map(), assiduousnessExtraWorks));
	    notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtra150WithLimitsMap(), assiduousnessExtraWorks));
	    notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtraNight160Map(), assiduousnessExtraWorks));
	    notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtraNight190Map(), assiduousnessExtraWorks));
	    notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtraNight190WithLimitsMap(),
		    assiduousnessExtraWorks));
	    notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtra25Map(), assiduousnessExtraWorks));
	    notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getUnjustifiedMap(), assiduousnessExtraWorks));
	    notCorrected.addAll(getNotCorrectedExtraWorkDays(employeeWorkSheet.getExtraWorkNightsMap(), assiduousnessExtraWorks));
	    for (WorkScheduleType workScheduleType : notCorrected) {
		new AssiduousnessExtraWork(employeeWorkSheet, correctionClosedMonth, oldAssiduousnessClosedMonth,
			workScheduleType);
	    }

	    for (LocalDate day = overlapsInterval.getStart().toLocalDate(); !day.isAfter(overlapsInterval.getEnd().toLocalDate()); day = day
		    .plusDays(1)) {
		AssiduousnessClosedDay assiduousnessClosedDay = oldAssiduousnessClosedMonth.getAssiduousnessClosedDay(day);
		WorkDaySheet workDaySheet = employeeWorkSheet.getWorkDaySheet(day);
		if (workDaySheet != null) {
		    if (assiduousnessClosedDay != null
			    && assiduousnessClosedDay.getIsCorrection()
			    && (!assiduousnessClosedDay.getCorrectedOnClosedMonth().getClosedYearMonth()
				    .isBefore(correctionClosedMonth.getClosedYearMonth()))) {
			assiduousnessClosedDay.correct(workDaySheet);
		    } else {
			new AssiduousnessClosedDay(newAssiduousnessClosedMonth, workDaySheet, correctionClosedMonth);
		    }
		}
	    }
	}
    }

    public DateTime getEndDate() {
	return getDate().plus(getDuration());
    }

    public LocalTime getEndLocalTime() {
	if (getJustificationMotive().getJustificationType().equals(JustificationType.OCCURRENCE)
		|| getJustificationMotive().getJustificationType().equals(JustificationType.MULTIPLE_MONTH_BALANCE)) {
	    return null;
	}
	return getEndDate().toLocalTime();
    }

    public LocalDate getEndLocalDate() {
	if (getJustificationMotive().getJustificationType().equals(JustificationType.BALANCE)) {
	    return null;
	}
	return getEndDate().toLocalDate();
    }

    @Override
    public Partial getPartialEndDate() {
	Partial p = new Partial();
	LocalDate y = getEndLocalDate();
	if (y != null) {
	    for (int i = 0; i < y.getFields().length; i++) {
		p = p.with(y.getFieldType(i), y.getValue(i));
	    }
	}
	LocalTime t = getEndLocalTime();
	if (t != null) {
	    for (int i = 0; i < t.getFields().length; i++) {
		p = p.with(t.getFieldType(i), t.getValue(i));
	    }
	}
	return p;
    }

    public Interval getTotalInterval() {
	return new Interval(getDate().getMillis(), getEndDate().getMillis() + 1);
    }

    // Check if the Leave occured in a particular date
    public boolean occuredInDate(LocalDate date) {
	return ((getDate().toLocalDate().isBefore(date) || getDate().toLocalDate().isEqual(date)) && (getEndDate().toLocalDate()
		.isAfter(date) || getEndDate().toLocalDate().isEqual(date)));
    }

    // Converts a Leave interval to TimePoint
    public List<TimePoint> toTimePoints(AttributeType attribute) {
	List<TimePoint> timePointList = new ArrayList<TimePoint>();
	EnumSet<AttributeType> attributesToAdd = EnumSet.of(attribute, AttributeType.JUSTIFICATION);
	timePointList.add(new TimePoint(getDate().toLocalTime(), new Attributes(attributesToAdd)));
	timePointList.add(new TimePoint((getDate().plus(getDuration())).toLocalTime(), new Attributes(attributesToAdd)));
	return timePointList;
    }

    public static void plotListInTimeline(List<Leave> leaveList, Iterator<AttributeType> attributesIt, Timeline timeline) {
	List<TimePoint> pointList = new ArrayList<TimePoint>();
	for (Leave leave : leaveList) {
	    // if (leave.getJustificationMotive().getJustificationType() ==
	    // JustificationType.BALANCE) {
	    // pointList.addAll(leave.toTimePoints(AttributeType.BALANCE));
	    // } else {
	    AttributeType at = attributesIt.next();
	    pointList.addAll(leave.toTimePoints(at));
	}
	timeline.plotList(pointList);
    }

    // Returns true if the justification is for the day
    public boolean justificationForDay(LocalDate day) {
	DateTime dayAtMidnight = day.toDateTimeAtStartOfDay();
	if (getDate().equals(getEndDate()) && dayAtMidnight.equals(getDate())) {
	    return true;
	}
	Interval justificationInterval = getTotalInterval();
	if (justificationInterval.contains(dayAtMidnight)) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean isLeave() {
	return true;
    }

    public int getWorkDaysBetween(Interval interval) {
	int days = 0;
	HashMap<LocalDate, WorkSchedule> workSchedules = getAssiduousness().getWorkSchedulesBetweenDates(
		interval.getStart().toLocalDate(), interval.getEnd().toLocalDate());
	for (LocalDate thisDay = interval.getStart().toLocalDate(); !thisDay.isAfter(interval.getEnd().toLocalDate()); thisDay = thisDay
		.plusDays(1)) {
	    if (workSchedules.get(thisDay) != null && (!getAssiduousness().isHoliday(thisDay)) && occuredInDate(thisDay)) {
		days++;
	    }
	}
	return days;
    }

    public int getWorkDaysBetween(int year) {
	LocalDate begin = new LocalDate(year, 1, 1);
	LocalDate end = new LocalDate(year, 12, 31);
	Interval yearInterval = new Interval(begin.toDateTimeAtMidnight(), end.toDateTimeAtMidnight());
	Interval overlap = getTotalInterval().overlap(yearInterval);
	if (overlap != null) {
	    return getWorkDaysBetween(overlap);
	}
	return 0;
    }

    @Override
    public Set<Partial> getYearMonths() {
	final DateTime start = getDate();
	final Set<Partial> result = super.getYearMonths();
	for (DateTime dateTime = getEndDate(); !matchYearMonth(dateTime, start); dateTime = dateTime.minusMonths(1)) {
	    final Partial partial = getPartial(dateTime);
	    result.add(partial);
	}
	return result;
    }

    private boolean matchYearMonth(DateTime dateTime1, DateTime dateTime2) {
	return dateTime1.getYear() == dateTime2.getYear() && dateTime1.getMonthOfYear() == dateTime2.getMonthOfYear();
    }

    @Override
    public void setDuration(Duration duration) {
	super.setDuration(duration);
	getAssiduousness().updateAssiduousnessRecordMonthIndex(this);
    }

    public boolean isEqual(DateTime dateTime, Duration duration, JustificationMotive justificationMotive, String notes,
	    LocalDate referenceDate, boolean isAnulated) {
	return isEqual(dateTime, justificationMotive, isAnulated) && getDuration().equals(duration) && equals(getNotes(), notes)
		&& equals(getReferenceDate(), referenceDate);
    }

    public boolean isEqual(DateTime dateTime, JustificationMotive justificationMotive, String notes, LocalDate referenceDate,
	    boolean isAnulated) {
	return isEqual(dateTime, justificationMotive, isAnulated) && equals(getNotes(), notes)
		&& equals(getReferenceDate(), referenceDate);
    }

    private boolean equals(Object o1, Object o2) {
	if (o1 == null) {
	    return o2 == null;
	}
	return o1.equals(o2);
    }
}
