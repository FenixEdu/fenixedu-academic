package DataBeans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.struts.util.MessageResources;

/**
 * @author Fernanda Quitério 10/Jan/2004
 *  
 */
public class InfoPaymentPhase extends InfoObject implements Serializable
{
	private Date startDate;
	private Date endDate;
	private Double value;
	private String description;

	private InfoGratuityValues infoGratuityValues;

	private List transactionList;

	public String toString()
	{
		StringBuffer object = new StringBuffer();
		object =
			object
				.append("\n[InfoPaymentPhase: ")
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

	/**
	 * @return Returns the description.
	 */
	public String getDescription()
	{

		MessageResources messageResources = MessageResources.getMessageResources("ServidorApresentacao.ApplicationResources");
		//		ResourceBundle bundle = ResourceBundle.getBundle(Globals.MESSAGES_KEY);
		String newDescription = null;
		//		try {
		//			newDescription = bundle.getString(this.description);
//		newDescription = messageResources.getMessage(this.description);
		//		}catch(NullPointerException npe) {
//		if (newDescription == null)
//		{
			newDescription = this.description;
//		}
		//		}
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
	 * @return Returns the infoGratuityValues.
	 */
	public InfoGratuityValues getInfoGratuityValues()
	{
		return infoGratuityValues;
	}

	/**
	 * @param infoGratuityValues
	 *            The infoGratuityValues to set.
	 */
	public void setInfoGratuityValues(InfoGratuityValues infoGratuityValues)
	{
		this.infoGratuityValues = infoGratuityValues;
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

}
