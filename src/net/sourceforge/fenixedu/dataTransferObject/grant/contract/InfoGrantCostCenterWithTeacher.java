/*
 * Created on Jun 25, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPerson;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantCostCenterWithTeacher extends InfoGrantCostCenter {

    public void copyFromDomain(IGrantCostCenter grantCostCenter) {
        if (grantCostCenter != null) {
            super.copyFromDomain(grantCostCenter);
            if (grantCostCenter.getResponsibleTeacher() != null) {
                setInfoResponsibleTeacher(InfoTeacherWithPerson.newInfoFromDomain(grantCostCenter
                        .getResponsibleTeacher()));
            }
        }
    }

    public static InfoGrantCostCenter newInfoFromDomain(IGrantCostCenter grantCostCenter) {
        InfoGrantCostCenterWithTeacher infoGrantCostCenter = null;
        if (grantCostCenter != null) {
            infoGrantCostCenter = new InfoGrantCostCenterWithTeacher();
            infoGrantCostCenter.copyFromDomain(grantCostCenter);
        }
        return infoGrantCostCenter;
    }

    public void copyToDomain(InfoGrantCostCenter infoGrantCostCenter, IGrantCostCenter grantCostCenter) throws ExcepcaoPersistencia {
        super.copyToDomain(infoGrantCostCenter, grantCostCenter);

        grantCostCenter.setResponsibleTeacher(InfoTeacherWithPerson
                .newDomainFromInfo(infoGrantCostCenter.getInfoResponsibleTeacher()));
    }

}
