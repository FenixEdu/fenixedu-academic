/*
 * Created on Nov 11, 2003 by jpvl
 *
 */
package DataBeans.degree.finalProject;

import DataBeans.InfoExecutionYear;
import DataBeans.InfoObject;

/**
 * @author jpvl
 */
public class InfoDegreeFinalProject extends InfoObject
{
	private String title;
	private InfoExecutionYear infoExecutionYear;

	/**
	 * @return Returns the infoExecutionYear.
	 */
	public InfoExecutionYear getInfoExecutionYear()
	{
		return this.infoExecutionYear;
	}

	/**
	 * @param infoExecutionYear The infoExecutionYear to set.
	 */
	public void setInfoExecutionYear(InfoExecutionYear infoExecutionYear)
	{
		this.infoExecutionYear = infoExecutionYear;
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
