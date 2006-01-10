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
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.PaymentPhase;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author T�nia Pous�o
 * 
 */
public class ReadGratuityValuesByDegreeCurricularPlanAndExecutionYear implements IService {

	public Object run(Integer degreeCurricularPlanID, String executionYearName)
			throws FenixServiceException, ExcepcaoPersistencia {
		if (degreeCurricularPlanID == null || executionYearName == null) {
			throw new FenixServiceException("error.impossible.noGratuityValues");
		}

		ISuportePersistente sp = null;
		GratuityValues gratuityValues = null;
		List infoPaymentPhases = null;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = sp
				.getIPersistentDegreeCurricularPlan();
		DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentDegreeCurricularPlan
				.readByOID(DegreeCurricularPlan.class, degreeCurricularPlanID);

		IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
		ExecutionYear executionYear = persistentExecutionYear
				.readExecutionYearByName(executionYearName);

		if (degreeCurricularPlan == null || executionYear == null) {
			throw new FenixServiceException("error.impossible.noGratuityValues");
		}

		// read execution degree
		IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
		ExecutionDegree executionDegree = persistentExecutionDegree
				.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan.getName(),
						degreeCurricularPlan.getDegree().getSigla(), executionYear.getYear());

		if (executionDegree == null) {
			throw new FenixServiceException("error.impossible.noGratuityValues");
		}

		// read execution degree's gratuity values
		gratuityValues = executionDegree.getGratuityValues();

		InfoGratuityValues infoGratuityValues = null;
		if (gratuityValues != null) {
			infoGratuityValues = InfoGratuityValuesWithInfoExecutionDegree
					.newInfoFromDomain(gratuityValues);

			infoPaymentPhases = new ArrayList();
			CollectionUtils.collect(gratuityValues.getPaymentPhaseList(), new Transformer() {
				public Object transform(Object input) {
					PaymentPhase paymentPhase = (PaymentPhase) input;
					return InfoPaymentPhase.newInfoFromDoamin(paymentPhase);
				}
			}, infoPaymentPhases);

			infoGratuityValues.setInfoPaymentPhases(infoPaymentPhases);

		}

		return infoGratuityValues;
	}
}