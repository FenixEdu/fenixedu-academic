/*
 * Created on Nov 13, 2003
 *  
 */
package Util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class OrientationType extends FenixValuedEnum {

    public static final int DEGREE_TYPE = 1;

    public static final int MASTER_TYPE = 2;

    public static final int PHD_TYPE = 3;

    public static OrientationType DEGREE = new OrientationType("DegreeFinalProject", DEGREE_TYPE);

    public static OrientationType MASTER = new OrientationType("MasterThesis", MASTER_TYPE);

    public static OrientationType PHD = new OrientationType("PHDThesis", PHD_TYPE);

    public OrientationType(String name, int value) {
        super(name, value);
    }

    public static OrientationType getEnum(String orientationType) {
        return (OrientationType) getEnum(OrientationType.class, orientationType);
    }

    public static OrientationType getEnum(int orientationType) {
        return (OrientationType) getEnum(OrientationType.class, orientationType);
    }

    public static Map getEnumMap() {
        return getEnumMap(OrientationType.class);
    }

    public static List getEnumList() {
        return getEnumList(OrientationType.class);
    }

    public static Iterator iterator() {
        return iterator(OrientationType.class);
    }

    public String toString() {
        String result = "Orientation Type :\n";
        result += "\n  - Orientation Type : " + this.getName();

        return result;
    }
}