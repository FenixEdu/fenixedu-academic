/*
 * Created on 05/Nov/2003
 *  
 */
package DataBeans.person;

import DataBeans.ISiteComponent;
import DataBeans.InfoObject;
import DataBeans.InfoPerson;

/**
 * @author Barbosa
 * @author Pica
 */

public class InfoQualification extends InfoObject implements ISiteComponent
{
	private Integer year;
	private String mark;
	private String school;
	private String title;
    private String degree;
	private InfoPerson infoPerson;

	public InfoQualification()
	{
	}

	public boolean equals(Object obj)
	{
		boolean result = false;
		if (obj != null && obj instanceof InfoQualification)
		{
			result =
				getMark().equals(((InfoQualification) obj).getDegree())
					&& getSchool().equals(((InfoQualification) obj).getSchool())
					&& getYear().equals(((InfoQualification) obj).getYear())
					&& getInfoPerson().equals(((InfoQualification) obj).getInfoPerson());
		}
		return result;
	}

	/**
	 * @return InfoPerson
	 */
	public InfoPerson getInfoPerson()
	{
		return infoPerson;
	}

	/**
	 * @return Integer
	 */
	public Integer getYear()
	{
		return year;
	}

	/**
	 * @return String
	 */
	public String getMark()
	{
		return mark;
	}

	/**
	 * @return String
	 */
	public String getSchool()
	{
		return school;
	}

	/**
	 * @return String
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Sets the personInfo.
	 * 
	 * @param infoPerson
	 *                    The personInfo to set
	 */
	public void setInfoPerson(InfoPerson infoPerson)
	{
		this.infoPerson = infoPerson;
	}

	/**
	 * Sets the mark of the qualification
	 * 
	 * @param mark.
	 */
	public void setMark(String mark)
	{
		this.mark = mark;
	}

	/**
	 * Sets the qualification year
	 * 
	 * @param year
	 *                    The Year to set
	 */
	public void setYear(Integer year)
	{
		this.year = year;
	}

	/**
	 * Sets the school of qualification
	 * 
	 * @param school;
	 *                    The school to set
	 */
	public void setSchool(String school)
	{
		this.school = school;
	}

	/**
	 * Sets the title of qualification
	 * 
	 * @param title;
	 *                    The title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
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

}