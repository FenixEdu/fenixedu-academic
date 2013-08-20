package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class AssociateCurricularCoursesToCompetenceCourse {
    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(String competenceCourseID, String[] curricularCoursesIDs) throws NotExistingServiceException {
        CompetenceCourse competenceCourse = AbstractDomainObject.fromExternalId(competenceCourseID);
        if (competenceCourse == null) {
            throw new NotExistingServiceException("error.manager.noCompetenceCourse");
        }

        List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();
        for (String curricularCourseID : curricularCoursesIDs) {
            CurricularCourse curricularCourse = (CurricularCourse) AbstractDomainObject.fromExternalId(curricularCourseID);
            if (curricularCourse != null) {
                curricularCourses.add(curricularCourse);
            }
        }
        competenceCourse.addCurricularCourses(curricularCourses);
    }
}