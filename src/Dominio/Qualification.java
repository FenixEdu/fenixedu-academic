package Dominio;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class Qualification extends DomainObject implements IQualification
{

	private Integer year;
	private String mark;
	private String school;
	private String title;
	private Integer personKey;

	private IPessoa person;

	public Qualification()
	{
	}

	public String toString()
	{
		String result = "Qualification :\n";
		result += "\n  - Internal Code : " + getIdInternal();
		result += "\n  - Year: " + year;
		result += "\n  - School : " + school;
		result += "\n  - Title : " + title;
		result += "\n  - Mark : " + mark;
		result += "\n  - Person : " + person;

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
	 * @return
	 */
	public Integer getYear()
	{
		return year;
	}

	/**
	 * @param year
	 */
	public void setYear(Integer year)
	{
		this.year = year;
	}

}
