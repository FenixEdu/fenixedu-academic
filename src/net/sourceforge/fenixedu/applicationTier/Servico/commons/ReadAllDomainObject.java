/*
 * Created on Oct 19, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadAllDomainObject<E> implements IService  {
    
    public E run(Class<E> clazz) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentObject persistentObject = sp.getIPersistentObject();

        return (E) persistentObject.readAll(clazz);
    }

}
