/*
 * Created on 2003/08/08
 * 
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            numberCachedObjects = sp.getNumberCachedItems();
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return numberCachedObjects;
    }

}