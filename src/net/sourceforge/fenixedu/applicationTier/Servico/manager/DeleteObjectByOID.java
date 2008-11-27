/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

import org.apache.commons.beanutils.MethodUtils;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DeleteObjectByOID extends FenixService {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static boolean run(Class clazz, Integer idInternal) throws FenixServiceException {
	try {
	    MethodUtils.invokeMethod(rootDomainObject.readDomainObjectByOID(clazz, idInternal), "delete", null);
	} catch (InvocationTargetException e) {
	    if (e.getTargetException() != null) {
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