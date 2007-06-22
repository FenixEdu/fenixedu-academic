package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.domain.assiduousness.UnitExtraWorkAmount;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.GiafInterface;

import org.apache.struts.action.ActionMessage;
import org.joda.time.DateTimeFieldType;

public class UpdateExtraWorkClosedMonth extends Service {

    public ActionMessage run(ClosedMonth closedMonth) {
	for (ExtraWorkRequest extraWorkRequest : rootDomainObject.getExtraWorkRequests()) {
	    if (extraWorkRequest.getPartialPayingDate().equals(closedMonth.getClosedYearMonth())
		    && extraWorkRequest.getApproved()) {
		GiafInterface giafInterface = new GiafInterface();
		try {
		    Double oldValue = extraWorkRequest.getAmount();
		    giafInterface.updateExtraWorkRequest(extraWorkRequest);
		    UnitExtraWorkAmount unitExtraWorkAmount = extraWorkRequest.getUnit()
			    .getUnitExtraWorkAmountByYear(
				    extraWorkRequest.getPartialPayingDate()
					    .get(DateTimeFieldType.year()));
		    unitExtraWorkAmount.updateValue(oldValue, extraWorkRequest.getAmount());
		} catch (ExcepcaoPersistencia e) {
		    return new ActionMessage("error.connectionError");
		}
	    }
	}
	return null;
    }
}