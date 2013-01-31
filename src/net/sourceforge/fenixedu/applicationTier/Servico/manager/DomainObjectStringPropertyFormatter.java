/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.apache.commons.beanutils.PropertyUtils;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DomainObjectStringPropertyFormatter extends FenixService {

	@Checked("RolePredicates.MANAGER_PREDICATE")
	@Service
	public static void run(Class clazz, String slotName) throws FenixServiceException {

		try {
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
		} catch (IllegalAccessException e) {
			throw new FenixServiceException(e);
		} catch (InvocationTargetException e) {
			throw new FenixServiceException(e);
		} catch (NoSuchMethodException e) {
			throw new FenixServiceException(e);
		}
	}

}