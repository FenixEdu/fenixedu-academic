/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.apache.commons.beanutils.PropertyUtils;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DomainObjectStringPropertyFormatter {

    @Atomic
    public static void run(Class clazz, String slotName) throws FenixServiceException {
        check(RolePredicates.MANAGER_PREDICATE);

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