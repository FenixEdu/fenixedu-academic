package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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