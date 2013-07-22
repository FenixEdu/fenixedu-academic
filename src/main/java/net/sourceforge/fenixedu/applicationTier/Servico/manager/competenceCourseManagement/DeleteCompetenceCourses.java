package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteCompetenceCourses {
    @Atomic
    public static void run(String[] competenceCourseIDs) {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        for (String competenceCourseID : competenceCourseIDs) {
            CompetenceCourse competenceCourse = FenixFramework.getDomainObject(competenceCourseID);
            if (competenceCourse != null) {
                competenceCourse.delete();
            }
        }
    }
}