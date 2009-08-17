package net.sourceforge.fenixedu.applicationTier.strategy.assiduousness.strategys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalTime;

public abstract class CalculateDailyWorkSheetStategy implements ICalculateDailyWorkSheetStrategy {

    public static final LocalTime DEFAULT_START_WEEKLY_REST_DAY = new LocalTime(7, 0, 0, 0);

    public WorkDaySheet calculateDailyBalance(Assiduousness assiduousness, WorkDaySheet workDaySheet, boolean isDayHoliday) {
	return calculateDailyBalance(assiduousness, workDaySheet, isDayHoliday, false);
    }

    public WorkDaySheet calculateDailyBalance(Assiduousness assiduousness, WorkDaySheet workDaySheet, boolean isDayHoliday,
	    boolean closingMonth) {
	if (workDaySheet.getWorkSchedule() != null
		&& !isDayHoliday
		&& (!workDaySheet.getWorkSchedule().getWorkScheduleType().getScheduleClockingType().equals(
			ScheduleClockingType.NOT_MANDATORY_CLOCKING) || closingMonth)) {

	    List<Leave> dayOccurrences = assiduousness.getLeavesByType(workDaySheet.getLeaves(), JustificationType.OCCURRENCE);

	    if (dayOccurrences.isEmpty() || !workDaySheet.getAssiduousnessRecords().isEmpty()) {
		List<Leave> timeLeaves = assiduousness.getLeavesByType(workDaySheet.getLeaves(), JustificationType.TIME);
		List<Leave> halfOccurrenceTimeLeaves = assiduousness.getLeavesByType(workDaySheet.getLeaves(),
			JustificationType.HALF_OCCURRENCE_TIME);
		List<Leave> balanceLeaves = assiduousness.getLeavesByType(workDaySheet.getLeaves(), JustificationType.BALANCE);
		balanceLeaves.addAll(assiduousness.getLeavesByType(workDaySheet.getLeaves(),
			JustificationType.HALF_MULTIPLE_MONTH_BALANCE));
		List<Leave> balanceOcurrenceLeaves = assiduousness.getLeavesByType(workDaySheet.getLeaves(),
			JustificationType.MULTIPLE_MONTH_BALANCE);
		List<Leave> halfOccurrenceLeaves = assiduousness.getLeavesByType(workDaySheet.getLeaves(),
			JustificationType.HALF_OCCURRENCE);
		if (!workDaySheet.getAssiduousnessRecords().isEmpty() || !timeLeaves.isEmpty()) {
		    workDaySheet.setTimeline(getTimeline(workDaySheet, timeLeaves));
		    workDaySheet = workDaySheet.getWorkSchedule().calculateWorkingPeriods(workDaySheet, timeLeaves);
		    if (!dayOccurrences.isEmpty()) {
			workDaySheet.setIrregular(true);
		    }
		} else {
		    if (workDaySheet.getWorkSchedule().getWorkScheduleType().getScheduleClockingType() != ScheduleClockingType.NOT_MANDATORY_CLOCKING) {
			setUnjustifiedDay(workDaySheet, halfOccurrenceTimeLeaves, balanceLeaves, balanceOcurrenceLeaves);
		    }
		}
		workDaySheet.discountBalanceLeaveInFixedPeriod(balanceLeaves);
		workDaySheet.discountBalanceOcurrenceLeaveInFixedPeriod(balanceOcurrenceLeaves);
		workDaySheet.discountBalance(halfOccurrenceTimeLeaves);

		if (!halfOccurrenceLeaves.isEmpty()) {
		    workDaySheet.discountHalfOccurrence();
		}

	    }
	} else {
	    if (!workDaySheet.getAssiduousnessRecords().isEmpty()) {
		DateTime firstClocking = workDaySheet.getAssiduousnessRecords().get(0).getDate();
		DateTime lastClocking = workDaySheet.getAssiduousnessRecords().get(
			workDaySheet.getAssiduousnessRecords().size() - 1).getDate();
		final Timeline timeline = new Timeline(workDaySheet.getDate(), firstClocking, lastClocking);
		Iterator<AttributeType> attributesIt = DomainConstants.WORKED_ATTRIBUTES.getAttributes().iterator();
		final WeekDay dayOfWeek = WeekDay.fromJodaTimeToWeekDay(workDaySheet.getDate().toDateTimeAtStartOfDay());
		if (dayOfWeek.equals(WeekDay.SATURDAY) || dayOfWeek.equals(WeekDay.SUNDAY) || isDayHoliday) {
		    timeline.plotListInTimeline(workDaySheet.getAssiduousnessRecords(), new ArrayList<Leave>(), attributesIt,
			    workDaySheet.getDate());
		} else {
		    timeline.plotListInTimeline(workDaySheet.getAssiduousnessRecords(), workDaySheet.getLeaves(), attributesIt,
			    workDaySheet.getDate());
		}
		Duration worked = timeline.calculateWorkPeriodDuration(null, timeline.getTimePoints().iterator().next(),
			new TimePoint(DEFAULT_START_WEEKLY_REST_DAY, AttributeType.NULL), new TimePoint(lastClocking
				.toLocalTime(), lastClocking.toLocalDate().equals(workDaySheet.getDate()) ? false : true,
				AttributeType.NULL), null, null);
		Duration weeklyRestDuration = worked;
		if (isDayHoliday) {
		    workDaySheet.setHolidayRest(weeklyRestDuration);
		} else if (dayOfWeek.equals(WeekDay.SATURDAY)) {
		    workDaySheet.setComplementaryWeeklyRest(weeklyRestDuration);
		} else if (dayOfWeek.equals(WeekDay.SUNDAY)) {
		    workDaySheet.setWeeklyRest(weeklyRestDuration);
		}
	    }
	}
	return workDaySheet;
    }

    protected abstract void setUnjustifiedDay(WorkDaySheet workDaySheet, List<Leave> halfOccurrenceTimeLeaves,
	    List<Leave> balanceLeaves, List<Leave> balanceOcurrenceLeaves);

    private Timeline getTimeline(WorkDaySheet workDaySheet, List<Leave> timeLeaves) {
	Timeline timeline = new Timeline(workDaySheet.getWorkSchedule().getWorkScheduleType());
	Iterator<AttributeType> attributesIt = DomainConstants.WORKED_ATTRIBUTES.getAttributes().iterator();
	timeline.plotListInTimeline(workDaySheet.getAssiduousnessRecords(), timeLeaves, attributesIt, workDaySheet.getDate());
	return timeline;
    }

}