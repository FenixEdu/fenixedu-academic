/*
 * Created on 14/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoGratuityValues;
import DataBeans.InfoPaymentPhase;
import Dominio.CursoExecucao;
import Dominio.GratuityValues;
import Dominio.ICursoExecucao;
import Dominio.IEmployee;
import Dominio.IGratuitySituation;
import Dominio.IGratuityValues;
import Dominio.IPaymentPhase;
import Dominio.IPessoa;
import Dominio.PaymentPhase;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.masterDegree.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentGratuitySituation;
import ServidorPersistente.IPersistentGratuityValues;
import ServidorPersistente.IPersistentPaymentPhase;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
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
			System.out.println("argumento null");
			throw new FenixServiceException("impossible.insertGratuityValues");
		}
		if (infoGratuityValues.getInfoExecutionDegree() == null
			|| infoGratuityValues.getInfoExecutionDegree().getIdInternal() == null
			|| infoGratuityValues.getInfoExecutionDegree().getIdInternal().intValue() <= 0)
		{
			System.out.println("executiondegree null");
			throw new FenixServiceException("impossible.insertGratuityValues");
		}

		if (infoGratuityValues.getInfoEmployee() == null
			|| infoGratuityValues.getInfoEmployee().getPerson() == null
			|| infoGratuityValues.getInfoEmployee().getPerson().getUsername() == null
			|| infoGratuityValues.getInfoEmployee().getPerson().getUsername().length() <= 0)
		{
			System.out.println("employee null");
			throw new FenixServiceException("impossible.insertGratuityValues");
		}
		System.out.println("passou os argumentos");

		ISuportePersistente sp = null;

		validateGratuity(sp, infoGratuityValues);
		System.out.println("validou a propina");

		try
		{
			sp = SuportePersistenteOJB.getInstance();
			IPersistentGratuityValues persistentGratuityValues = sp.getIPersistentGratuityValues();

			IGratuityValues gratuityValues = new GratuityValues();
			gratuityValues.setIdInternal(infoGratuityValues.getIdInternal());

			gratuityValues = (IGratuityValues) persistentGratuityValues.readByOId(gratuityValues, true);
			if (gratuityValues == null) //it doesn't exist in database, then write it
			{
				System.out.println("propina nova");

				gratuityValues = new GratuityValues();

				//execution Degree
				ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
				ICursoExecucao executionDegree = new CursoExecucao();
				executionDegree.setIdInternal(
					infoGratuityValues.getInfoExecutionDegree().getIdInternal());

				executionDegree =
					(ICursoExecucao) persistentExecutionDegree.readByOId(executionDegree, false);
				gratuityValues.setExecutionDegree(executionDegree);

				persistentGratuityValues.simpleLockWrite(gratuityValues);
			}

			validatePaymentPhasesWithTransaction(sp, gratuityValues);
			System.out.println("validou prestacoes sem transaccoes");

			registerWhoAndWhen(sp, infoGratuityValues, gratuityValues);
			System.out.println("colocou o autor da mudança da propina");

			gratuityValues.setAnualValue(infoGratuityValues.getAnualValue());
			gratuityValues.setScholarShipValue(infoGratuityValues.getScholarShipValue());
			gratuityValues.setFinalProofValue(infoGratuityValues.getFinalProofValue());
			gratuityValues.setCreditValue(infoGratuityValues.getCreditValue());
			gratuityValues.setCourseValue(infoGratuityValues.getCourseValue());
			gratuityValues.setProofRequestPayment(infoGratuityValues.getProofRequestPayment());
			gratuityValues.setStartPayment(infoGratuityValues.getStartPayment());
			gratuityValues.setEndPayment(infoGratuityValues.getEndPayment());

			//write all payment phases
			writePaymentPhases(sp, infoGratuityValues, gratuityValues);
			System.out.println("escreveu as prestacoes");

			//update gratuity values in all student curricular plan that belong to this execution
			// degree
			updateStudentsGratuitySituation(sp, infoGratuityValues, gratuityValues);
			System.out.println("actualizou situacoes de estudantes que se referem a esta propina");

		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException("impossible.insertGratuityValues");
		}
		catch (FenixServiceException fenixServiceException)
		{
			throw fenixServiceException;
		}

		return Boolean.TRUE;
	}

	private void validateGratuity(ISuportePersistente sp, InfoGratuityValues infoGratuityValues)
		throws FenixServiceException
	{
		//find the gratuity's value
		Double gratuityValue = null;

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

		System.out.println("obteve o valor da propina: " + gratuityValue);

		List paymentPhasesList = infoGratuityValues.getInfoPaymentPhases();
		if (paymentPhasesList != null && paymentPhasesList.size() > 0)
		{
			//verify if total of all payment phases isn't greater then anual value
			ListIterator iterator = paymentPhasesList.listIterator();
			double totalValuePaymentPhases = 0;
			while (iterator.hasNext())
			{
				InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();
				totalValuePaymentPhases += infoPaymentPhase.getValue().floatValue();
			}
			if (totalValuePaymentPhases > gratuityValue.doubleValue())
			{
				System.out.println("valores de prestacoes sao maiores que o valor da propina");

				throw new FenixServiceException("error.masterDegree.gatuyiuty.totalValuePaymentPhases");
			}
			System.out.println("os valores das prestacoes estao correctos");

			//verify if all payment phases's dates are correct
			validateDatesOfPaymentPhases(paymentPhasesList);
			System.out.println("validou datas de prestacoes");

			//registration Payment
			if (infoGratuityValues.getRegistrationPayment() != null
				&& infoGratuityValues.getRegistrationPayment().equals(Boolean.TRUE))
			{
				iterator = paymentPhasesList.listIterator();
				InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();
				infoPaymentPhase.setDescription(SessionConstants.REGISTRATION_PAYMENT);
				System.out.println("mudou a descricao da prestacao de registo");

			}
		}
	}

	private void validateDatesOfPaymentPhases(List paymentPhasesList) throws FenixServiceException
	{
		Collections.sort(paymentPhasesList, new BeanComparator("endDate"));

		ListIterator iterator = paymentPhasesList.listIterator();
		while (iterator.hasNext())
		{
			InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();

			if (iterator.hasNext())
			{
				InfoPaymentPhase infoPaymentPhase2Compare = (InfoPaymentPhase) iterator.next();
				if (!infoPaymentPhase.getEndDate().before(infoPaymentPhase2Compare.getStartDate()))
				{
					System.out.println("datas de prestacoes coincidentes");

					throw new FenixServiceException("error.impossible.paymentPhaseWithWrongDates");
				}
				iterator.previous();
			}
		}
	}

	private void validatePaymentPhasesWithTransaction(
		ISuportePersistente sp,
		IGratuityValues gratuityValues)
		throws FenixServiceException
	{
		System.out.println("em validatePaymentPhasesWithTransaction");
		//verify if any payment phase has any transaction associated
		IPersistentPaymentPhase persistentPaymentPhase = sp.getIPersistentPaymentPhase();
		if (gratuityValues.getPaymentPhaseList() != null
			&& gratuityValues.getPaymentPhaseList().size() > 0)
		{
			ListIterator iterator = gratuityValues.getPaymentPhaseList().listIterator();
			while (iterator.hasNext())
			{
				IPaymentPhase paymentPhase = (IPaymentPhase) iterator.next();

				if (paymentPhase.getTransactionList() != null
					&& paymentPhase.getTransactionList().size() > 0)
				{
					System.out.println("ja existem transaccoes para as prestacoes");

					throw new FenixServiceException("error.impossible.paymentPhaseWithTransactional");
				}
			}
		}
	}

	private void registerWhoAndWhen(
		ISuportePersistente sp,
		InfoGratuityValues infoGratuityValues,
		IGratuityValues gratuityValues)
		throws ExcepcaoPersistencia
	{
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
	}

	private void writePaymentPhases(
		ISuportePersistente sp,
		InfoGratuityValues infoGratuityValues,
		IGratuityValues gratuityValues)
		throws FenixServiceException
	{
		IPersistentPaymentPhase persistentPaymentPhase = sp.getIPersistentPaymentPhase();

		try
		{
			if (gratuityValues.getPaymentPhaseList() != null
				&& gratuityValues.getPaymentPhaseList().size() > 0)
			{
				System.out.println("ha prestacoes, apagam-se");

				persistentPaymentPhase.deletePaymentPhasesOfThisGratuity(gratuityValues.getIdInternal());
			}

			if (infoGratuityValues.getInfoPaymentPhases() != null
				&& infoGratuityValues.getInfoPaymentPhases().size() > 0)
			{
				ListIterator iterator = infoGratuityValues.getInfoPaymentPhases().listIterator();
				while (iterator.hasNext())
				{
					InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();

					IPaymentPhase paymentPhase = new PaymentPhase();
					paymentPhase.setStartDate(infoPaymentPhase.getStartDate());
					paymentPhase.setEndDate(infoPaymentPhase.getEndDate());
					paymentPhase.setValue(infoPaymentPhase.getValue());
					paymentPhase.setDescription(infoPaymentPhase.getDescription());

					paymentPhase.setGratuityValues(gratuityValues);

					persistentPaymentPhase.simpleLockWrite(paymentPhase);
				}
			}
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException("impossible.insertGratuityValues");
		}
	}

	private void updateStudentsGratuitySituation(
		ISuportePersistente sp,
		InfoGratuityValues infoGratuityValues,
		IGratuityValues gratuityValues)
		throws FenixServiceException
	{
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
			sp.getIStudentCurricularPlanPersistente();
		IPersistentGratuitySituation persistentGratuitySituation = sp.getIPersistentGratuitySituation();

		List studentCurricularPlanList = null;
		List gratuitySituationList = null;
		try
		{
			//find all students curricular plan with the degree curricular plan that belongs to this
			// execution degree

			gratuitySituationList =
				persistentGratuitySituation.readGratuitySituationsByDegreeCurricularPlan(
					gratuityValues.getExecutionDegree().getCurricularPlan());

			Iterator iterGratuitySituation = gratuitySituationList.iterator();
			while (iterGratuitySituation.hasNext())
			{
				IGratuitySituation gratuitySituation = (IGratuitySituation) iterGratuitySituation.next();
				if (gratuitySituation != null
					&& (gratuitySituation.getGratuityValues() == null
						|| (gratuityValues.getIdInternal() != null
							&& !gratuitySituation.getGratuityValues().getIdInternal().equals(
								gratuityValues.getIdInternal()))))
				{
					System.out.println("actualiza situacao de propina");

					gratuitySituation.setGratuityValues(gratuityValues);
					persistentGratuitySituation.simpleLockWrite(gratuitySituation);
				}
			}
			
			//			studentCurricularPlanList =
			//				persistentStudentCurricularPlan.readByDegreeCurricularPlan(
			//					gratuityValues.getExecutionDegree().getCurricularPlan());
			//
			//			if (studentCurricularPlanList != null && studentCurricularPlanList.size() > 0)
			//			{
			//				System.out.println("existem estudantes para este plano curricular");
			//
			//				ListIterator iterator = studentCurricularPlanList.listIterator();
			//				//for each student curricular plan update the correspondent gratuity situatuion
			//				//with the gratuity values key
			//				while (iterator.hasNext())
			//				{
			//					IStudentCurricularPlan studentCurricularPlan =
			//						(IStudentCurricularPlan) iterator.next();
			//
			//					IGratuitySituation gratuitySituation =
			//						persistentGratuitySituation.readGratuitySituatuionByStudentCurricularPlan(
			//							studentCurricularPlan);
			//
			//					// ler gratuity situation
			//					
			//					if (gratuitySituation != null
			//						&& (gratuitySituation.getGratuityValues() == null
			//							|| (gratuityValues.getIdInternal() != null
			//								&& !gratuitySituation.getGratuityValues().getIdInternal().equals(
			//									gratuityValues.getIdInternal()))))
			//					{
			//						System.out.println("actualiza situacao de propina");
			//
			//						gratuitySituation.setGratuityValues(gratuityValues);
			//						persistentGratuitySituation.simpleLockWrite(gratuitySituation);
			//					}
			//				}
			//			}
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException("impossible.insertGratuityValues");
		}
	}
}
