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
public interface ICourseEquivalency extends IDomainObject {
    /**
     * @return
     */
    public abstract ICurricularCourse getCurricularCourse();

    /**
     * @return
     */
    public abstract ISeminary getSeminary();

    /**
     * @param course
     */
    public abstract void setCurricularCourse(ICurricularCourse course);

    /**
     * @param seminary
     */
    public abstract void setSeminary(ISeminary seminary);

    public IModality getModality();

    /**
     * @param modality
     */
    public void setModality(IModality modality);

    public List getThemes();

    /**
     * @param list
     */
    public void setThemes(List list);
}