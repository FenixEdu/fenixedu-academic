package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import middleware.marks.CreateFile;

import DataBeans.ISiteComponent;
import DataBeans.InfoMark;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteSubmitMarks;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.Evaluation;
import Dominio.Frequenta;
import Dominio.Funcionario;
import Dominio.IDisciplinaExecucao;
import Dominio.IEnrolment;
import Dominio.IEvaluation;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.IPessoa;
import Dominio.ISite;
import Dominio.ITeacher;
import Dominio.ResponsibleFor;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;

/**
 * @author Tânia Pousão
 *
 */
public class SubmitMarks implements IServico {
	private static SubmitMarks _service = new SubmitMarks();

	private List enrolmentEvaluationList = new ArrayList();
	private List errorsNotEnrolmented = new ArrayList();
	private List errorsMarkNotPublished = new ArrayList();
	private boolean noMarks = false;
	private boolean allMarksNotPublished = true;

	/**
	 * The actor of this class.
	 **/
	private SubmitMarks() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "SubmitMarks";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourse
	 */
	public static SubmitMarks getService() {
		return _service;
	}

	public Object run(Integer executionCourseCode, Integer evaluationCode, Date evaluationDate, UserView userView)
		throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			//execution course and execution course's site
			IDisciplinaExecucaoPersistente persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();

			IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
			executionCourse.setIdInternal(executionCourseCode);
			executionCourse = (IDisciplinaExecucao) persistentExecutionCourse.readByOId(executionCourse, false);

			IPersistentSite persistentSite = sp.getIPersistentSite();
			ISite site = persistentSite.readByExecutionCourse(executionCourse);

			//evaluation
			IPersistentEvaluation persistentEvaluation = sp.getIPersistentEvaluation();
			IEvaluation evaluation = new Evaluation();
			evaluation.setIdInternal(evaluationCode);
			evaluation = (IEvaluation) persistentEvaluation.readByOId(evaluation, false);

			//final evaluation's marks list
			IPersistentMark persistentMark = sp.getIPersistentMark();
			List marksList = persistentMark.readBy(evaluation);

			List infoMarksList = submitMarks(userView, evaluationDate, executionCourse, evaluation, marksList);

			CreateFile.fileWithMarksList(executionCourse.getAssociatedCurricularCourses(), enrolmentEvaluationList);
			
