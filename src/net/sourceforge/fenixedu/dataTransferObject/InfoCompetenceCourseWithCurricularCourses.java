package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;

public class InfoCompetenceCourseWithCurricularCourses extends
		InfoCompetenceCourse {
	
    public void copyFromDomain(ICompetenceCourse competenceCourse) {
        super.copyFromDomain(competenceCourse);
        if (competenceCourse != null) {
        	List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();
        	for (ICurricularCourse course : competenceCourse.getAssociatedCurricularCourses()) {
				infoCurricularCourses.add(InfoCurricularCourseWithInfoDegreeCurricularPlan.newInfoFromDomain(course));
			}
            setAssociatedCurricularCourses(infoCurricularCourses);
        }
    }

    public static InfoCompetenceCourse newInfoFromDomain(ICompetenceCourse competenceCourse) {
        InfoCompetenceCourseWithCurricularCourses infoCompetenceCourse = null;
        if (competenceCourse != null) {
        	infoCompetenceCourse = new InfoCompetenceCourseWithCurricularCourses();
        	infoCompetenceCourse.copyFromDomain(competenceCourse);
        }

        return infoCompetenceCourse;
    }

}
