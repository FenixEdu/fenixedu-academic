package Dominio;

import java.util.Date;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class Qualification extends DomainObject implements IQualification
{

	private String mark;
	private String school;
	private String title;
    private String degree;
    private Date lastModificationDate;
	private Integer personKey;
    private Date date;
	private String branch;
    private String specializationArea;
    private String degreeRecognition;
    private Date equivalenceDate;
    private String equivalenceSchool;
    private ICountry country;
    private Integer countryKey;

	private IPessoa person;

	public Qualification()
	{
	}

	public String toString()
	{
		String result = "Qualification :\n";
		result += "\n  - Internal Code : " + getIdInternal();
		result += "\n  - School : " + school;
		result += "\n  - Title : " + title;
		result += "\n  - Mark : " + mark;
		result += "\n  - Person : " + person;
        result += "\n  - Last Modication Date : " + lastModificationDate;
        result += "\n  - Branch : " + branch;
        result += "\n  - Specialization Area : " + specializationArea;
        result += "\n  - Degree Recognition : " + degreeRecognition;
        result += "\n  - Equivalence School : " + equivalenceSchool;
        result += "\n  - Equivalence Date : " +  equivalenceDate;
        result += "\n  - Country : " + country.getName();
        
		return result;
	}

	/**
	 * @return
	 */
	public String getMark()
	{
		return mark;
	}

	/**
	 * @param mark
	 */
	public void setMark(String mark)
	{
		this.mark = mark;
	}

	/**
	 * @return
	 */
	public IPessoa getPerson()
	{
		return person;
	}

	/**
	 * @param person
	 */
	public void setPerson(IPessoa person)
	{
		this.person = person;
	}

	/**
	 * @return
	 */
	public Integer getPersonKey()
	{
		return personKey;
	}

	/**
	 * @param personKey
	 */
	public void setPersonKey(Integer personKey)
	{
		this.personKey = personKey;
	}

	/**
	 * @return
	 */
	public String getSchool()
	{
		return school;
	}

	/**
	 * @param school
	 */
	public void setSchool(String school)
	{
		this.school = school;
	}

	/**
	 * @return
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
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

    /* (non-Javadoc)
     * @see Dominio.IQualification#SetLastModificationDate(java.util.Date)
     */
    public void setLastModificationDate(Date lastModificationDate)
    {
        this.lastModificationDate = lastModificationDate;
    }

    /* (non-Javadoc)
     * @see Dominio.IQualification#getLastModificationDate()
     */
    public Date getLastModificationDate()
    {
        return lastModificationDate;
    }
	/**
	 * @return Returns the branch.
	 */
	public String getBranch()
	{
		return branch;
	}

	/**
	 * @param branch The branch to set.
	 */
	public void setBranch(String Branch)
	{
		branch = Branch;
	}

	/**
	 * @return Returns the degreeRecognition.
	 */
	public String getDegreeRecognition()
	{
		return degreeRecognition;
	}

	/**
	 * @param degreeRecognition The degreeRecognition to set.
	 */
	public void setDegreeRecognition(String degreeRecognition)
	{
		this.degreeRecognition = degreeRecognition;
	}

	/**
	 * @return Returns the equivalenceDate.
	 */
	public Date getEquivalenceDate()
	{
		return equivalenceDate;
	}

	/**
	 * @param equivalenceDate The equivalenceDate to set.
	 */
	public void setEquivalenceDate(Date equivalenceDate)
	{
		this.equivalenceDate = equivalenceDate;
	}

	/**
	 * @return Returns the equivalenceSchool.
	 */
	public String getEquivalenceSchool()
	{
		return equivalenceSchool;
	}

	/**
	 * @param equivalenceSchool The equivalenceSchool to set.
	 */
	public void setEquivalenceSchool(String equivalenceSchool)
	{
		this.equivalenceSchool = equivalenceSchool;
	}

	/**
	 * @return Returns the specializationArea.
	 */
	public String getSpecializationArea()
	{
		return specializationArea;
	}

	/**
	 * @param specializationArea The specializationArea to set.
	 */
	public void setSpecializationArea(String specializationArea)
	{
		this.specializationArea = specializationArea;
	}

	/**
	 * @return Returns the qualificationDate.
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * @param qualificationDate The qualificationDate to set.
	 */
	public void setDate(Date date)
	{
		this.date = date;
	}

	/**
	 * @param country The country to set.
	 */
	public void setCountry(ICountry country)
	{
		this.country = country;
	}

	/**
	 * @return Returns the country.
	 */
	public ICountry getCountry()
	{
		return country;
	}

	/**
	 * @return Returns the countryKey.
	 */
	public Integer getCountryKey()
	{
		return countryKey;
	}

	/**
	 * @param countryKey The countryKey to set.
	 */
	public void setCountryKey(Integer countryKey)
	{
		this.countryKey = countryKey;
	}

}
