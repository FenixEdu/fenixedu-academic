package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;

import org.joda.time.DateMidnight;

public class AssiduousnessClosedDay extends AssiduousnessClosedDay_Base {

    public AssiduousnessClosedDay(AssiduousnessClosedMonth assiduousnessClosedMonth, WorkDaySheet workDaySheet) {
	super();
	setAssiduousnessClosedMonth(assiduousnessClosedMonth);
	setDay(workDaySheet.getDate());
	setSchedule(workDaySheet.getWorkScheduleAcronym());
	setBalance(workDaySheet.getBalanceTime().toDurationFrom(new DateMidnight()));
	setUnjustified(workDaySheet.getUnjustifiedTime());
	setNotes(workDaySheet.getNotes());
	setClockings(workDaySheet.getClockingsFormattedToManagement());
	setExtraWorkFirstLevel(workDaySheet.getExtraWorkFirstLevel());
	setExtraWorkSecondLevel(workDaySheet.getExtraWorkSecondLevel());
	setExtraWorkSecondLevelWithLimit(workDaySheet.getExtraWorkSecondLevelWithLimit());
	setNightlyExtraWorkFirstLevel(workDaySheet.getNightExtraWorkFirstLevel());
	setNightlyExtraWorkSecondLevel(workDaySheet.getNightExtraWorkSecondLevel());
	setNightlyExtraWorkSecondLevelWithLimit(workDaySheet.getNightExtraWorkSecondLevelWithLimit());
	setNightlyWorkBalance(workDaySheet.getNightWorkBalance());
    }

    public void delete() {
	removeRootDomainObject();
	removeAssiduousnessClosedMonth();
	deleteDomainObject();
    }
}
