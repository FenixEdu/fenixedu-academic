/*
 * Created on Feb 13, 2004
 */
package middleware.middlewareDomain.grant;

import middleware.middlewareDomain.MWDomainObject;

/**
 * @author pica
 * @author barbosa
 */
public class MWGrantCourse extends MWDomainObject
{
    private Integer idInternal;
    private Integer courseCode;
    private String sigla;
    private String completeName;
    private String shortName;
    private String area;
    
	public MWGrantCourse()
	{
		super();
	}

	/**
	 * @return Returns the area.
	 */
	public String getArea()
	{
		return area;
	}

	/**
	 * @param area The area to set.
	 */
	public void setArea(String area)
	{
		this.area = area;
	}

	/**
	 * @return Returns the completeName.
	 */
	public String getCompleteName()
	{
		return completeName;
	}

	/**
	 * @param completeName The completeName to set.
	 */
	public void setCompleteName(String completeName)
	{
		this.completeName = completeName;
	}

	/**
	 * @return Returns the courseCode.
	 */
	public Integer getCourseCode()
	{
		return courseCode;
	}

	/**
	 * @param courseCode The courseCode to set.
	 */
	public void setCourseCode(Integer courseCode)
	{
		this.courseCode = courseCode;
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
	 * @return Returns the shortName.
	 */
	public String getShortName()
	{
		return shortName;
	}

	/**
	 * @param shortName The shortName to set.
	 */
	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}

	/**
	 * @return Returns the sigla.
	 */
	public String getSigla()
	{
		return sigla;
	}

	/**
	 * @param sigla The sigla to set.
	 */
	public void setSigla(String sigla)
	{
		this.sigla = sigla;
	}

}
