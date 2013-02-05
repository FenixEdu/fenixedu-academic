package net.sourceforge.fenixedu.util.classProperties;

import net.sourceforge.fenixedu.util.FenixUtil;

/**
 * @author David Santos in Apr 6, 2004
 */

public class GeneralClassPropertyValue extends FenixUtil {
    protected String value;

    public GeneralClassPropertyValue(String value) {
        this.value = value;
    }

    /**
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * @param name
     *            The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }
}