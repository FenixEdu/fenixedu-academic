/*
 * Created on Nov 11, 2003 by jpvl
 *
 */
package DataBeans.degree.finalProject;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;

/**
 * @author jpvl
 */
public class InfoOrientation extends InfoObject
{
	private InfoTeacher infoTeacher;
	private InfoDegreeFinalProject infoDegreeFinalProject;
	
	
	/**
	 * @return Returns the infoDegreeFinalProject.
	 */
	public InfoDegreeFinalProject getInfoDegreeFinalProject()
	{
		return this.infoDegreeFinalProject;
	}

	/**
	 * @param infoDegreeFinalProject The infoDegreeFinalProject to set.
	 */
	public void setInfoDegreeFinalProject(InfoDegreeFinalProject infoDegreeFinalProject)
	{
		this.infoDegreeFinalProject = infoDegreeFinalProject;
	}

	/**
	 * @return Returns the infoTeacher.
	 */
	public InfoTeacher getInfoTeacher()
	{
		return this.infoTeacher;
	}

	/**
	 * @param infoTeacher The infoTeacher to set.
	 */
	public void setInfoTeacher(InfoTeacher infoTeacher)
	{
		this.infoTeacher = infoTeacher;
	}

}
