package Dominio;

import java.util.Date;

import Util.CreditLineType;

/**
 * @author Fernanda Quitério 28/Nov/2003
 *  
 */
public class CreditLine extends DomainObject implements ICreditLine
{
	private Double credits;
	private CreditLineType creditLineType;
	private String explanation;
	private Date startDate;
	private Date endDate;

	private Integer keyTeacher;
	private ITeacher teacher;

	public CreditLine()
	{
	}

	public boolean equals(Object arg0)
	{
		CreditLine creditLine = (CreditLine) arg0;

		boolean result = false;
		if (((creditLine.getCredits() == null && this.getCredits() == null)
			|| (creditLine.getCredits().equals(this.getCredits())))
			&& ((creditLine.getEndDate() == null && this.getEndDate() == null)
				|| (creditLine.getEndDate().equals(this.getEndDate())))
			&& ((creditLine.getStartDate() == null && this.getStartDate() == null)
				|| (creditLine.getStartDate().equals(this.getStartDate())))
			&& ((creditLine.getExplanation() == null && this.getExplanation() == null)
				|| (creditLine.getExplanation().equals(this.getExplanation())))
			&& ((creditLine.getTeacher() == null && this.getTeacher() == null)
				|| (creditLine.getTeacher().equals(this.getTeacher())))
			&& ((creditLine.getCreditLineType() == null && this.getCreditLineType() == null)
				|| (creditLine.getCreditLineType().equals(this.getCreditLineType()))))
		{
			result = true;
		}
		return result;
	}
	
	public String toString() {
		String result = "[CREDIT_LINE";
		result += ", Id internal =" + this.getIdInternal();
		result += ", credits =" + this.getCredits();
		result += ", type =" + this.getCreditLineType().toString();
		result += ", explanation =" + this.getExplanation();
		result += ", start date =" + this.getStartDate();
		result += ", end date =" + this.getEndDate();
		result += ", teacher =" + this.getTeacher().toString();
		result += "]";
		return result;
  }

	/**
	 * @return Returns the credits.
	 */
	public Double getCredits()
	{
		return credits;
	} /**
	   * @param credits
	   *            The credits to set.
	   */
	public void setCredits(Double credits)
	{
		this.credits = credits;
	} /**
	   * @return Returns the endDate.
	   */
	public Date getEndDate()
	{
		return endDate;
	} /**
	   * @param endDate
	   *            The endDate to set.
	   */
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	} /**
	   * @return Returns the explanation.
	   */
	public String getExplanation()
	{
		return explanation;
	} /**
	   * @param explanation
	   *            The explanation to set.
	   */
	public void setExplanation(String explanation)
	{
		this.explanation = explanation;
	} /**
	   * @return Returns the keyTeacher.
	   */
	public Integer getKeyTeacher()
	{
		return keyTeacher;
	} /**
	   * @param keyTeacher
	   *            The keyTeacher to set.
	   */
	public void setKeyTeacher(Integer keyTeacher)
	{
		this.keyTeacher = keyTeacher;
	} /**
	   * @return Returns the startDate.
	   */
	public Date getStartDate()
	{
		return startDate;
	} /**
	   * @param startDate
	   *            The startDate to set.
	   */
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	} /**
	   * @return Returns the teacher.
	   */
	public ITeacher getTeacher()
	{
		return teacher;
	} /**
	   * @param teacher
	   *            The teacher to set.
	   */
	public void setTeacher(ITeacher teacher)
	{
		this.teacher = teacher;
	} /**
	   * @return Returns the type.
	   */
	public CreditLineType getCreditLineType()
	{
		return creditLineType;
	} /**
	   * @param type
	   *            The type to set.
	   */
	public void setCreditLineType(CreditLineType creditLineType)
	{
		this.creditLineType = creditLineType;
	}

}
