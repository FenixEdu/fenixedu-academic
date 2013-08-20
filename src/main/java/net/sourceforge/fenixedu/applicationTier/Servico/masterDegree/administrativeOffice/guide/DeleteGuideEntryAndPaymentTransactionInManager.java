package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.domain.GuideEntry;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DeleteGuideEntryAndPaymentTransactionInManager {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(String guideEntryID) throws InvalidChangeServiceException {
        GuideEntry guideEntry = AbstractDomainObject.fromExternalId(guideEntryID);

        if (!guideEntry.canBeDeleted()) {
            throw new InvalidChangeServiceException();
        }

        guideEntry.delete();
    }

}