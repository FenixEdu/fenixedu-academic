package ServidorAplicacao.Servico.manager;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.IPerson;
import Dominio.IRole;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */

public class ReadRolesByUser implements IService {

    /**
     * Executes the service. Returns the current infodegree.
     */
    public List run(String username) throws FenixServiceException {
        List result = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
            if (person == null) {
                throw new FenixServiceException("error.noUsername");
            }

            result = (List) CollectionUtils.collect(person.getPersonRoles(), new Transformer() {
                public Object transform(Object arg0) {
                    return Cloner.copyIRole2InfoRole((IRole) arg0);
                }
            });
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException("error.noRoles", excepcaoPersistencia);
        }

        return result;
    }
}