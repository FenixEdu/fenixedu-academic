/*
 * Created on Feb 13, 2004
 */
package middleware.middlewareDomain.grant;

import middleware.middlewareDomain.MWDomainObject;

/**
 * @author pica
 * @author barbosa
 */
public class MWGrantPaymentEntity extends MWDomainObject
{
    private Integer idInternal;
    private MWGrantCostCenter costCenter;
    private Integer keyCostCenter;
    private MWGrantProject project;
    private Integer keyProject;
    
	public MWGrantPaymentEntity()
	{
		super();
	}

	/**
	 * @return Returns the costCenter.
	 */
	public MWGrantCostCenter getCostCenter()
	{
		return costCenter;
	}

	/**
	 * @param costCenter The costCenter to set.
	 */
	public void setCostCenter(MWGrantCostCenter costCenter)
	{
		this.costCenter = costCenter;
	}

	/**
	 * @return Returns the idInternal.
	 */
	public Integer getIdInternal()
	{
		return idInternal;
	}

	/**
	 * @param idInternal The idInternal to set.
	 */
	public void setIdInternal(Integer idInternal)
	{
		this.idInternal = idInternal;
	}

	/**
	 * @return Returns the keyCostCenter.
	 */
	public Integer getKeyCostCenter()
	{
		return keyCostCenter;
	}

	/**
	 * @param keyCostCenter The keyCostCenter to set.
	 */
	public void setKeyCostCenter(Integer keyCostCenter)
	{
		this.keyCostCenter = keyCostCenter;
	}

	/**
	 * @return Returns the keyProject.
	 */
	public Integer getKeyProject()
	{
		return keyProject;
	}

	/**
	 * @param keyProject The keyProject to set.
	 */
	public void setKeyProject(Integer keyProject)
	{
		this.keyProject = keyProject;
	}

	/**
	 * @return Returns the project.
	 */
	public MWGrantProject getProject()
	{
		return project;
	}

	/**
	 * @param project The project to set.
	 */
	public void setProject(MWGrantProject project)
	{
		this.project = project;
	}

}
