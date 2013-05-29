package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DeleteCompetenceCourses {
    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(String[] competenceCourseIDs) {
        for (String competenceCourseID : competenceCourseIDs) {
            CompetenceCourse competenceCourse = AbstractDomainObject.fromExternalId(competenceCourseID);
            if (competenceCourse != null) {
                competenceCourse.delete();
            }
        }
    }
}