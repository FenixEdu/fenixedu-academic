package net.sourceforge.fenixedu.util.enrollment;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.util.FenixValuedEnum;

/**
 * @author David Santos in Jun 15, 2004
 */

public class EnrollmentCondition extends FenixValuedEnum {
    public static final int FINAL_TYPE = 1;

    public static final int TEMPORARY_TYPE = 2;

    public static final int IMPOSSIBLE_TYPE = 3;

    public static final int VALIDATED_TYPE = 4;

    public static final int INVISIBLE_TYPE = 5;

    public static final EnrollmentCondition TEMPORARY = new EnrollmentCondition(
            "EnrollmentCondition.temporary", EnrollmentCondition.TEMPORARY_TYPE);

    public static final EnrollmentCondition FINAL = new EnrollmentCondition("EnrollmentCondition.final",
            EnrollmentCondition.FINAL_TYPE);

    public static final EnrollmentCondition IMPOSSIBLE = new EnrollmentCondition(
            "EnrollmentCondition.impossible", EnrollmentCondition.IMPOSSIBLE_TYPE);

    public static final EnrollmentCondition VALIDATED = new EnrollmentCondition(
            "EnrollmentCondition.validated", EnrollmentCondition.VALIDATED_TYPE);

    public static final EnrollmentCondition INVISIBLE = new EnrollmentCondition(
            "EnrollmentCondition.invisible", EnrollmentCondition.INVISIBLE_TYPE);

    private EnrollmentCondition(String name, int value) {
        super(name, value);
    }

    public static EnrollmentCondition getEnum(String state) {
        return (EnrollmentCondition) getEnum(EnrollmentCondition.class, state);
    }

    public static EnrollmentCondition getEnum(int state) {
        return (EnrollmentCondition) getEnum(EnrollmentCondition.class, state);
    }

    public static Map getEnumMap() {
        return getEnumMap(EnrollmentCondition.class);
    }

    public static List getEnumList() {
        return getEnumList(EnrollmentCondition.class);
    }

    public static Iterator iterator() {
        return iterator(EnrollmentCondition.class);
    }

    public String toString() {
        String result = "Enrollment Condition Type:\n";
        result += "\n  - Enrollment Condition Type : " + this.getName();

        return result;
    }

}