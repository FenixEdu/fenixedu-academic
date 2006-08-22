/*
 * Created on Jun 25, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantCostCenterWithTeacher extends InfoGrantCostCenter {

    public void copyFromDomain(GrantCostCenter grantCostCenter) {
        if (grantCostCenter != null) {
            super.copyFromDomain(grantCostCenter);
            if (grantCostCenter.getResponsibleTeacher() != null) {
                setInfoResponsibleTeacher(InfoTeacher.newInfoFromDomain(grantCostCenter
                        .getResponsibleTeacher()));
            }
        }
    }

    public static InfoGrantCostCenter newInfoFromDomain(GrantCostCenter grantCostCenter) {
        InfoGrantCostCenterWithTeacher infoGrantCostCenter = null;
        if (grantCostCenter != null) {
            infoGrantCostCenter = new InfoGrantCostCenterWithTeacher();
            infoGrantCostCenter.copyFromDomain(grantCostCenter);
        }
        return infoGrantCostCenter;
    }

}
