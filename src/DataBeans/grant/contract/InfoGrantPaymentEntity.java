/*
 * Created on Jan 22, 2004
 */
package DataBeans.grant.contract;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import Dominio.grant.contract.IGrantPaymentEntity;

/**
 * @author pica
 * @author barbosa
 */
public abstract class InfoGrantPaymentEntity extends InfoObject {

    protected String number;

    protected String designation;

    protected String ojbConcreteClass;

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
     * @return Returns the ojbConcreteClass.
     */
    public String getOjbConcreteClass() {
        return ojbConcreteClass;
    }

    /**
     * @param ojbConcreteClass
     *            The ojbConcreteClass to set.
     */
    public void setOjbConcreteClass(String ojbConcreteClass) {
        this.ojbConcreteClass = ojbConcreteClass;
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

    public static InfoGrantPaymentEntity newInfoFromDomain(
            IGrantPaymentEntity grantPaymentEntity) {
        if (grantPaymentEntity != null) 
        {
            if (grantPaymentEntity.getOjbConcreteClass().equals("Dominio.grant.contract.GrantProject"))
            {
                return InfoGrantProjectWithTeacherAndCostCenter.newInfoFromDomain(grantPaymentEntity);
            } else
                return InfoGrantCostCenterWithTeacher.newInfoFromDomain(grantPaymentEntity);
        }
        else
            return null;

    }

}
