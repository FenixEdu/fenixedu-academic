/*
 * Created on Nov 11, 2003 by jpvl
 *
 */
package Dominio.degree.finalProject;

import Dominio.IExecutionYear;

/**
 * @author jpvl
 */
public interface IDegreeFinalProject
{
	/**
	 * @return Returns the executionYear.
	 */
	public abstract IExecutionYear getExecutionYear();
	/**
	 * @param executionYear The executionYear to set.
	 */
	public abstract void setExecutionYear(IExecutionYear executionYear);
	/**
	 * @return Returns the title.
	 */
	public abstract String getTitle();
	/**
	 * @param title The title to set.
	 */
	public abstract void setTitle(String title);
}