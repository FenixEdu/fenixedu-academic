/*
 * Created on 12/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.gesdis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.SiteView;
import DataBeans.gesdis.InfoSiteCourseReport;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import Dominio.IDisciplinaExecucao;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Dominio.ResponsibleFor;
import Dominio.gesdis.ICourseReport;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.gesdis.IPersistentCourseReport;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCourseInformation implements IServico {

	private static ReadCourseInformation service = new ReadCourseInformation();

	/**
	 * The singleton access method of this class.
	 */
	public static ReadCourseInformation getService() {
		return service;
	}

	/**
	 *  
	 */
	private ReadCourseInformation() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public final String getNome() {
		return "ReadCourseInformation";
	}

	/**
	 * Executes the service.
	 */
	public SiteView run(Integer executionCourseId)
		throws FenixServiceException {

		try {

			SiteView siteView = new SiteView();

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();

			IDisciplinaExecucao executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
					new DisciplinaExecucao(executionCourseId),
					false);

			InfoSiteCourseReport infoSiteCourseReport =
				new InfoSiteCourseReport();

			infoSiteCourseReport.setInfoExecutionCourse(
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					executionCourse));

			IPersistentResponsibleFor persistentResponsibleFor =
				sp.getIPersistentResponsibleFor();
			List responsiblesFor =
				persistentResponsibleFor.readByExecutionCourse(executionCourse);
			List infoResponsibleTeachers =
				getInfoResponsibleTeachers(responsiblesFor);
			infoSiteCourseReport.setInfoResponsibleTeachers(
				infoResponsibleTeachers);

			List curricularCourses =
				executionCourse.getAssociatedCurricularCourses();
			List infoCurricularCourses =
				getInfoCurricularCourses(curricularCourses);
			infoSiteCourseReport.setInfoCurricularCourses(
				infoCurricularCourses);

			IPersistentCurriculum persistentCurriculum =
				sp.getIPersistentCurriculum();
			List infoCurriculums = getInfoCurriculums(curricularCourses, persistentCurriculum);
			infoSiteCourseReport.setInfoCurriculums(infoCurriculums);

			IPersistentProfessorship persistentProfessorship =
				sp.getIPersistentProfessorship();
			List professorShips =
				persistentProfessorship.readByExecutionCourse(executionCourse);
			List infoLecturingTeachers = getInfoLecturingTeachers(professorShips);
			infoSiteCourseReport.setInfoLecturingTeacher(infoLecturingTeachers);

			// TODO: faltam as bibliografic references

			IPersistentCourseReport persistentCourseReport =
				sp.getIPersistentCourseReport();
			ICourseReport courseReport =
				persistentCourseReport.readCourseReportByExecutionCourse(
					executionCourse);
			infoSiteCourseReport.setInfoCourseReport(
				Cloner.copyICourseReport2InfoCourseReport(courseReport));

			siteView.setComponent(infoSiteCourseReport);

			return siteView;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

	private List getInfoLecturingTeachers(List professorShips) {

		List infoLecturingTeachers = new ArrayList();
		Iterator iter = professorShips.iterator();
		while (iter.hasNext()) {
			IProfessorship professorship = (IProfessorship) iter.next();
			ITeacher teacher = professorship.getTeacher();
			infoLecturingTeachers.add(
				Cloner.copyITeacher2InfoTeacher(teacher));
		}
		return infoLecturingTeachers;
	}

	private List getInfoCurriculums(List curricularCourses, IPersistentCurriculum persistentCurriculum) throws ExcepcaoPersistencia {

		List infoCurriculums = new ArrayList();
		Iterator iter = curricularCourses.iterator();
		while (iter.hasNext()) {
			ICurricularCourse curricularCourse =
				(ICurricularCourse) iter.next();
			ICurriculum curriculum =
				persistentCurriculum.readCurriculumByCurricularCourse(
					curricularCourse);
			infoCurriculums.add(
				Cloner.copyICurriculum2InfoCurriculum(curriculum));
		}
		return infoCurriculums;
	}

	private List getInfoResponsibleTeachers(List responsiblesFor) {

		List infoResponsibleTeachers = new ArrayList();
		Iterator iter = responsiblesFor.iterator();
		while (iter.hasNext()) {
			ResponsibleFor responsibleFor = (ResponsibleFor) iter.next();
			ITeacher teacher = responsibleFor.getTeacher();
			infoResponsibleTeachers.add(
				Cloner.copyITeacher2InfoTeacher(teacher));
		}
		return infoResponsibleTeachers;
	}

	private List getInfoCurricularCourses(List curricularCourses) {

		Iterator iter;
		List infoCurricularCourses = new ArrayList();
		iter = curricularCourses.iterator();
		while (iter.hasNext()) {
			ICurricularCourse curricularCourse =
				(ICurricularCourse) iter.next();
			infoCurricularCourses.add(
				Cloner.copyCurricularCourse2InfoCurricularCourse(
					curricularCourse));
		}
		return infoCurricularCourses;
	}
}
