/*
 * Created on 5/Ago/2003, 16:08:50
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package DataBeans.Seminaries;
import java.util.List;

import DataBeans.InfoObject;
import Dominio.Seminaries.ICandidacy;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 5/Ago/2003, 16:08:50
 * 
 */
public class InfoCandidacy extends InfoObject
{

	private List caseStudyChoices;
	private String motivation;
	private String seminaryName;
	private Integer themeIdInternal;
	private Integer modalityIdInternal;
	private Integer seminaryIdInternal;
	private Integer studentIdInternal;
	private Integer curricularCourseIdInternal;
	private Boolean approved;
	/**
	 * @return
	 */
	public List getCaseStudyChoices()
	{
		return caseStudyChoices;
	}
	/**
	 * @return
	 */
	public Integer getCurricularCourseIdInternal()
	{
		return curricularCourseIdInternal;
	}
	
	/**
	 * @return
	 */
	public Integer getModalityIdInternal()
	{
		return modalityIdInternal;
	}
	/**
	 * @return
	 */
	public Integer getSeminaryIdInternal()
	{
		return seminaryIdInternal;
	}
	/**
	 * @return
	 */
	public Integer getStudentIdInternal()
	{
		return studentIdInternal;
	}
	/**
	 * @return
	 */
	public Integer getThemeIdInternal()
	{
		return themeIdInternal;
	}
	/**
	 * @param list
	 */
	public void setCaseStudyChoices(List list)
	{
		caseStudyChoices = list;
	}
	/**
	 * @param integer
	 */
	public void setCurricularCourseIdInternal(Integer integer)
	{
		curricularCourseIdInternal = integer;
	}
	
	/**
	 * @param integer
	 */
	public void setModalityIdInternal(Integer integer)
	{
		modalityIdInternal = integer;
	}
	/**
	 * @param integer
	 */
	public void setSeminaryIdInternal(Integer integer)
	{
		seminaryIdInternal = integer;
	}
	/**
	 * @param integer
	 */
	public void setStudentIdInternal(Integer integer)
	{
		studentIdInternal = integer;
	}
	/**
	 * @param integer
	 */
	public void setThemeIdInternal(Integer integer)
	{
		themeIdInternal = integer;
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
		motivation = string;
	}
	public String toString()
	{
		String result = "[InfoCandidacy:";
		result += "Theme=" + this.themeIdInternal + ";";
		result += "IdIntenal=" + super.getIdInternal() + ";";
		result += "Motivation=" + this.motivation + ";";
		result += "Student=" + this.studentIdInternal + ";";
		result += "CaseStudyChoices" + this.caseStudyChoices + ";";
		result += "CurricularCourse=" + this.curricularCourseIdInternal + ";";
		result += "Seminary:=" + this.seminaryIdInternal + ";";
		result += "Modality=" + this.modalityIdInternal + "]";
		return result;
	}
	/**
	 * @return
	 */
	public String getSeminaryName()
	{
		return seminaryName;
	}
	/**
	 * @param string
	 */
	public void setSeminaryName(String string)
	{
		seminaryName = string;
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
		approved = boolean1;
	}
	
    public void copyFromDomain(ICandidacy candidacy) {
        super.copyFromDomain(candidacy);
        if (candidacy != null) {
        	setApproved(candidacy.getApproved());
        	setCurricularCourseIdInternal(candidacy.getCurricularCourseIdInternal());
        	setModalityIdInternal(candidacy.getModalityIdInternal());
        	setMotivation(candidacy.getMotivation());
        	setSeminaryIdInternal(candidacy.getSeminaryIdInternal());
        	setStudentIdInternal(candidacy.getStudentIdInternal());
        	setThemeIdInternal(candidacy.getThemeIdInternal());
        }
    }

    public static InfoCandidacy newInfoFromDomain(ICandidacy candidacy) {
    	InfoCandidacy infoCandidacy = null;
        if (candidacy != null) {
        	infoCandidacy = new InfoCandidacy();
        	infoCandidacy.copyFromDomain(candidacy);
        }
        return infoCandidacy;
    }
}
