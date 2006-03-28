package net.sourceforge.fenixedu.domain.assiduousness.util;

import net.sourceforge.fenixedu.util.WeekDay;

public class DateUtilities {

    
    public static WeekDay convertToWeekDays(int weekday) {
        switch (weekday) {
        case 1:
            return WeekDay.MONDAY;
        case 2:
            return WeekDay.TUESDAY;
        case 3:
            return WeekDay.WEDNESDAY;
        case 4:
            return WeekDay.THURSDAY;
        case 5:
            return WeekDay.FRIDAY;
        case 6:
            return WeekDay.SATURDAY;
        case 7:    
            return WeekDay.SUNDAY;
        default:
            return null;
        }
    }
}
