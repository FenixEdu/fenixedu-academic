/*
 * Created on Jan 21, 2004
 */
package Dominio.grant.contract;

import Dominio.DomainObject;
import Dominio.ITeacher;

/**
 * @author pica
 * @author barbosa
 */
public abstract class GrantPaymentEntity extends DomainObject implements IGrantPaymentEntity {

    private static final String grantCostCenterOjbConcreteClass = "Dominio.grant.contract.GrantCostCenter";

    private static final String grantProjectOjbConcreteClass = "Dominio.grant.contract.GrantProject";

    protected String number;

    protected String designation;

    protected String ojbConcreteClass;

    protected ITeacher responsibleTeacher;

    protected Integer keyResponsibleTeacher;

    /**
     * Constructor
     */
    public GrantPaymentEntity() {
        this.ojbConcreteClass = this.getClass().getName();
    }

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
     * @return Returns the keyResponsibleTeacher.
     */
    public Integer getKeyResponsibleTeacher() {
        return keyResponsibleTeacher;
    }

    /**
     * @param keyResponsibleTeacher
     *            The keyResponsibleTeacher to set.
     */
    public void setKeyResponsibleTeacher(Integer keyResponsibleTeacher) {
        this.keyResponsibleTeacher = keyResponsibleTeacher;
    }

    /**
     * @return Returns the responsibleTeacher.
     */
    public ITeacher getResponsibleTeacher() {
        return responsibleTeacher;
    }

    /**
     * @param responsibleTeacher
     *            The responsibleTeacher to set.
     */
    public void setResponsibleTeacher(ITeacher responsibleTeacher) {
        this.responsibleTeacher = responsibleTeacher;
    }

    /**
     * @return Returns the grantCostCenterOjbConcreteClass.
     */
    public static String getGrantCostCenterOjbConcreteClass() {
        return grantCostCenterOjbConcreteClass;
    }

    /**
     * @return Returns the grantProjectOjbConcreteClass.
     */
    public static String getGrantProjectOjbConcreteClass() {
        return grantProjectOjbConcreteClass;
    }
}