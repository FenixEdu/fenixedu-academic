/*
 * Created on Jan 22, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import java.lang.reflect.Proxy;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantProject;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantProject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.ojb.broker.core.proxy.ProxyHelper;

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
     * @return Returns the grantCostCenterOjbConcreteClass.
     */
    public static String getGrantCostCenterOjbConcreteClass() {
        return GrantCostCenter.class.getName();
    }

    /**
     * @return Returns the grantProjectOjbConcreteClass.
     */
    public static String getGrantProjectOjbConcreteClass() {
        return GrantProject.class.getName();
    }

    /**
     * @param infoResponsibleTeacher
     *            The infoResponsibleTeacher to set.
     */
    public void setInfoResponsibleTeacher(InfoTeacher infoResponsibleTeacher) {
        this.infoResponsibleTeacher = infoResponsibleTeacher;
    }

    public static InfoGrantPaymentEntity newInfoFromDomain(IGrantPaymentEntity grantPaymentEntity) {
        if (grantPaymentEntity != null) {

            if (grantPaymentEntity instanceof Proxy) {
                grantPaymentEntity = (IGrantPaymentEntity) ProxyHelper.getRealObject(grantPaymentEntity);
            }

            if (grantPaymentEntity instanceof IGrantProject) {
                return InfoGrantProjectWithTeacherAndCostCenter
                        .newInfoFromDomain((IGrantProject) grantPaymentEntity);
            } else if (grantPaymentEntity instanceof IGrantCostCenter) {
                return InfoGrantCostCenterWithTeacher
                        .newInfoFromDomain((IGrantCostCenter) grantPaymentEntity);
            }
        }
        return null;
    }

    public static IGrantPaymentEntity newDomainFromInfo(InfoGrantPaymentEntity infoGrantPaymentEntity)
            throws ExcepcaoPersistencia {
        IGrantPaymentEntity grantPaymentEntity = null;
        if (infoGrantPaymentEntity != null) {
            if (infoGrantPaymentEntity.getOjbConcreteClass().equals(GrantCostCenter.class.getName())) {
                return InfoGrantCostCenterWithTeacher
                        .newDomainFromInfo((InfoGrantCostCenter) infoGrantPaymentEntity);
            } else if (infoGrantPaymentEntity.getOjbConcreteClass().equals(GrantProject.class.getName())) {
                return InfoGrantProjectWithTeacherAndCostCenter
                        .newDomainFromInfo((InfoGrantProject) infoGrantPaymentEntity);
            }
        }
        return grantPaymentEntity;
    }

}