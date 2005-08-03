/*
 * Created on 14/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.IEvaluation;
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
            List<IExam> associatedExams = new ArrayList();
            List<IEvaluation> associatedEvaluations = executionCourse.getAssociatedEvaluations();
            for(IEvaluation evaluation : associatedEvaluations){
                if (evaluation instanceof Exam){
                    associatedExams.add((IExam) evaluation);
                }
            }
            setAssociatedInfoExams(copyIExam2InfoExam(associatedExams));
        }
    }

    /**
     * @param associatedExams
     * @return
     */
    private List copyIExam2InfoExam(List associatedExams) {
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