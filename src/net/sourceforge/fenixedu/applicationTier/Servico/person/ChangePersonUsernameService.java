/*
 * Created on May 27, 2004
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Pica
 * @author Barbosa
 */
public class ChangePersonUsernameService implements IService {

    public ChangePersonUsernameService() {
    }

    public void run(String newUsername, Integer personId) throws FenixServiceException,
            ExcepcaoPersistencia {
        ISuportePersistente sp = null;
        IPessoaPersistente pessoaPersistente = null;

        IPerson person = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
            pessoaPersistente = sp.getIPessoaPersistente();

            person = (IPerson) pessoaPersistente.readByOID(Person.class, personId);

            if (person == null || newUsername == null) {
                throw new FenixServiceException();
            }

            pessoaPersistente.simpleLockWrite(person);
            person.setUsername(newUsername);

        } catch (ExcepcaoPersistencia ex) {
            throw new ExcepcaoPersistencia();
        }
    }
}