/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.beanutils.MethodUtils;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.IllegalWriteException;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DeleteObjectByOID {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static Boolean run(Class clazz, Integer idInternal) throws FenixServiceException {
        try {
            MethodUtils.invokeMethod(RootDomainObject.getInstance().readDomainObjectByOID(clazz, idInternal), "delete", null);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() != null) {
                if (e.getTargetException() instanceof IllegalWriteException) {
                    throw ((IllegalWriteException) e.getTargetException());
                }
                throw new FenixServiceException(e.getTargetException());
            }
            throw new FenixServiceException(e);
        } catch (NoSuchMethodException e) {
            throw new FenixServiceException(e);
        } catch (IllegalAccessException e) {
            throw new FenixServiceException(e);
        }

        return true;
    }
}