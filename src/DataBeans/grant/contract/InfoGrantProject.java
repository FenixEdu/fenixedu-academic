/*
 * Created on Jan 21, 2004
 */
package DataBeans.grant.contract;

import DataBeans.InfoTeacher;


/**
 * @author pica
 * @author barbosa
 */
public class InfoGrantProject extends InfoGrantPaymentEntity
{
	private InfoTeacher infoResponsibleTeacher;
	
	public InfoGrantProject()
	{
	}

	/**
	 * @return Returns the infoGrantResponsibleTeacher.
	 */
	public InfoTeacher getInfoResponsibleTeacher()
	{
		return infoResponsibleTeacher;
	}

	/**
	 * @param infoGrantResponsibleTeacher The infoGrantResponsibleTeacher to set.
	 */
	public void setInfoResponsibleTeacher(InfoTeacher infoResponsibleTeacher)
	{
		this.infoResponsibleTeacher = infoResponsibleTeacher;
	}

}
