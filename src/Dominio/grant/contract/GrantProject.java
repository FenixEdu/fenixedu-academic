/*
 * Created on Jan 21, 2004
 */
package Dominio.grant.contract;

import Dominio.ITeacher;

/**
 * @author pica
 * @author barbosa
 */
public class GrantProject extends GrantPaymentEntity implements IGrantProject
{
	private ITeacher responsibleTeacher;
	private Integer keyResponsibleTeacher;
	
	public GrantProject()
	{
		super();
	}

	/**
	 * @return Returns the responsibleTeacher.
	 */
	public ITeacher getResponsibleTeacher()
	{
		return responsibleTeacher;
	}

	/**
	 * @param responsibleTeacher The responsibleTeacher to set.
	 */
	public void setResponsibleTeacher(ITeacher responsibleTeacher)
	{
		this.responsibleTeacher = responsibleTeacher;
	}

	/**
	 * @return Returns the keyResponsibleTeacher.
	 */
	public Integer getKeyResponsibleTeacher()
	{
		return keyResponsibleTeacher;
	}

	/**
	 * @param keyResponsibleTeacher The keyResponsibleTeacher to set.
	 */
	public void setKeyResponsibleTeacher(Integer keyResponsibleTeacher)
	{
		this.keyResponsibleTeacher = keyResponsibleTeacher;
	}
}
