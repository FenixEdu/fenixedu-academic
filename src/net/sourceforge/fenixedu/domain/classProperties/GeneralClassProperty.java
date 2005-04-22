package net.sourceforge.fenixedu.domain.classProperties;

import net.sourceforge.fenixedu.util.classProperties.GeneralClassPropertyName;
import net.sourceforge.fenixedu.util.classProperties.GeneralClassPropertyValue;

/**
 * @author David Santos in Apr 7, 2004
 */

public abstract class GeneralClassProperty extends GeneralClassProperty_Base {
    protected GeneralClassPropertyName name;

    protected GeneralClassPropertyValue value;

    /**
     * @return Returns the name.
     */
    public GeneralClassPropertyName getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(GeneralClassPropertyName name) {
        this.name = name;
    }

    /**
     * @return Returns the value.
     */
    public GeneralClassPropertyValue getValue() {
        return value;
    }

    /**
     * @param value
     *            The value to set.
     */
    public void setValue(GeneralClassPropertyValue value) {
        this.value = value;
    }

}