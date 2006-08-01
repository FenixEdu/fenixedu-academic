package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.specialSeason;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Enrolment;

public class DeleteSpecialSeasonEvaluations extends Service {
	
	public void run(Collection<Enrolment> enrolments) {

		for (Enrolment enrolment : enrolments) {
			enrolment.deleteSpecialSeasonEvaluation();
		}
	}

}
