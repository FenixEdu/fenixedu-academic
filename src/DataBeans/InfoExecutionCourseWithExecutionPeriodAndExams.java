/*
 * Created on 14/Jul/2004
 *
 */
package DataBeans;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import Dominio.IExam;
import Dominio.IExecutionCourse;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoExecutionCourseWithExecutionPeriodAndExams extends
        InfoExecutionCourseWithExecutionPeriod {

    public void copyFromDomain(IExecutionCourse executionCourse) {
        super.copyFromDomain(executionCourse);
        if (executionCourse != null) {
            setAssociatedInfoExams(copyIExam2InfoExam(executionCourse.getAssociatedExams()));
        }
    }

    /**
     * @param associatedExams
     * @return
     */
    private List copyIExam2InfoExam(List associatedExams) {
        List associatedInfoExams = (List) CollectionUtils.collect(associatedExams, new Transformer() {

            public Object transform(Object arg0) {
                return InfoExam.newInfoFromDomain((IExam) arg0);
            }
        });
        return associatedInfoExams;
    }

    public static InfoExecutionCourse newInfoFromDomain(IExecutionCourse executionCourse) {
        InfoExecutionCourseWithExecutionPeriodAndExams infoExecutionCourse = null;
        if (executionCourse != null) {
            infoExecutionCourse = new InfoExecutionCourseWithExecutionPeriodAndExams();
            infoExecutionCourse.copyFromDomain(executionCourse);
        }
        return infoExecutionCourse;
    }
}