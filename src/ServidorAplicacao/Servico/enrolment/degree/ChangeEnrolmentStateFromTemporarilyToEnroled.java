package ServidorAplicacao.Servico.enrolment.degree;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import Dominio.DegreeCurricularPlan;
import Dominio.EnrolmentEvaluation;
import Dominio.Frequenta;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDisciplinaExecucao;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IFrequenta;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentState;
import Util.StudentCurricularPlanState;

/**
 * @author dcs-rjao
 *
 * 9/Abr/2003
 */
public class ChangeEnrolmentStateFromTemporarilyToEnroled implements IServico {

	private static ISuportePersistente persistentSupport = null;
	private static IPersistentStudent studentDAO = null;
	private static IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;
	private static IStudentCurricularPlanPersistente persistentStudentCurricularPlan = null;
	private static IPersistentEnrolment persistentEnrolment = null;
	private static IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = null;
	private static IFrequentaPersistente frequentaPersistente = null;


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
	public void run(String degreeCurricularPlanName) throws FenixServiceException {

		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
			persistentEnrolment = persistentSupport.getIPersistentEnrolment();
			frequentaPersistente = persistentSupport.getIFrequentaPersistente();
			persistentDegreeCurricularPlan = persistentSupport.getIPersistentDegreeCurricularPlan();
			persistentEnrolmentEvaluation = persistentSupport.getIPersistentEnrolmentEvaluation();
			
			IDegreeCurricularPlan degreeCurricularPlanCriteria = new DegreeCurricularPlan();
			degreeCurricularPlanCriteria.setName(degreeCurricularPlanName);
			IDegreeCurricularPlan degreeCurricularPlan =
				(IDegreeCurricularPlan) persistentDegreeCurricularPlan.readDomainObjectByCriteria(degreeCurricularPlanCriteria);

			//			IStudent student = studentDAO.readByUsername(((UserView) userView).getUtilizador());
			//			IStudentCurricularPlan studentActiveCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());
			//			List TemporarilyEnrolemts = persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(studentActiveCurricularPlan, EnrolmentState.TEMPORARILY_ENROLED);

//			List listActiveStudentCurricularPlans =
//				persistentStudentCurricularPlan.readAllByDegreeCurricularPlanAndState(degreeCurricularPlan, StudentCurricularPlanState.ACTIVE_OBJ);
//			Iterator iterator = listActiveStudentCurricularPlans.iterator();
//			while (iterator.hasNext()) {
//				IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator.next();
//				List TemporarilyEnrolemts =
//					persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(studentCurricularPlan, EnrolmentState.TEMPORARILY_ENROLED);
//
//				Iterator temporarilyEnrolemtsIterator = TemporarilyEnrolemts.iterator();
//				while (temporarilyEnrolemtsIterator.hasNext()) {
//					IEnrolment enrolment = (IEnrolment) temporarilyEnrolemtsIterator.next();
//					enrolment.setEnrolmentState(EnrolmentState.ENROLED);
//					persistentEnrolment.lockWrite(enrolment);
//					createEnrolmentEvaluation(enrolment);
//					createAttend(enrolment);
//				}
//			}
			List TemporarilyEnrolemts =
				persistentEnrolment.readEnrolmentsByStudentCurricularPlanStateAndEnrolmentStateAndDegreeCurricularPlans(StudentCurricularPlanState.ACTIVE_OBJ, EnrolmentState.TEMPORARILY_ENROLED, degreeCurricularPlan);

			Iterator temporarilyEnrolemtsIterator = TemporarilyEnrolemts.iterator();
			while (temporarilyEnrolemtsIterator.hasNext()) {
				IEnrolment enrolment = (IEnrolment) temporarilyEnrolemtsIterator.next();
				enrolment.setEnrolmentState(EnrolmentState.ENROLED);
				persistentEnrolment.lockWrite(enrolment);
				createEnrolmentEvaluation(enrolment);
				createAttend(enrolment);
			}

		} catch (ExistingPersistentException e) {
			throw new FenixServiceException(e);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}

	public static void createAttend(IEnrolment enrolment) throws ExcepcaoPersistencia, ExistingPersistentException {
		persistentSupport = SuportePersistenteOJB.getInstance();
		frequentaPersistente = persistentSupport.getIFrequentaPersistente();
		try {
			IStudent student = enrolment.getStudentCurricularPlan().getStudent();
			IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) enrolment.getCurricularCourseScope().getCurricularCourse().getAssociatedExecutionCourses().get(0);

			IFrequenta frequenta = frequentaPersistente.readByAlunoAndDisciplinaExecucao(student, executionCourse);

			if (frequenta == null) {
				frequenta = new Frequenta();
				frequentaPersistente.simpleLockWrite(frequenta);
				frequenta.setAluno(student);
				frequenta.setEnrolment(enrolment);
				frequenta.setDisciplinaExecucao(executionCourse);
			}
		} catch (ExistingPersistentException e) {
			throw e;
		} catch (ExcepcaoPersistencia e) {
			throw e;
		}
	}

	public static void createEnrolmentEvaluation(IEnrolment enrolment) throws ExcepcaoPersistencia {
		persistentSupport = SuportePersistenteOJB.getInstance();
		persistentEnrolmentEvaluation = persistentSupport.getIPersistentEnrolmentEvaluation();
		try {
			IEnrolmentEvaluation enrolmentEvaluation = persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(enrolment, enrolment.getEnrolmentEvaluationType(), null);
			if (enrolmentEvaluation == null) {
				enrolmentEvaluation = new EnrolmentEvaluation();
				persistentEnrolmentEvaluation.simpleLockWrite(enrolmentEvaluation);
				enrolmentEvaluation.setEnrolment(enrolment);
				enrolmentEvaluation.setEnrolmentEvaluationType(enrolment.getEnrolmentEvaluationType());
				enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
				enrolmentEvaluation.setWhen(new Date());
				// TODO [DAVID]: Quando o algoritmo do checksum estiver feito tem de se actualizar este campo
				enrolmentEvaluation.setCheckSum(null);
			}
		} catch (ExcepcaoPersistencia e) {
			throw e;
		}
	}

}