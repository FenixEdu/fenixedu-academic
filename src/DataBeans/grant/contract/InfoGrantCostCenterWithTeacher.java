/*
 * Created on Jun 25, 2004
 */
package DataBeans.grant.contract;

import DataBeans.InfoTeacherWithPerson;
import Dominio.grant.contract.GrantCostCenter;
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
    
    public void copyToDomain(InfoGrantCostCenter infoGrantCostCenter, IGrantCostCenter grantCostCenter) 
    {
        super.copyToDomain(infoGrantCostCenter, grantCostCenter);

        grantCostCenter.setResponsibleTeacher(InfoTeacherWithPerson
                .newDomainFromInfo(infoGrantCostCenter.getInfoResponsibleTeacher()));
    }
    
    public static IGrantCostCenter newDomainFromInfo(InfoGrantCostCenter infoGrantCostCenter)
    {
        IGrantCostCenter grantCostCenter = null;
        InfoGrantCostCenterWithTeacher infoGrantCostCenterWithTeacher = null;
        if(infoGrantCostCenter != null)
        {
            grantCostCenter = new GrantCostCenter();
            infoGrantCostCenterWithTeacher = new InfoGrantCostCenterWithTeacher();
            infoGrantCostCenterWithTeacher.copyToDomain(infoGrantCostCenter,grantCostCenter);
        }
        return grantCostCenter;
    }

}
