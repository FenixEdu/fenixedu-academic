/*
 * Created on 12/Nov/2003
 *
 */
package Dominio.reimbursementGuide;

import java.util.Calendar;

import Dominio.DomainObject;
import Dominio.IGuide;

/**
 * 
 * 
 * This class contains all the information regarding a Reimbursement Guide.
 * Value is the amount of money returned and cannot exceed the value of the Payment Guide
 * and the sum of all the Reimbursement Guides 's values cannot exceed the Payment Guide value. 
 * The employee is the employee who made the modification to the Reimbursement Guide.
 * The paymentGuide is the Guide from wich the reimbursement is made.
 * The justification is a text with the description of the reimbursement
 * The creationDate is the date in which the reimbursement guide is created
 * The modificationDate is the date in wich the reimbursement guide was last modified  
 *@author João Mota
 *
 */
public class ReimbursementGuide extends DomainObject implements IReimbursementGuide {
	
	protected IGuide paymentGuide;
	protected Double value;
	protected String justification;
	protected Calendar creationDate;
	
	private Integer keyPaymentGuide;
	
	/**
	 * 
	 */
	public ReimbursementGuide() {

	}

	/**
	 * @return
	 */
	public Calendar getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 */
	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return
	 */
	public String getJustification() {
		return justification;
	}

	/**
	 * @param justification
	 */
	public void setJustification(String justification) {
		this.justification = justification;
	}

	/**
	 * @return
	 */
	public IGuide getPaymentGuide() {
		return paymentGuide;
	}

	/**
	 * @param paymentGuide
	 */
	public void setPaymentGuide(IGuide paymentGuide) {
		this.paymentGuide = paymentGuide;
	}

	/**
	 * @return
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * @param value
	 */
	public void setValue(Double value) {
		this.value = value;
	}

	/**
	 * @return
	 */
	public Integer getKeyPaymentGuide() {
		return keyPaymentGuide;
	}

	/**
	 * @param keyPaymentGuide
	 */
	public void setKeyPaymentGuide(Integer keyPaymentGuide) {
		this.keyPaymentGuide = keyPaymentGuide;
	}

}
