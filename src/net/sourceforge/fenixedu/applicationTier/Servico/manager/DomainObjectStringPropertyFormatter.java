/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DomainObjectStringPropertyFormatter extends Service {

    public void run(Class clazz, String slotName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

	Collection<DomainObject> domainObjects = rootDomainObject.readAllDomainObjects(clazz);
	for (DomainObject domainObject : domainObjects) {

	    Object propertyToFormat = PropertyUtils.getSimpleProperty(domainObject, slotName);

	    if (propertyToFormat != null && propertyToFormat instanceof String) {
		String strPropertyToFormat = (String) propertyToFormat;
		strPropertyToFormat = strPropertyToFormat.trim();
		String propertyFormatted = StringFormatter.prettyPrint(strPropertyToFormat);
		PropertyUtils.setSimpleProperty(domainObject, slotName, propertyFormatted);
	    }

	}
    }

}
