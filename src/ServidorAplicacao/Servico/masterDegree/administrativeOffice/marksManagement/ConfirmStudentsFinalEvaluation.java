package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import DataBeans.InfoCurricularCourseScope;
import DataBeans.util.Cloner;
import Dominio.CurricularCourseScope;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.Funcionario;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IPessoa;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentState;

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
			IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
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
			IEnrolmentEvaluation enrolmentEvaluationForCriteria = new EnrolmentEvaluation();
			enrolmentEvaluationForCriteria.setEnrolment(enrolment);
			List enrolmentEvaluations = persistentEnrolmentEvaluation.readByCriteria(enrolmentEvaluationForCriteria);

			if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
				ListIterator iterEnrolmentEvaluations = enrolmentEvaluations.listIterator();
				while (iterEnrolmentEvaluations.hasNext()) {
					IEnrolmentEvaluation enrolmentEvaluationElem = (IEnrolmentEvaluation) iterEnrolmentEvaluations.next();
					persistentEnrolmentEvaluation.simpleLockWrite(enrolmentEvaluationElem);

					enrolmentEvaluationElem.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
					Calendar calendar = Calendar.getInstance();
					enrolmentEvaluationElem.setWhen(calendar.getTime());
					enrolmentEvaluationElem.setEmployee(employee);
//					TODO: checksum
					enrolmentEvaluationElem.setCheckSum("");
					
					// update state of enrolment: aproved or notAproved
					IEnrolment enrolmentToEdit = enrolmentEvaluationElem.getEnrolment();
					persistentEnrolment.simpleLockWrite(enrolmentToEdit);
					
					EnrolmentState newEnrolmentState = EnrolmentState.APROVED;
					try{
						Integer grade = new Integer(enrolmentEvaluationElem.getGrade()); 
					} catch(NumberFormatException e){
						newEnrolmentState = EnrolmentState.NOT_APROVED;
					}
					enrolmentToEdit.setEnrolmentState(newEnrolmentState);
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
}