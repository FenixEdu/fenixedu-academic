package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoGratuitySituation;
import DataBeans.util.Cloner;
import Dominio.GratuitySituation;
import Dominio.IGratuitySituation;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuitySituation;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class ReadGratuitySituationById implements IService {

    public ReadGratuitySituationById() {

    }

    public InfoGratuitySituation run(Integer gratuitySituationID) throws FenixServiceException {

        ISuportePersistente sp = null;
        InfoGratuitySituation infoGratuitySituation = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            IPersistentGratuitySituation persistentGratuitySituation = sp
                    .getIPersistentGratuitySituation();

            IGratuitySituation gratuitySituation = (IGratuitySituation) persistentGratuitySituation
                    .readByOID(GratuitySituation.class, gratuitySituationID, true);

            if (gratuitySituation == null) {
                throw new NonExistingServiceException(
                        "error.exception.masterDegree.gratuity.notExistingGratuitySituation");
            }
            infoGratuitySituation = Cloner
                    .copyIGratuitySituation2InfoGratuitySituation(gratuitySituation);

        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException(e);
        }

        return infoGratuitySituation;

    }
}