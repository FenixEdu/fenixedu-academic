/*
 * Created on 10/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            gratuitySituation.setExemptionValue(null);
            gratuitySituation.setExemptionType(null);
            gratuitySituation.setExemptionDescription(null);
            // gratuitySituation.setEmployee(null);
            // gratuitySituation.setWhen(null);
        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException(e);
        }

        return Boolean.TRUE;
    }
}
