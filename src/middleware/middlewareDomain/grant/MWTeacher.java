/*
 * Created on Feb 13, 2004
 */
package middleware.middlewareDomain.grant;

import middleware.middlewareDomain.MWDomainObject;

/**
 * @author pica
 * @author barbosa
 */
public class MWTeacher extends MWDomainObject
{
    private Integer idInternal;
    private Integer number;
    private Integer keyDepartment; //Nao esta a ser utilizado
    private String degree;
    private String category;
    private MWPerson person;
    private Integer keyPerson;
    
	public MWTeacher()
	{
		super();
	}

	/**
	 * @return Returns the category.
	 */
	public String getCategory()
	{
		return category;
	}

	/**
	 * @param category The category to set.
	 */
	public void setCategory(String category)
	{
		this.category = category;
	}

	/**
	 * @return Returns the degree.
	 */
	public String getDegree()
	{
		return degree;
	}

	/**
	 * @param degree The degree to set.
	 */
	public void setDegree(String degree)
	{
		this.degree = degree;
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
	 * @return Returns the keyDepartment.
	 */
	public Integer getKeyDepartment()
	{
		return keyDepartment;
	}

	/**
	 * @param keyDepartment The keyDepartment to set.
	 */
	public void setKeyDepartment(Integer keyDepartment)
	{
		this.keyDepartment = keyDepartment;
	}

	/**
	 * @return Returns the keyPerson.
	 */
	public Integer getKeyPerson()
	{
		return keyPerson;
	}

	/**
	 * @param keyPerson The keyPerson to set.
	 */
	public void setKeyPerson(Integer keyPerson)
	{
		this.keyPerson = keyPerson;
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
	 * @return Returns the person.
	 */
	public MWPerson getPerson()
	{
		return person;
	}

	/**
	 * @param person The person to set.
	 */
	public void setPerson(MWPerson person)
	{
		this.person = person;
	}

}
