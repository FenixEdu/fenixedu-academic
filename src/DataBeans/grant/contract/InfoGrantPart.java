/*
 * Created on Jan 21, 2004
 */
package DataBeans.grant.contract;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;

/**
 * @author pica
 * @author barbosa
 */
public class InfoGrantPart extends InfoObject {

    private Integer percentage;
    private InfoGrantSubsidy infoGrantSubsidy;
    private InfoGrantPaymentEntity infoGrantPaymentEntity;
    private InfoTeacher infoResponsibleTeacher;

    /**
     * @return Returns the infoGrantPaymentEntity.
     */
    public InfoGrantPaymentEntity getInfoGrantPaymentEntity() {
        return infoGrantPaymentEntity;
    }

    /**
     * @param infoGrantPaymentEntity
     *            The infoGrantPaymentEntity to set.
     */
    public void setInfoGrantPaymentEntity(
            InfoGrantPaymentEntity infoGrantPaymentEntity) {
        this.infoGrantPaymentEntity = infoGrantPaymentEntity;
    }

    /**
     * @return Returns the infoGrantResponsibleTeacher.
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

    /**
     * @return Returns the infoGrantSubsidy.
     */
    public InfoGrantSubsidy getInfoGrantSubsidy() {
        return infoGrantSubsidy;
    }

    /**
     * @param infoGrantSubsidy
     *            The infoGrantSubsidy to set.
     */
    public void setInfoGrantSubsidy(InfoGrantSubsidy infoGrantSubsidy) {
        this.infoGrantSubsidy = infoGrantSubsidy;
    }

    /**
     * @return Returns the percentage.
     */
    public Integer getPercentage() {
        return percentage;
    }

    /**
     * @param percentage
     *            The percentage to set.
     */
    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

}
