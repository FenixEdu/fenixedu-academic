/*
 * Created on 17/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.domain.gesdis;

import net.sourceforge.fenixedu.domain.ICurricularCourse;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class CourseHistoric extends CourseHistoric_Base {

    private ICurricularCourse curricularCourse;

    /**
     * @return Returns the curricularCourse.
     */
    public ICurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

    /**
     * @param curricularCourse
     *            The curricularCourse to set.
     */
    public void setCurricularCourse(ICurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

    public String toString() {
        String result = "[Dominio.gesdis.CourseHistoric ";
        result += ", enrolled=" + getEnrolled();
        result += ", evaluated=" + getEvaluated();
        result += ", approved=" + getApproved();
        result += ", curricularYear=" + getCurricularYear();
        result += ", semester=" + getEnrolled();
        result += ", curricularCourse=" + getCurricularCourse();
        result += "]";
        return result;
    }
}