/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainObject;

import org.apache.commons.beanutils.PropertyUtils;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.IllegalWriteException;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class TransferDomainObjectProperty extends FenixService {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(DomainObject srcObject, DomainObject dstObject, String slotName) throws FenixServiceException {
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
                if (e.getTargetException() instanceof IllegalWriteException) {
                    throw ((IllegalWriteException) e.getTargetException());
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