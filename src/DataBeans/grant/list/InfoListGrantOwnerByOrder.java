/*
 * Created on Jun 22, 2004
 *
 */
package DataBeans.grant.list;

import java.util.Date;

import DataBeans.InfoObject;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoListGrantOwnerByOrder extends InfoObject {
    private Integer grantOwnerId;

    private Integer grantOwnerNumber;

    private String firstName;

    private String lastName;

    private Integer contractNumber; //Only used in the list of grant owner by criteria

    private Date beginContract; //Only used in the list of grant owner by criteria

    private Date endContract; //Only used in the list of grant owner by criteria

    private String grantType; //Sigla of the grantType

    private String insurancePaymentEntity; //Number of the paymentEntity

    /**
     * @return Returns the beginContract.
     */
    public Date getBeginContract() {
        return beginContract;
    }

    /**
     * @param beginContract
     *            The beginContract to set.
     */
    public void setBeginContract(Date beginContract) {
        this.beginContract = beginContract;
    }

    /**
     * @return Returns the endContract.
     */
    public Date getEndContract() {
        return endContract;
    }

    /**
     * @param endContract
     *            The endContract to set.
     */
    public void setEndContract(Date endContract) {
        this.endContract = endContract;
    }

    /**
     * @return Returns the contractNumber.
     */
    public Integer getContractNumber() {
        return contractNumber;
    }

    /**
     * @param contractNumber
     *            The contractNumber to set.
     */
    public void setContractNumber(Integer contractNumber) {
        this.contractNumber = contractNumber;
    }

    /**
     * @return Returns the firstName.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     *            The firstName to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return Returns the grantOwnerId.
     */
    public Integer getGrantOwnerId() {
        return grantOwnerId;
    }

    /**
     * @param grantOwnerId
     *            The grantOwnerId to set.
     */
    public void setGrantOwnerId(Integer grantOwnerId) {
        this.grantOwnerId = grantOwnerId;
    }

    /**
     * @return Returns the grantType.
     */
    public String getGrantType() {
        return grantType;
    }

    /**
     * @param grantType
     *            The grantType to set.
     */
    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    /**
     * @return Returns the insurancePaymentEntity.
     */
    public String getInsurancePaymentEntity() {
        return insurancePaymentEntity;
    }

    /**
     * @param insurancePaymentEntity
     *            The insurancePaymentEntity to set.
     */
    public void setInsurancePaymentEntity(String insurancePaymentEntity) {
        this.insurancePaymentEntity = insurancePaymentEntity;
    }

    /**
     * @return Returns the lastName.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     *            The lastName to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return Returns the grantOwnerNumber.
     */
    public Integer getGrantOwnerNumber() {
        return grantOwnerNumber;
    }

    /**
     * @param grantOwnerNumber
     *            The grantOwnerNumber to set.
     */
    public void setGrantOwnerNumber(Integer grantOwnerNumber) {
        this.grantOwnerNumber = grantOwnerNumber;
    }
}