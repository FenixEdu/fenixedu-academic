/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author pica
 * @author barbosa
 */
public class AprovalType extends FenixValuedEnum {
    public static int YES_TYPE = 0;

    public static int NO_TYPE = 1;

    public static AprovalType YES = new AprovalType("Yes", YES_TYPE);

    public static AprovalType NO = new AprovalType("No", NO_TYPE);

    public AprovalType(String name, int value) {
        super(name, value);
    }

    public static AprovalType getEnum(String approvalType) {
        return (AprovalType) getEnum(AprovalType.class, approvalType);
    }

    public static AprovalType getEnum(int approvalType) {
        return (AprovalType) getEnum(AprovalType.class, approvalType);
    }

    public static Map getEnumMap() {
        return getEnumMap(AprovalType.class);
    }

    public static List getEnumList() {
        return getEnumList(AprovalType.class);
    }

    public static Iterator Iterator() {
        return iterator(AprovalType.class);
    }

    public String toString() {
        String result = "Aproval Type :\n";
        result += "\n - Approval Type : " + this.getName();

        return result;
    }
}