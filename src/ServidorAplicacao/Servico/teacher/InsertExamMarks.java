package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import DataBeans.ISiteComponent;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoMark;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteMarks;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.Exam;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.ISite;
import Dominio.Mark;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 *
 */
public class InsertExamMarks implements IServico {
	private static InsertExamMarks _servico = new InsertExamMarks();

	/**
		* The actor of this class.
		**/
	private InsertExamMarks() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "InsertExamMarks";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourse
	 */
	public static InsertExamMarks getService() {
		return _servico;
	}

	public Object run(Integer executionCourseCode, Integer examCode, List examMarks)
		throws ExcepcaoInexistente, FenixServiceException {

		ISite site = null;
		IDisciplinaExecucao executionCourse = null;
		IExam exam = null;
		List infoMarksList = null;
		List marksErrors = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente executionCourseDAO = sp.getIDisciplinaExecucaoPersistente();
			IPersistentSite persistentSite = sp.getIPersistentSite();
			IPersistentExam persistentExam = sp.getIPersistentExam();
			IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
			IPersistentMark persistentMark = sp.getIPersistentMark();

			executionCourse = new DisciplinaExecucao(executionCourseCode);
			executionCourse = (IDisciplinaExecucao) executionCourseDAO.readByOId(executionCourse, false);

			site = persistentSite.readByExecutionCourse(executionCourse);

			exam = new Exam(examCode);
			exam = (IExam) persistentExam.readByOId(exam, false);

			List attendList = persistentAttend.readByExecutionCourse(executionCourse);

			infoMarksList = new ArrayList();
			marksErrors = new ArrayList();
			boolean foundStudent = false;
			ListIterator iterMarks = examMarks.listIterator();
			
			while (iterMarks.hasNext()) {
				InfoMark infoMark = (InfoMark) iterMarks.next();

				if (!isValidMark(infoMark)) {
					marksErrors.add(infoMark);
				} else {
					IMark mark = new Mark();
					ListIterator iterAttend = attendList.listIterator();
					while (iterAttend.hasNext()) {
						IFrequenta attend = (IFrequenta) iterAttend.next();
						if (attend.getAluno().getNumber().equals(infoMark.getInfoFrequenta().getAluno().getNumber())) {
							foundStudent = true;

							IMark markToEdit = persistentMark.readBy(exam, attend);
							if(markToEdit == null){
								mark.setAttend(attend);
								mark.setExam(exam);
								mark.setMark(infoMark.getMark());

								persistentMark.lockWrite(mark);								
							} else{
								persistentMark.simpleLockWrite(markToEdit);
								
								markToEdit.setMark(infoMark.getMark());
							}
						}
					}
					if (!foundStudent) {
						marksErrors.add(infoMark);
					} else {
						infoMarksList.add(Cloner.copyIMark2InfoMark(mark));
						foundStudent = false;
					}
				}
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
			FenixServiceException newEx = new FenixServiceException("");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return createSiteView(site, exam, infoMarksList, marksErrors);
	}

	private Object createSiteView(ISite site, IExam exam, List infoMarksList, List marksErrors) throws FenixServiceException {
		InfoSiteMarks infoSiteMarks = new InfoSiteMarks();
		infoSiteMarks.setInfoExam(Cloner.copyIExam2InfoExam(exam));
		infoSiteMarks.setMarksList(infoMarksList);
		infoSiteMarks.setMarksListErrors(marksErrors);

		TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
		ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null, null, null);

		TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent, infoSiteMarks);
		return siteView;
	}

	private boolean isValidMark(InfoMark infoMark) {
		InfoDegreeCurricularPlan infoDegreeCurricularPlan =
			infoMark.getInfoFrequenta().getInfoEnrolment().getInfoStudentCurricularPlan().getInfoDegreeCurricularPlan();
		IDegreeCurricularPlan degreeCurricularPlan =
			Cloner.copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(infoDegreeCurricularPlan);

		// test marks by execution course: strategy 
		IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory.getInstance();
		IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy =
			degreeCurricularPlanStrategyFactory.getDegreeCurricularPlanStrategy(degreeCurricularPlan);

		return degreeCurricularPlanStrategy.checkMark(infoMark.getMark());
	}
}
