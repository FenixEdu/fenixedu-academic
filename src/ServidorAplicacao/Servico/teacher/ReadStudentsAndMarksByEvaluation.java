package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoEnrolment;
import DataBeans.InfoEvaluation;
import DataBeans.InfoMark;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteMarks;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.Evaluation;
import Dominio.Frequenta;
import Dominio.IExecutionCourse;
import Dominio.IEvaluation;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.ISite;
import Dominio.Mark;
import Dominio.Site;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *
 */
public class ReadStudentsAndMarksByEvaluation implements IServico {
	private static ReadStudentsAndMarksByEvaluation _servico =
		new ReadStudentsAndMarksByEvaluation();

	/**
		* The actor of this class.
		**/
	private ReadStudentsAndMarksByEvaluation() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "ReadStudentsAndMarksByEvaluation";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourse
	 */
	public static ReadStudentsAndMarksByEvaluation getService() {
		return _servico;
	}

	public Object run(Integer executionCourseCode, Integer evaluationCode)
		throws ExcepcaoInexistente, FenixServiceException {
		
		List attendList = new ArrayList();
		List infoMarksList = new ArrayList();

		ISite site = new Site();
		IExecutionCourse executionCourse = new ExecutionCourse();
		IEvaluation evaluation = new Evaluation();
		InfoEvaluation infoEvaluation = new InfoEvaluation();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
	
			//Execution Course
			executionCourse = new ExecutionCourse();
			executionCourse.setIdInternal(executionCourseCode);

			IPersistentExecutionCourse disciplinaExecucaoDAO = sp.getIDisciplinaExecucaoPersistente();
			executionCourse = (IExecutionCourse) disciplinaExecucaoDAO.readByOId(executionCourse, false);

			//Site
			IPersistentSite siteDAO = sp.getIPersistentSite();
			site = siteDAO.readByExecutionCourse(executionCourse);

			//Evaluation
			evaluation = new Evaluation();
			evaluation.setIdInternal(evaluationCode);
			IPersistentEvaluation evaluationDAO = sp.getIPersistentEvaluation();
			evaluation = (IEvaluation) evaluationDAO.readByOId(evaluation, false);
			infoEvaluation = Cloner.copyIEvaluation2InfoEvaluation(evaluation);
			
			//Attends
			IFrequentaPersistente frequentaPersistente = sp.getIFrequentaPersistente();
			attendList = frequentaPersistente.readByExecutionCourse(executionCourse);
			
			if (attendList != null) {
				Iterator attendIterador = attendList.listIterator();
				IFrequenta frequenta = new Frequenta();
				
				IPersistentMark persistentMark = sp.getIPersistentMark();

				IMark mark = new Mark();
				InfoMark infoMark = new InfoMark();
				infoMarksList = new ArrayList();
				
				while (attendIterador.hasNext()) {
					frequenta = (IFrequenta) attendIterador.next();
					
					//mark
					mark = persistentMark.readBy(evaluation, frequenta);
					
					if (mark == null) {
						//student without mark
						mark = new Mark();
						mark.setAttend(frequenta);
						mark.setEvaluation(evaluation);
						mark.setMark(new String(""));
						mark.setPublishedMark(new String(""));
					
					}

					infoMark = Cloner.copyIMark2InfoMark(mark);
					InfoEnrolment infoEnrolment = new InfoEnrolment();
										
					if(mark.getAttend().getEnrolment()!=null){
					
						infoEnrolment = Cloner.copyIEnrolment2InfoEnrolment(mark.getAttend().getEnrolment());		 
						infoEnrolment.setEvaluationType(mark.getAttend().getEnrolment().getEnrolmentEvaluationType());
						infoMark.getInfoFrequenta().setInfoEnrolment(infoEnrolment);					
					}

					infoMarksList.add(infoMark);
					
				}
			}
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException("error.impossibleReadMarksList");
		}

		InfoSiteMarks infoSiteMarks = new InfoSiteMarks();
		infoSiteMarks.setMarksList(infoMarksList);
		infoSiteMarks.setInfoEvaluation(infoEvaluation);

		TeacherAdministrationSiteComponentBuilder componentBuilder =
			new TeacherAdministrationSiteComponentBuilder();
		ISiteComponent commonComponent =
			componentBuilder.getComponent(
				new InfoSiteCommon(),
				site,
				null,
				null,
				null);

		TeacherAdministrationSiteView siteView =
			new TeacherAdministrationSiteView(commonComponent, infoSiteMarks);

		return siteView;
	}
}