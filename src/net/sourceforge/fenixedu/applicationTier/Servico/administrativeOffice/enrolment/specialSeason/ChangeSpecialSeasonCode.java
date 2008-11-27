package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.enrolment.specialSeason;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.SpecialSeasonCode;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ChangeSpecialSeasonCode extends FenixService {

    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(Registration registration, ExecutionYear executionYear, SpecialSeasonCode specialSeasonCode)
	    throws FenixServiceException {
	if (executionYear == null) {
	    throw new FenixServiceException("executionYear.invalid.argument");
	}

	registration.setSpecialSeasonCode(executionYear, specialSeasonCode);
    }

}