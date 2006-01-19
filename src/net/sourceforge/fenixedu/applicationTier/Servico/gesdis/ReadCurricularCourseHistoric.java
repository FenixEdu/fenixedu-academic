/*
 * Created on Feb 25, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseHistoric;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseHistoricWithInfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseHistoric;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.gesdis.CourseHistoric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 * 
 */
public class ReadCurricularCourseHistoric implements IService {
	/**
	 * 
	 */
	public ReadCurricularCourseHistoric() {
		super();
	}

	public InfoSiteCourseHistoric run(Integer curricularCourseId) throws FenixServiceException,
			ExcepcaoPersistencia {
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
		IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

		CurricularCourse curricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(
				CurricularCourse.class, curricularCourseId);

		ExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
		Integer semester = executionPeriod.getSemester();
		// TODO: corrigir o calculo do semestre
		semester = new Integer(semester.intValue() == 2 ? 1 : 2);
		return getInfoSiteCourseHistoric(curricularCourse, semester, sp);
	}

	/**
	 * @param curricularCourse
	 * @param sp
	 * @return
	 */
	private InfoSiteCourseHistoric getInfoSiteCourseHistoric(CurricularCourse curricularCourse,
			Integer semester, ISuportePersistente sp) throws ExcepcaoPersistencia {
		InfoSiteCourseHistoric infoSiteCourseHistoric = new InfoSiteCourseHistoric();
		InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
				.newInfoFromDomain(curricularCourse);
		infoSiteCourseHistoric.setInfoCurricularCourse(infoCurricularCourse);

		final List<CourseHistoric> courseHistorics = curricularCourse.getAssociatedCourseHistorics();

		// the historic must only show info regarding the years previous to the
		// year chosen by the user
		List<InfoCourseHistoric> infoCourseHistorics = new ArrayList<InfoCourseHistoric>();
		for (CourseHistoric courseHistoric : courseHistorics) {
			if (courseHistoric.getSemester().equals(semester)) {
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