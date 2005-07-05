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
public class StudentCourseReport extends StudentCourseReport_Base {

    public boolean equals(Object obj) {
        if (obj instanceof IStudentCourseReport) {
            final IStudentCourseReport studentCourseReport = (IStudentCourseReport) obj;
            return this.getIdInternal().equals(studentCourseReport.getIdInternal());
        }
        return false;
    }
}