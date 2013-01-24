package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.dismissal;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.Credits;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteCredits {

    @Service
    public static void run(StudentCurricularPlan studentCurricularPlan, String[] creditsIDs) throws FenixServiceException {
	for (String creditsID : creditsIDs) {
	    Credits credits = getCreditsByID(studentCurricularPlan, Integer.valueOf(creditsID));
	    if (credits == null) {
		throw new FenixServiceException();
	    }
	    credits.delete();
	}
    }

    private static Credits getCreditsByID(StudentCurricularPlan studentCurricularPlan, Integer creditsID) {
	for (Credits credits : studentCurricularPlan.getCreditsSet()) {
	    if (credits.getIdInternal().equals(creditsID)) {
		return credits;
	    }
	}
	return null;
    }

}
