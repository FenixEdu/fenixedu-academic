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

package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ChangePersonalInfo implements IService {

    public IUserView run(IUserView userView, InfoPerson newInfoPerson) throws ExcepcaoInexistente,
            FenixServiceException, ExistingPersistentException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());

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