/*
 * Created on 1/Set/2003, 15:30:27
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package DataBeans.Seminaries;

import java.util.List;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoStudent;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 1/Set/2003, 15:30:27
 * 
 */
public class InfoCandidacyDetails
{
    private InfoStudent student;
    private List cases;
    private InfoTheme theme;
    private InfoModality modality;
    private InfoSeminary seminary;  
    private String motivation; 
    private InfoCurricularCourse curricularCourse;
    private Integer idInternal;
    private Double classification;
    private Integer completedCourses;
    private Boolean approved;
    private InfoClassification infoClassification;

	/**
	 * @return
	 */
	public List getCases()
	{
		return cases;
	}

	/**
	 * @return
	 */
	public InfoModality getModality()
	{
		return modality;
	}

	/**
	 * @return
	 */
	public InfoSeminary getSeminary()
	{
		return seminary;
	}

	/**
	 * @return
	 */
	public InfoStudent getStudent()
	{
		return student;
	}

	/**
	 * @return
	 */
	public InfoTheme getTheme()
	{
		return theme;
	}

	/**
	 * @param list
	 */
	public void setCases(List list)
	{
		cases= list;
	}

	/**
	 * @param modality
	 */
	public void setModality(InfoModality modality)
	{
		this.modality= modality;
	}

	/**
	 * @param seminary
	 */
	public void setSeminary(InfoSeminary seminary)
	{
		this.seminary= seminary;
	}

	/**
	 * @param student
	 */
	public void setStudent(InfoStudent student)
	{
		this.student= student;
	}

	/**
	 * @param theme
	 */
	public void setTheme(InfoTheme theme)
	{
		this.theme= theme;
	}

	/**
	 * @return
	 */
	public String getMotivation()
	{
		return motivation;
	}

	/**
	 * @param string
	 */
	public void setMotivation(String string)
	{
		motivation= string;
	}

	/**
	 * @return
	 */
	public InfoCurricularCourse getCurricularCourse()
	{
		return curricularCourse;
	}

	/**
	 * @param course
	 */
	public void setCurricularCourse(InfoCurricularCourse course)
	{
		curricularCourse= course;
	}

	/**
	 * @return
	 */
	public Integer getIdInternal()
	{
		return idInternal;
	}

	/**
	 * @param integer
	 */
	public void setIdInternal(Integer integer)
	{
		idInternal= integer;
	}

	/**
	 * @return
	 * @deprecated
	 */
	public Double getClassification()
	{
		return classification;
	}

	/**
	 * @return
	 *  @deprecated
	 */
	public Integer getCompletedCourses()
	{
		return completedCourses;
	}

	/**
	 * @param integer
	 *  @deprecated
	 */
	public void setClassification(Double integer)
	{
		classification= integer;
	}

	/**
	 * @param integer
	 *  @deprecated
	 */
	public void setCompletedCourses(Integer integer)
	{
		completedCourses= integer;
	}

	/**
	 * @return
	 */
	public Boolean getApproved()
	{
		return approved;
	}

	/**
	 * @param boolean1
	 */
	public void setApproved(Boolean boolean1)
	{
		approved= boolean1;
	}

	/**
	 * @return
	 */
	public InfoClassification getInfoClassification()
	{
		return infoClassification;
	}

	/**
	 * @param classification
	 */
	public void setInfoClassification(InfoClassification classification)
	{
		infoClassification = classification;
	}

}
