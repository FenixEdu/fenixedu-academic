package ServidorAplicacao.Servico.commons.student;

import java.util.Collections;
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
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetEnrolmentGrade implements IServico {

	private static GetEnrolmentGrade servico = new GetEnrolmentGrade();

	/**
	 * The singleton access method of this class.
	 **/
	public static GetEnrolmentGrade getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private GetEnrolmentGrade() {
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "GetEnrolmentGrade";
	}

	public InfoEnrolmentEvaluation run(IEnrolment enrolment) throws FenixServiceException {

		List enrolmentEvaluations = enrolment.getEvaluations();

		if ((enrolment == null) || (enrolment.getEvaluations() == null) || (enrolment.getEvaluations().size() == 0)) {
			return null;
		}

		// if there's only one evaluation ...
		if (enrolmentEvaluations.size() == 1) {

			IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) enrolmentEvaluations.get(0);

			try {
				enrolmentEvaluation.setGrade((new Integer(enrolmentEvaluation.getGrade())).toString());

			} catch (NumberFormatException e) {

			}
			InfoEnrolmentEvaluation infolatestEvaluation = Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrolmentEvaluation);
			if (enrolmentEvaluation.getEmployee() != null) {

				try {
					if (String.valueOf(enrolmentEvaluation.getEmployee().getIdInternal()) != null
						|| String.valueOf(enrolmentEvaluation.getEmployee().getIdInternal()).length() > 0) {
						IEmployee employee = readEmployee(enrolmentEvaluation.getEmployee().getIdInternal().intValue());
						enrolmentEvaluation.setEmployee(employee);
						infolatestEvaluation.setInfoEmployee(Cloner.copyIPerson2InfoPerson(employee.getPerson()));
					}
				} catch (ExcepcaoPersistencia e1) {
					throw new FenixServiceException(e1);
				}
			}
			return infolatestEvaluation;

		}

		BeanComparator dateComparator = new BeanComparator("when");

		Collections.sort(enrolmentEvaluations, dateComparator);
		Collections.reverse(enrolmentEvaluations);

		IEnrolmentEvaluation latestEvaluation = (IEnrolmentEvaluation) enrolmentEvaluations.get(0);

		if (enrolment.getEnrolmentState().equals(EnrolmentState.NOT_APROVED)) {
			return getInfoLatestEvaluation(latestEvaluation);
		}

		// if the last evaluation is Not of "IMPROVEMENT" type
		if (!latestEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.IMPROVEMENT_OBJ)) {
			try {
				latestEvaluation.setGrade((new Integer(latestEvaluation.getGrade())).toString());
			} catch (NumberFormatException e) {

			}

			InfoEnrolmentEvaluation infolatestEvaluation = Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(latestEvaluation);
			try {
				if (latestEvaluation.getEmployee() != null) {
					if (String.valueOf(latestEvaluation.getEmployee().getIdInternal()) != null
						|| String.valueOf(latestEvaluation.getEmployee().getIdInternal()).length() > 0) {
						IEmployee employee = readEmployee(latestEvaluation.getEmployee().getIdInternal().intValue());
						latestEvaluation.setEmployee(employee);
						infolatestEvaluation.setInfoEmployee(Cloner.copyIPerson2InfoPerson(employee.getPerson()));
					}
				}
			} catch (ExcepcaoPersistencia e1) {
				throw new FenixServiceException(e1);
			}
			return infolatestEvaluation;
		} else {
			IEnrolmentEvaluation previousEvaluation = null;
			for (int i = 1; i < enrolmentEvaluations.size(); i++) {
				previousEvaluation = (IEnrolmentEvaluation) enrolmentEvaluations.get(i);
				if (!previousEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.IMPROVEMENT_OBJ)) {
					break;
				}
			}

			Integer latestMark = new Integer(0);
			try {
				latestMark = Integer.valueOf(latestEvaluation.getGrade());
			} catch (NumberFormatException e) {
				// If there's an Exception , the the student wasn't able to improve
				//exception is to be ignored				
			}

			// if there is no exception we must check wick is the higher grade

			Integer previousMark = new Integer(0);
			try {
				previousMark = Integer.valueOf(previousEvaluation.getGrade());
			} catch (NumberFormatException e) {
//				latestEvaluation.setGrade((new Integer(latestEvaluation.getGrade())).toString());
				InfoEnrolmentEvaluation infolatestEvaluation = Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(latestEvaluation);
				try {

					if (latestEvaluation.getEmployee() != null) {
						if (String.valueOf(latestEvaluation.getEmployee().getIdInternal()) != null
							|| String.valueOf(latestEvaluation.getEmployee().getIdInternal()).length() > 0) {
							IEmployee employee = readEmployee(latestEvaluation.getEmployee().getIdInternal().intValue());
							latestEvaluation.setEmployee(employee);
							infolatestEvaluation.setInfoEmployee(Cloner.copyIPerson2InfoPerson(employee.getPerson()));
						}
					}
					return infolatestEvaluation;
				} catch (ExcepcaoPersistencia e1) {
					throw new FenixServiceException(e1);
				}

			}

			if (previousMark.intValue() >= latestMark.intValue()) {
				try {
					previousEvaluation.setGrade((new Integer(previousEvaluation.getGrade())).toString());
					InfoEnrolmentEvaluation infolatestEvaluation =
						Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(previousEvaluation);
					if (previousEvaluation.getEmployee() != null) {
						if (String.valueOf(previousEvaluation.getEmployee().getIdInternal()) != null
							|| String.valueOf(previousEvaluation.getEmployee().getIdInternal()).length() > 0) {
							IEmployee employee = readEmployee(previousEvaluation.getEmployee().getIdInternal().intValue());
							latestEvaluation.setEmployee(employee);
							infolatestEvaluation.setInfoEmployee(Cloner.copyIPerson2InfoPerson(employee.getPerson()));
						}
					}
					return infolatestEvaluation;
				} catch (ExcepcaoPersistencia e1) {
					throw new FenixServiceException(e1);
				}

			} else {
				try {
					latestEvaluation.setGrade((new Integer(latestEvaluation.getGrade())).toString());
					InfoEnrolmentEvaluation infolatestEvaluation =
						Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(latestEvaluation);
					if (latestEvaluation.getEmployee() != null) {
						if (String.valueOf(latestEvaluation.getEmployee().getIdInternal()) != null
							|| String.valueOf(latestEvaluation.getEmployee().getIdInternal()).length() > 0) {
							IEmployee employee = readEmployee(latestEvaluation.getEmployee().getIdInternal().intValue());
							latestEvaluation.setEmployee(employee);
							infolatestEvaluation.setInfoEmployee(Cloner.copyIPerson2InfoPerson(employee.getPerson()));
						}
					}
					return infolatestEvaluation;
				} catch (ExcepcaoPersistencia e1) {
					throw new FenixServiceException(e1);
				}
			}
		}

	}

	private InfoEnrolmentEvaluation getInfoLatestEvaluation(IEnrolmentEvaluation latestEvaluation) throws FenixServiceException {
		InfoEnrolmentEvaluation infolatestEvaluation = Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(latestEvaluation);
		if (latestEvaluation.getEmployee() != null) {
			if (String.valueOf(latestEvaluation.getEmployee().getIdInternal()) != null
				|| String.valueOf(latestEvaluation.getEmployee().getIdInternal()).length() > 0) {
				try {
					IEmployee employee = readEmployee(latestEvaluation.getEmployee().getIdInternal().intValue());
					latestEvaluation.setEmployee(employee);
					infolatestEvaluation.setInfoEmployee(Cloner.copyIPerson2InfoPerson(employee.getPerson()));
				} catch (ExcepcaoPersistencia e) {
					throw new FenixServiceException(e);
				}
			}
		}
		return infolatestEvaluation;
	}

	private IEmployee readEmployee(int id) throws ExcepcaoPersistencia {
		IEmployee employee = null;
		IPersistentEmployee persistentEmployee;
		try {
			persistentEmployee = SuportePersistenteOJB.getInstance().getIPersistentEmployee();
			employee = persistentEmployee.readByIdInternal(id);
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		}
		return employee;
	}

}