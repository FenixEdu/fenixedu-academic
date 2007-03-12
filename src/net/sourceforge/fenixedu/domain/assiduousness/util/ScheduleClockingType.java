package net.sourceforge.fenixedu.domain.assiduousness.util;

import java.util.ArrayList;
import java.util.List;

public enum ScheduleClockingType {
    NORMAL_CLOCKING, NOT_MANDATORY_CLOCKING, RIGID_CLOCKING, OLD_RIGID_CLOCKING;

    public static List<ScheduleClockingType> getValidScheduleClockingTypes() {
	List<ScheduleClockingType> scheduleClockingTypes = new ArrayList<ScheduleClockingType>();
	scheduleClockingTypes.add(ScheduleClockingType.NORMAL_CLOCKING);
	scheduleClockingTypes.add(ScheduleClockingType.NOT_MANDATORY_CLOCKING);
	scheduleClockingTypes.add(ScheduleClockingType.RIGID_CLOCKING);
	return scheduleClockingTypes;
    }
}
