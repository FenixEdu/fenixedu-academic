/*
 * Created on 10/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoGratuityValues;
import DataBeans.InfoPaymentPhase;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import Dominio.IGratuityValues;
import Dominio.IPaymentPhase;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.masterDegree.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuityValues;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadGratuityValuesByExecutionDegree implements IService {

    public Object run(Integer executionDegreeID) throws FenixServiceException {
        if (executionDegreeID == null) {
            throw new FenixServiceException("error.impossible.noGratuityValues");
        }

        ISuportePersistente sp = null;
        IGratuityValues gratuityValues = null;
        List infoPaymentPhases = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
            IPersistentGratuityValues persistentGratuityValues = sp.getIPersistentGratuityValues();

            ICursoExecucao executionDegree = new CursoExecucao();
            executionDegree.setIdInternal(executionDegreeID);

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

            InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) CollectionUtils.find(
                    infoPaymentPhases, new Predicate() {
                        public boolean evaluate(Object input) {
                            InfoPaymentPhase aInfoPaymentPhase = (InfoPaymentPhase) input;
                            if (aInfoPaymentPhase.getDescription() != null
                                    && aInfoPaymentPhase.getDescription().equals(
                                            SessionConstants.REGISTRATION_PAYMENT)) {
                                return true;
                            }
                            return false;
                        }
                    });
            if (infoPaymentPhase != null) {
                infoGratuityValues.setRegistrationPayment(Boolean.TRUE);
            }

            infoGratuityValues.setInfoPaymentPhases(infoPaymentPhases);
        }
        return infoGratuityValues;
    }
}