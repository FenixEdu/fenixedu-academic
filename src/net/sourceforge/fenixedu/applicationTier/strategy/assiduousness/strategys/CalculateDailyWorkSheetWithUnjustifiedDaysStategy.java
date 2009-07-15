package net.sourceforge.fenixedu.applicationTier.strategy.assiduousness.strategys;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;

import org.joda.time.Duration;

public class CalculateDailyWorkSheetWithUnjustifiedDaysStategy extends CalculateDailyWorkSheetStategy {

    @Override
    protected void setUnjustifiedDay(WorkDaySheet workDaySheet, List<Leave> halfOccurrenceTimeLeaves, List<Leave> balanceLeaves,
	    List<Leave> balanceOcurrenceLeaves) {
	if (balanceLeaves.isEmpty()
		&& balanceOcurrenceLeaves.isEmpty()
		&& halfOccurrenceTimeLeaves.isEmpty()
		&& workDaySheet.getWorkSchedule().getWorkScheduleType().getScheduleClockingType() != ScheduleClockingType.NOT_MANDATORY_CLOCKING) {
	    workDaySheet.setUnjustifiedDay();
	} else {
	    workDaySheet.setBalanceTime(Duration.ZERO.minus(
		    workDaySheet.getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration())
		    .toPeriod());
	    if (workDaySheet.getWorkSchedule().getWorkScheduleType().getFixedWorkPeriod() != null) {
		workDaySheet.setUnjustifiedTime(workDaySheet.getWorkSchedule().getWorkScheduleType().getFixedWorkPeriod()
			.getWorkPeriodDuration());
	    }
	}
    }

}