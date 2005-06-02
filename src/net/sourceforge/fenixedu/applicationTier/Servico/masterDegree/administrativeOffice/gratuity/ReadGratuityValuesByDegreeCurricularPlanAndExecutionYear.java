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
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IGratuityValues;
import net.sourceforge.fenixedu.domain.IPaymentPhase;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuityValues;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

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

            // read execution degree
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
            IExecutionDegree executionDegree = persistentExecutionDegree
                    .readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan.getName(),
                            degreeCurricularPlan.getDegree().getSigla(), executionYear.getYear());

            if (executionDegree == null) {
                throw new FenixServiceException("error.impossible.noGratuityValues");
            }

            // read execution degree's gratuity values
            IPersistentGratuityValues persistentGratuityValues = sp.getIPersistentGratuityValues();
            gratuityValues = executionDegree.getGratuityValues();

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

            infoGratuityValues.setInfoPaymentPhases(infoPaymentPhases);

        }

        return infoGratuityValues;
    }
}