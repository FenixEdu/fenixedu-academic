/*
 * Created on 14/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuityValues;
import net.sourceforge.fenixedu.dataTransferObject.InfoPaymentPhase;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.PaymentPhase;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author Tânia Pousão
 * 
 */
public class InsertGratuityData extends Service {

	public Object run(InfoGratuityValues infoGratuityValues) throws FenixServiceException,
			ExcepcaoPersistencia {
		if (infoGratuityValues == null) {
			throw new FenixServiceException("impossible.insertGratuityValues");
		}
		if (infoGratuityValues.getInfoExecutionDegree() == null
				|| infoGratuityValues.getInfoExecutionDegree().getIdInternal() == null
				|| infoGratuityValues.getInfoExecutionDegree().getIdInternal().intValue() <= 0) {
			throw new FenixServiceException("impossible.insertGratuityValues");
		}

		if (infoGratuityValues.getInfoEmployee() == null
				|| infoGratuityValues.getInfoEmployee().getPerson() == null
				|| infoGratuityValues.getInfoEmployee().getPerson().getUsername() == null
				|| infoGratuityValues.getInfoEmployee().getPerson().getUsername().length() <= 0) {
			throw new FenixServiceException("impossible.insertGratuityValues");
		}

        checkPaymentMode(infoGratuityValues);

		validateGratuity(infoGratuityValues);

        ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(infoGratuityValues.getInfoExecutionDegree().getIdInternal());
        GratuityValues gratuityValues = executionDegree.getGratuityValues();

		if (gratuityValues == null) // it doesn't exist in database, then
		// write it
		{
			gratuityValues = new GratuityValues();

			gratuityValues.setExecutionDegree(executionDegree);
		}

		executionDegree.setGratuityValues(gratuityValues);

		validatePaymentPhasesWithTransaction(gratuityValues);
		// validateGratuitySituationWithTransaction(sp, gratuityValues);

		registerWhoAndWhen(infoGratuityValues, gratuityValues);

		gratuityValues.setAnualValue(infoGratuityValues.getAnualValue());
		gratuityValues.setScholarShipValue(infoGratuityValues.getScholarShipValue());
		gratuityValues.setFinalProofValue(infoGratuityValues.getFinalProofValue());
		gratuityValues.setCreditValue(infoGratuityValues.getCreditValue());
		gratuityValues.setCourseValue(infoGratuityValues.getCourseValue());
		gratuityValues.setProofRequestPayment(infoGratuityValues.getProofRequestPayment());
		gratuityValues.setStartPayment(infoGratuityValues.getStartPayment());
		gratuityValues.setEndPayment(infoGratuityValues.getEndPayment());

		// write all payment phases
		writePaymentPhases(infoGratuityValues, gratuityValues);

		// update gratuity values in all student curricular plan that belong
		// to this execution
		// degree
		// updateStudentsGratuitySituation(sp, gratuityValues,
		// gratuityValue);

		return Boolean.TRUE;
	}

    private void checkPaymentMode(InfoGratuityValues infoGratuityValues) throws FenixServiceException {
        if (infoGratuityValues.getEndPayment() == null
                && (infoGratuityValues.getInfoPaymentPhases() == null || infoGratuityValues
                        .getInfoPaymentPhases().size() == 0)) {
            throw new FenixServiceException("error.masterDegree.gratuity.paymentPhasesNeedToBeDefined");
        }
    }

