/*
 * Created on 13/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.teacher;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface ITeachingCareer extends ICareer {

    public ICategory getCategory();

    public String getCourseOrPosition();

    public void setCategory(ICategory category);

    public void setCourseOrPosition(String courseOrPosition);
}