package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import org.joda.time.DateTimeFieldType;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.domain.assiduousness.UnitExtraWorkAmount;

public class DeleteExtraWorkRequest extends Service {

    public void run(Integer extraWorkRequestID) {
	ExtraWorkRequest extraWorkRequest = (ExtraWorkRequest) RootDomainObject.readDomainObjectByOID(
		ExtraWorkRequest.class, extraWorkRequestID);
	if (extraWorkRequest.getApproved()) {
	    UnitExtraWorkAmount unitExtraWorkAmount = extraWorkRequest.getUnit()
		    .getUnitExtraWorkAmountByYear(
			    extraWorkRequest.getPartialPayingDate().get(DateTimeFieldType.year()));
	    unitExtraWorkAmount.sumSpent(extraWorkRequest.getAmount());
	}
	extraWorkRequest.delete();
    }

}
