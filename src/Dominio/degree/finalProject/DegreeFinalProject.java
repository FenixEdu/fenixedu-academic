/*
 * Created on Nov 11, 2003 by jpvl
 *  
 */
package Dominio.degree.finalProject;

import Dominio.DomainObject;
import Dominio.IExecutionYear;

/**
 * @author jpvl
 */
public class DegreeFinalProject extends DomainObject implements IDegreeFinalProject
{
	private Integer keyExecutionYear;
	private IExecutionYear executionYear;
	private String title;

	public DegreeFinalProject()
	{}

	/**
	 * @return Returns the executionYear.
	 */
	public IExecutionYear getExecutionYear()
	{
		return this.executionYear;
	}

	/**
	 * @param executionYear The executionYear to set.
	 */
	public void setExecutionYear(IExecutionYear executionYear)
	{
		this.executionYear = executionYear;
	}

	/**
	 * @return Returns the keyExecutionYear.
	 */
	public Integer getKeyExecutionYear()
	{
		return this.keyExecutionYear;
	}

	/**
	 * @param keyExecutionYear The keyExecutionYear to set.
	 */
	public void setKeyExecutionYear(Integer keyExecutionYear)
	{
		this.keyExecutionYear = keyExecutionYear;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle()
	{
		return this.title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

}
