package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoGratuitySituation;
import DataBeans.util.Cloner;
import Dominio.IGratuitySituation;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuitySituation;
import ServidorPersistente.IPersistentStudentCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            sp = SuportePersistenteOJB.getInstance();

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

                gratuitySituationsList = (List) persistentGratuitySituation
                        .readGratuitySituatuionListByStudentCurricularPlan(studentCurricularPlan);

                Iterator itGratuitySituations = gratuitySituationsList.iterator();
                while (itGratuitySituations.hasNext()) {
                    gratuitySituation = (IGratuitySituation) itGratuitySituations.next();
                    if (gratuitySituation != null) {
                        infoGratuitySituation = Cloner
                                .copyIGratuitySituation2InfoGratuitySituation(gratuitySituation);

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