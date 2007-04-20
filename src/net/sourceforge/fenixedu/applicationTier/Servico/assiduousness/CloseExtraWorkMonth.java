package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonthJustification;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.util.AnulationState;
import net.sourceforge.fenixedu.domain.assiduousness.util.DayType;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.DateMidnight;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

public class CloseExtraWorkMonth extends Service {

    public String run(ClosedMonth closedMonth) {
	YearMonthDay beginDate = new YearMonthDay().withField(DateTimeFieldType.year(),
		closedMonth.getClosedYearMonth().get(DateTimeFieldType.year())).withField(
		DateTimeFieldType.monthOfYear(),
		closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear())).withField(
		DateTimeFieldType.dayOfMonth(), 1);
	YearMonthDay endDate = new YearMonthDay().withField(DateTimeFieldType.year(),
		closedMonth.getClosedYearMonth().get(DateTimeFieldType.year())).withField(
		DateTimeFieldType.monthOfYear(),
		closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear())).withField(
		DateTimeFieldType.dayOfMonth(), beginDate.dayOfMonth().getMaximumValue());
	StringBuilder result = new StringBuilder();
	HashMap<Assiduousness, List<Leave>> allLeaves = getLeaves(beginDate, endDate);
	HashMap<Assiduousness, AssiduousnessClosedMonth> allAssiduousnessClosedMonths = getAssiduousnessClosedMonths(closedMonth);
	for (Assiduousness assiduousness : rootDomainObject.getAssiduousnesss()) {
	    if (assiduousness.isStatusActive(beginDate, endDate)) {
		result
			.append(getAssiduousnessMonthBalance(assiduousness,
				allLeaves.get(assiduousness), allAssiduousnessClosedMonths
					.get(assiduousness), closedMonth, beginDate, endDate));
	    }
	}

	// result.append(getExtraWorkMonthBalance(assiduousness,
        // allLeaves.get(assiduousness),
	// beginDate, endDate));
	//        
	return result.toString();
    }

    private String getAssiduousnessMonthBalance(Assiduousness assiduousness, List<Leave> leaves,
	    AssiduousnessClosedMonth assiduousnessClosedMonth, ClosedMonth closedMonth,
	    YearMonthDay beginDate, YearMonthDay endDate) {
	StringBuilder result = new StringBuilder();
	if (leaves != null && !leaves.isEmpty()) {
	    Collections.sort(leaves, AssiduousnessRecord.COMPARATOR_BY_DATE);
	    for (Leave leave : leaves) {
		if (leave.getJustificationMotive().getJustificationType().equals(
			JustificationType.OCCURRENCE)) {
		    result.append(getLeaveLine(leave, beginDate, endDate));
		}
	    }
	}
	HashMap<JustificationMotive, Duration> pastJustificationsDurations = assiduousnessClosedMonth
		.getPastJustificationsDurations();
	for (ClosedMonthJustification closedMonthJustification : assiduousnessClosedMonth
		.getClosedMonthJustifications()) {
	    Duration pastDurationToDiscount = Duration.ZERO;
	    Duration pastDuration = pastJustificationsDurations.get(closedMonthJustification
		    .getJustificationMotive());
	    if (pastDuration != null) {
		Period pastToDiscount = Period.hours(
			pastDuration.toPeriod().getHours()
				% Assiduousness.normalWorkDayDuration.toPeriod().getHours())
			.withMinutes(pastDuration.toPeriod().getMinutes());

		pastDurationToDiscount = pastToDiscount.toDurationFrom(new DateMidnight());
	    }
	    pastDurationToDiscount = pastDurationToDiscount.plus(closedMonthJustification
		    .getJustificationDuration());
	    int justificationDays = pastDurationToDiscount.toPeriod().getHours()
		    / Assiduousness.normalWorkDayDuration.toPeriod().getHours();
	    result.append(getLeaveLine(assiduousness, closedMonthJustification.getJustificationMotive(),
		    closedMonth, justificationDays));
	}
	return result.toString();
    }

    private StringBuilder getLeaveLine(Assiduousness assiduousness,
	    JustificationMotive justificationMotive, ClosedMonth closedMonth, int justificationDays) {
	StringBuilder line = new StringBuilder();
	Interval justificationInterval = getJustificationInterval(assiduousness, justificationMotive,
		closedMonth, justificationDays);
	YearMonthDay start = justificationInterval.getStart().toYearMonthDay();
	YearMonthDay end = justificationInterval.getEnd().toYearMonthDay();
	line.append(">> " + start.getYear()).append(" ");
	line.append(start.getMonthOfYear() + 1).append(" ");
	DecimalFormat f = new DecimalFormat("000000");
	line.append(f.format(assiduousness.getEmployee().getEmployeeNumber())).append(" ");
	line.append("F").append(" ");
	line.append(justificationMotive.getIdInternal()).append(" ");
	line.append(start).append(" ");
	line.append(end).append(" ");
	int days = Days.daysBetween(start, end).getDays() + 1;
	line.append(days).append("00 ");
	Interval interval = new Interval(start.toDateTimeAtMidnight().getMillis(), end
		.toDateTimeAtMidnight().getMillis());
	line.append(getUtilDays(assiduousness, interval)).append("00\r\n");
	return line;
    }

    private StringBuilder getLeaveLine(Leave leave, YearMonthDay beginDate, YearMonthDay endDate) {
	StringBuilder line = new StringBuilder();
	YearMonthDay start = beginDate;
	YearMonthDay end = endDate;
	if (leave.getDate().toYearMonthDay().isAfter(beginDate)) {
	    start = leave.getDate().toYearMonthDay();
	}
	if (leave.getEndYearMonthDay() == null) {
	    end = leave.getDate().toYearMonthDay();
	} else if (leave.getEndYearMonthDay().isBefore(endDate)) {
	    end = leave.getEndYearMonthDay();
	}
	line.append(start.getYear()).append(" ");
	line.append(start.getMonthOfYear() + 1).append(" ");
	DecimalFormat f = new DecimalFormat("000000");
	line.append(f.format(leave.getAssiduousness().getEmployee().getEmployeeNumber())).append(" ");
	line.append("F").append(" ");
	line.append(leave.getJustificationMotive().getIdInternal()).append(" ");
	line.append(start).append(" ");
	line.append(end).append(" ");
	int days = Days.daysBetween(start, end).getDays() + 1;
	line.append(days).append("00 ");
	Interval interval = new Interval(start.toDateTimeAtMidnight().getMillis(), end
		.toDateTimeAtMidnight().getMillis());
	line.append(getUtilDays(leave.getAssiduousness(), interval)).append("00\r\n");
	return line;
    }

    private int getUtilDays(Assiduousness assiduousness, Interval interval) {
	int days = 0;
	for (YearMonthDay thisDay = interval.getStart().toYearMonthDay(); !thisDay.isAfter(interval
		.getEnd().toYearMonthDay()); thisDay = thisDay.plusDays(1)) {
	    WeekDay dayOfWeek = WeekDay.fromJodaTimeToWeekDay(thisDay.toDateTimeAtMidnight());
	    if ((!dayOfWeek.equals(WeekDay.SATURDAY)) && (!dayOfWeek.equals(WeekDay.SUNDAY))
		    && (!assiduousness.isHoliday(thisDay))) {
		days++;
	    }
	}
	return days;
    }

    private Interval getJustificationInterval(Assiduousness assiduousness,
	    JustificationMotive justificationMotive, ClosedMonth closedMonth, int daysNumber) {
	YearMonthDay lastDay = new YearMonthDay(closedMonth.getClosedYearMonth().get(
		DateTimeFieldType.year()), closedMonth.getClosedYearMonth().get(
		DateTimeFieldType.monthOfYear()) + 1, 1);
	WeekDay dayOfWeek = WeekDay.fromJodaTimeToWeekDay(lastDay.toDateTimeAtMidnight());
	do {
	    lastDay = lastDay.minusDays(1);
	    dayOfWeek = WeekDay.fromJodaTimeToWeekDay(lastDay.toDateTimeAtMidnight());
	} while ((dayOfWeek.equals(WeekDay.SATURDAY)) || (dayOfWeek.equals(WeekDay.SUNDAY))
		|| (assiduousness.isHoliday(lastDay)));
	YearMonthDay firstDay = lastDay;
	if (justificationMotive.getDayType().equals(DayType.WORKDAY)) {
	    while (daysNumber != 1) {
		dayOfWeek = WeekDay.fromJodaTimeToWeekDay(firstDay.toDateTimeAtMidnight());
		if (!(dayOfWeek.equals(WeekDay.SATURDAY)) && !(dayOfWeek.equals(WeekDay.SUNDAY))
			&& !(assiduousness.isHoliday(firstDay))) {
		    daysNumber--;
		}
		firstDay = firstDay.minusDays(1);
	    }
	} else {
	    firstDay = firstDay.minusDays(daysNumber);
	}
	return new Interval(firstDay.toDateTimeAtMidnight(), lastDay.toDateTimeAtMidnight());
    }

    public HashMap<Assiduousness, List<Leave>> getLeaves(YearMonthDay beginDate, YearMonthDay endDate) {
	HashMap<Assiduousness, List<Leave>> assiduousnessLeaves = new HashMap<Assiduousness, List<Leave>>();
	Interval interval = new Interval(beginDate.toDateTimeAtMidnight(),
		Assiduousness.defaultEndWorkDay.toDateTime(endDate.toDateMidnight()));
	for (AssiduousnessRecord assiduousnessRecord : rootDomainObject.getAssiduousnessRecords()) {
	    if (assiduousnessRecord.isLeave() && !assiduousnessRecord.isAnulated()) {
		Interval leaveInterval = new Interval(assiduousnessRecord.getDate(),
			((Leave) assiduousnessRecord).getEndDate().plusSeconds(1));
		if (leaveInterval.overlaps(interval)) {

		    List<Leave> leavesList = assiduousnessLeaves.get(assiduousnessRecord
			    .getAssiduousness());
		    if (leavesList == null) {
			leavesList = new ArrayList<Leave>();
		    }
		    leavesList.add((Leave) assiduousnessRecord);
		    assiduousnessLeaves.put(assiduousnessRecord.getAssiduousness(), leavesList);
		}
	    }
	}
	return assiduousnessLeaves;
    }

    private HashMap<Assiduousness, AssiduousnessClosedMonth> getAssiduousnessClosedMonths(
	    ClosedMonth closedMonth) {
	HashMap<Assiduousness, AssiduousnessClosedMonth> allAssiduousnessClosedMonths = new HashMap<Assiduousness, AssiduousnessClosedMonth>();
	for (AssiduousnessClosedMonth assiduousnessClosedMonth : closedMonth
		.getAssiduousnessClosedMonths()) {
	    allAssiduousnessClosedMonths.put(assiduousnessClosedMonth.getAssiduousness(),
		    assiduousnessClosedMonth);
	}
	return allAssiduousnessClosedMonths;
    }
}