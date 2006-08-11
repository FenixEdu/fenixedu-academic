/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

/**
 * @author João Mota
 *  
 */
public class InfoExecutionCourseWithExecutionPeriod extends InfoExecutionCourse {

    public void copyFromDomain(ExecutionCourse executionCourse) {
        super.copyFromDomain(executionCourse);
        if (executionCourse != null) {
            setInfoExecutionPeriod(InfoExecutionPeriod
                    .newInfoFromDomain(executionCourse.getExecutionPeriod()));
        }
    }

    public static InfoExecutionCourse newInfoFromDomain(ExecutionCourse executionCourse) {
        InfoExecutionCourseWithExecutionPeriod infoExecutionCourse = null;
        if (executionCourse != null) {
            infoExecutionCourse = new InfoExecutionCourseWithExecutionPeriod();
            infoExecutionCourse.copyFromDomain(executionCourse);
        }
        return infoExecutionCourse;
    }
}