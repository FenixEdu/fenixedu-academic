/*
 * Created on 6/Jan/2004
 *  
 */
package Dominio;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;

/**
 * @author Tânia Pousão
 *  
 */
public class PaymentPhase extends DomainObject implements IPaymentPhase
{
	private Date startDate;
	private Date endDate;
	private Double value;
	private String description;

	private IGratuityValues gratuityValues;
	private Integer keyGratuityValues;

	private List transactionList;

	/**
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		
		MessageResources messages = MessageResources.getMessageResources("ServidorApresentacao.ApplicationResources");
		
		String newDescription = null;
		newDescription = messages.getMessage(this.description);
		if (newDescription == null)
		{
			newDescription = this.description;
		}
		return newDescription;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return Returns the endDate.
	 */
	public Date getEndDate()
	{
		return endDate;
	}

	/**
	 * @param endDate
	 *            The endDate to set.
	 */
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * @return Returns the startDate.
	 */
	public Date getStartDate()
	{
		return startDate;
	}

	/**
	 * @param startDate
	 *            The startDate to set.
	 */
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * @return Returns the value.
	 */
	public Double getValue()
	{
		return value;
	}

	/**
	 * @param value
	 *            The value to set.
	 */
	public void setValue(Double value)
	{
		this.value = value;
	}

	/**
	 * @return Returns the gratuity.
	 */
	public IGratuityValues getGratuityValues()
	{
		return gratuityValues;
	}

	/**
	 * @param gratuity
	 *            The gratuity to set.
	 */
	public void setGratuityValues(IGratuityValues gratuityValues)
	{
		this.gratuityValues = gratuityValues;
	}

	/**
	 * @return Returns the keyGratuity.
	 */
	public Integer getKeyGratuityValues()
	{
		return keyGratuityValues;
	}

	/**
	 * @param keyGratuity
	 *            The keyGratuity to set.
	 */
	public void setKeyGratuityValues(Integer keyGratuityValues)
	{
		this.keyGratuityValues = keyGratuityValues;
	}

	/**
	 * @return Returns the transactionList.
	 */
	public List getTransactionList()
	{
		return transactionList;
	}

	/**
	 * @param transactionList
	 *            The transactionList to set.
	 */
	public void setTransactionList(List transactionList)
	{
		this.transactionList = transactionList;
	}

	public String toString()
	{
		StringBuffer object = new StringBuffer();
		object =
		object
		.append("\n[PaymentPhase: ")
		.append("idInternal= ")
		.append(getIdInternal())
		.append(" starDate= ")
		.append(startDate)
		.append("; endDate= ")
		.append(endDate)
		.append("; value= ")
		.append(value)
		.append("; description= ")
		.append(description)
		.append("\n");

		return object.toString();
	}

	public boolean equals(Object object)
	{
		//TODO: to make
		return true;
	}
}
