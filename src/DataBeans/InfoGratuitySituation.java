/*
 * Created on 10/Jan/2004
 *
 */
package DataBeans;

import java.io.Serializable;
import java.util.List;

import Util.ExemptionGratuityType;

/**
 * @author Tânia Pousão
 *
 */
public class InfoGratuitySituation extends InfoObject implements Serializable
{
	private Double payedValue;
	private Double remainingValue;
	private Integer exemptionPercentage;
	private ExemptionGratuityType exemptionType;
	private String exemptionDescription;

	private InfoGratuityValues infoGratuityValues;
		
	private InfoStudentCurricularPlan infoStudentCurricularPlan;
	
	private List transactionList;
	
	/**
	 * @return Returns the exemptionDescription.
	 */
	public String getExemptionDescription()
	{
		return exemptionDescription;
	}

	/**
	 * @param exemptionDescription The exemptionDescription to set.
	 */
	public void setExemptionDescription(String exemptionDescription)
	{
		this.exemptionDescription = exemptionDescription;
	}

	/**
	 * @return Returns the exemptionPercentage.
	 */
	public Integer getExemptionPercentage()
	{
		return exemptionPercentage;
	}

	/**
	 * @param exemptionPercentage The exemptionPercentage to set.
	 */
	public void setExemptionPercentage(Integer exemptionPercentage)
	{
		this.exemptionPercentage = exemptionPercentage;
	}

	/**
	 * @return Returns the exemptionType.
	 */
	public ExemptionGratuityType getExemptionType()
	{
		return exemptionType;
	}

	/**
	 * @param exemptionType The exemptionType to set.
	 */
	public void setExemptionType(ExemptionGratuityType exemptionType)
	{
		this.exemptionType = exemptionType;
	}

	/**
	 * @return Returns the gratuity.
	 */
	public InfoGratuityValues getInfoGratuityValues()
	{
		return infoGratuityValues;
	}

	/**
	 * @param gratuity The gratuity to set.
	 */
	public void setInfoGratuityValues(InfoGratuityValues gratuity)
	{
		this.infoGratuityValues = gratuity;
	}

	/**
	 * @return Returns the payedValue.
	 */
	public Double getPayedValue()
	{
		return payedValue;
	}

	/**
	 * @param payedValue The payedValue to set.
	 */
	public void setPayedValue(Double payedValue)
	{
		this.payedValue = payedValue;
	}

	/**
	 * @return Returns the remainingValue.
	 */
	public Double getRemainingValue()
	{
		return remainingValue;
	}

	/**
	 * @param remainingValue The remainingValue to set.
	 */
	public void setRemainingValue(Double remainingValue)
	{
		this.remainingValue = remainingValue;
	}

	/**
	 * @return Returns the student.
	 */
	public InfoStudentCurricularPlan getInfoStudentCurricularPlan()
	{
		return infoStudentCurricularPlan;
	}

	/**
	 * @param student The student to set.
	 */
	public void setInfoStudentCurricularPlan(InfoStudentCurricularPlan studentCurricularPlan)
	{
		this.infoStudentCurricularPlan = studentCurricularPlan;
	}

	/**
	 * @return Returns the transactionList.
	 */
	public List getTransactionList()
	{
		return transactionList;
	}

	/**
	 * @param transactionList The transactionList to set.
	 */
	public void setTransactionList(List transactionList)
	{
		this.transactionList = transactionList;
	}
	
	public String toString()
	{
		String result = new String();
		result +="[InfoGratuitySituation: exemptionPercentage" + this.exemptionPercentage;
		result +="\nexemptionType: " + this.exemptionType;
		result +="\nexemptionDescription: " + this.exemptionDescription;
		result +="\npayedValue: " + this.payedValue;
		result +="\nremainingValue: " + this.remainingValue;
		result +="\ninfoGratuityValues: " + this.infoGratuityValues;
		result +="\ninfoStudentCurricularPlan: " + this.infoStudentCurricularPlan;
		return result;
	}

}
