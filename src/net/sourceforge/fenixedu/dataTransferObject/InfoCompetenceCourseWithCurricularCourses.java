package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;

public class InfoCompetenceCourseWithCurricularCourses extends
		InfoCompetenceCourse {
	
    public void copyFromDomain(CompetenceCourse competenceCourse) {
        super.copyFromDomain(competenceCourse);
        if (competenceCourse != null) {
        	List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();
        	for (CurricularCourse course : competenceCourse.getAssociatedCurricularCourses()) {
				if (course.getCurricularStage().equals(CurricularStage.OLD)) {
                    infoCurricularCourses.add(InfoCurricularCourseWithInfoDegreeCurricularPlan.newInfoFromDomain(course));                    
                }
			}
            setAssociatedCurricularCourses(infoCurricularCourses);
        }
    }

    public static InfoCompetenceCourse newInfoFromDomain(CompetenceCourse competenceCourse) {
        InfoCompetenceCourseWithCurricularCourses infoCompetenceCourse = null;
        if (competenceCourse != null) {
        	infoCompetenceCourse = new InfoCompetenceCourseWithCurricularCourses();
        	infoCompetenceCourse.copyFromDomain(competenceCourse);
        }

        return infoCompetenceCourse;
    }

}
