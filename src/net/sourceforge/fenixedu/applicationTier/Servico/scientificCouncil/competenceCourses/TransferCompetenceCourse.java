package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.competenceCourses;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;

public class TransferCompetenceCourse extends Service {

    public void run(CompetenceCourse competenceCourse, CompetenceCourseGroupUnit competenceCourseGroupUnit) throws FenixServiceException {

        if (competenceCourse == null || competenceCourseGroupUnit == null) {
            throw new InvalidArgumentsServiceException();
        }
        
        competenceCourse.transfer(competenceCourseGroupUnit);
    }

}
