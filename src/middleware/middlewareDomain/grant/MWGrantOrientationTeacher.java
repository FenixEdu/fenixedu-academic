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
public class MWGrantOrientationTeacher extends MWDomainObject
{
    private Integer idInternal;
    private Date beginDate;
    private Date endDate;
    private MWTeacher teacher;
    private Integer keyTeacher;
    private MWGrantContract contract;
    private Integer keyContract;

	public MWGrantOrientationTeacher()
	{
		super();
	}

	/**
	 * @return Returns the beginDate.
	 */
	public Date getBeginDate()
	{
		return beginDate;
	}

	/**
	 * @param beginDate The beginDate to set.
	 */
	public void setBeginDate(Date beginDate)
	{
		this.beginDate = beginDate;
	}

	/**
	 * @return Returns the contract.
	 */
	public MWGrantContract getContract()
	{
		return contract;
	}

	/**
	 * @param contract The contract to set.
	 */
	public void setContract(MWGrantContract contract)
	{
		this.contract = contract;
	}

	/**
	 * @return Returns the endDate.
	 */
	public Date getEndDate()
	{
		return endDate;
	}

	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
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
	 * @return Returns the keyContract.
	 */
	public Integer getKeyContract()
	{
		return keyContract;
	}

	/**
	 * @param keyContract The keyContract to set.
	 */
	public void setKeyContract(Integer keyContract)
	{
		this.keyContract = keyContract;
	}

	/**
	 * @return Returns the keyTeacher.
	 */
	public Integer getKeyTeacher()
	{
		return keyTeacher;
	}

	/**
	 * @param keyTeacher The keyTeacher to set.
	 */
	public void setKeyTeacher(Integer keyTeacher)
	{
		this.keyTeacher = keyTeacher;
	}

	/**
	 * @return Returns the teacher.
	 */
	public MWTeacher getTeacher()
	{
		return teacher;
	}

	/**
	 * @param teacher The teacher to set.
	 */
	public void setTeacher(MWTeacher teacher)
	{
		this.teacher = teacher;
	}

}
