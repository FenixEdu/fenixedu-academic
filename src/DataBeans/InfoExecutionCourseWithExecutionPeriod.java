/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.IExecutionCourse;

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

    public static InfoExecutionCourse newInfoFromDomain(
            IExecutionCourse executionCourse) {
        InfoExecutionCourseWithExecutionPeriod infoExecutionCourse = null;
        if (executionCourse != null) {
            infoExecutionCourse.copyFromDomain(executionCourse);
        }
        return infoExecutionCourse;
    }
}