/*
 * Created on 31/Jul/2003, 9:23:48
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package Dominio.Seminaries;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IDomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 31/Jul/2003, 9:23:47
 * 
 */
public interface ICourseEquivalency extends IDomainObject
{
	/**
	 * @return
	 */
	public abstract ICurricularCourse getCurricularCourse();
	/**
	 * @return
	 */
	public abstract Integer getCurricularCourseIdInternal();
	
	/**
	 * @return
	 */
	public abstract ISeminary getSeminary();
	/**
	 * @return
	 */
	public abstract Integer getSeminaryIdInternal();
	/**
	 * @param course
	 */
	public abstract void setCurricularCourse(ICurricularCourse course);
	/**
	 * @param integer
	 */
	public abstract void setCurricularCourseIdInternal(Integer integer);
	
	/**
	 * @param seminary
	 */
	public abstract void setSeminary(ISeminary seminary);
	/**
	 * @param integer
	 */
	public abstract void setSeminaryIdInternal(Integer integer);
	public IModality getModality();
	/**
	 * @return
	 */
	public Integer getModalityIdInternal();
	/**
	 * @param modality
	 */
	public void setModality(IModality modality);
	/**
	 * @param integer
	 */
	public void setModalityIdInternal(Integer integer);
    
    public List getThemes();

    /**
     * @param list
     */
    public void setThemes(List list);
}