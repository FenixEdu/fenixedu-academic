/*
 * Created on 7/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences - Java - Code Generation -
 * Code and Comments
 */
package net.sourceforge.fenixedu.domain.gesdis;


/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class CourseReport extends CourseReport_Base {

    public String toString() {
        String result = "[Dominio.gesdis.CourseReport ";
        result += ", report=" + getReport();
        result += ", executionCourse=" + getExecutionCourse();
        result += "]";
        return result;
    }

}