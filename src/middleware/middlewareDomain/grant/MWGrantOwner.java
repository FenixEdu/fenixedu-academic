/*
 * Created on Feb 13, 2004
 */
package middleware.middlewareDomain.grant;

import java.util.Date;

import middleware.middlewareDomain.MWDomainObject;
import middleware.middlewareDomain.grant.MWPerson;

/**
 * @author pica
 * @author barbosa
 */
public class MWGrantOwner extends MWDomainObject
{
	private Integer idInternal;
    private Integer number;
    private Date dateSendCGD;
    private Integer cardCopyNumber;
    private MWPerson person;
    private Integer keyPerson;
    
    
	public MWGrantOwner()
	{
		super();
	}

	/**
	 * @return Returns the cardCopyNumber.
	 */
	public Integer getCardCopyNumber()
	{
		return cardCopyNumber;
	}

	/**
	 * @param cardCopyNumber The cardCopyNumber to set.
	 */
	public void setCardCopyNumber(Integer cardCopyNumber)
	{
		this.cardCopyNumber = cardCopyNumber;
	}

	/**
	 * @return Returns the dateSendCGD.
	 */
	public Date getDateSendCGD()
	{
		return dateSendCGD;
	}

	/**
	 * @param dateSendCGD The dateSendCGD to set.
	 */
	public void setDateSendCGD(Date dateSendCGD)
	{
		this.dateSendCGD = dateSendCGD;
	}

	/**
	 * @return Returns the idInternal.
	 */
	public Integer getIdInternal()
	{
		return idInternal;
	}

	/**
	 * @param idInternal The idInternal to set.
	 */
	public void setIdInternal(Integer idInternal)
	{
		this.idInternal = idInternal;
	}

	/**
	 * @return Returns the keyPerson.
	 */
	public Integer getKeyPerson()
	{
		return keyPerson;
	}

	/**
	 * @param keyPerson The keyPerson to set.
	 */
	public void setKeyPerson(Integer keyPerson)
	{
		this.keyPerson = keyPerson;
	}

	/**
	 * @return Returns the number.
	 */
	public Integer getNumber()
	{
		return number;
	}

	/**
	 * @param number The number to set.
	 */
	public void setNumber(Integer number)
	{
		this.number = number;
	}

	/**
	 * @return Returns the person.
	 */
	public MWPerson getPerson()
	{
		return person;
	}

	/**
	 * @param person The person to set.
	 */
	public void setPerson(MWPerson person)
	{
		this.person = person;
	}

}
