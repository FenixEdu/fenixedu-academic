/*
 * Created on Jun 18, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity extends InfoGrantPart {
    public void copyFromDomain(GrantPart grantPart) {
        super.copyFromDomain(grantPart);
        if (grantPart != null) {
            setInfoGrantPaymentEntity(InfoGrantPaymentEntity.newInfoFromDomain(grantPart
                    .getGrantPaymentEntity()));
            setInfoResponsibleTeacher(InfoTeacher.newInfoFromDomain(grantPart
                    .getResponsibleTeacher()));
            setInfoGrantSubsidy(InfoGrantSubsidyWithContract.newInfoFromDomain(grantPart
                    .getGrantSubsidy()));
        }
    }

    public static InfoGrantPart newInfoFromDomain(GrantPart grantPart) {
        InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity infoGrantPartWithSubsidyAndTeacherAndPaymentEntity = null;
        if (grantPart != null) {
            infoGrantPartWithSubsidyAndTeacherAndPaymentEntity = new InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity();
            infoGrantPartWithSubsidyAndTeacherAndPaymentEntity.copyFromDomain(grantPart);
        }
        return infoGrantPartWithSubsidyAndTeacherAndPaymentEntity;
    }

}
