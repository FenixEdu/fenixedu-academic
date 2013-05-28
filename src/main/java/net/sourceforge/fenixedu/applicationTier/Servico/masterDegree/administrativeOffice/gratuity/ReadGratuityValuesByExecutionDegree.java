package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuityValues;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuityValuesWithInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoPaymentPhase;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.PaymentPhase;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.PresentationConstants;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadGratuityValuesByExecutionDegree {

    @Service
    public static Object run(Integer executionDegreeID) throws FenixServiceException {
        if (executionDegreeID == null) {
            throw new FenixServiceException("error.impossible.noGratuityValues");
        }

        List infoPaymentPhases = null;

        ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(executionDegreeID);
        GratuityValues gratuityValues = executionDegree.getGratuityValues();

        InfoGratuityValues infoGratuityValues = null;
        if (gratuityValues != null) {
            infoGratuityValues = InfoGratuityValuesWithInfoExecutionDegree.newInfoFromDomain(gratuityValues);

            infoPaymentPhases = new ArrayList();
            CollectionUtils.collect(gratuityValues.getPaymentPhaseList(), new Transformer() {
                @Override
                public Object transform(Object input) {
                    PaymentPhase paymentPhase = (PaymentPhase) input;
                    return InfoPaymentPhase.newInfoFromDoamin(paymentPhase);
                }
            }, infoPaymentPhases);

            InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) CollectionUtils.find(infoPaymentPhases, new Predicate() {
                @Override
                public boolean evaluate(Object input) {
                    InfoPaymentPhase aInfoPaymentPhase = (InfoPaymentPhase) input;
                    if (aInfoPaymentPhase.getDescription() != null
                            && aInfoPaymentPhase.getDescription().equals(PresentationConstants.REGISTRATION_PAYMENT)) {
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