			return createSiteView(
				site,
				evaluation,
				infoMarksList,
				errorsNotEnrolmented,
				errorsMarkNotPublished,
				allMarksNotPublished,
				noMarks);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FenixServiceException();
		}
	}

	private List submitMarks(
		UserView userView,
		Date evaluationDate,
		IDisciplinaExecucao executionCourse,
		IEvaluation evaluation,
		List marksList)
		throws FenixServiceException {

		ISuportePersistente sp;
		List infoMarksList = null;

		try {
			sp = SuportePersistenteOJB.getInstance();

			if (marksList == null || marksList.size() <= 0) {
				noMarks = true;
			} else {
				IFrequentaPersistente frequentaPersistente = sp.getIFrequentaPersistente();
				IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();

				infoMarksList = new ArrayList();

				Iterator iterador = marksList.listIterator();
				while (iterador.hasNext()) {
					IMark mark = (IMark) iterador.next();

					//attend
					IFrequenta frequenta = new Frequenta();
					frequenta.setIdInternal(mark.getKeyAttend());
					frequenta = (Frequenta) frequentaPersistente.readByOId(frequenta, false);

					//enrolment
					IEnrolment enrolment = new Enrolment();
					enrolment.setIdInternal(frequenta.getKeyEnrolment());
					enrolment = (Enrolment) persistentEnrolment.readByOId(enrolment, false);
					if (enrolment == null) {
						errorsNotEnrolmented.add(Cloner.copyIMark2InfoMark(mark));
						continue;
					}

					if ((mark.getPublishedMark() == null) || (mark.getPublishedMark().length() <= 0)) {
						errorsMarkNotPublished.add(Cloner.copyIMark2InfoMark(mark));
						continue;
					} else {
						allMarksNotPublished = false;
					}

					//data for enrolment evaluation
					setEnrolmentEvaluation(executionCourse, evaluationDate, enrolment, mark.getPublishedMark(), userView);

					InfoMark infoMark = Cloner.copyIMark2InfoMark(mark);
					infoMarksList.add(infoMark);
				}
			}

			return infoMarksList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FenixServiceException();
		}
	}

	private void setEnrolmentEvaluation(
		IDisciplinaExecucao executionCourse,
		Date evaluationDate,
		IEnrolment enrolment,
		String publishedMark,
		UserView userView)
		throws FenixServiceException {

		ISuportePersistente sp;
		EnrolmentEvaluation enrolmentEvaluation = null;

		try {
			sp = SuportePersistenteOJB.getInstance();

			IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
			enrolmentEvaluation =
				(EnrolmentEvaluation) persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolmentEvaluationTypeAndGrade(
					enrolment,
					EnrolmentEvaluationType.NORMAL_OBJ,
					publishedMark);

			if (enrolmentEvaluation == null) {
				throw new FenixServiceException();
			}

			enrolmentEvaluation.setGrade(publishedMark);
			enrolmentEvaluation.setEnrolment(enrolment);
			enrolmentEvaluation.setEnrolmentEvaluationType(enrolment.getEnrolmentEvaluationType());
			enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
			enrolmentEvaluation.setObservation(new String("Submissão da Pauta"));

			//teacher responsible for execution course
			IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
			List professors = persistentResponsibleFor.readByExecutionCourse(executionCourse);
			ITeacher teacher = ((ResponsibleFor) professors.listIterator().next()).getTeacher();
			enrolmentEvaluation.setPersonResponsibleForGrade(teacher.getPerson());

			//employee logged
			IPessoaPersistente pessoaPersistente = sp.getIPessoaPersistente();
			IPessoa pessoa = pessoaPersistente.lerPessoaPorUsername(userView.getUtilizador());
			Funcionario funcionario = lerFuncionario(pessoa);
			enrolmentEvaluation.setEmployee(funcionario);
			//enrolmentEvaluation.setEmployeeKey(new Integer(funcionario.getCodigoInterno()));

			Calendar calendar = Calendar.getInstance();
			enrolmentEvaluation.setWhen(calendar.getTime());
			enrolmentEvaluation.setGradeAvailableDate(calendar.getTime());
			if (evaluationDate != null) {
				enrolmentEvaluation.setExamDate(evaluationDate);
			} else {
				enrolmentEvaluation.setExamDate(calendar.getTime());
			}

			enrolmentEvaluation.setCheckSum("");

			enrolmentEvaluationList.add(enrolmentEvaluation);

			persistentEnrolmentEvaluation.lockWrite(enrolmentEvaluation);
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException();
		}
	}

	private Funcionario lerFuncionario(IPessoa pessoa) {
		Funcionario funcionario = null;
		SuportePersistente spJDBC = SuportePersistente.getInstance();
		IFuncionarioPersistente funcionarioPersistente = spJDBC.iFuncionarioPersistente();

		try {
			spJDBC.iniciarTransaccao();

			try {
				funcionario = funcionarioPersistente.lerFuncionarioPorPessoa(pessoa.getIdInternal().intValue());

			} catch (Exception e) {
				spJDBC.cancelarTransaccao();
				e.printStackTrace();
				return funcionario;
			}

			spJDBC.confirmarTransaccao();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return funcionario;
		}
	}

	private Object createSiteView(
		ISite site,
		IEvaluation evaluation,
		List marksList,
		List errorsNotEnrolmented,
		List errorsMarkNotPublished,
		boolean allMarksNotPublished,
		boolean noMarks)
		throws FenixServiceException {

		InfoSiteSubmitMarks infoSiteSubmitMarks = new InfoSiteSubmitMarks();

		infoSiteSubmitMarks.setInfoEvaluation(Cloner.copyIEvaluation2InfoEvaluation(evaluation));
		infoSiteSubmitMarks.setMarksList(marksList);
		infoSiteSubmitMarks.setErrorsNotEnrolmented(errorsNotEnrolmented);
		infoSiteSubmitMarks.setErrorsMarkNotPublished(errorsMarkNotPublished);
		infoSiteSubmitMarks.setAllMarksNotPublished(allMarksNotPublished);
		infoSiteSubmitMarks.setNoMarks(noMarks);

		TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
		ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null, null, null);

		TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent, infoSiteSubmitMarks);
		return siteView;
	}
}