/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.domain.grant.contract;

/**
 * @author pica
 * @author barbosa
 */
public abstract class GrantPaymentEntity extends GrantPaymentEntity_Base {
    private static final String grantCostCenterOjbConcreteClass = GrantCostCenter.class.getName();
    private static final String grantProjectOjbConcreteClass = GrantProject.class.getName();

    /**
     * Constructor
     */
    public GrantPaymentEntity() {
        this.setOjbConcreteClass(this.getClass().getName());
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