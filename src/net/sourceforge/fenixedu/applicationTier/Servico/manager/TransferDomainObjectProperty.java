/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.PropertyUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class TransferDomainObjectProperty implements IService {

    public void run(Integer sourceObjectID, Integer destinationObjectID, Class clazz, String slotName)
            throws ExcepcaoPersistencia, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        DomainObject srcObject = sp.getIPersistentObject().readByOID(clazz, sourceObjectID);
        DomainObject dstObject = sp.getIPersistentObject().readByOID(clazz, destinationObjectID);

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
