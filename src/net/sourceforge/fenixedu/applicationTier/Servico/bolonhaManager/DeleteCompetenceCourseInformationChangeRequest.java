package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest;

public class DeleteCompetenceCourseInformationChangeRequest extends Service {
    public void run(CompetenceCourseInformationChangeRequest request) {
	request.delete();
    }
}
