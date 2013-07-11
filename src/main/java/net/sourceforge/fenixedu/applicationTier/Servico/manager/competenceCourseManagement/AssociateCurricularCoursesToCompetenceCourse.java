package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class AssociateCurricularCoursesToCompetenceCourse {
    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Atomic
    public static void run(String competenceCourseID, String[] curricularCoursesIDs) throws NotExistingServiceException {
        CompetenceCourse competenceCourse = FenixFramework.getDomainObject(competenceCourseID);
        if (competenceCourse == null) {
            throw new NotExistingServiceException("error.manager.noCompetenceCourse");
        }

        List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();
        for (String curricularCourseID : curricularCoursesIDs) {
            CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseID);
            if (curricularCourse != null) {
                curricularCourses.add(curricularCourse);
            }
        }
        competenceCourse.addCurricularCourses(curricularCourses);
    }
}