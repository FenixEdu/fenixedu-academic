package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import DataBeans.ISiteComponent;
import DataBeans.InfoEvaluation;
import DataBeans.InfoFrequenta;
import DataBeans.InfoMark;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteMarks;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.Evaluation;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IEvaluation;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.ISite;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.Mark;
import Dominio.Student;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 *
 */
public class InsertEvaluationMarks implements IServico {
	private static InsertEvaluationMarks _servico = new InsertEvaluationMarks();

	/**
		* The actor of this class.
		**/
	private InsertEvaluationMarks() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "InsertEvaluationMarks";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourse
	 */
	public static InsertEvaluationMarks getService() {
		return _servico;
	}

	public Object run(Integer executionCourseCode, Integer evaluationCode, List evaluationsMarks)
		throws ExcepcaoInexistente, FenixServiceException {

		ISite site = null;
		IExecutionCourse executionCourse = null;
		IEvaluation evaluation = null;
		List infoMarksList = null;
		List marksErrorsInvalidMark = null;
		List marksErrorsStudentExistence = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse executionCourseDAO = sp.getIDisciplinaExecucaoPersistente();
			IPersistentSite persistentSite = sp.getIPersistentSite();
			IPersistentEvaluation persistentEvaluation = sp.getIPersistentEvaluation();
			IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
			IPersistentMark persistentMark = sp.getIPersistentMark();

			//Execution Course
			executionCourse = new ExecutionCourse(executionCourseCode);
			executionCourse = (IExecutionCourse) executionCourseDAO.readByOId(executionCourse, false);

			//Site
			site = persistentSite.readByExecutionCourse(executionCourse);

			//Evaluation
			evaluation = new Evaluation();
			evaluation.setIdInternal(evaluationCode);
			evaluation = (IEvaluation) persistentEvaluation.readByOId(evaluation, false);

			//Attend List
			List attendList = persistentAttend.readByExecutionCourse(executionCourse);

			infoMarksList = new ArrayList();
			marksErrorsInvalidMark = new ArrayList();
			marksErrorsStudentExistence = new ArrayList();

			ListIterator iterMarks = evaluationsMarks.listIterator();
			while (iterMarks.hasNext()) {
				InfoMark infoMark = (InfoMark) iterMarks.next();

				//verify if the student exists
				infoMark = verifyStudentExistance(infoMark, attendList);
				if (infoMark.getInfoFrequenta().getAluno().getIdInternal() == null) {
					marksErrorsStudentExistence.add(infoMark);
				} else {

					infoMark = completeMark(infoMark, evaluation, executionCourse);

					if (!isValidMark(infoMark)) {
						marksErrorsInvalidMark.add(infoMark);
					} else {
						IMark mark = null;
						ListIterator iterAttend = attendList.listIterator();
						while (iterAttend.hasNext()) {
							IFrequenta attend = (IFrequenta) iterAttend.next();
							if (attend.getAluno().getNumber().equals(infoMark.getInfoFrequenta().getAluno().getNumber())) {

								mark = persistentMark.readBy(evaluation, attend);
								if (mark == null) {
									mark = new Mark();

									persistentMark.simpleLockWrite(mark);

									mark.setAttend(attend);
									mark.setEvaluation(evaluation);
									mark.setMark(infoMark.getMark());
									mark.setPublishedMark(infoMark.getPublishedMark());
	
								} else {
									persistentMark.simpleLockWrite(mark);
									mark.setMark(infoMark.getMark());

								}
								infoMark = Cloner.copyIMark2InfoMark(mark);
							}
						}
					}
				}
				infoMarksList.add(infoMark);
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
			FenixServiceException newEx = new FenixServiceException("");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return createSiteView(site, evaluation, infoMarksList, marksErrorsInvalidMark, marksErrorsStudentExistence);
	}

	/**
	 * @param infoMark
	 * @return
	 */
	private InfoMark verifyStudentExistance(InfoMark infoMark, List attends) {
		boolean result = false;

		Iterator iter = attends.iterator();
		while (iter.hasNext() && !result) {
			IFrequenta attend = (IFrequenta) iter.next();
			if (infoMark.getInfoFrequenta().getAluno().getNumber() != null
				&& attend.getAluno().getNumber().equals(infoMark.getInfoFrequenta().getAluno().getNumber())) {
				result = true;
				infoMark.getInfoFrequenta().setAluno(Cloner.copyIStudent2InfoStudent(attend.getAluno()));
			}
		}
		return infoMark;
	}

	private InfoMark completeMark(InfoMark infoMark, IEvaluation evaluation, IExecutionCourse disciplinaExecucao)
		throws FenixServiceException {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			//Evaluation
			InfoEvaluation infoEvaluation = Cloner.copyIEvaluation2InfoEvaluation(evaluation);

			//Student
			IPersistentStudent persistentStudent = sp.getIPersistentStudent();
			IStudent student = new Student();

			student.setIdInternal(infoMark.getInfoFrequenta().getAluno().getIdInternal());
			student = (IStudent) persistentStudent.readByOId(student, false);
			//Attend			
			IFrequentaPersistente frequentaPersistente = sp.getIFrequentaPersistente();
			IFrequenta frequenta = frequentaPersistente.readByAlunoAndDisciplinaExecucao(student, disciplinaExecucao);
			InfoFrequenta infoFrequenta = Cloner.copyIFrequenta2InfoFrequenta(frequenta);

			infoMark.setInfoFrequenta(infoFrequenta);
			infoMark.setInfoEvaluation(infoEvaluation);
			infoMark.setMark(infoMark.getMark().toUpperCase());
			return infoMark;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

	private Object createSiteView(
		ISite site,
		IEvaluation evaluation,
		List infoMarksList,
		List marksErrorsInvalidMark,
		List marksErrorsStudentExistence)
		throws FenixServiceException {
		InfoSiteMarks infoSiteMarks = new InfoSiteMarks();
		infoSiteMarks.setInfoEvaluation(Cloner.copyIEvaluation2InfoEvaluation(evaluation));
		infoSiteMarks.setMarksList(infoMarksList);
		infoSiteMarks.setMarksListErrors(marksErrorsInvalidMark);
		infoSiteMarks.setStudentsListErrors(marksErrorsStudentExistence);

		TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
		ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null, null, null);

		TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent, infoSiteMarks);
		return siteView;
	}

	private boolean isValidMark(InfoMark infoMark) {
		IStudentCurricularPlan studentCurricularPlan = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IStudentCurricularPlanPersistente curricularPlanPersistente = sp.getIStudentCurricularPlanPersistente();

			studentCurricularPlan =
				curricularPlanPersistente.readActiveStudentCurricularPlan(
					infoMark.getInfoFrequenta().getAluno().getNumber(),
					infoMark.getInfoFrequenta().getAluno().getDegreeType());
		} catch (Exception e) {
			e.printStackTrace();
		}

		IDegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();

		// test marks by execution course: strategy 
		IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory.getInstance();
		IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy =
			degreeCurricularPlanStrategyFactory.getDegreeCurricularPlanStrategy(degreeCurricularPlan);
		if (infoMark.getMark() == null || infoMark.getMark().length() == 0) {
			return true;
		} else {
			return degreeCurricularPlanStrategy.checkMark(infoMark.getMark());
		}
	}
}
