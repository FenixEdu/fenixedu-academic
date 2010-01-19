package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.competenceCourses;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class TransferCompetenceCourse extends FenixService {

    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Service
    public static void run(CompetenceCourse competenceCourse, CompetenceCourseGroupUnit competenceCourseGroupUnit)
	    throws FenixServiceException {

	if (competenceCourse == null || competenceCourseGroupUnit == null) {
	    throw new InvalidArgumentsServiceException();
	}

	competenceCourse.transfer(competenceCourseGroupUnit);
    }

}