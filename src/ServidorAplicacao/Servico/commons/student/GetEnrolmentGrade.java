package ServidorAplicacao.Servico.commons.student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.util.Cloner;
import Dominio.IEmployee;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;
import Util.MarkType;


public class GetEnrolmentGrade implements IServico {

	private static GetEnrolmentGrade servico = new GetEnrolmentGrade();

	public static GetEnrolmentGrade getService() {
		return servico;
	}

	private GetEnrolmentGrade() {
	}

	public final String getNome() {
		return "GetEnrolmentGrade";
	}

	public InfoEnrolmentEvaluation run(IEnrolment enrolment) throws FenixServiceException {

		List enrolmentEvaluations = enrolment.getEvaluations();

		if ((enrolment == null) || (enrolment.getEvaluations() == null) || (enrolment.getEvaluations().size() == 0)) {
			return null;
		}

		if (enrolmentEvaluations.size() == 1)
		{
			IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) enrolmentEvaluations.get(0);
			return this.getInfoLatestEvaluation(enrolmentEvaluation);
		} else
		{
			IEnrolmentEvaluation enrolmentEvaluation = this.getRealEnrollmentEvaluation(enrolment);
			return this.getInfoLatestEvaluation(enrolmentEvaluation);
		}
	}

	/**
	 * @param latestEvaluation
	 * @return
	 * @throws FenixServiceException
	 */
	private InfoEnrolmentEvaluation getInfoLatestEvaluation(IEnrolmentEvaluation latestEvaluation) throws FenixServiceException {
		InfoEnrolmentEvaluation infolatestEvaluation = Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(latestEvaluation);
		if (latestEvaluation.getEmployee() != null) {
			IEmployee employee = readEmployee(latestEvaluation.getEmployee().getIdInternal());
			infolatestEvaluation.setInfoEmployee(Cloner.copyIPerson2InfoPerson(employee.getPerson()));
		}
		return infolatestEvaluation;
	}

	/**
	 * @param id
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	private IEmployee readEmployee(Integer id) throws FenixServiceException {
		IEmployee employee = null;
		IPersistentEmployee persistentEmployee;
		try {
			persistentEmployee = SuportePersistenteOJB.getInstance().getIPersistentEmployee();
			employee = persistentEmployee.readByIdInternal(id.intValue());
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
		return employee;
	}

	/**
	 * @param enrolment
	 * @return
	 */
	private IEnrolmentEvaluation getRealEnrollmentEvaluation(IEnrolment enrolment)
	{
		List enrollmentEvaluationsList = enrolment.getEvaluations();
		IEnrolmentEvaluation enrolmentEvaluationToReturn = null;

		if (enrolment.getEnrolmentState().equals(EnrolmentState.NOT_APROVED))
		{
			Iterator iterator = enrollmentEvaluationsList.iterator();
			while (iterator.hasNext())
			{
				IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) iterator.next();
				List aux = Arrays.asList(MarkType.REP_MARKS);
				if(aux.contains(enrolmentEvaluation.getGrade()))
				{
					enrolmentEvaluationToReturn = enrolmentEvaluation;
				}
			}
			return enrolmentEvaluationToReturn;
		} else if (enrolment.getEnrolmentState().equals(EnrolmentState.NOT_EVALUATED))
		{
			Iterator iterator = enrollmentEvaluationsList.iterator();
			while (iterator.hasNext())
			{
				IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) iterator.next();
				List aux = Arrays.asList(MarkType.NA_MARKS);
				if(aux.contains(enrolmentEvaluation.getGrade()) || enrolmentEvaluation.getGrade().equals(""))
				{
					enrolmentEvaluationToReturn = enrolmentEvaluation;
				}
			}
		} else if (enrolment.getEnrolmentState().equals(EnrolmentState.APROVED))
		{
			List enrolmentEvaluationsToAnalyse = new ArrayList();
			enrolmentEvaluationsToAnalyse.addAll(enrollmentEvaluationsList);
			Iterator iterator = enrollmentEvaluationsList.iterator();
			while (iterator.hasNext())
			{
				IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) iterator.next();
				List aux = Arrays.asList(MarkType.OTHER_MARKS);
				List enrolmentEvaluationsToRemove = new ArrayList();
				if(aux.contains(enrolmentEvaluation.getGrade()) || enrolmentEvaluation.getGrade().equals(""))
				{
					enrolmentEvaluationsToRemove.add(enrolmentEvaluation);
				}
				enrolmentEvaluationsToAnalyse.removeAll(enrolmentEvaluationsToRemove);
			}

			if(!enrolmentEvaluationsToAnalyse.isEmpty())
			{
				BeanComparator dateComparator = new BeanComparator("grade");
				Collections.sort(enrolmentEvaluationsToAnalyse, dateComparator);
				Collections.reverse(enrolmentEvaluationsToAnalyse);
				enrolmentEvaluationToReturn = (IEnrolmentEvaluation) enrolmentEvaluationsToAnalyse.get(0);
			} else
			{
				iterator = enrollmentEvaluationsList.iterator();
				while (iterator.hasNext())
				{
					IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) iterator.next();
					List aux = Arrays.asList(MarkType.AP_MARKS);
					if(aux.contains(enrolmentEvaluation.getGrade()))
					{
						enrolmentEvaluationToReturn = enrolmentEvaluation;
					}
				}
			}
		}

		return enrolmentEvaluationToReturn;
	}
}