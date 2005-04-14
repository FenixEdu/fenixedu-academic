package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadGratuitySituationsByStudentNumber implements IService {

    public ReadGratuitySituationsByStudentNumber() {

    }

    public List run(Integer studentNumber) throws FenixServiceException {
        ISuportePersistente sp = null;
        List infoGratuitySituationsList = new ArrayList();

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente();

            IPersistentGratuitySituation persistentGratuitySituation = sp
                    .getIPersistentGratuitySituation();

            List studentCurricularPlansList = persistentStudentCurricularPlan
                    .readAllFromStudent(studentNumber.intValue());

            List gratuitySituationsList = null;
            IStudentCurricularPlan studentCurricularPlan = null;
            IGratuitySituation gratuitySituation = null;
            InfoGratuitySituation infoGratuitySituation = null;

            Iterator it = studentCurricularPlansList.iterator();
            while (it.hasNext()) {
                studentCurricularPlan = (IStudentCurricularPlan) it.next();

                gratuitySituationsList = persistentGratuitySituation
                        .readGratuitySituatuionListByStudentCurricularPlan(studentCurricularPlan);

                Iterator itGratuitySituations = gratuitySituationsList.iterator();
                while (itGratuitySituations.hasNext()) {
                    gratuitySituation = (IGratuitySituation) itGratuitySituations.next();
                    if (gratuitySituation != null) {
                        infoGratuitySituation = InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree
                                .newInfoFromDomain(gratuitySituation);
                        infoGratuitySituationsList.add(infoGratuitySituation);
                    }
                }
            }

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossible.insertExemptionGratuity");
        }

        return infoGratuitySituationsList;
    }
}