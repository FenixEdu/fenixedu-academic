package ServidorAplicacao.Servico.equivalence;

import java.util.Date;
import java.util.Iterator;

import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.equivalence.InfoCurricularCourseScopeGrade;
import DataBeans.equivalence.InfoEquivalenceContext;
import DataBeans.util.Cloner;
import Dominio.Enrolment;
import Dominio.EnrolmentEquivalence;
import Dominio.EnrolmentEquivalenceRestriction;
import Dominio.EnrolmentEvaluation;
import Dominio.Funcionario;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEquivalence;
import Dominio.IEnrolmentEquivalenceRestriction;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IPessoa;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEquivalence;
import ServidorPersistente.IPersistentEnrolmentEquivalenceRestriction;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
/**
 * @author David Santos
 * 9/Jul/2003
 */

public class ConfirmEquivalence implements IServico {

	private static ConfirmEquivalence service = new ConfirmEquivalence();
	private ISuportePersistente persistentSupport = null;
	private IPersistentEnrolment persistentEnrolment = null;
	private IPersistentEnrolmentEquivalence persistentEnrolmentEquivalence = null;
	private IPersistentEnrolmentEquivalenceRestriction persistentEnrolmentEquivalenceRestriction = null;
	private IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = null;
	private IPessoaPersistente pessoaPersistente = null;
	private IFuncionarioPersistente funcionarioPersistente = null;

	public static ConfirmEquivalence getService() {
		return ConfirmEquivalence.service;
	}

	private ConfirmEquivalence() {
	}

	public final String getNome() {
		return "ConfirmEquivalence";
	}

	public InfoEquivalenceContext run(InfoEquivalenceContext infoEquivalenceContext) throws FenixServiceException {

		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentEnrolment = persistentSupport.getIPersistentEnrolment();
			persistentEnrolmentEquivalence = persistentSupport.getIPersistentEnrolmentEquivalence();
			persistentEnrolmentEquivalenceRestriction = persistentSupport.getIPersistentEnrolmentEquivalenceRestriction();
			persistentEnrolmentEvaluation = persistentSupport.getIPersistentEnrolmentEvaluation();
			pessoaPersistente = persistentSupport.getIPessoaPersistente();

			funcionarioPersistente = SuportePersistente.getInstance().iFuncionarioPersistente();

			//			IPersistentEnrolmentEquivalence persistentEnrolmentEquivalence = persistentSupport.getIPersistentEnrolmentEq();

			Iterator scopesToGetEquivalenceIterator = infoEquivalenceContext.getChosenInfoCurricularCourseScopesToGetEquivalenceWithGrade().iterator();
			while (scopesToGetEquivalenceIterator.hasNext()) {
				InfoCurricularCourseScopeGrade infoCurricularCourseScopeGrade = (InfoCurricularCourseScopeGrade) scopesToGetEquivalenceIterator.next();

				IEnrolment newEnrolment = createNewEnrolment(infoEquivalenceContext, infoCurricularCourseScopeGrade);

				createNewEnrolmentEvaluation(infoEquivalenceContext, newEnrolment, infoCurricularCourseScopeGrade);

				IEnrolmentEquivalence enrolmentEquivalence = createNewEnrolmentEquivalence(newEnrolment);

				Iterator enrolmentsToGiveEquivalenceIterator = infoEquivalenceContext.getChosenInfoEnrolmentsToGiveEquivalence().iterator();
				while (enrolmentsToGiveEquivalenceIterator.hasNext()) {
					IEnrolment enrolmentToGiveEquivalence = (IEnrolment) enrolmentsToGiveEquivalenceIterator.next();
					createNewEnrolmentEquivalenceRestriction(enrolmentEquivalence, enrolmentToGiveEquivalence);
				}
				//				TODO DAVID-RICARDO: Tratar opções
			}
		} catch (ExistingPersistentException e1) {
			throw new FenixServiceException(e1);
		} catch (ExcepcaoPersistencia e1) {
			throw new FenixServiceException(e1);
		}

