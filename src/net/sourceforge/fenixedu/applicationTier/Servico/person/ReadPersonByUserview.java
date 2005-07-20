package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonWithInfoCountryAndAdvisories;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadPersonByUserview implements IService {

    public InfoPerson run(IUserView userView) throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        String username = new String(userView.getUtilizador());
        IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);

        if (person == null) {
            throw new ExcepcaoInexistente("Unknown Person !!");
        }

        return InfoPersonWithInfoCountryAndAdvisories.newInfoFromDomain(person);

    }

}