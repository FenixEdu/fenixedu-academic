/*
 * Created on 10/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoGratuityValues;
import DataBeans.util.Cloner;
import Dominio.DegreeCurricularPlan;
import Dominio.IExecutionDegree;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionYear;
import Dominio.IGratuityValues;
import Dominio.IPaymentPhase;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentGratuityValues;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadGratuityValuesByDegreeCurricularPlanAndExecutionYear implements IService {

    public Object run(Integer degreeCurricularPlanID, String executionYearName)
            throws FenixServiceException {
        if (degreeCurricularPlanID == null || executionYearName == null) {
            throw new FenixServiceException("error.impossible.noGratuityValues");
        }

        ISuportePersistente sp = null;
        IGratuityValues gratuityValues = null;
        List infoPaymentPhases = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = sp
                    .getIPersistentDegreeCurricularPlan();
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan
                    .readByOID(DegreeCurricularPlan.class, degreeCurricularPlanID);

            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
            IExecutionYear executionYear = persistentExecutionYear
                    .readExecutionYearByName(executionYearName);

            if (degreeCurricularPlan == null || executionYear == null) {
                throw new FenixServiceException("error.impossible.noGratuityValues");
            }

            //read execution degree
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
            IExecutionDegree executionDegree = persistentExecutionDegree
                    .readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
            if (executionDegree == null) {
                throw new FenixServiceException("error.impossible.noGratuityValues");
            }

            //read execution degree's gratuity values
            IPersistentGratuityValues persistentGratuityValues = sp.getIPersistentGratuityValues();
            gratuityValues = persistentGratuityValues
                    .readGratuityValuesByExecutionDegree(executionDegree);

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossible.noGratuityValues");
        }

        InfoGratuityValues infoGratuityValues = null;
        if (gratuityValues != null) {
            infoGratuityValues = Cloner.copyIGratuityValues2InfoGratuityValues(gratuityValues);

            infoPaymentPhases = new ArrayList();
            CollectionUtils.collect(gratuityValues.getPaymentPhaseList(), new Transformer() {
                public Object transform(Object input) {
                    IPaymentPhase paymentPhase = (IPaymentPhase) input;
                    return Cloner.copyIPaymentPhase2InfoPaymentPhase(paymentPhase);
                }
            }, infoPaymentPhases);

            infoGratuityValues.setInfoPaymentPhases(infoPaymentPhases);

        }

        return infoGratuityValues;
    }
}