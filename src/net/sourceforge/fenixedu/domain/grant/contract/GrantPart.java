/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ITeacher;

/**
 * @author pica
 * @author barbosa
 */
public class GrantPart extends DomainObject implements IGrantPart {

    private Integer percentage;

    private IGrantSubsidy grantSubsidy;

    private IGrantPaymentEntity grantPaymentEntity;

    private ITeacher responsibleTeacher;

    private Integer keyGrantSubsidy;

    private Integer keyGrantPaymentEntity;

    private Integer keyResponsibleTeacher;

    public GrantPart() {
    }

    /**
     * @param idInternal
     */
    public GrantPart(Integer idInternal) {
        super(idInternal);
    }

    /**
     * @return Returns the grantPaymentEntity.
     */
    public IGrantPaymentEntity getGrantPaymentEntity() {
        return grantPaymentEntity;
    }

    /**
     * @param grantPaymentEntity
     *            The grantPaymentEntity to set.
     */
    public void setGrantPaymentEntity(IGrantPaymentEntity grantPaymentEntity) {
        this.grantPaymentEntity = grantPaymentEntity;
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
     * @return Returns the grantSubsidy.
     */
    public IGrantSubsidy getGrantSubsidy() {
        return grantSubsidy;
    }

    /**
     * @param grantSubsidy
     *            The grantSubsidy to set.
     */
    public void setGrantSubsidy(IGrantSubsidy grantSubsidy) {
        this.grantSubsidy = grantSubsidy;
    }

    /**
     * @return Returns the keyGrantPaymentEntity.
     */
    public Integer getKeyGrantPaymentEntity() {
        return keyGrantPaymentEntity;
    }

    /**
     * @param keyGrantPaymentEntity
     *            The keyGrantPaymentEntity to set.
     */
    public void setKeyGrantPaymentEntity(Integer keyGrantPaymentEntity) {
        this.keyGrantPaymentEntity = keyGrantPaymentEntity;
    }

    /**
     * @return Returns the keyResponsibleTeacher.
     */
    public Integer getKeyResponsibleTeacher() {
        return keyResponsibleTeacher;
    }

    /**
     * @param keyGrantResponsibleTeacher
     *            The keyGrantResponsibleTeacher to set.
     */
    public void setKeyResponsibleTeacher(Integer keyResponsibleTeacher) {
        this.keyResponsibleTeacher = keyResponsibleTeacher;
    }

    /**
     * @return Returns the keyGrantSubsidy.
     */
    public Integer getKeyGrantSubsidy() {
        return keyGrantSubsidy;
    }

    /**
     * @param keyGrantSubsidy
     *            The keyGrantSubsidy to set.
     */
    public void setKeyGrantSubsidy(Integer keyGrantSubsidy) {
        this.keyGrantSubsidy = keyGrantSubsidy;
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