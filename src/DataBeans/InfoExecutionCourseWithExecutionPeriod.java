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

    public static InfoExecutionCourse copyFromDomain(
            IExecutionCourse executionCourse) {
        InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.copyFromDomain(executionCourse);
        if (infoExecutionCourse != null) {            
            infoExecutionCourse
                    .setInfoExecutionPeriod(InfoExecutionPeriodWithInfoExecutionYear
                            .copyFromDomain(executionCourse
                                    .getExecutionPeriod()));
        }
        return infoExecutionCourse;
    }

}