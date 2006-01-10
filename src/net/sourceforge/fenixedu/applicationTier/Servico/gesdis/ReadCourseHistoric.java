/*
 * Created on Feb 17, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseHistoric;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseHistoricWithInfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseHistoric;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.gesdis.CourseHistoric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 * 
 */
public class ReadCourseHistoric implements IService {

	public List run(Integer executionCourseId) throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
		ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(
				ExecutionCourse.class, executionCourseId);
		Integer semester = executionCourse.getExecutionPeriod().getSemester();
		List curricularCourses = executionCourse.getAssociatedCurricularCourses();
		return getInfoSiteCoursesHistoric(executionCourse, curricularCourses, semester, sp);
	}

	/**
	 * @param curricularCourses
	 * @param sp
	 * @return
	 */
	private List getInfoSiteCoursesHistoric(ExecutionCourse executionCourse, List curricularCourses,
			Integer semester, ISuportePersistente sp) throws ExcepcaoPersistencia {
		List infoSiteCoursesHistoric = new ArrayList();
		Iterator iter = curricularCourses.iterator();
		while (iter.hasNext()) {
			CurricularCourse curricularCourse = (CurricularCourse) iter.next();
			infoSiteCoursesHistoric.add(getInfoSiteCourseHistoric(executionCourse.getExecutionPeriod()
					.getExecutionYear(), curricularCourse, semester, sp));
		}
		return infoSiteCoursesHistoric;
	}

	/**
	 * @param curricularCourse
	 * @param sp
	 * @return
	 */
	private InfoSiteCourseHistoric getInfoSiteCourseHistoric(final ExecutionYear executionYear,
			CurricularCourse curricularCourse, Integer semester, ISuportePersistente sp)
			throws ExcepcaoPersistencia {
		InfoSiteCourseHistoric infoSiteCourseHistoric = new InfoSiteCourseHistoric();

		InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegreeCurricularPlan
				.newInfoFromDomain(curricularCourse);
		infoSiteCourseHistoric.setInfoCurricularCourse(infoCurricularCourse);

		final List<CourseHistoric> courseHistorics = curricularCourse.getAssociatedCourseHistorics();

		// the historic must only show info regarding the years previous to the
		// year chosen by the user
		List<InfoCourseHistoric> infoCourseHistorics = new ArrayList<InfoCourseHistoric>();
		final IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
		for (CourseHistoric courseHistoric : courseHistorics) {
			ExecutionYear courseHistoricExecutionYear = persistentExecutionYear
					.readExecutionYearByName(courseHistoric.getCurricularYear());
			if (courseHistoric.getSemester().equals(semester)
					&& courseHistoricExecutionYear.getBeginDate().before(executionYear.getBeginDate())) {
				infoCourseHistorics.add(InfoCourseHistoricWithInfoCurricularCourse
						.newInfoFromDomain(courseHistoric));
			}
		}

		Collections.sort(infoCourseHistorics, new Comparator() {
			public int compare(Object o1, Object o2) {
				InfoCourseHistoric infoCourseHistoric1 = (InfoCourseHistoric) o1;
				InfoCourseHistoric infoCourseHistoric2 = (InfoCourseHistoric) o2;
				return infoCourseHistoric2.getCurricularYear().compareTo(
						infoCourseHistoric1.getCurricularYear());
			}
		});

		infoSiteCourseHistoric.setInfoCourseHistorics(infoCourseHistorics);
		return infoSiteCourseHistoric;
	}
}