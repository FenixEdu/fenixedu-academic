/*
 * Created on 13/Nov/2003
 *
 */
package Dominio.reimbursementGuide;

import java.util.Calendar;

import Dominio.IDomainObject;
import Dominio.IGuide;

/**
 *fenix-head
 *Dominio.reimbursementGuide
 * @author João Mota
 *13/Nov/2003
 *
 */
public interface IReimbursementGuide extends IDomainObject{
	/**
	 * @return
	 */
	public Calendar getCreationDate();
	/**
	 * @param creationDate
	 */
	public void setCreationDate(Calendar creationDate);
	/**
	 * @return
	 */
	public String getJustification();
	/**
	 * @param justification
	 */
	public void setJustification(String justification);
	/**
	 * @return
	 */
	public IGuide getPaymentGuide();
	/**
	 * @param paymentGuide
	 */
	public void setPaymentGuide(IGuide paymentGuide);
	/**
	 * @return
	 */
	public Double getValue();
	/**
	 * @param value
	 */
	public void setValue(Double value);
}