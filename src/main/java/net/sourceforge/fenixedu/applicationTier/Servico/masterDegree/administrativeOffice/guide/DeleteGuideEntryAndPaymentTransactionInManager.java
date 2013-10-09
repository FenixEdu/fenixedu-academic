package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteGuideEntryAndPaymentTransactionInManager {

    @Atomic
    public static void run(String guideEntryID) throws InvalidChangeServiceException {
        check(RolePredicates.MANAGER_PREDICATE);
        GuideEntry guideEntry = FenixFramework.getDomainObject(guideEntryID);

        if (!guideEntry.canBeDeleted()) {
            throw new InvalidChangeServiceException();
        }

        guideEntry.delete();
    }

}