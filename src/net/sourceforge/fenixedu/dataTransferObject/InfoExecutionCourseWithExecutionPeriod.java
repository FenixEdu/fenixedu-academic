/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IExecutionCourse;

/**
 * @author João Mota
 *  
 */
public class InfoExecutionCourseWithExecutionPeriod extends InfoExecutionCourse {

    public void copyFromDomain(IExecutionCourse executionCourse) {
        super.copyFromDomain(executionCourse);
        if (executionCourse != null) {
            setInfoExecutionPeriod(InfoExecutionPeriodWithInfoExecutionYear
                    .newInfoFromDomain(executionCourse.getExecutionPeriod()));
        }
    }

    public static InfoExecutionCourse newInfoFromDomain(IExecutionCourse executionCourse) {
        InfoExecutionCourseWithExecutionPeriod infoExecutionCourse = null;
        if (executionCourse != null) {
            infoExecutionCourse = new InfoExecutionCourseWithExecutionPeriod();
            infoExecutionCourse.copyFromDomain(executionCourse);
        }
        return infoExecutionCourse;
    }
}