package ServidorAplicacao.Servico.commons.externalPerson;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExternalPerson;
import DataBeans.util.Cloner;
import Dominio.ExternalPerson;
import Dominio.IExternalPerson;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class ReadExternalPersonByID implements IService {

    /**
     * The actor of this class.
     */
    public ReadExternalPersonByID() {
    }

    public Object run(Integer externalPersonID) throws FenixServiceException {
        InfoExternalPerson infoExternalPerson = null;
        IExternalPerson externalPerson = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            externalPerson = (IExternalPerson) sp.getIPersistentExternalPerson().readByOID(
                    ExternalPerson.class, externalPersonID);

            if (externalPerson == null)
                throw new NonExistingServiceException("error.exception.commons.ExternalPersonNotFound");

            infoExternalPerson = Cloner.copyIExternalPerson2InfoExternalPerson(externalPerson);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoExternalPerson;
    }
}