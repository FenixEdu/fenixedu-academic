/*
 * Created on 1/Ago/2003, 21:13:05
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package DataBeans.Seminaries;

import java.util.List;

import DataBeans.InfoCurricularCourse;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 1/Ago/2003, 21:13:05
 * 
 */
public class InfoEquivalency
{
    private Integer idInternal;
    private InfoCurricularCourse curricularCourse;
    private InfoModality modality;
    private String seminaryName;
    private Boolean hasTheme;
    private Boolean hasCaseStudy;
    private List themes;
    
    private Integer seminaryIdInternal;
    private Integer curricularCourseIdInternal;
    private Integer modalityIdInternal;
	/**
	 * @return
	 */
	public InfoCurricularCourse getCurricularCourse()
	{
		return curricularCourse;
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
	public Integer getIdInternal()
	{
		return idInternal;
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
	 * @param course
	 */
	public void setCurricularCourse(InfoCurricularCourse course)
	{
		curricularCourse= course;
	}

	/**
	 * @param integer
	 */
	public void setCurricularCourseIdInternal(Integer integer)
	{
		curricularCourseIdInternal= integer;
	}

	/**
	 * @param integer
	 */
	public void setIdInternal(Integer integer)
	{
		idInternal= integer;
	}

	/**
	 * @param modality
	 */
	public void setModality(InfoModality modality)
	{
		this.modality= modality;
	}

	/**
	 * @param integer
	 */
	public void setModalityIdInternal(Integer integer)
	{
		modalityIdInternal= integer;
	}

	/**
	 * @param integer
	 */
	public void setSeminaryIdInternal(Integer integer)
	{
		seminaryIdInternal= integer;
	}
    
    public String toString()
    {
        String result = "[InfoEquivalency:";
        result += "IdInternal: "+this.getIdInternal() +";";
        result += "CurricularCourse" + this.getCurricularCourse() +";";
        result += "CurricularCourseInternal " + this.getCurricularCourseIdInternal() +";";
        result += "Seminary:" + this.getSeminaryIdInternal() +";";
        result += "Modality:" + this.getModality()+"]";
        return result;
    }

	/**
	 * @return
	 */
	public List getThemes()
	{
		return themes;
	}

	/**
	 * @param list
	 */
	public void setThemes(List list)
	{
		themes= list;
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
		seminaryName= string;
	}

	/**
	 * @return
	 */
	public Boolean getHasCaseStudy()
	{
		return hasCaseStudy;
	}

	/**
	 * @return
	 */
	public Boolean getHasTheme()
	{
		return hasTheme;
	}

	/**
	 * @param boolean1
	 */
	public void setHasCaseStudy(Boolean boolean1)
	{
		hasCaseStudy = boolean1;
	}

	/**
	 * @param boolean1
	 */
	public void setHasTheme(Boolean boolean1)
	{
		hasTheme = boolean1;
	}

}
