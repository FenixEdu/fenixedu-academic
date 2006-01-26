package net.sourceforge.fenixedu.domain.assiduousness.util;

import net.sourceforge.fenixedu.domain.assiduousness.util.WeekDays;

public class DateUtilities {

    
    public static WeekDays convertToWeekDays(int weekday) {
        switch (weekday) {
        case 1:
            return WeekDays.MONDAY;
        case 2:
            return WeekDays.TUESDAY;
        case 3:
            return WeekDays.WEDNESDAY;
        case 4:
            return WeekDays.THURSDAY;
        case 5:
            return WeekDays.FRIDAY;
        case 6:
            return WeekDays.SATURDAY;
        case 7:    
            return WeekDays.SUNDAY;
        default:
            return null;
        }
    }
}
