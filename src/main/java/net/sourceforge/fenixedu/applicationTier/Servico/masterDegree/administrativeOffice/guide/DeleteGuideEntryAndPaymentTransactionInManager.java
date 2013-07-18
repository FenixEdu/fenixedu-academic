package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteGuideEntryAndPaymentTransactionInManager {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer guideEntryID) throws InvalidChangeServiceException {
        GuideEntry guideEntry = RootDomainObject.getInstance().readGuideEntryByOID(guideEntryID);

        if (!guideEntry.canBeDeleted()) {
            throw new InvalidChangeServiceException();
        }

        guideEntry.delete();
    }

}