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
public class ClearCache implements IService {

    public Boolean run() throws FenixServiceException {

        Boolean result = new Boolean(false);
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.clearCache();
            result = new Boolean(true);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return result;
    }

}