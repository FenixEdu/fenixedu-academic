/*
 * Created on Nov 4, 2004
 */
package net.sourceforge.fenixedu.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author nmgo
 * @author lmre
 */
public class EnrolmentAction extends FenixValuedEnum {
    
    public static final int ENROL_TYPE = 1;

    public static final int UNENROL_TYPE = 2;
    
    public static final EnrolmentAction ENROL = new EnrolmentAction("enrol",
            EnrolmentAction.ENROL_TYPE);

    public static final EnrolmentAction UNENROL = new EnrolmentAction("unenrol",
            EnrolmentAction.UNENROL_TYPE);
    
    /**
     * @param name
     * @param value
     */
    private EnrolmentAction(String name, int value) {
        super(name, value);
    }

    public static EnrolmentAction getEnum(String actionType) {
        return (EnrolmentAction) getEnum(EnrolmentAction.class, actionType);
    }

    public static EnrolmentAction getEnum(int actionType) {
        return (EnrolmentAction) getEnum(EnrolmentAction.class, actionType);
    }
    
    
    public static Map getEnumMap() {
        return getEnumMap(EnrolmentAction.class);
    }

    public static List getEnumList() {
        return getEnumList(EnrolmentAction.class);
    }

    public static Iterator iterator() {
        return iterator(EnrolmentAction.class);
    }

    public String toString() {
        return this.getName();
    }

}
