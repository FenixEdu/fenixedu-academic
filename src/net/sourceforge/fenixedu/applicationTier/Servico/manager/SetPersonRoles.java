/*
 * Created on 2003/12/04
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */

public class SetPersonRoles implements IService {

    public Boolean run(String username, List roleOIDs) throws FenixServiceException {
        Boolean result = new Boolean(false);
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPerson person = persistentSuport.getIPessoaPersistente().lerPessoaPorUsername(username);
            persistentSuport.getIPessoaPersistente().simpleLockWrite(person);
            person.setPersonRoles(new ArrayList());
            for (int i = 0; i < roleOIDs.size(); i++) {
                IRole role = (IRole) persistentSuport.getIPessoaPersistente().readByOID(Role.class,
                        ((Integer) roleOIDs.get(i)));
                person.getPersonRoles().add(role);
            }
            result = new Boolean(true);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return result;
    }
}