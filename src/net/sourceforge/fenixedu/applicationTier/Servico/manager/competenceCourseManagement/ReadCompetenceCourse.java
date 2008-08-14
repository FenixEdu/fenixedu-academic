package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourseWithCurricularCourses;
import net.sourceforge.fenixedu.domain.CompetenceCourse;

public class ReadCompetenceCourse extends Service {

    public InfoCompetenceCourse run(Integer competenceCourseID) throws Exception {
	CompetenceCourse competenceCourse = rootDomainObject.readCompetenceCourseByOID(competenceCourseID);
	if (competenceCourse == null) {
	    throw new NotExistingServiceException("Invalid CompetenceCourse ID");
	}
	return InfoCompetenceCourseWithCurricularCourses.newInfoFromDomain(competenceCourse);
    }

}
