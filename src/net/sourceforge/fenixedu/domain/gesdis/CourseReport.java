/*
 * Created on 7/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences - Java - Code Generation -
 * Code and Comments
 */
package net.sourceforge.fenixedu.domain.gesdis;

import net.sourceforge.fenixedu.domain.IExecutionCourse;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class CourseReport extends CourseReport_Base {
    private IExecutionCourse executionCourse;

    public IExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public void setExecutionCourse(IExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    public String toString() {
        String result = "[Dominio.gesdis.CourseReport ";
        result += ", report=" + getReport();
        result += ", executionCourse=" + getExecutionCourse();
        result += "]";
        return result;
    }

}