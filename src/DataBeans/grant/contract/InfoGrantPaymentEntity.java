/*
 * Created on Jan 22, 2004
 */
package DataBeans.grant.contract;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import Dominio.grant.contract.IGrantCostCenter;
import Dominio.grant.contract.IGrantPaymentEntity;
import Dominio.grant.contract.IGrantProject;

/**
 * @author pica
 * @author barbosa
 */
public abstract class InfoGrantPaymentEntity extends InfoObject {

	private static final String grantCostCenterOjbConcreteClass = "Dominio.grant.contract.GrantCostCenter";
	private static final String grantProjectOjbConcreteClass = "Dominio.grant.contract.GrantProject";
	
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


    /**
     * @param infoResponsibleTeacher
     *            The infoResponsibleTeacher to set.
     */
    public void setInfoResponsibleTeacher(InfoTeacher infoResponsibleTeacher) {
        this.infoResponsibleTeacher = infoResponsibleTeacher;
    }

    public static InfoGrantPaymentEntity newInfoFromDomain(
            IGrantPaymentEntity grantPaymentEntity) {
        if (grantPaymentEntity != null)  {
            //if (grantPaymentEntity.getOjbConcreteClass().equals("Dominio.grant.contract.GrantProject")) {
        	if(grantPaymentEntity instanceof IGrantProject) {
                return InfoGrantProjectWithTeacherAndCostCenter.newInfoFromDomain(grantPaymentEntity);
            } else if (grantPaymentEntity instanceof IGrantCostCenter) {
            	return InfoGrantCostCenterWithTeacher.newInfoFromDomain(grantPaymentEntity);
            }
        }
        return null;
    }    

    public static IGrantPaymentEntity newDomainFromInfo(InfoGrantPaymentEntity infoGrantPaymentEntity)
    {
        IGrantPaymentEntity grantPaymentEntity = null;
        if(infoGrantPaymentEntity != null)
        {
            if (infoGrantPaymentEntity instanceof InfoGrantCostCenter)
                return InfoGrantCostCenterWithTeacher.newDomainFromInfo((InfoGrantCostCenter) infoGrantPaymentEntity);
            else if (infoGrantPaymentEntity instanceof InfoGrantProject)
                return InfoGrantProjectWithTeacherAndCostCenter.newDomainFromInfo((InfoGrantProject) infoGrantPaymentEntity);
        }
        return grantPaymentEntity;
    }

}
