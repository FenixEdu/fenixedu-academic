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
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.gesdis.ICourseHistoric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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

		ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(
				CurricularCourse.class, curricularCourseId);

		IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
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
	private InfoSiteCourseHistoric getInfoSiteCourseHistoric(ICurricularCourse curricularCourse,
			Integer semester, ISuportePersistente sp) throws ExcepcaoPersistencia {
		InfoSiteCourseHistoric infoSiteCourseHistoric = new InfoSiteCourseHistoric();
		InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
				.newInfoFromDomain(curricularCourse);
		infoSiteCourseHistoric.setInfoCurricularCourse(infoCurricularCourse);

		final List<ICourseHistoric> courseHistorics = curricularCourse.getAssociatedCourseHistorics();

		// the historic must only show info regarding the years previous to the
		// year chosen by the user
		List<InfoCourseHistoric> infoCourseHistorics = new ArrayList<InfoCourseHistoric>();
		for (ICourseHistoric courseHistoric : courseHistorics) {
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