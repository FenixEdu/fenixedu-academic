package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteGuideEntryAndPaymentTransactionInManager extends Service {

    public void run(Integer guideEntryID) throws InvalidChangeServiceException {
	GuideEntry guideEntry = rootDomainObject.readGuideEntryByOID(guideEntryID);

	if (!guideEntry.canBeDeleted()) {
	    throw new InvalidChangeServiceException();
	}

	guideEntry.delete();
    }

}
