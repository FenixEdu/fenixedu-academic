package Dominio;

import java.util.Date;

import Util.CreditLineType;

/**
 * @author Fernanda Quitério 28/Nov/2003
 *  
 */
public interface ICreditLine extends IDomainObject
{
	public Double getCredits();
	public void setCredits(Double credits);
	
	public Date getEndDate();
	public void setEndDate(Date endDate);
	
	public String getExplanation();
	public void setExplanation(String explanation);
	
	public Integer getKeyTeacher();
	public void setKeyTeacher(Integer keyTeacher);
	
	public Date getStartDate();
	public void setStartDate(Date startDate);
	
	public ITeacher getTeacher();
	public void setTeacher(ITeacher teacher);
	
	public CreditLineType getCreditLineType();
	public void setCreditLineType(CreditLineType creditLineType);

}
