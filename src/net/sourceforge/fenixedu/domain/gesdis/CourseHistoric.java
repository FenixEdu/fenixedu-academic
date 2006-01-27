/*
 * Created on 17/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.domain.gesdis;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class CourseHistoric extends CourseHistoric_Base {

    public String toString() {
        String result = "[Dominio.gesdis.CourseHistoric ";
        result += ", enrolled=" + getEnrolled();
        result += ", evaluated=" + getEvaluated();
        result += ", approved=" + getApproved();
        result += ", curricularYear=" + getCurricularYear();
        result += ", semester=" + getSemester();
        result += ", curricularCourse=" + getCurricularCourse();
        result += "]";
        return result;
    }
    
    public void delete(){
        removeCurricularCourse();
        super.deleteDomainObject();
    }
}