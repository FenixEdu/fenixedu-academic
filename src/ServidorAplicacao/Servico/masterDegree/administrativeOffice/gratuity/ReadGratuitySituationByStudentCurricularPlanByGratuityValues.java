/*
 * Created on 10/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoGratuitySituation;
import DataBeans.util.Cloner;
import Dominio.GratuityValues;
import Dominio.IGratuitySituation;
import Dominio.IGratuityValues;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuitySituation;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadGratuitySituationByStudentCurricularPlanByGratuityValues implements IService {

    public Object run(Integer studentCurricularPlanID, Integer gratuityValuesID)
            throws FenixServiceException {
        ISuportePersistente sp = null;
        IGratuitySituation gratuitySituation = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

            IPersistentGratuitySituation persistentGratuitySituation = sp
                    .getIPersistentGratuitySituation();

            IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
            studentCurricularPlan.setIdInternal(studentCurricularPlanID);

            IGratuityValues gratuityValues = new GratuityValues();
            gratuityValues.setIdInternal(gratuityValuesID);

            gratuitySituation = persistentGratuitySituation
                    .readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(
                            studentCurricularPlan, gratuityValues);
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossible.insertExemptionGratuity");
        }

        InfoGratuitySituation infoGratuitySituation = null;
        if (gratuitySituation != null) {
            infoGratuitySituation = Cloner
                    .copyIGratuitySituation2InfoGratuitySituation(gratuitySituation);
        }

        return infoGratuitySituation;
    }
}