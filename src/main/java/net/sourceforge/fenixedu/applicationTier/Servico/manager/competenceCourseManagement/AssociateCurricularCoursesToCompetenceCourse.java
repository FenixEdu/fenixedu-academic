package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class AssociateCurricularCoursesToCompetenceCourse {
    @Atomic
    public static void run(String competenceCourseID, String[] curricularCoursesIDs) throws NotExistingServiceException {
        check(RolePredicates.MANAGER_PREDICATE);
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