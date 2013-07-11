package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteCompetenceCourses {
    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Atomic
    public static void run(String[] competenceCourseIDs) {
        for (String competenceCourseID : competenceCourseIDs) {
            CompetenceCourse competenceCourse = FenixFramework.getDomainObject(competenceCourseID);
            if (competenceCourse != null) {
                competenceCourse.delete();
            }
        }
    }
}