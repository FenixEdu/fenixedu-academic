package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseHistoric;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseHistoricWithInfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseHistoric;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.gesdis.CourseHistoric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadCourseHistoric extends Service {

	public List run(Integer executionCourseId) throws FenixServiceException, ExcepcaoPersistencia {
		ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
		Integer semester = executionCourse.getExecutionPeriod().getSemester();
		List<CurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCourses();
		return getInfoSiteCoursesHistoric(executionCourse, curricularCourses, semester);
	}

	private List<InfoSiteCourseHistoric> getInfoSiteCoursesHistoric(ExecutionCourse executionCourse, List<CurricularCourse> curricularCourses, Integer semester) throws ExcepcaoPersistencia {
		List<InfoSiteCourseHistoric> result = new ArrayList<InfoSiteCourseHistoric>();
		
        for (CurricularCourse curricularCourse : curricularCourses) {
            result.add(
                    getInfoSiteCourseHistoric(
                            executionCourse.getExecutionPeriod().getExecutionYear(), 
                            curricularCourse, 
                            semester));
		}
		
        return result;
	}

	private InfoSiteCourseHistoric getInfoSiteCourseHistoric(final ExecutionYear executionYear, CurricularCourse curricularCourse, Integer semester) throws ExcepcaoPersistencia {
		InfoSiteCourseHistoric infoSiteCourseHistoric = new InfoSiteCourseHistoric();

		InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);
		infoSiteCourseHistoric.setInfoCurricularCourse(infoCurricularCourse);

		final List<CourseHistoric> courseHistorics = curricularCourse.getAssociatedCourseHistorics();

		// the historic must only show info regarding the years previous to the
		// year chosen by the user
		List<InfoCourseHistoric> infoCourseHistorics = new ArrayList<InfoCourseHistoric>();
		for (CourseHistoric courseHistoric : courseHistorics) {
			ExecutionYear courseHistoricExecutionYear = ExecutionYear.readExecutionYearByName(courseHistoric.getCurricularYear());
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