		return infoEquivalenceContext;
	}

	private IEnrolmentEquivalenceRestriction createNewEnrolmentEquivalenceRestriction(
		IEnrolmentEquivalence enrolmentEquivalence,
		IEnrolment enrolmentToGiveEquivalence)
		throws ExistingPersistentException, ExcepcaoPersistencia {
		try {
			IEnrolmentEquivalenceRestriction enrolmentEquivalenceRestriction = new EnrolmentEquivalenceRestriction();
			enrolmentEquivalenceRestriction.setEnrolmentEquivalence(enrolmentEquivalence);
			enrolmentEquivalenceRestriction.setEquivalentEnrolment(enrolmentToGiveEquivalence);
			persistentEnrolmentEquivalenceRestriction.lockWrite(enrolmentEquivalenceRestriction);
			return enrolmentEquivalenceRestriction;
		} catch (ExistingPersistentException e) {
			throw e;
		} catch (ExcepcaoPersistencia e) {
			throw e;
		}
	}

	private IEnrolmentEquivalence createNewEnrolmentEquivalence(IEnrolment newEnrolment) throws ExistingPersistentException, ExcepcaoPersistencia {
		try {
			IEnrolmentEquivalence enrolmentEquivalence = new EnrolmentEquivalence();
			enrolmentEquivalence.setEnrolment(newEnrolment);
			persistentEnrolmentEquivalence.lockWrite(enrolmentEquivalence);
			return enrolmentEquivalence;
		} catch (ExistingPersistentException e) {
			throw e;
		} catch (ExcepcaoPersistencia e) {
			throw e;
		}
	}

	private IEnrolmentEvaluation createNewEnrolmentEvaluation(
		InfoEquivalenceContext infoEquivalenceContext,
		IEnrolment newEnrolment,
		InfoCurricularCourseScopeGrade infoCurricularCourseScopeGrade)
		throws ExistingPersistentException, ExcepcaoPersistencia {
		try {
			IUserView userView = infoEquivalenceContext.getResponsible();
			IPessoa pessoa = pessoaPersistente.lerPessoaPorUsername(userView.getUtilizador());
			Funcionario funcionario = funcionarioPersistente.lerFuncionarioPorPessoa(pessoa.getIdInternal().intValue());

			IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
			enrolmentEvaluation.setEnrolment(newEnrolment);
			enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
			enrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.EQUIVALENCE_OBJ);
			enrolmentEvaluation.setGrade(infoCurricularCourseScopeGrade.getGrade());
			enrolmentEvaluation.setPersonResponsibleForGrade(pessoa);
			enrolmentEvaluation.setEmployee(funcionario);
			enrolmentEvaluation.setWhen(new Date());
			// TODO DAVID-RICARDO: Quando o algoritmo do checksum estiver feito tem de ser actualizar este campo
			enrolmentEvaluation.setCheckSum(null);
			persistentEnrolmentEvaluation.lockWrite(enrolmentEvaluation);
			return enrolmentEvaluation;
		} catch (ExistingPersistentException e) {
			throw e;
		} catch (ExcepcaoPersistencia e) {
			throw e;
		}
	}

	private IEnrolment createNewEnrolment(InfoEquivalenceContext infoEquivalenceContext, InfoCurricularCourseScopeGrade infoCurricularCourseScopeGrade)
		throws ExistingPersistentException, ExcepcaoPersistencia {
		IEnrolment newEnrolment;
		try {
			InfoCurricularCourseScope infoCurricularCourseScopeToEnrol = infoCurricularCourseScopeGrade.getInfoCurricularCourseScope();
			ICurricularCourseScope curricularCourseScopeToEnrol = Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCurricularCourseScopeToEnrol);

			InfoExecutionPeriod infoExecutionPeriod = infoEquivalenceContext.getCurrentInfoExecutionPeriod();
			IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);

			InfoStudentCurricularPlan infoStudentCurricularPlan = infoEquivalenceContext.getInfoStudentCurricularPlan();
			IStudentCurricularPlan studentCurricularPlan = Cloner.copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);

			newEnrolment = new Enrolment();
			newEnrolment.setCurricularCourseScope(curricularCourseScopeToEnrol);
			newEnrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.EQUIVALENCE_OBJ);
			newEnrolment.setEnrolmentState(EnrolmentState.APROVED);
			newEnrolment.setExecutionPeriod(executionPeriod);
			newEnrolment.setStudentCurricularPlan(studentCurricularPlan);

			persistentEnrolment.lockWrite(newEnrolment);
			return newEnrolment;
		} catch (ExistingPersistentException e) {
			throw e;
		} catch (ExcepcaoPersistencia e) {
			throw e;
		}
	}
}