package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class AssiduousnessClosedDay extends AssiduousnessClosedDay_Base {

    public AssiduousnessClosedDay(AssiduousnessClosedMonth assiduousnessClosedMonth, WorkDaySheet workDaySheet) {
	super();
	setAssiduousnessClosedMonth(assiduousnessClosedMonth);
	init(workDaySheet);
    }

    public AssiduousnessClosedDay(AssiduousnessClosedMonth assiduousnessClosedMonth, WorkDaySheet workDaySheet,
	    ClosedMonth correctedOnClosedMonth) {
	super();
	setCorrectedOnClosedMonth(correctedOnClosedMonth);
	setAssiduousnessClosedMonth(assiduousnessClosedMonth);
	correct(workDaySheet);
    }

    public void correct(WorkDaySheet workDaySheet) {
	init(workDaySheet);
	setIsCorrection(Boolean.TRUE);
    }

    private void init(WorkDaySheet workDaySheet) {
	setDay(workDaySheet.getDate());
	setSchedule(workDaySheet.getWorkScheduleAcronym());
	if (workDaySheet.getWorkSchedule() != null
		&& workDaySheet.getWorkSchedule().getWorkScheduleType().getScheduleClockingType().equals(
			ScheduleClockingType.NOT_MANDATORY_CLOCKING)) {
	    setBalance(Duration.ZERO);
	} else {
	    setBalance(workDaySheet.getBalanceTime());
	}
	setUnjustified(workDaySheet.getUnjustifiedTime());
	setNotes(workDaySheet.getNotes());
	setClockings(workDaySheet.getClockings());
	setExtraWorkFirstLevel(workDaySheet.getOvertimeFirstLevel());
	setExtraWorkSecondLevel(workDaySheet.getOvertimeSecondLevel());
	setExtraWorkSecondLevelWithLimit(workDaySheet.getOvertimeSecondLevelWithLimit());
	setNightlyExtraWorkFirstLevel(workDaySheet.getNightlyOvertimeFirstLevel());
	setNightlyExtraWorkSecondLevel(workDaySheet.getNightlyOvertimeSecondLevel());
	setNightlyExtraWorkSecondLevelWithLimit(workDaySheet.getNightlyOvertimeSecondLevelWithLimit());
	setNightlyWorkBalance(workDaySheet.getNightWorkBalance());
	setUnjustifiedDay(workDaySheet.getUnjustifiedDay());
	setSaturdayBalance(workDaySheet.getComplementaryWeeklyRest());
	setHolidayBalance(workDaySheet.getHolidayRest());
	setSundayBalance(workDaySheet.getWeeklyRest());
	setLastModifiedDate(new DateTime());
	setIsCorrection(Boolean.FALSE);
    }

    public void delete() {
	removeRootDomainObject();
	removeAssiduousnessClosedMonth();
	removeCorrectedOnClosedMonth();
	deleteDomainObject();
    }
}
