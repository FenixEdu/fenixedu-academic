package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
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

            IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
            studentCurricularPlan.setIdInternal(studentCurricularPlanID);

            gratuitySituations = persistentGratuitySituation
                    .readGratuitySituatuionListByStudentCurricularPlan(studentCurricularPlan);

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossible.insertExemptionGratuity");
        }

        InfoGratuitySituation infoGratuitySituation = null;
        for (Iterator iter = gratuitySituations.iterator(); iter.hasNext();) {
            gratuitySituation = (IGratuitySituation) iter.next();

            if (gratuitySituation != null) {
                infoGratuitySituation = Cloner
                        .copyIGratuitySituation2InfoGratuitySituation(gratuitySituation);

                infoGratuitySituations.add(infoGratuitySituation);
            }
        }

        return infoGratuitySituations;
    }
}