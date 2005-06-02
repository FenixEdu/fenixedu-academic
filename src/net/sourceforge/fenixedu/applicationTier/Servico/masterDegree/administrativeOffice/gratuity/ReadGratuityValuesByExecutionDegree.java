/*
 * Created on 10/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuityValues;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuityValuesWithInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoPaymentPhase;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IGratuityValues;
import net.sourceforge.fenixedu.domain.IPaymentPhase;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuityValues;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentGratuityValues persistentGratuityValues = sp.getIPersistentGratuityValues();

            gratuityValues = persistentGratuityValues
                    .readGratuityValuesByExecutionDegree(executionDegreeID);
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossible.noGratuityValues");
        }

        InfoGratuityValues infoGratuityValues = null;
        if (gratuityValues != null) {
            infoGratuityValues = InfoGratuityValuesWithInfoExecutionDegree
                    .newInfoFromDomain(gratuityValues);

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