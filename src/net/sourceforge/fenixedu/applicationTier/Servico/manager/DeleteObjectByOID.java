/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.applicationTier.Service;

import org.apache.commons.beanutils.MethodUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DeleteObjectByOID extends Service {

    public boolean run(Class clazz, Integer idInternal) throws NoSuchMethodException,
	    IllegalAccessException, InvocationTargetException {

	MethodUtils.invokeMethod(rootDomainObject.readDomainObjectByOID(clazz, idInternal), "delete",
		null);
	return true;
    }
}
