/*
 * Created on Feb 13, 2004
 */
package middleware.middlewareDomain.grant;

import middleware.middlewareDomain.MWDomainObject;

/**
 * @author pica
 * @author barbosa
 */
public class MWGrantPart extends MWDomainObject
{
    private Integer idInternal;
    private Integer percentage;
    private MWGrantSubsidy grantSubsidy;
    private MWGrantPaymentEntity grantPaymentEntity;
    private MWTeacher responsibleTeacher;
    private Integer keyGrantSubsidy;
    private Integer keyGrantPaymentEntity;
    private Integer keyResponsibleTeacher;
    
	public MWGrantPart()
	{
		super();
	}

	/**
	 * @return Returns the grantPaymentEntity.
	 */
	public MWGrantPaymentEntity getGrantPaymentEntity()
	{
		return grantPaymentEntity;
	}

	/**
	 * @param grantPaymentEntity The grantPaymentEntity to set.
	 */
	public void setGrantPaymentEntity(MWGrantPaymentEntity grantPaymentEntity)
	{
		this.grantPaymentEntity = grantPaymentEntity;
	}

	/**
	 * @return Returns the grantSubsidy.
	 */
	public MWGrantSubsidy getGrantSubsidy()
	{
		return grantSubsidy;
	}

	/**
	 * @param grantSubsidy The grantSubsidy to set.
	 */
	public void setGrantSubsidy(MWGrantSubsidy grantSubsidy)
	{
		this.grantSubsidy = grantSubsidy;
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
	 * @return Returns the keyGrantPaymentEntity.
	 */
	public Integer getKeyGrantPaymentEntity()
	{
		return keyGrantPaymentEntity;
	}

	/**
	 * @param keyGrantPaymentEntity The keyGrantPaymentEntity to set.
	 */
	public void setKeyGrantPaymentEntity(Integer keyGrantPaymentEntity)
	{
		this.keyGrantPaymentEntity = keyGrantPaymentEntity;
	}

	/**
	 * @return Returns the keyGrantSubsidy.
	 */
	public Integer getKeyGrantSubsidy()
	{
		return keyGrantSubsidy;
	}

	/**
	 * @param keyGrantSubsidy The keyGrantSubsidy to set.
	 */
	public void setKeyGrantSubsidy(Integer keyGrantSubsidy)
	{
		this.keyGrantSubsidy = keyGrantSubsidy;
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

	/**
	 * @return Returns the percentage.
	 */
	public Integer getPercentage()
	{
		return percentage;
	}

	/**
	 * @param percentage The percentage to set.
	 */
	public void setPercentage(Integer percentage)
	{
		this.percentage = percentage;
	}

	/**
	 * @return Returns the responsibleTeacher.
	 */
	public MWTeacher getResponsibleTeacher()
	{
		return responsibleTeacher;
	}

	/**
	 * @param responsibleTeacher The responsibleTeacher to set.
	 */
	public void setResponsibleTeacher(MWTeacher responsibleTeacher)
	{
		this.responsibleTeacher = responsibleTeacher;
	}

}
