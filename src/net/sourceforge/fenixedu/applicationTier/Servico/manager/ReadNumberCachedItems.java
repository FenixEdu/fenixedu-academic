/*
 * Created on 2003/08/08
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ReadNumberCachedItems implements IService {

    /**
     * Returns info list of all execution periods.
     */
    public Integer run() throws FenixServiceException {
        Integer numberCachedObjects = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            numberCachedObjects = sp.getNumberCachedItems();
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return numberCachedObjects;
    }

}