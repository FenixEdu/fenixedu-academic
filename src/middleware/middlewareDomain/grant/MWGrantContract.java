/*
 * Created on Feb 13, 2004
 */
package middleware.middlewareDomain.grant;

import java.util.Date;

import middleware.middlewareDomain.MWDomainObject;

/**
 * @author pica
 * @author barbosa
 */
public class MWGrantContract extends MWDomainObject
{
    private Integer idInternal;
    private Integer contractNumber;
    private Date dateBeginContract;
    private Date dateEndContract;
    private String endContractMotive;

    private MWGrantOwner grantOwner;
    private Integer keyGrantOwner;
    private MWGrantType grantType;
    private Integer keyGrantType;
    private MWGrantPaymentEntity paymentEntity;
    private Integer keyPaymentEntity;
    private MWGrantSubsidy subsidy;
    private Integer keyGrantSubsidy;
    private MWGrantOrientationTeacher orientationTeacher;
    private Integer keyGrantOrientationTeacher;

	public MWGrantContract()
	{
		super();
	}

	/**
	 * @return Returns the contractNumber.
	 */
	public Integer getContractNumber()
	{
		return contractNumber;
	}

	/**
	 * @param contractNumber The contractNumber to set.
	 */
	public void setContractNumber(Integer contractNumber)
	{
		this.contractNumber = contractNumber;
	}

	/**
	 * @return Returns the dateBeginContract.
	 */
	public Date getDateBeginContract()
	{
		return dateBeginContract;
	}

	/**
	 * @param dateBeginContract The dateBeginContract to set.
	 */
	public void setDateBeginContract(Date dateBeginContract)
	{
		this.dateBeginContract = dateBeginContract;
	}

	/**
	 * @return Returns the dateEndContract.
	 */
	public Date getDateEndContract()
	{
		return dateEndContract;
	}

	/**
	 * @param dateEndContract The dateEndContract to set.
	 */
	public void setDateEndContract(Date dateEndContract)
	{
		this.dateEndContract = dateEndContract;
	}

	/**
	 * @return Returns the endContractMotive.
	 */
	public String getEndContractMotive()
	{
		return endContractMotive;
	}

	/**
	 * @param endContractMotive The endContractMotive to set.
	 */
	public void setEndContractMotive(String endContractMotive)
	{
		this.endContractMotive = endContractMotive;
	}

	/**
	 * @return Returns the grantOwner.
	 */
	public MWGrantOwner getGrantOwner()
	{
		return grantOwner;
	}

	/**
	 * @param grantOwner The grantOwner to set.
	 */
	public void setGrantOwner(MWGrantOwner grantOwner)
	{
		this.grantOwner = grantOwner;
	}

	/**
	 * @return Returns the grantType.
	 */
	public MWGrantType getGrantType()
	{
		return grantType;
	}

	/**
	 * @param grantType The grantType to set.
	 */
	public void setGrantType(MWGrantType grantType)
	{
		this.grantType = grantType;
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
	 * @return Returns the keyGrantOrientationTeacher.
	 */
	public Integer getKeyGrantOrientationTeacher()
	{
		return keyGrantOrientationTeacher;
	}

	/**
	 * @param keyGrantOrientationTeacher The keyGrantOrientationTeacher to set.
	 */
	public void setKeyGrantOrientationTeacher(Integer keyGrantOrientationTeacher)
	{
		this.keyGrantOrientationTeacher = keyGrantOrientationTeacher;
	}

	/**
	 * @return Returns the keyGrantOwner.
	 */
	public Integer getKeyGrantOwner()
	{
		return keyGrantOwner;
	}

	/**
	 * @param keyGrantOwner The keyGrantOwner to set.
	 */
	public void setKeyGrantOwner(Integer keyGrantOwner)
	{
		this.keyGrantOwner = keyGrantOwner;
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
	 * @return Returns the keyGrantType.
	 */
	public Integer getKeyGrantType()
	{
		return keyGrantType;
	}

	/**
	 * @param keyGrantType The keyGrantType to set.
	 */
	public void setKeyGrantType(Integer keyGrantType)
	{
		this.keyGrantType = keyGrantType;
	}

	/**
	 * @return Returns the keyPaymentEntity.
	 */
	public Integer getKeyPaymentEntity()
	{
		return keyPaymentEntity;
	}

	/**
	 * @param keyPaymentEntity The keyPaymentEntity to set.
	 */
	public void setKeyPaymentEntity(Integer keyPaymentEntity)
	{
		this.keyPaymentEntity = keyPaymentEntity;
	}

	/**
	 * @return Returns the orientationTeacher.
	 */
	public MWGrantOrientationTeacher getOrientationTeacher()
	{
		return orientationTeacher;
	}

	/**
	 * @param orientationTeacher The orientationTeacher to set.
	 */
	public void setOrientationTeacher(MWGrantOrientationTeacher orientationTeacher)
	{
		this.orientationTeacher = orientationTeacher;
	}

	/**
	 * @return Returns the paymentEntity.
	 */
	public MWGrantPaymentEntity getPaymentEntity()
	{
		return paymentEntity;
	}

	/**
	 * @param paymentEntity The paymentEntity to set.
	 */
	public void setPaymentEntity(MWGrantPaymentEntity paymentEntity)
	{
		this.paymentEntity = paymentEntity;
	}

	/**
	 * @return Returns the subsidy.
	 */
	public MWGrantSubsidy getSubsidy()
	{
		return subsidy;
	}

	/**
	 * @param subsidy The subsidy to set.
	 */
	public void setSubsidy(MWGrantSubsidy subsidy)
	{
		this.subsidy = subsidy;
	}

}
