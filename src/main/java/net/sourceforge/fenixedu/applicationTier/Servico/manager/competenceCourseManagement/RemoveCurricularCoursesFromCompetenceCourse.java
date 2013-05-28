package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class RemoveCurricularCoursesFromCompetenceCourse {
    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer competenceCourseID, Integer[] curricularCoursesIDs) throws NotExistingServiceException {
        CompetenceCourse competenceCourse = AbstractDomainObject.fromExternalId(competenceCourseID);
        if (competenceCourse == null) {
            throw new NotExistingServiceException("error.manager.noCompetenceCourse");
        }

        for (Integer curricularCourseID : curricularCoursesIDs) {
            CurricularCourse curricularCourse = (CurricularCourse) AbstractDomainObject.fromExternalId(curricularCourseID);
            if (curricularCourse != null) {
                competenceCourse.getAssociatedCurricularCourses().remove(curricularCourse);
            }
        }
    }
}