package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class RemoveCurricularCoursesFromCompetenceCourse {
    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer competenceCourseID, Integer[] curricularCoursesIDs) throws NotExistingServiceException {
        CompetenceCourse competenceCourse = RootDomainObject.getInstance().readCompetenceCourseByOID(competenceCourseID);
        if (competenceCourse == null) {
            throw new NotExistingServiceException("error.manager.noCompetenceCourse");
        }

        for (Integer curricularCourseID : curricularCoursesIDs) {
            CurricularCourse curricularCourse = (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(curricularCourseID);
            if (curricularCourse != null) {
                competenceCourse.getAssociatedCurricularCourses().remove(curricularCourse);
            }
        }
    }
}