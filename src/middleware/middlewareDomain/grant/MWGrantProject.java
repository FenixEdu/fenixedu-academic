/*
 * Created on Feb 13, 2004
 */
package middleware.middlewareDomain.grant;

import middleware.middlewareDomain.MWDomainObject;

/**
 * @author pica
 * @author barbosa
 */
public class MWGrantProject extends MWDomainObject
{
    private Integer idInternal;
    private Integer number;
    private String designation;
    private MWTeacher teacher;
    private Integer keyTeacher;
    
	public MWGrantProject()
	{
		super();
	}

	/**
	 * @return Returns the designation.
	 */
	public String getDesignation()
	{
		return designation;
	}

	/**
	 * @param designation The designation to set.
	 */
	public void setDesignation(String designation)
	{
		this.designation = designation;
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
	 * @return Returns the number.
	 */
	public Integer getNumber()
	{
		return number;
	}

	/**
	 * @param number The number to set.
	 */
	public void setNumber(Integer number)
	{
		this.number = number;
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
