/*
 * Created on 14/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;

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
//        List associatedInfoExams = (List) CollectionUtils.collect(associatedExams, new Transformer() {
//
//            public Object transform(Object arg0) {
//                return InfoExam.newInfoFromDomain((IExam) arg0);
//            }
//        });
        List associatedInfoExams = new ArrayList(associatedExams.size());
        for (Iterator iterator = associatedExams.iterator(); iterator.hasNext(); ) {
            Object object = iterator.next();
            if (object != null && object instanceof IExam) {
                associatedInfoExams.add(InfoExam.newInfoFromDomain((IExam) object));
            }
        }
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