/*
 * Created on 10/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.GratuitySituation;
import Dominio.IGratuitySituation;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuitySituation;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *
 */
/**
 * @author Tânia Pousão
 *  
 */
public class DeleteGratuitySituationById implements IService {

    /*
     * This clean the exemption's value, but not delete the object.
     *  
     */
    public Boolean run(Integer gratuitySituationID) throws FenixServiceException {
        ISuportePersistente sp = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            IPersistentGratuitySituation persistentGratuitySituation = sp
                    .getIPersistentGratuitySituation();

            IGratuitySituation gratuitySituation = (IGratuitySituation) persistentGratuitySituation
                    .readByOID(GratuitySituation.class, gratuitySituationID, true);
            if (gratuitySituation == null) {
                return Boolean.TRUE;
            }

            gratuitySituation.setExemptionPercentage(null);
            gratuitySituation.setExemptionType(null);
            gratuitySituation.setExemptionDescription(null);
            //            gratuitySituation.setEmployee(null);
            //            gratuitySituation.setWhen(null);
        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException(e);
        }

        return Boolean.TRUE;
    }
}