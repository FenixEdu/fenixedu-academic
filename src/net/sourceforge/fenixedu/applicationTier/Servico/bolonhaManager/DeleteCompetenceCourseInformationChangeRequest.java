package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest;

public class DeleteCompetenceCourseInformationChangeRequest extends FenixService {

    public void run(CompetenceCourseInformationChangeRequest request) {
	request.delete();
    }
}
