package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.domain.assiduousness.UnitExtraWorkAmount;

public class DeleteExtraWorkRequest extends FenixService {

    public void run(Integer extraWorkRequestID) {
	ExtraWorkRequest extraWorkRequest = (ExtraWorkRequest) RootDomainObject.readDomainObjectByOID(ExtraWorkRequest.class,
		extraWorkRequestID);
	if (extraWorkRequest.getApproved()) {
	    UnitExtraWorkAmount unitExtraWorkAmount = extraWorkRequest.getUnit().getUnitExtraWorkAmountByYear(
		    extraWorkRequest.getPaymentYear());
	    unitExtraWorkAmount.subtractSpent(extraWorkRequest.getAmount());
	}
	extraWorkRequest.delete();
    }

}
