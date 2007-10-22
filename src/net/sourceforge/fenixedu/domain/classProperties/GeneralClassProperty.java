package net.sourceforge.fenixedu.domain.classProperties;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.classProperties.GeneralClassPropertyName;
import net.sourceforge.fenixedu.util.classProperties.GeneralClassPropertyValue;

/**
 * @author David Santos in Apr 7, 2004
 */

public abstract class GeneralClassProperty extends GeneralClassProperty_Base {

    public GeneralClassProperty() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public GeneralClassPropertyName getName() {
	return new GeneralClassPropertyName(getNameString());
    }

    public void setName(GeneralClassPropertyName name) {
	setNameString(name.getName());
    }

    public GeneralClassPropertyValue getValue() {
	return new GeneralClassPropertyValue(getValueString());
    }

    public void setValue(GeneralClassPropertyValue value) {
	setValueString(value.getValue());
    }

}