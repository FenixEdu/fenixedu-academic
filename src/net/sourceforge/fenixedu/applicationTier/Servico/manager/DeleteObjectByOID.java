/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.applicationTier.FenixService;

import org.apache.commons.beanutils.MethodUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DeleteObjectByOID extends FenixService {

    public boolean run(Class clazz, Integer idInternal) throws Throwable {

	try {
	    MethodUtils.invokeMethod(rootDomainObject.readDomainObjectByOID(clazz, idInternal), "delete", null);
	} catch (InvocationTargetException e) {
	    if (e.getTargetException() != null) {
		throw e.getTargetException();
	    }
	    throw e;
	}

	return true;
    }
}
