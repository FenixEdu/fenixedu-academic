/*
 * Created on May 27, 2004
 *
 */

package ServidorAplicacao.Servico.person;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IPessoa;
import Dominio.Pessoa;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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

        IPessoa person = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
            pessoaPersistente = sp.getIPessoaPersistente();

            person = (IPessoa) pessoaPersistente.readByOID(Pessoa.class, personId);

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