package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadGratuitySituationListByStudentCurricularPlan implements IService {

    public ReadGratuitySituationListByStudentCurricularPlan() {
    }

    public List run(Integer studentCurricularPlanID) throws FenixServiceException {
        ISuportePersistente sp = null;
        List infoGratuitySituations = new ArrayList();
        List gratuitySituations = null;
        IGratuitySituation gratuitySituation = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentGratuitySituation persistentGratuitySituation = sp
                    .getIPersistentGratuitySituation();

            gratuitySituations = persistentGratuitySituation
                    .readGratuitySituatuionListByStudentCurricularPlan(studentCurricularPlanID);

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossible.insertExemptionGratuity");
        }

        InfoGratuitySituation infoGratuitySituation = null;
        for (Iterator iter = gratuitySituations.iterator(); iter.hasNext();) {
            gratuitySituation = (IGratuitySituation) iter.next();

            if (gratuitySituation != null) {
                infoGratuitySituation = InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree
                        .newInfoFromDomain(gratuitySituation);

                infoGratuitySituations.add(infoGratuitySituation);
            }
        }

        return infoGratuitySituations;
    }
}
