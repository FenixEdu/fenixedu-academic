/*
 * Created on 26/Mar/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.domain;

import java.util.List;

/**
 * @author João Mota
 * @author jpvl
 */
public interface IProfessorship extends IDomainObject {
    public ITeacher getTeacher();

    public IExecutionCourse getExecutionCourse();

    public Double getHours();

    public void setHours(Double credits);

    public void setTeacher(ITeacher teacher);

    public void setExecutionCourse(IExecutionCourse executionCourse);

    public List getAssociatedShiftProfessorship();

    public void setAssociatedShiftProfessorship(List associatedTeacherShiftPercentage);

    public List getSupportLessons();

    public void setSupportLessons(List supportLessons);

}