/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class TransferDomainObjectProperty extends Service {

    public void run(DomainObject srcObject, DomainObject dstObject, Class clazz, String slotName)
            throws ExcepcaoPersistencia, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
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

    }


}
