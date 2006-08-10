package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.specialSeason;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.SpecialSeasonCode;
import net.sourceforge.fenixedu.domain.student.Registration;

public class ChangeSpecialSeasonCode extends Service {
	
	public void run(Registration student, ExecutionYear executionYear, SpecialSeasonCode specialSeasonCode) throws FenixServiceException {
		if(executionYear == null) {
			throw new FenixServiceException("executionYear.invalid.argument");
		}

		student.setSpecialSeasonCode(executionYear, specialSeasonCode);
	}

}
