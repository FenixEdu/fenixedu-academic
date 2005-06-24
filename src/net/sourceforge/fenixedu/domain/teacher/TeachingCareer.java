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
public class TeachingCareer extends TeachingCareer_Base {

    public TeachingCareer() {
        super();
        setOjbConcreteClass(TeachingCareer.class.getName());
    }
    
    public String toString() {
        String result = "[Dominio.teacher.TeachingCareer ";
        result += ", beginYear=" + getBeginYear();
        result += ", endYear=" + getEndYear();
        result += ", category=" + getCategory();
        result += ", courseOrPosition=" + getCourseOrPosition();
        result += ", teacher=" + getTeacher();
        result += "]";
        return result;
    }

}
