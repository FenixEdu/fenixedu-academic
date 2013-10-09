/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.apache.commons.beanutils.PropertyUtils;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.core.WriteOnReadError;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class TransferDomainObjectProperty {

    @Atomic
    public static void run(DomainObject srcObject, DomainObject dstObject, String slotName) throws FenixServiceException {
        check(RolePredicates.MANAGER_PREDICATE);
        try {
            Object srcProperty = PropertyUtils.getSimpleProperty(srcObject, slotName);

            if (srcProperty != null && srcProperty instanceof Collection) {
                Collection srcCollection = (Collection) srcProperty;

                Object dstProperty = PropertyUtils.getSimpleProperty(dstObject, slotName);
                if (dstProperty instanceof Collection) {
                    Collection dstCollection = (Collection) dstProperty;
                    dstCollection.addAll(srcCollection);
                }

            } else {
                PropertyUtils.setSimpleProperty(dstObject, slotName, srcProperty);
            }
        } catch (InvocationTargetException e) {
            if (e.getTargetException() != null) {
                if (e.getTargetException() instanceof WriteOnReadError) {
                    throw ((WriteOnReadError) e.getTargetException());
                }
                throw new FenixServiceException(e.getTargetException());
            }
            throw new FenixServiceException(e);
        } catch (IllegalAccessException e) {
            throw new FenixServiceException(e);
        } catch (NoSuchMethodException e) {
            throw new FenixServiceException(e);
        }

    }

}