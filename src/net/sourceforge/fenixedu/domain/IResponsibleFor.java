/*
 * Created on 26/Mar/2003
 *
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author João Mota
 * 
 *  
 */
public interface IResponsibleFor extends IDomainObject {
    public ITeacher getTeacher();

    public IExecutionCourse getExecutionCourse();

    public void setTeacher(ITeacher teacher);

    public void setExecutionCourse(IExecutionCourse executionCourse);
}