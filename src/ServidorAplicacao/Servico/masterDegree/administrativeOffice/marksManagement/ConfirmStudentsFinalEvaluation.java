package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.beanutils.BeanComparator;

import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.util.Cloner;
import Dominio.CurricularCourseScope;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.Funcionario;
import Dominio.ICurricularCourseScope;
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
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;
import Util.EnrolmentEvaluationState;

/**
 * @author Fernanda Quitério
 * 10/07/2003
 *
 */
public class ConfirmStudentsFinalEvaluation implements IServico {
	private static ConfirmStudentsFinalEvaluation _servico = new ConfirmStudentsFinalEvaluation();

	/**
		* The actor of this class.
		**/
	private ConfirmStudentsFinalEvaluation() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "ConfirmStudentsFinalEvaluation";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourse
	 */
	public static ConfirmStudentsFinalEvaluation getService() {
		return _servico;
	}

	public Boolean run(Integer scopeCode, IUserView userView) throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourseScope persistentCurricularCourseScope = sp.getIPersistentCurricularCourseScope();
			IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
			IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();

			//			employee
			IPessoa person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
			Funcionario employee = readEmployee(person);

			//	get curricularCourseScope for enrolmentEvaluation
			ICurricularCourseScope curricularCourseScope = new CurricularCourseScope();
			curricularCourseScope.setIdInternal(scopeCode);
			curricularCourseScope = (ICurricularCourseScope) persistentCurricularCourseScope.readByOId(curricularCourseScope, false);

			//			this becomes necessary to use criteria
			InfoCurricularCourseScope infoCurricularCourseScope =
				Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
			ICurricularCourseScope curricularCourseScopeForCriteria =
				Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCurricularCourseScope);

			IEnrolment enrolment = new Enrolment();
			enrolment.setCurricularCourseScope(curricularCourseScopeForCriteria);
			IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
			enrolmentEvaluation.setEnrolment(enrolment);
			List enrolmentEvaluations = persistentEnrolmentEvaluation.readByCriteria(enrolmentEvaluation);

			if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {

				ListIterator iterEnrolmentEvaluations = enrolmentEvaluations.listIterator();
				while (iterEnrolmentEvaluations.hasNext()) {
					IEnrolmentEvaluation enrolmentEvaluationElem = (IEnrolmentEvaluation) iterEnrolmentEvaluations.next();

					persistentEnrolmentEvaluation.lockWrite(enrolmentEvaluationElem);

					enrolmentEvaluationElem.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
					Calendar calendar = Calendar.getInstance();
					enrolmentEvaluationElem.setWhen(calendar.getTime());
					enrolmentEvaluationElem.setEmployee(employee);
//					TODO: checksum
					enrolmentEvaluationElem.setCheckSum("");
				}
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
			FenixServiceException newEx = new FenixServiceException("");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return Boolean.TRUE;
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