package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.domain.GuideEntry;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteGuideEntryAndPaymentTransactionInManager {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Atomic
    public static void run(String guideEntryID) throws InvalidChangeServiceException {
        GuideEntry guideEntry = FenixFramework.getDomainObject(guideEntryID);

        if (!guideEntry.canBeDeleted()) {
            throw new InvalidChangeServiceException();
        }

        guideEntry.delete();
    }

}