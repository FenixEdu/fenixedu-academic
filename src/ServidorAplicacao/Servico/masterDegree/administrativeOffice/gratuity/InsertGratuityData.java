/*
 * Created on 14/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoGratuityValues;
import DataBeans.InfoPaymentPhase;
import Dominio.CursoExecucao;
import Dominio.GratuityValues;
import Dominio.ICursoExecucao;
import Dominio.IEmployee;
import Dominio.IGratuityValues;
import Dominio.IPaymentPhase;
import Dominio.IPessoa;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.masterDegree.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentGratuityValues;
import ServidorPersistente.IPersistentPaymentPhase;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *  
 */
public class InsertGratuityData implements IService
{



	/**
	 * Constructor
	 */
	public InsertGratuityData()
	{
	}

	public Object run(InfoGratuityValues infoGratuityValues) throws FenixServiceException
	{
		if (infoGratuityValues == null)
		{
			throw new FenixServiceException("impossible.insertGratuityValues");
		}

		if (infoGratuityValues.getInfoExecutionDegree() == null
			|| infoGratuityValues.getInfoExecutionDegree().getIdInternal() == null
			|| infoGratuityValues.getInfoExecutionDegree().getIdInternal().intValue() <= 0)
		{
			throw new FenixServiceException("impossible.insertGratuityValues");
		}

		if (infoGratuityValues.getInfoEmployee() == null
			|| infoGratuityValues.getInfoEmployee().getPerson() == null
			|| infoGratuityValues.getInfoEmployee().getPerson().getUsername() == null
			|| infoGratuityValues.getInfoEmployee().getPerson().getUsername().length() <= 0)
		{
			throw new FenixServiceException("impossible.insertGratuityValues");
		}

		validateGratuity(infoGratuityValues);

		//write gratuity values
		ISuportePersistente sp = null;

		try
		{
			sp = SuportePersistenteOJB.getInstance();
			IPersistentGratuityValues persistentGratuityValues = sp.getIPersistentGrtuityValues();

			IGratuityValues gratuityValues = new GratuityValues();
			gratuityValues.setIdInternal(infoGratuityValues.getIdInternal());

			gratuityValues = (IGratuityValues) persistentGratuityValues.readByOId(gratuityValues, true);
			if (gratuityValues == null) //it doesn't exists in database, then write it
			{

				gratuityValues = new GratuityValues();

				//execution Degree
				ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
				ICursoExecucao executionDegree = new CursoExecucao();
				executionDegree.setIdInternal(
					infoGratuityValues.getInfoExecutionDegree().getIdInternal());
				
				executionDegree =
					(ICursoExecucao) persistentExecutionDegree.readByOId(executionDegree, false);
				gratuityValues.setExecutionDegree(executionDegree);
			}
			
			//employee who made register
			IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
			IPessoa person =
				persistentPerson.lerPessoaPorUsername(
					infoGratuityValues.getInfoEmployee().getPerson().getUsername());
			if (person != null)
			{
			IPersistentEmployee persistentEmployee = sp.getIPersistentEmployee();
				IEmployee employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());
				gratuityValues.setEmployee(employee);
			}
			
			Calendar now = Calendar.getInstance();
			gratuityValues.setWhen(now.getTime());
			
			gratuityValues.setAnualValue(infoGratuityValues.getAnualValue());
			gratuityValues.setScholarShipValue(infoGratuityValues.getScholarShipValue());
			gratuityValues.setFinalProofValue(infoGratuityValues.getFinalProofValue());
			gratuityValues.setCreditValue(infoGratuityValues.getCreditValue());
			gratuityValues.setCourseValue(infoGratuityValues.getCourseValue());
			gratuityValues.setProofRequestPayment(infoGratuityValues.getProofRequestPayment());
			gratuityValues.setStartPayment(infoGratuityValues.getStartPayment());
			gratuityValues.setEndPayment(infoGratuityValues.getEndPayment());


			//TODO: write all payment phases
			writePaymentPhases(sp, infoGratuityValues);
			
			//TODO: update gratuity values in all student curricular plan that belong to this execution
			// degree

		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException("exception persistent error!");
		}

		return Boolean.TRUE;
	}

	private void validateGratuity(InfoGratuityValues infoGratuityValues) throws FenixServiceException
	{
		Double gratuityValue = null; //save gratuity's value

		if (infoGratuityValues.getAnualValue() != null)
		{
			gratuityValue = infoGratuityValues.getAnualValue();
		}
		else
		{
			if (infoGratuityValues.getProofRequestPayment() != null
				&& infoGratuityValues.getProofRequestPayment().equals(Boolean.TRUE))
			{
				gratuityValue = infoGratuityValues.getScholarShipValue();
			}
			else
			{
				gratuityValue =
					new Double(
						infoGratuityValues.getScholarShipValue().doubleValue()
							+ infoGratuityValues.getFinalProofValue().doubleValue());
			}
		}

		List paymentPhasesList = infoGratuityValues.getInfoPaymentPhases();
		if (paymentPhasesList != null && paymentPhasesList.size() > 0)
		{
			ListIterator iterator = paymentPhasesList.listIterator();
			double totalValuePaymentPhases = 0; //total of all payment phases
			while (iterator.hasNext())
			{
				InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();
				totalValuePaymentPhases = +infoPaymentPhase.getValue().floatValue();
			}

			if (totalValuePaymentPhases > gratuityValue.doubleValue())
			{
				throw new FenixServiceException("error.masterDegree.gatuyiuty.totalValuePaymentPhases");
			}

			//TODO: datas dos pagamentos sobrepostas

			if (infoGratuityValues.getRegistrationPayment().equals(Boolean.TRUE))
			{
				iterator = paymentPhasesList.listIterator();
				InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();
				infoPaymentPhase.setDescription(SessionConstants.REGISTRATION_PAYMENT);
			}
		}
	}
	
	private void writePaymentPhases(ISuportePersistente sp, InfoGratuityValues infoGratuityValues)
	{
		IPersistentPaymentPhase persistentPaymentPhase = sp.getIPersistentPaymentPhase();
		
		ListIterator iterator = infoGratuityValues.getInfoPaymentPhases().listIterator();
		while (iterator.hasNext())
		{
			InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();
			
			//IPaymentPhase paymentPhase = new Pay
}
	}	
}
