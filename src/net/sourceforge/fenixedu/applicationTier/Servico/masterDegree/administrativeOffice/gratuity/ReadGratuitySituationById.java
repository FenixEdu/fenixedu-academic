package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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