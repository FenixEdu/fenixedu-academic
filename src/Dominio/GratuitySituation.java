/*
 * Created on 5/Jan/2004
 *
 */
package Dominio;

import java.util.Date;
import java.util.List;

import Util.ExemptionGratuityType;

/**
 * @author Tânia Pousão
 *
 */
public class GratuitySituation extends DomainObject implements IGratuitySituation
{
	private Double payedValue;
	private Double remainingValue;
	private Integer exemptionPercentage;
	private ExemptionGratuityType exemptionType;
	private String exemptionDescription;

	private IGratuityValues gratuityValues;
	private Integer keyGratuityValues;
	
	private IStudentCurricularPlan studentCurricularPlan;
	private Integer keyStudentCurricularPlan;
	
	private Date when;
	private IEmployee employee;
	private Integer keyEmployee; 
	
	private List transactionList;
	
	/**
	 * @return Returns the employee.
	 */
	public IEmployee getEmployee()
	{
		return employee;
	}

	/**
	 * @param employee The employee to set.
	 */
	public void setEmployee(IEmployee employee)
	{
		this.employee = employee;
	}

	/**
	 * @return Returns the employeeKey.
	 */
	public Integer getEmployeeKey()
	{
		return keyEmployee;
	}

	/**
	 * @param employeeKey The employeeKey to set.
	 */
	public void setEmployeeKey(Integer employeeKey)
	{
		this.keyEmployee = employeeKey;
	}

	/**
	 * @return Returns the when.
	 */
	public Date getWhen()
	{
		return when;
	}

	/**
	 * @param when The when to set.
	 */
	public void setWhen(Date when)
	{
		this.when = when;
	}

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
	public IGratuityValues getGratuityValues()
	{
		return gratuityValues;
	}

	/**
	 * @param gratuity The gratuity to set.
	 */
	public void setGratuityValues(IGratuityValues gratuity)
	{
		this.gratuityValues = gratuity;
	}

	/**
	 * @return Returns the keyGratuity.
	 */
	public Integer getKeyGratuityValues()
	{
		return keyGratuityValues;
	}

	/**
	 * @param keyGratuity The keyGratuity to set.
	 */
	public void setKeyGratuityValues(Integer keyGratuityValues)
	{
		this.keyGratuityValues = keyGratuityValues;
	}

	/**
	 * @return Returns the keyStudent.
	 */
	public Integer getKeyStudentCurricularPlan()
	{
		return keyStudentCurricularPlan;
	}

	/**
	 * @param keyStudent The keyStudent to set.
	 */
	public void setKeyStudentCurricularPlan(Integer keyStudent)
	{
		this.keyStudentCurricularPlan = keyStudent;
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
	public IStudentCurricularPlan getStudentCurricularPlan()
	{
		return studentCurricularPlan;
	}

	/**
	 * @param student The student to set.
	 */
	public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
	{
		this.studentCurricularPlan = studentCurricularPlan;
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
		//TODO: to make
		return null;
	}

	public boolean equals(Object object)
	{
		//TODO: to make
		return true;
	}
}
