package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class ClosedMonth extends ClosedMonth_Base {

    public ClosedMonth() {
        super();
    }

    public static boolean isMonthClosed(Partial yearMonth) {
        for (ClosedMonth closedMonth : RootDomainObject.getInstance().getClosedMonths()) {
            if (closedMonth.getClosedYearMonth().equals(yearMonth)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMonthClosedForExtraWork(Partial yearMonth) {
        for (ClosedMonth closedMonth : RootDomainObject.getInstance().getClosedMonths()) {
            if (closedMonth.getClosedYearMonth().equals(yearMonth)
                    && closedMonth.getClosedForExtraWork()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMonthClosed(YearMonthDay day) {
        Partial yearMonth = new Partial().with(DateTimeFieldType.monthOfYear(), day.getMonthOfYear())
                .with(DateTimeFieldType.year(), day.getYear());
        return isMonthClosed(yearMonth);
    }

}
