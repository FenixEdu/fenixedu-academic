/*
 * 
 * Created on 02 de Dezembro de 2002, 16:25
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

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

    public Object run(IUserView userView) throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;

        String username = new String(userView.getUtilizador());
        IPerson person = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (person == null) {
            throw new ExcepcaoInexistente("Unknown Person !!");
        }

        InfoPerson infoPerson = InfoPersonWithInfoCountryAndAdvisories.newInfoFromDomain(person);

        return infoPerson;
    }

}