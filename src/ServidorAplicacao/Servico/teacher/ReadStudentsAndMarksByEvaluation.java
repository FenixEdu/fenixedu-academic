package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoEvaluation;
import DataBeans.InfoMark;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteMarks;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.Evaluation;
import Dominio.IDisciplinaExecucao;
import Dominio.IEvaluation;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.ISite;
import Dominio.Mark;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
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
	private static ReadStudentsAndMarksByEvaluation _servico = new ReadStudentsAndMarksByEvaluation();

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

	public Object run(Integer executionCourseCode, Integer evaluationCode) throws ExcepcaoInexistente, FenixServiceException {
		List attendList = null;
		List infoMarksList = null;

		ISite site = null;
		IDisciplinaExecucao executionCourse = null;
		IEvaluation evaluation = null;
		InfoEvaluation infoEvaluation = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			//Execution Course
			executionCourse = new DisciplinaExecucao();
			executionCourse.setIdInternal(executionCourseCode);

			IDisciplinaExecucaoPersistente disciplinaExecucaoDAO = sp.getIDisciplinaExecucaoPersistente();
			executionCourse = (IDisciplinaExecucao) disciplinaExecucaoDAO.readByOId(executionCourse, false);

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
				IFrequenta frequenta = null;

				IPersistentMark persistentMark = sp.getIPersistentMark();
				IMark mark = null;
				InfoMark infoMark = null;
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

		TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
		ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null, null, null);

		TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent, infoSiteMarks);

		return siteView;
	}
}