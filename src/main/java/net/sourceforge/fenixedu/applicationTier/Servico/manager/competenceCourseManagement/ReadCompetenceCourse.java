package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourseWithCurricularCourses;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadCompetenceCourse {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Atomic
    public static InfoCompetenceCourse run(String competenceCourseID) throws NotExistingServiceException {
        CompetenceCourse competenceCourse = FenixFramework.getDomainObject(competenceCourseID);
        if (competenceCourse == null) {
            throw new NotExistingServiceException("Invalid CompetenceCourse ID");
        }
        return InfoCompetenceCourseWithCurricularCourses.newInfoFromDomain(competenceCourse);
    }

}