/*
 * Created on 7/Jun/2004
 *  
 */
package net.sourceforge.fenixedu.domain.sms;

import java.util.Date;

import net.sourceforge.fenixedu.domain.IPerson;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class SentSms extends SentSms_Base {

	protected Date sendDate;

	protected Date deliveryDate;

	protected SmsDeliveryType deliveryType;

	protected IPerson person;

	public SentSms() {
		super();
	}

	/**
	 * @return Returns the deliveryDate.
	 */
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param deliveryDate
	 *            The deliveryDate to set.
	 */
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	/**
	 * @return Returns the deliveryType.
	 */
	public SmsDeliveryType getDeliveryType() {
		return deliveryType;
	}

	/**
	 * @param deliveryType
	 *            The deliveryType to set.
	 */
	public void setDeliveryType(SmsDeliveryType deliveryType) {
		this.deliveryType = deliveryType;
	}

	/**
	 * @return Returns the person.
	 */
	public IPerson getPerson() {
		return person;
	}

	/**
	 * @param person
	 *            The person to set.
	 */
	public void setPerson(IPerson person) {
		this.person = person;
	}

	/**
	 * @return Returns the sendDate.
	 */
	public Date getSendDate() {
		return sendDate;
	}

	/**
	 * @param sendDate
	 *            The sendDate to set.
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

}