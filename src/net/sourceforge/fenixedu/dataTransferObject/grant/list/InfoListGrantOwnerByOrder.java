/*
 * Created on Jun 22, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.list;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.util.NameUtils;

import org.apache.commons.lang.StringUtils;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoListGrantOwnerByOrder extends InfoObject {

    private final DomainReference<GrantOwner> domainReference;

    private Integer contractNumber;
    // Only used in the list of grant owner by criteria

    private Date beginContract;
    // Only used in the list of grant owner by criteria

    private Date endContract;
    // Only used in the list of grant owner by criteria

    private String grantType;
    // Sigla of the grantType

    private String insurancePaymentEntity;
    // Number of the paymentEntity

    private String numberPaymentEntity;

    private String designation;

    private Double totalInsurance = 0.0;

    private long totalOfDays;

    private Double totalOfGrantPayment = 0.0;

    private Double valueOfGrantPayment = 0.0;

    public InfoListGrantOwnerByOrder(final GrantOwner domainObject) {
	domainReference = new DomainReference<GrantOwner>(domainObject);
    }

    public GrantOwner getGrantOwner() {
	return domainReference == null ? null : domainReference.getObject();
    }

    @Override
    public boolean equals(Object obj) {
	return obj != null && getGrantOwner() == ((InfoListGrantOwnerByOrder) obj).getGrantOwner();
    }

    public Integer getGrantOwnerId() {
	return getGrantOwner().getIdInternal();
    }

    public Integer getGrantOwnerNumber() {
	return getGrantOwner().getNumber();
    }

    public String getFirstName() {
	return getGrantOwner().hasPerson() ? NameUtils.getFirstName(getGrantOwner().getPerson().getName()) : StringUtils.EMPTY;
    }

    public String getLastName() {
	return getGrantOwner().hasPerson() ? NameUtils.getLastName(getGrantOwner().getPerson().getName()) : StringUtils.EMPTY;
    }

    public Double getTotalOfGrantPayment() {
	return totalOfGrantPayment;
    }

    public void setTotalOfGrantPayment(Double totalOfGrantPayment) {
	this.totalOfGrantPayment = totalOfGrantPayment;
    }

    public Double getTotalInsurance() {
	return totalInsurance;
    }

    public String getTotalInsuranceString() {
	DecimalFormatSymbols s = new DecimalFormatSymbols();
	s.setDecimalSeparator('.');
	DecimalFormat nf = new DecimalFormat("0.00", s);
	nf.setMaximumFractionDigits(2);
	nf.setMinimumFractionDigits(2);
	return nf.format(totalInsurance);
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

    public Date getBeginContract() {
	return beginContract;
    }

    public void setBeginContract(Date beginContract) {
	this.beginContract = beginContract;
    }

    public Date getEndContract() {
	return endContract;
    }

    public void setEndContract(Date endContract) {
	this.endContract = endContract;
    }

    public Integer getContractNumber() {
	return contractNumber;
    }

    public void setContractNumber(Integer contractNumber) {
	this.contractNumber = contractNumber;
    }

    public String getGrantType() {
	return grantType;
    }

    public void setGrantType(String grantType) {
	this.grantType = grantType;
    }

    public String getInsurancePaymentEntity() {
	return insurancePaymentEntity;
    }

    public void setInsurancePaymentEntity(String insurancePaymentEntity) {
	this.insurancePaymentEntity = insurancePaymentEntity;
    }

    public Double getValueOfGrantPayment() {
	return valueOfGrantPayment;
    }

    public void setValueOfGrantPayment(Double valueOfGrantPayment) {
	this.valueOfGrantPayment = valueOfGrantPayment;
    }

}
