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

package ServidorAplicacao.Servico.person;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoPerson;
import DataBeans.InfoPersonWithInfoCountryAndAdvisories;
import Dominio.IPessoa;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadPersonByUsername implements IService {

    /**
     * The actor of this class.
     */
    public ReadPersonByUsername() {
    }

    public Object run(UserView userView) throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;

        String username = new String(userView.getUtilizador());
        IPessoa person = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
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

    public Object run(String username) throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;

        IPessoa person = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (person == null)
            throw new ExcepcaoInexistente("Unknown Person !!");

        InfoPerson infoPerson = InfoPersonWithInfoCountryAndAdvisories.newInfoFromDomain(person);

        return infoPerson;
    }
}