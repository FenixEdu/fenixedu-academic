package net.sourceforge.fenixedu.domain.classProperties;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.util.classProperties.GeneralClassPropertyName;
import net.sourceforge.fenixedu.util.classProperties.GeneralClassPropertyValue;

/**
 * @author David Santos in Apr 7, 2004
 */

public interface IGeneralClassProperty extends IDomainObject {
    public String getOjbConcreteClass();

    public void setOjbConcreteClass(String ojbConcreteClass);

    public GeneralClassPropertyName getName();

    public void setName(GeneralClassPropertyName name);

    public GeneralClassPropertyValue getValue();

    public void setValue(GeneralClassPropertyValue value);
}