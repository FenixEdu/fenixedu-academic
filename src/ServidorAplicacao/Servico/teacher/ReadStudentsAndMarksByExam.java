package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoExam;
import DataBeans.InfoMark;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteMarks;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.Exam;
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
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *
 */
public class ReadStudentsAndMarksByExam implements IServico {
	private static ReadStudentsAndMarksByExam _servico = new ReadStudentsAndMarksByExam();

	/**
		* The actor of this class.
		**/
	private ReadStudentsAndMarksByExam() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "ReadStudentsAndMarksByExam";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourse
	 */
	public static ReadStudentsAndMarksByExam getService() {
		return _servico;
	}

	public Object run(Integer executionCourseCode, Integer examCode) throws ExcepcaoInexistente, FenixServiceException {
		List attendList = null;
		List infoMarksList = null;

		ISite site = null;
		IDisciplinaExecucao executionCourse = null;
		IExam exam = null;
		InfoExam infoExam = null;

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

			//Exam
			exam = new Exam();
			exam.setIdInternal(examCode);

			IPersistentExam examDAO = sp.getIPersistentExam();
			exam = (IExam) examDAO.readByOId(exam, false);
			infoExam = Cloner.copyIExam2InfoExam(exam);

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
					mark = persistentMark.readBy(exam, frequenta);
					if (mark == null) {
						//student without mark
						mark = new Mark();
						mark.setAttend(frequenta);
						mark.setExam(exam);
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
		infoSiteMarks.setInfoExam(infoExam);

		TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
		ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null, null, null);

		TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent, infoSiteMarks);

		return siteView;
	}
}