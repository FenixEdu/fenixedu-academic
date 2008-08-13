/*
 * Created on 10/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author T�nia Pous�o
 * 
 */
public class ReadGratuityValuesByDegreeCurricularPlanAndExecutionYear extends Service {

	public Object run(Integer degreeCurricularPlanID, String executionYearName)
			throws FenixServiceException{
		if (degreeCurricularPlanID == null || executionYearName == null) {
			throw new FenixServiceException("error.impossible.noGratuityValues");
		}

		GratuityValues gratuityValues = null;
		List infoPaymentPhases = null;

		DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
		final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(executionYearName);
		if (degreeCurricularPlan == null || executionYear == null) {
			throw new FenixServiceException("error.impossible.noGratuityValues");
		}

		// read execution degree
		ExecutionDegree executionDegree = ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear.getYear());

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