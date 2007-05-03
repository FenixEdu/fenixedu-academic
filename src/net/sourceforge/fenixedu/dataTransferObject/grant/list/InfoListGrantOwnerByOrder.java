/*
 * Created on Jun 22, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.list;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoListGrantOwnerByOrder extends InfoObject {
    private Integer grantOwnerId;

    private Integer grantOwnerNumber;

    private String firstName;

    private String lastName;

    private Integer contractNumber; //Only used in the list of grant owner by

    // criteria

    private Date beginContract; //Only used in the list of grant owner by

    // criteria

    private Date endContract; //Only used in the list of grant owner by

    // criteria

    private String grantType; //Sigla of the grantType

    private String insurancePaymentEntity; //Number of the paymentEntity
    
    private String numberPaymentEntity;
    
    private String designation;
    
    private Double totalInsurance;
    
    private long totalOfDays;
    
    private Double totalOfGrantPayment;
    
    private Double valueOfGrantPayment;

    public Double getTotalOfGrantPayment() {
		return totalOfGrantPayment;
	}

	public void setTotalOfGrantPayment(Double totalOfGrantPayment) {
		this.totalOfGrantPayment = totalOfGrantPayment;
	}

	public Double getTotalInsurance() {
		return totalInsurance;
	}

	public void setTotalInsurance(Double totalInsurance) {
		this.totalInsurance = totalInsurance;
	}

	public long getTotalOfDays() {
		return totalOfDays;
	}

	public void setTotalOfDays(long totalOfDays) {
		this.totalOfDays = totalOfDays;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getNumberPaymentEntity() {
		return numberPaymentEntity;
	}

	public void setNumberPaymentEntity(String numberPaymentEntity) {
		this.numberPaymentEntity = numberPaymentEntity;
	}

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

	public Double getValueOfGrantPayment() {
		return valueOfGrantPayment;
	}

	public void setValueOfGrantPayment(Double valueOfGrantPayment) {
		this.valueOfGrantPayment = valueOfGrantPayment;
	}
}