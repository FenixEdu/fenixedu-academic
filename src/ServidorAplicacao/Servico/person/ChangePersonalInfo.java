/*
 * ChangeMasterDegreeCandidate.java O Servico ChangeMasterDegreeCandidate altera
 * a informacao de um candidato de Mestrado Nota : E suposto os campos
 * (numeroCandidato, anoCandidatura, chaveCursoMestrado, username) nao se
 * puderem alterar Created on 02 de Dezembro de 2002, 16:25
 */

/**
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 */

package ServidorAplicacao.Servico.person;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoPerson;
import Dominio.IPessoa;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class ChangePersonalInfo implements IService {

    /**
     * The actor of this class.
     */
    public ChangePersonalInfo() {
    }

    public UserView run(UserView userView, InfoPerson newInfoPerson) throws ExcepcaoInexistente,
            FenixServiceException, ExistingPersistentException, ExcepcaoPersistencia {

        ISuportePersistente sp = null;
        IPessoa person = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
        if (person == null) {
            throw new ExcepcaoInexistente("Unknown Person !!");
        }
        sp.getIPessoaPersistente().simpleLockWrite(person);

        // Change personal Information
        person.setTelemovel(newInfoPerson.getTelemovel());
        person.setWorkPhone(newInfoPerson.getWorkPhone());
        person.setEmail(newInfoPerson.getEmail());
        person.setAvailableEmail(newInfoPerson.getAvailableEmail());
        person.setEnderecoWeb(newInfoPerson.getEnderecoWeb());
        person.setAvailableWebSite(newInfoPerson.getAvailableWebSite());
        person.setAvailablePhoto(newInfoPerson.getAvailablePhoto());

        return userView;
    }
}