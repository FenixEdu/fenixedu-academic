/*
 * Created on Jun 25, 2004
 */
package DataBeans.grant.contract;

import DataBeans.InfoTeacherWithPerson;
import Dominio.grant.contract.IGrantCostCenter;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantCostCenterWithTeacher extends InfoGrantCostCenter {

    public void copyFromDomain(IGrantCostCenter grantCostCenter) {
        if (grantCostCenter != null) {
            super.copyFromDomain(grantCostCenter);
            if (grantCostCenter.getResponsibleTeacher() != null) {
                setInfoResponsibleTeacher(InfoTeacherWithPerson
                        .newInfoFromDomain(grantCostCenter
                                .getResponsibleTeacher()));
            }
        }
    }

    public static InfoGrantCostCenter newInfoFromDomain(
            IGrantCostCenter grantCostCenter) {
        InfoGrantCostCenterWithTeacher infoGrantCostCenter = null;
        if (grantCostCenter != null) {
            infoGrantCostCenter = new InfoGrantCostCenterWithTeacher();
            infoGrantCostCenter.copyFromDomain(grantCostCenter);
        }
        return infoGrantCostCenter;
    }

}
