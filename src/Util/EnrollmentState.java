package Util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author dcs-rjao
 * 
 * 2/Abr/2003
 */
public class EnrollmentState extends FenixValuedEnum {

    public static final int APROVED_TYPE = 1;

    public static final int NOT_APROVED_TYPE = 2;

    public static final int ENROLLED_TYPE = 3;

    public static final int TEMPORARILY_ENROLLED_TYPE = 4;

    public static final int ANNULED_TYPE = 5;

    public static final int NOT_EVALUATED_TYPE = 6;

    public static final EnrollmentState APROVED = new EnrollmentState("msg.approved",
            EnrollmentState.APROVED_TYPE);

    public static final EnrollmentState NOT_APROVED = new EnrollmentState("msg.notApproved",
            EnrollmentState.NOT_APROVED_TYPE);

    public static final EnrollmentState ENROLLED = new EnrollmentState("msg.enrolled",
            EnrollmentState.ENROLLED_TYPE);

    public static final EnrollmentState TEMPORARILY_ENROLED = new EnrollmentState(
            "msg.temporarilyEnrolled", EnrollmentState.TEMPORARILY_ENROLLED_TYPE);

    public static final EnrollmentState ANNULED = new EnrollmentState("msg.annuled",
            EnrollmentState.ANNULED_TYPE);

    public static final EnrollmentState NOT_EVALUATED = new EnrollmentState("msg.notEvaluated",
            EnrollmentState.NOT_EVALUATED_TYPE);

    private EnrollmentState(String name, int value) {
        super(name, value);
    }

    public static EnrollmentState getEnum(String state) {
        return (EnrollmentState) getEnum(EnrollmentState.class, state);
    }

    public static EnrollmentState getEnum(int state) {
        return (EnrollmentState) getEnum(EnrollmentState.class, state);
    }

    public static Map getEnumMap() {
        return getEnumMap(EnrollmentState.class);
    }

    public static List getEnumList() {
        return getEnumList(EnrollmentState.class);
    }

    public static Iterator iterator() {
        return iterator(EnrollmentState.class);
    }

    public String toString() {
        return this.getName();
    }

}