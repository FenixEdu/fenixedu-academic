package ServidorAplicacao.Servico.commons.student;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.util.Cloner;
import Dominio.Funcionario;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;

/**
 * 
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
					if (String.valueOf(enrolmentEvaluation.getEmployee().getCodigoInterno()) != null || String.valueOf(enrolmentEvaluation.getEmployee().getCodigoInterno()).length() > 0) {
						Funcionario funcionario = readEmployee(enrolmentEvaluation.getEmployee().getCodigoInterno());
						enrolmentEvaluation.setEmployee(funcionario);
						infolatestEvaluation.setInfoEmployee(Cloner.copyIPerson2InfoPerson(funcionario.getPerson()));
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
					if (String.valueOf(latestEvaluation.getEmployee().getCodigoInterno()) != null || String.valueOf(latestEvaluation.getEmployee().getCodigoInterno()).length() > 0) {
						Funcionario funcionario = readEmployee(latestEvaluation.getEmployee().getCodigoInterno());
						latestEvaluation.setEmployee(funcionario);
						infolatestEvaluation.setInfoEmployee(Cloner.copyIPerson2InfoPerson(funcionario.getPerson()));
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

			Integer latestMark = null;
			try {
				latestMark = new Integer(latestEvaluation.getGrade());
			} catch (NumberFormatException e) {
				// If there's an Exception , the the student wasn't able to improve

				latestEvaluation.setGrade((new Integer(latestEvaluation.getGrade())).toString());
				InfoEnrolmentEvaluation infolatestEvaluation = Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(latestEvaluation);

				try {
					if (latestEvaluation.getEmployee() != null) {
						if (String.valueOf(latestEvaluation.getEmployee().getCodigoInterno()) != null || String.valueOf(latestEvaluation.getEmployee().getCodigoInterno()).length() > 0) {
							Funcionario funcionario = readEmployee(latestEvaluation.getEmployee().getCodigoInterno());
							latestEvaluation.setEmployee(funcionario);
							infolatestEvaluation.setInfoEmployee(Cloner.copyIPerson2InfoPerson(funcionario.getPerson()));
						}
					}
				} catch (ExcepcaoPersistencia e1) {
					throw new FenixServiceException(e1);
				}
				return infolatestEvaluation;
			}

			// if there is no exception we must check wick is the higher grade

			Integer previousMark = null;
			try {
				previousMark = new Integer(previousEvaluation.getGrade());
			} catch (NumberFormatException e) {
				latestEvaluation.setGrade((new Integer(latestEvaluation.getGrade())).toString());
				InfoEnrolmentEvaluation infolatestEvaluation = Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(latestEvaluation);
				try {

					if (latestEvaluation.getEmployee() != null) {
						if (String.valueOf(latestEvaluation.getEmployee().getCodigoInterno()) != null || String.valueOf(latestEvaluation.getEmployee().getCodigoInterno()).length() > 0) {
							Funcionario funcionario = readEmployee(latestEvaluation.getEmployee().getCodigoInterno());
							latestEvaluation.setEmployee(funcionario);
							infolatestEvaluation.setInfoEmployee(Cloner.copyIPerson2InfoPerson(funcionario.getPerson()));
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
					InfoEnrolmentEvaluation infolatestEvaluation = Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(previousEvaluation);
					if (previousEvaluation.getEmployee() != null) {
						if (String.valueOf(previousEvaluation.getEmployee().getCodigoInterno()) != null || String.valueOf(previousEvaluation.getEmployee().getCodigoInterno()).length() > 0) {
							Funcionario funcionario = readEmployee(previousEvaluation.getEmployee().getCodigoInterno());
							latestEvaluation.setEmployee(funcionario);
							infolatestEvaluation.setInfoEmployee(Cloner.copyIPerson2InfoPerson(funcionario.getPerson()));
						}
					}
					return infolatestEvaluation;
				} catch (ExcepcaoPersistencia e1) {
					throw new FenixServiceException(e1);
				}

			} else {
				try {
					latestEvaluation.setGrade((new Integer(latestEvaluation.getGrade())).toString());
					InfoEnrolmentEvaluation infolatestEvaluation = Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(latestEvaluation);
					if (latestEvaluation.getEmployee() != null) {
						if (String.valueOf(latestEvaluation.getEmployee().getCodigoInterno()) != null || String.valueOf(latestEvaluation.getEmployee().getCodigoInterno()).length() > 0) {
							Funcionario funcionario = readEmployee(latestEvaluation.getEmployee().getCodigoInterno());
							latestEvaluation.setEmployee(funcionario);
							infolatestEvaluation.setInfoEmployee(Cloner.copyIPerson2InfoPerson(funcionario.getPerson()));
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
			if (String.valueOf(latestEvaluation.getEmployee().getCodigoInterno()) != null || String.valueOf(latestEvaluation.getEmployee().getCodigoInterno()).length() > 0) {
				try {
					Funcionario funcionario = readEmployee(latestEvaluation.getEmployee().getCodigoInterno());
					latestEvaluation.setEmployee(funcionario);
					infolatestEvaluation.setInfoEmployee(Cloner.copyIPerson2InfoPerson(funcionario.getPerson()));
				} catch (ExcepcaoPersistencia e) {
					throw new FenixServiceException(e);
				}
			}
		}
		return infolatestEvaluation;
	}

	private Funcionario readEmployee(int funcionarioID) throws ExcepcaoPersistencia {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentEmployee funcionarioPersistente = sp.getIPersistentEmployee();
		Funcionario _funcionario = (Funcionario) funcionarioPersistente.readByCodigoInterno(funcionarioID);

		return _funcionario;

	}

}