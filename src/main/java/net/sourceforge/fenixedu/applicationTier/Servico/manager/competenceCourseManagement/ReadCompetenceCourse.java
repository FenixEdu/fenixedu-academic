package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourseWithCurricularCourses;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadCompetenceCourse {

    @Atomic
    public static InfoCompetenceCourse run(String competenceCourseID) throws NotExistingServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        CompetenceCourse competenceCourse = FenixFramework.getDomainObject(competenceCourseID);
        if (competenceCourse == null) {
            throw new NotExistingServiceException("Invalid CompetenceCourse ID");
        }
        return InfoCompetenceCourseWithCurricularCourses.newInfoFromDomain(competenceCourse);
    }

}