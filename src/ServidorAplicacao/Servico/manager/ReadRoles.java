package ServidorAplicacao.Servico.manager;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.IRole;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */

public class ReadRoles implements IService {

    /**
     * Executes the service. Returns the current infodegree.
     */
    public List run() throws FenixServiceException {
        List result = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            result = (List) CollectionUtils.collect(sp.getIPersistentRole().readAll(),
                    new Transformer() {
                        public Object transform(Object arg0) {
                            return Cloner.copyIRole2InfoRole((IRole) arg0);
                        }
                    });
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        return result;
    }
}