package Dominio.classProperties;

import Dominio.IDomainObject;
import Util.classProperties.GeneralClassPropertyName;
import Util.classProperties.GeneralClassPropertyValue;

/**
 * @author David Santos in Apr 7, 2004
 */

public interface IGeneralClassProperty extends IDomainObject
{
	public String getOjbConcreteClass();
	public void setOjbConcreteClass(String ojbConcreteClass);
	public GeneralClassPropertyName getName();
	public void setName(GeneralClassPropertyName name);
	public GeneralClassPropertyValue getValue();
	public void setValue(GeneralClassPropertyValue value);
}