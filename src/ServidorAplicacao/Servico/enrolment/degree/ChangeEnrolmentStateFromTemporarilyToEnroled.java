package ServidorAplicacao.Servico.enrolment.degree;

import java.util.Iterator;
import java.util.List;

import Dominio.EnrolmentEvaluation;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 9/Abr/2003
 */
public class ChangeEnrolmentStateFromTemporarilyToEnroled implements IServico {

	private static ChangeEnrolmentStateFromTemporarilyToEnroled _servico = new ChangeEnrolmentStateFromTemporarilyToEnroled();
	/**
	 * The singleton access method of this class.
	 **/
	public static ChangeEnrolmentStateFromTemporarilyToEnroled getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ChangeEnrolmentStateFromTemporarilyToEnroled() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ChangeEnrolmentStateFromTemporarilyToEnroled";
	}

	/**
	 * @param infoStudent
	 * @param infoDegree
	 * @return EnrolmentContext
	 * @throws FenixServiceException
	 */
	public void run(IUserView userView) throws FenixServiceException {

		try {
			ISuportePersistente persistentSupport =	SuportePersistenteOJB.getInstance();
			IPersistentStudent studentDAO =	persistentSupport.getIPersistentStudent();
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan =	persistentSupport.getIStudentCurricularPlanPersistente();
			IPersistentEnrolment persistentEnrolment =	persistentSupport.getIPersistentEnrolment();

			// FIXME DAVID-RICARDO: Este algoritmo não devia ser para todos os cursos em vez de ser só para um estudante?
			IStudent student = studentDAO.readByUsername(((UserView) userView).getUtilizador());
			IStudentCurricularPlan studentActiveCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());
			List TemporarilyEnrolemts = persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(studentActiveCurricularPlan, EnrolmentState.TEMPORARILY_ENROLED);

			Iterator iterator = TemporarilyEnrolemts.iterator();
			while (iterator.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterator.next();
				enrolment.setEnrolmentState(EnrolmentState.ENROLED);
				persistentEnrolment.lockWrite(enrolment);
				createEnrolmentEvaluation(enrolment);
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace(System.out);
			throw new FenixServiceException(ex);
		} catch (IllegalStateException ex) {
			ex.printStackTrace(System.out);
			throw new FenixServiceException(ex);
		}

	}

	private void createEnrolmentEvaluation(IEnrolment enrolment) {
		IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
		enrolmentEvaluation.setEnrolment(enrolment);
		enrolmentEvaluation.setEnrolmentEvaluationType(enrolment.getEnrolmentEvaluationType());
		enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
		// TODO DAVID-RICARDO: Quando o algoritmo do checksum estiver feito tem de ser actualizar este campo
		enrolmentEvaluation.setCheckSum(null);
	}
	
}