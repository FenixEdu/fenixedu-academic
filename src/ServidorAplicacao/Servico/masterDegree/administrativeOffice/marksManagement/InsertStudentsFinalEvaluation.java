package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.beanutils.BeanComparator;

import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.util.Cloner;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.Funcionario;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import Dominio.Student;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 * @author Fernanda Quitério
 *
 */
public class InsertStudentsFinalEvaluation implements IServico {
	private static InsertStudentsFinalEvaluation _servico = new InsertStudentsFinalEvaluation();

	/**
		* The actor of this class.
		**/
	private InsertStudentsFinalEvaluation() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "InsertStudentsFinalEvaluation";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourse
	 */
	public static InsertStudentsFinalEvaluation getService() {
		return _servico;
	}

	public List run(List evaluations, Integer teacherNumber, Date evaluationDate, IUserView userView)
		throws FenixServiceException {

		List infoEvaluationsWithError = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
			IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

			//			responsible teacher
			ITeacher teacher = persistentTeacher.readTeacherByNumber(teacherNumber);
			if (teacher == null) {
				throw new NonExistingServiceException();
			}
			//			employee
			IPessoa person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
			Funcionario employee = readEmployee(person);

			infoEvaluationsWithError = new ArrayList();
			Calendar calendario = Calendar.getInstance();
			ListIterator iterEnrolmentEvaluations = evaluations.listIterator();
			while (iterEnrolmentEvaluations.hasNext()) {
				InfoEnrolmentEvaluation infoEnrolmentEvaluation = (InfoEnrolmentEvaluation) iterEnrolmentEvaluations.next();

				infoEnrolmentEvaluation = completeEnrolmentEvaluation(infoEnrolmentEvaluation);

				if (!isValidEvaluation(infoEnrolmentEvaluation)) {
					infoEvaluationsWithError.add(infoEnrolmentEvaluation);
				} else {
					IEnrolment enrolmentForCriteria = new Enrolment();
					enrolmentForCriteria.setIdInternal(infoEnrolmentEvaluation.getInfoEnrolment().getIdInternal());

					IEnrolmentEvaluation enrolmentEvaluationForCriteria = new EnrolmentEvaluation();
					enrolmentEvaluationForCriteria.setEnrolment(enrolmentForCriteria);
					List enrolmentEvaluationsForEnrolment =
						(List) persistentEnrolmentEvaluation.readByCriteria(enrolmentEvaluationForCriteria);
					if (enrolmentEvaluationsForEnrolment.size() == 0) {
						//		it will never happen!!
						throw new FenixServiceException();
					}
					Collections.sort(enrolmentEvaluationsForEnrolment, new BeanComparator("enrolment.idInternal"));

					//		we want last enrolment evaluation in case of improvement
					IEnrolmentEvaluation enrolmentEvaluation =
						(IEnrolmentEvaluation) enrolmentEvaluationsForEnrolment.get(enrolmentEvaluationsForEnrolment.size() - 1);

					persistentEnrolmentEvaluation.lockWrite(enrolmentEvaluation);

					if (evaluationDate != null) {
						enrolmentEvaluation.setExamDate(evaluationDate);
					} else {
						enrolmentEvaluation.setExamDate(calendario.getTime());
					}
					enrolmentEvaluation.setGrade(infoEnrolmentEvaluation.getGrade());
					enrolmentEvaluation.setPersonResponsibleForGrade(teacher.getPerson());
					enrolmentEvaluation.setGradeAvailableDate(calendario.getTime());
					enrolmentEvaluation.setEmployee(employee);
					enrolmentEvaluation.setWhen(calendario.getTime());
					enrolmentEvaluation.setObservation("Lançamento de Notas na Secretaria");
					enrolmentEvaluation.setCheckSum("");
				}
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
			FenixServiceException newEx = new FenixServiceException("");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return infoEvaluationsWithError;
	}

	private Funcionario readEmployee(IPessoa person) {
		Funcionario employee = null;
		SuportePersistente spJDBC = SuportePersistente.getInstance();
		IFuncionarioPersistente persistentEmployee = spJDBC.iFuncionarioPersistente();

		try {
			spJDBC.iniciarTransaccao();

			try {
				employee = persistentEmployee.lerFuncionarioPorPessoa(person.getIdInternal().intValue());

			} catch (Exception e) {
				spJDBC.cancelarTransaccao();
				e.printStackTrace();
				return employee;
			}

			spJDBC.confirmarTransaccao();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return employee;
		}
	}

	private InfoEnrolmentEvaluation completeEnrolmentEvaluation(InfoEnrolmentEvaluation infoEnrolmentEvaluation)
		throws FenixServiceException {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentStudent persistentStudent = sp.getIPersistentStudent();

			//			Student
			IStudent student = new Student();
			student.setIdInternal(
				infoEnrolmentEvaluation.getInfoEnrolment().getInfoStudentCurricularPlan().getInfoStudent().getIdInternal());
			student = (IStudent) persistentStudent.readByOId(student, false);

			infoEnrolmentEvaluation.getInfoEnrolment().getInfoStudentCurricularPlan().setInfoStudent(
				Cloner.copyIStudent2InfoStudent(student));

			return infoEnrolmentEvaluation;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

	private boolean isValidEvaluation(InfoEnrolmentEvaluation infoEnrolmentEvaluation) {
		IStudentCurricularPlan studentCurricularPlan = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IStudentCurricularPlanPersistente curricularPlanPersistente = sp.getIStudentCurricularPlanPersistente();

			studentCurricularPlan =
				curricularPlanPersistente.readActiveStudentCurricularPlan(
					infoEnrolmentEvaluation.getInfoEnrolment().getInfoStudentCurricularPlan().getInfoStudent().getNumber(),
					infoEnrolmentEvaluation.getInfoEnrolment().getInfoStudentCurricularPlan().getInfoStudent().getDegreeType());
		} catch (Exception e) {
			e.printStackTrace();
		}

		IDegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();

		// test marks by execution course: strategy 
		IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory.getInstance();
		IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy =
			degreeCurricularPlanStrategyFactory.getDegreeCurricularPlanStrategy(degreeCurricularPlan);
		if (infoEnrolmentEvaluation.getGrade() == null || infoEnrolmentEvaluation.getGrade().length() == 0) {
			return false;
		} else {
			return degreeCurricularPlanStrategy.checkMark(infoEnrolmentEvaluation.getGrade());
		}
	}
}