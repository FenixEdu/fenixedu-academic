/*
 * Created on 31/Jul/2003, 9:20:49
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package Dominio.Seminaries;

import java.util.List;

import Dominio.DomainObject;
import Dominio.ICurricularCourse;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 31/Jul/2003, 9:20:49
 * 
 */
public class CourseEquivalency extends DomainObject implements ICourseEquivalency
{
 
    private ISeminary seminary;
    private ICurricularCourse curricularCourse;
    private IModality modality;
    private List themes;
    
    private Integer seminaryIdInternal;
    private Integer curricularCourseIdInternal;
    private Integer modalityIdInternal;
	/**
	 * @return
	 */
	public ICurricularCourse getCurricularCourse()
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
	public ISeminary getSeminary()
	{
		return seminary;
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
	public void setCurricularCourse(ICurricularCourse course)
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
	 * @param seminary
	 */
	public void setSeminary(ISeminary seminary)
	{
		this.seminary= seminary;
	}

	/**
	 * @param integer
	 */
	public void setSeminaryIdInternal(Integer integer)
	{
		seminaryIdInternal= integer;
	}

	/**
	 * @return
	 */
	public IModality getModality()
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
	 * @param modality
	 */
	public void setModality(IModality modality)
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

}
