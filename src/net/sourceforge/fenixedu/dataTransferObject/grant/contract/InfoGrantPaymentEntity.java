/*
 * Created on Jan 22, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantProject;

/**
 * @author pica
 * @author barbosa
 */
public abstract class InfoGrantPaymentEntity extends InfoObject {
    protected String number;

    protected String designation;

    protected InfoTeacher infoResponsibleTeacher;

    /**
     * @return Returns the designation.
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * @param designation
     *            The designation to set.
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * @return Returns the number.
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number
     *            The number to set.
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return Returns the infoResponsibleTeacher.
     */
    public InfoTeacher getInfoResponsibleTeacher() {
        return infoResponsibleTeacher;
    }

    /**
     * @param infoResponsibleTeacher
     *            The infoResponsibleTeacher to set.
     */
    public void setInfoResponsibleTeacher(InfoTeacher infoResponsibleTeacher) {
        this.infoResponsibleTeacher = infoResponsibleTeacher;
    }

    public static InfoGrantPaymentEntity newInfoFromDomain(GrantPaymentEntity grantPaymentEntity) {
        if (grantPaymentEntity != null) {
            if (grantPaymentEntity instanceof GrantProject) {
                return InfoGrantProjectWithTeacherAndCostCenter
                        .newInfoFromDomain((GrantProject) grantPaymentEntity);
            } else if (grantPaymentEntity instanceof GrantCostCenter) {
                return InfoGrantCostCenterWithTeacher
                        .newInfoFromDomain((GrantCostCenter) grantPaymentEntity);
            }
        }
        return null;
    }

}
