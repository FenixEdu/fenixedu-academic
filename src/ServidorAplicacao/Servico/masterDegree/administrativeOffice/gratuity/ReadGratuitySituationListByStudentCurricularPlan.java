package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoGratuitySituation;
import DataBeans.util.Cloner;
import Dominio.IGratuitySituation;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
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
public class ReadGratuitySituationListByStudentCurricularPlan implements IService {

    public ReadGratuitySituationListByStudentCurricularPlan() {
    }

    public List run(Integer studentCurricularPlanID) throws FenixServiceException {
        ISuportePersistente sp = null;
        List infoGratuitySituations = new ArrayList();
        List gratuitySituations = null;
        IGratuitySituation gratuitySituation = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

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