/*
 * Created on Jun 26, 2004
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainObject;

/**
 * @author Barbosa
 * @author Pica
 */
public class GrantInsurance extends DomainObject implements IGrantInsurance {

    private Date dateBeginInsurance;

    private Date dateEndInsurance;

    private Double totalValue;

    private IGrantPaymentEntity grantPaymentEntity;

    private Integer keyGrantPaymentEntity;

    private IGrantContract grantContract;

    private Integer keyGrantContract;

    public GrantInsurance() {
    }

    /**
     * @param idInternal
     */
    public GrantInsurance(Integer idInternal) {
        super(idInternal);
    }

    /**
     * @return Returns the grantContract.
     */
    public IGrantContract getGrantContract() {
        return grantContract;
    }

    /**
     * @param grantContract
     *            The grantContract to set.
     */
    public void setGrantContract(IGrantContract grantContract) {
        this.grantContract = grantContract;
    }

    /**
     * @return Returns the keyGrantContract.
     */
    public Integer getKeyGrantContract() {
        return keyGrantContract;
    }

    /**
     * @param keyGrantContract
     *            The keyGrantContract to set.
     */
    public void setKeyGrantContract(Integer keyGrantContract) {
        this.keyGrantContract = keyGrantContract;
    }

    /**
     * @return Returns the beginInsurance.
     */
    public Date getDateBeginInsurance() {
        return dateBeginInsurance;
    }

    /**
     * @param beginInsurance
     *            The beginInsurance to set.
     */
    public void setDateBeginInsurance(Date beginInsurance) {
        this.dateBeginInsurance = beginInsurance;
    }

    /**
     * @return Returns the endInsurance.
     */
    public Date getDateEndInsurance() {
        return dateEndInsurance;
    }

    /**
     * @param endInsurance
     *            The endInsurance to set.
     */
    public void setDateEndInsurance(Date endInsurance) {
        this.dateEndInsurance = endInsurance;
    }

    /**
     * @return Returns the totalValue.
     */
    public Double getTotalValue() {
        return totalValue;
    }

    /**
     * @param totalValue
     *            The totalValue to set.
     */
    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
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
}