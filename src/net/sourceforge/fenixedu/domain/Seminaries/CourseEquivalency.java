/*
 * Created on 31/Jul/2003, 9:20:49
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ICurricularCourse;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 31/Jul/2003, 9:20:49
 *  
 */
public class CourseEquivalency extends CourseEquivalency_Base {

    private ISeminary seminary;

    private ICurricularCourse curricularCourse;

    private IModality modality;

    private List themes;

    /**
     * @return
     */
    public ICurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

    /**
     * @return
     */
    public ISeminary getSeminary() {
        return seminary;
    }

    /**
     * @param course
     */
    public void setCurricularCourse(ICurricularCourse course) {
        curricularCourse = course;
    }

    /**
     * @param seminary
     */
    public void setSeminary(ISeminary seminary) {
        this.seminary = seminary;
    }

    /**
     * @return
     */
    public IModality getModality() {
        return modality;
    }

    /**
     * @param modality
     */
    public void setModality(IModality modality) {
        this.modality = modality;
    }

    /**
     * @return
     */
    public List getThemes() {
        return themes;
    }

    /**
     * @param list
     */
    public void setThemes(List list) {
        themes = list;
    }

}