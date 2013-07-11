package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.dismissal;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.Credits;
import pt.ist.fenixframework.Atomic;

public class DeleteCredits {

    @Atomic
    public static void run(StudentCurricularPlan studentCurricularPlan, String[] creditsIDs) throws FenixServiceException {
        for (String creditsID : creditsIDs) {
            Credits credits = getCreditsByID(studentCurricularPlan, creditsID);
            if (credits == null) {
                throw new FenixServiceException();
            }
            credits.delete();
        }
    }

    private static Credits getCreditsByID(StudentCurricularPlan studentCurricularPlan, String creditsID) {
        for (Credits credits : studentCurricularPlan.getCreditsSet()) {
            if (credits.getExternalId().equals(creditsID)) {
                return credits;
            }
        }
        return null;
    }

}
