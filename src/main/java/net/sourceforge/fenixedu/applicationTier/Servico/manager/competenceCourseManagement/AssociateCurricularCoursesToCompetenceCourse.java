package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class AssociateCurricularCoursesToCompetenceCourse {
    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer competenceCourseID, Integer[] curricularCoursesIDs) throws NotExistingServiceException {
        CompetenceCourse competenceCourse = RootDomainObject.getInstance().readCompetenceCourseByOID(competenceCourseID);
        if (competenceCourse == null) {
            throw new NotExistingServiceException("error.manager.noCompetenceCourse");
        }

        List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();
        for (Integer curricularCourseID : curricularCoursesIDs) {
            CurricularCourse curricularCourse = (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(curricularCourseID);
            if (curricularCourse != null) {
                curricularCourses.add(curricularCourse);
            }
        }
        competenceCourse.addCurricularCourses(curricularCourses);
    }
}