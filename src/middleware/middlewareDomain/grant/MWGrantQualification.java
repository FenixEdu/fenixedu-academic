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
public class MWGrantQualification extends MWDomainObject
{
    private Integer idInternal;
    private Date date;
    private String mark;
    private String branch;
    private String specializationArea;
    private String school;
    private String degreeRecognition;
    private Date equivalenceDate;
    private String equivalenceSchool;
    
    private MWGrantCourse title;
    private Integer keyTitle;
    private MWGrantDegree degree;
    private Integer keyDegree;
    
    private MWPerson person;
    private Integer keyPerson;
    
	public MWGrantQualification()
	{
		super();
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
	public void setBranch(String branch)
	{
		this.branch = branch;
	}

	/**
	 * @return Returns the date.
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * @param date The date to set.
	 */
	public void setDate(Date date)
	{
		this.date = date;
	}

	/**
	 * @return Returns the degree.
	 */
	public MWGrantDegree getDegree()
	{
		return degree;
	}

	/**
	 * @param degree The degree to set.
	 */
	public void setDegree(MWGrantDegree degree)
	{
		this.degree = degree;
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
	 * @return Returns the keyDegree.
	 */
	public Integer getKeyDegree()
	{
		return keyDegree;
	}

	/**
	 * @param keyDegree The keyDegree to set.
	 */
	public void setKeyDegree(Integer keyDegree)
	{
		this.keyDegree = keyDegree;
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
	 * @return Returns the keyTitle.
	 */
	public Integer getKeyTitle()
	{
		return keyTitle;
	}

	/**
	 * @param keyTitle The keyTitle to set.
	 */
	public void setKeyTitle(Integer keyTitle)
	{
		this.keyTitle = keyTitle;
	}

	/**
	 * @return Returns the mark.
	 */
	public String getMark()
	{
		return mark;
	}

	/**
	 * @param mark The mark to set.
	 */
	public void setMark(String mark)
	{
		this.mark = mark;
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

	/**
	 * @return Returns the school.
	 */
	public String getSchool()
	{
		return school;
	}

	/**
	 * @param school The school to set.
	 */
	public void setSchool(String school)
	{
		this.school = school;
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
	 * @return Returns the title.
	 */
	public MWGrantCourse getTitle()
	{
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(MWGrantCourse title)
	{
		this.title = title;
	}

}
