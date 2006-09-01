/*
 * Created on 5/Fev/2005
 */
package net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness;

import net.sourceforge.fenixedu.dataTransferObject.InfoCostCenter;
import net.sourceforge.fenixedu.dataTransferObject.InfoEmployee;
import net.sourceforge.fenixedu.domain.managementAssiduousness.ExtraWorkRequests;

/**
 * @author Tânia Pousão
 *
 */
public class InfoExtraWorkRequestsWithAll extends InfoExtraWorkRequests {

    public void copyFromDomain(ExtraWorkRequests extraWorkRequests) {
        super.copyFromDomain(extraWorkRequests);
        if(extraWorkRequests != null) {
            setInfoEmployee(InfoEmployee.newInfoFromDomain(extraWorkRequests.getEmployee()));
            setInfoCostCenterExtraWork(InfoCostCenter.newInfoFromDomain(extraWorkRequests.getCostCenterExtraWork()));
            setInfoCostCenterMoney(InfoCostCenter.newInfoFromDomain(extraWorkRequests.getCostCenterMoney()));            
        }
    }
    
    public static InfoExtraWorkRequests newInfoFromDomain(ExtraWorkRequests extraWorkRequests) {
        InfoExtraWorkRequestsWithAll infoExtraWorkRequestsWithAll = null;
        if (extraWorkRequests != null) {
            infoExtraWorkRequestsWithAll = new InfoExtraWorkRequestsWithAll();
            infoExtraWorkRequestsWithAll.copyFromDomain(extraWorkRequests);
        }
        return infoExtraWorkRequestsWithAll;
    }
}