    private Double validateGratuity(InfoGratuityValues infoGratuityValues) throws FenixServiceException {
		// find the gratuity's value
		Double gratuityValue = null;

		if (infoGratuityValues.getAnualValue() != null) {
			gratuityValue = infoGratuityValues.getAnualValue();
		} else {
			if (infoGratuityValues.getProofRequestPayment() != null
					&& infoGratuityValues.getProofRequestPayment().equals(Boolean.TRUE)) {
				gratuityValue = infoGratuityValues.getScholarShipValue();
			} else {
				gratuityValue = new Double(infoGratuityValues.getScholarShipValue().doubleValue()
						+ infoGratuityValues.getFinalProofValue().doubleValue());
			}
		}

		List<InfoPaymentPhase> paymentPhasesList = infoGratuityValues.getInfoPaymentPhases();
		if (paymentPhasesList != null && paymentPhasesList.size() > 0) {
			// verify if total of all payment phases isn't greater then anual
			// value
			ListIterator iterator = paymentPhasesList.listIterator();
			double totalValuePaymentPhases = 0;
			while (iterator.hasNext()) {
				InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();
				totalValuePaymentPhases += infoPaymentPhase.getValue().floatValue();
			}
			if (totalValuePaymentPhases > gratuityValue.doubleValue()) {
				throw new FenixServiceException("error.masterDegree.gatuyiuty.totalValuePaymentPhases");
			}
			// verify if all payment phases's dates are correct
			validateDatesOfPaymentPhases(paymentPhasesList);

			// registration Payment
			if (infoGratuityValues.getRegistrationPayment() != null
					&& infoGratuityValues.getRegistrationPayment().equals(Boolean.TRUE)) {
				iterator = paymentPhasesList.listIterator();
				InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();
				infoPaymentPhase.setDescription(SessionConstants.REGISTRATION_PAYMENT_KEY);
			}
		}
		return gratuityValue;
	}

	private void validateDatesOfPaymentPhases(List<InfoPaymentPhase> paymentPhasesList) throws FenixServiceException {
		List<InfoPaymentPhase> paymentPhaseListAux = new ArrayList<InfoPaymentPhase>(paymentPhasesList);
		Collections.sort(paymentPhaseListAux, new BeanComparator("endDate"));

		ListIterator iterator = paymentPhaseListAux.listIterator();
		while (iterator.hasNext()) {
			InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();

			if (iterator.hasNext()) {
				InfoPaymentPhase infoPaymentPhase2Compare = (InfoPaymentPhase) iterator.next();
				if ((infoPaymentPhase2Compare.getStartDate() != null && !infoPaymentPhase.getEndDate()
						.before(infoPaymentPhase2Compare.getStartDate()))
						|| (infoPaymentPhase2Compare.getStartDate() == null && !infoPaymentPhase
								.getEndDate().before(infoPaymentPhase2Compare.getEndDate()))) {
					throw new FenixServiceException("error.impossible.paymentPhaseWithWrongDates");
				}
				iterator.previous();
			}
		}
	}

    private void validatePaymentPhasesWithTransaction(GratuityValues gratuityValues)
            throws FenixServiceException {
	}

    private void registerWhoAndWhen(InfoGratuityValues infoGratuityValues, GratuityValues gratuityValues)
            throws ExcepcaoPersistencia {
		// employee who made register
		Person person = Person.readPersonByUsername(infoGratuityValues.getInfoEmployee()
				.getPerson().getUsername());
		if (person != null) {
			gratuityValues.setEmployee(person.getEmployee());
		}

		Calendar now = Calendar.getInstance();
		gratuityValues.setWhen(now.getTime());
	}

    private void writePaymentPhases(InfoGratuityValues infoGratuityValues, GratuityValues gratuityValues)
            throws FenixServiceException, ExcepcaoPersistencia {
		if (gratuityValues.hasAnyPaymentPhaseList()) {
			for (PaymentPhase paymentPhase : gratuityValues.getPaymentPhaseList()) {
				paymentPhase.delete();
			}
		}

		if (infoGratuityValues.getInfoPaymentPhases() != null
				&& infoGratuityValues.getInfoPaymentPhases().size() > 0) {
			ListIterator iterator = infoGratuityValues.getInfoPaymentPhases().listIterator();
			while (iterator.hasNext()) {
				InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();

				PaymentPhase paymentPhase = new PaymentPhase();
				paymentPhase.setStartDate(infoPaymentPhase.getStartDate());
				paymentPhase.setEndDate(infoPaymentPhase.getEndDate());
				paymentPhase.setValue(infoPaymentPhase.getValue());
				if (infoPaymentPhase.getDescription() == null) {
					infoPaymentPhase.setDescription(String.valueOf(iterator.previousIndex()));
				}
				paymentPhase.setDescription(infoPaymentPhase.getDescription());

				paymentPhase.setGratuityValues(gratuityValues);
			}
		}
	}

}
