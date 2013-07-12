/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.apache.commons.beanutils.PropertyUtils;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DomainObjectStringPropertyFormatter {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Atomic
    public static void run(Class clazz, String slotName) throws FenixServiceException {

        try {
            Collection<DomainObject> domainObjects = DomainObjectUtil.readAllDomainObjects(clazz);
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