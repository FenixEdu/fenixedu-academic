package ServidorAplicacao.Servico.commons.externalPerson;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class SearchExternalPersonsByName implements IService {

    /**
     * The actor of this class.
     */
    public SearchExternalPersonsByName() {
    }

    public List run(String name) throws FenixServiceException {
        List infoExternalPersons = new ArrayList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            List externalPersons = sp.getIPersistentExternalPerson().readByName(name);
            infoExternalPersons = Cloner.copyListIExternalPerson2ListInfoExternalPerson(externalPersons);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoExternalPersons;
    }
}