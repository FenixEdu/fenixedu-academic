/*
 * Created on 19/Ago/2003
 */
package net.sourceforge.fenixedu.util.tests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.util.FenixUtil;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Susana Fernandes
 */
public class CorrectionAvailability extends FenixUtil {
    public static final int NEVER = 1;

    public static final int ALWAYS = 2;

    public static final int AFTER_CLOSING = 3;

    public static final String NEVER_STRING = "Nunca";

    public static final String ALWAYS_STRING = "Sempre";

    public static final String AFTER_CLOSING_STRING = "Depois do fecho";

    private Integer availability;

    public CorrectionAvailability() {
    }

    public CorrectionAvailability(int availability) {
        this.availability = new Integer(availability);
    }

    public CorrectionAvailability(Integer availability) {
        this.availability = availability;
    }

    public CorrectionAvailability(String availabilityString) {
        this.availability = getAvailabilityCode(availabilityString);
    }

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = new Integer(availability);
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public List getAllAvailabilities() {
        List result = new ArrayList();
        result.add(new LabelValueBean(NEVER_STRING, new Integer(NEVER).toString()));
        result.add(new LabelValueBean(ALWAYS_STRING, new Integer(ALWAYS).toString()));
        result.add(new LabelValueBean(AFTER_CLOSING_STRING, new Integer(AFTER_CLOSING).toString()));
        return result;
    }

    public String getTypeString() {
        if (availability.intValue() == NEVER)
            return new String(NEVER_STRING);
        else if (availability.intValue() == ALWAYS)
            return new String(ALWAYS_STRING);
        else if (availability.intValue() == AFTER_CLOSING)
            return new String(AFTER_CLOSING_STRING);
        return null;
    }

    public Integer getAvailabilityCode(String typeName) {
        if (typeName.equals(NEVER_STRING))
            return new Integer(NEVER);
        else if (typeName.equals(ALWAYS_STRING))
            return new Integer(ALWAYS);
        else if (typeName.equals(AFTER_CLOSING_STRING))
            return new Integer(AFTER_CLOSING);
        return null;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof CorrectionAvailability) {
            CorrectionAvailability ca = (CorrectionAvailability) obj;
            result = this.getAvailability().equals(ca.getAvailability());
        }
        return result;
    }
}