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
 * <br>
 *@author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a>
 *
 */
public class ReimbursementGuide extends DomainObject implements IReimbursementGuide {
    
    protected Integer number;
	protected IGuide guide;
	protected Double value;
	protected String justification;
	protected Calendar creationDate;
	
	private Integer keyGuide;
	
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
	public IGuide getGuide() {
		return guide;
	}

	/**
	 * @param paymentGuide
	 */
	public void setGuide(IGuide paymentGuide) {
		this.guide = paymentGuide;
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
	public Integer getKeyGuide() {
		return keyGuide;
	}

	/**
	 * @param keyPaymentGuide
	 */
	public void setKeyGuide(Integer keyPaymentGuide) {
		this.keyGuide = keyPaymentGuide;
	}

	/**
	 * @return
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * @param number
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

}
