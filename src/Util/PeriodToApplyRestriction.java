package Util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author David Santos Feb 6, 2004
 */

public class PeriodToApplyRestriction extends FenixValuedEnum {
    public static final int BOTH_SEMESTERS_INT = 0;

    public static final int FIRST_SEMESTER_INT = 1;

    public static final int SECOND_SEMESTER_INT = 2;

    public static final PeriodToApplyRestriction BOTH_SEMESTERS = new PeriodToApplyRestriction(
            "label.manager.both.semesters", PeriodToApplyRestriction.BOTH_SEMESTERS_INT);

    public static final PeriodToApplyRestriction FIRST_SEMESTER = new PeriodToApplyRestriction(
            "label.manager.first.semester", PeriodToApplyRestriction.FIRST_SEMESTER_INT);

    public static final PeriodToApplyRestriction SECOND_SEMESTER = new PeriodToApplyRestriction(
            "label.manager.second.semester", PeriodToApplyRestriction.SECOND_SEMESTER_INT);

    /**
     * @param arg0
     * @param arg1
     */
    public PeriodToApplyRestriction(String arg0, int arg1) {
        super(arg0, arg1);
    }

    public static PeriodToApplyRestriction getEnum(String name) {
        return (PeriodToApplyRestriction) getEnum(PeriodToApplyRestriction.class, name);
    }

    public static PeriodToApplyRestriction getEnum(int value) {
        return (PeriodToApplyRestriction) getEnum(PeriodToApplyRestriction.class, value);
    }

    public static Map getEnumMap() {
        return getEnumMap(PeriodToApplyRestriction.class);
    }

    public static List getEnumList() {
        return getEnumList(PeriodToApplyRestriction.class);
    }

    public static Iterator iterator() {
        return iterator(PeriodToApplyRestriction.class);
    }

    public String toString() {
        String result = "Period To Apply Restriction:\n";
        result += "\n  - Period : " + this.getName();

        return result;

    }
}