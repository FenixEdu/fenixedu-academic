package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourseWithCurricularCourses;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadCompetenceCourse {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static InfoCompetenceCourse run(Integer competenceCourseID) throws NotExistingServiceException {
        CompetenceCourse competenceCourse = RootDomainObject.getInstance().readCompetenceCourseByOID(competenceCourseID);
        if (competenceCourse == null) {
            throw new NotExistingServiceException("Invalid CompetenceCourse ID");
        }
        return InfoCompetenceCourseWithCurricularCourses.newInfoFromDomain(competenceCourse);
    }

}