/*
 * Created on 14/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoExecutionCourseWithExecutionPeriodAndExams extends
        InfoExecutionCourseWithExecutionPeriod {

    public void copyFromDomain(ExecutionCourse executionCourse) {
        super.copyFromDomain(executionCourse);
        if (executionCourse != null) {
            List<Exam> associatedExams = new ArrayList();
            List<Evaluation> associatedEvaluations = executionCourse.getAssociatedEvaluations();
            for(Evaluation evaluation : associatedEvaluations){
                if (evaluation instanceof Exam){
                    associatedExams.add((Exam) evaluation);
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
            if (object != null && object instanceof Exam) {
                associatedInfoExams.add(InfoExam.newInfoFromDomain((Exam) object));
            }
        }
        return associatedInfoExams;
    }

    public static InfoExecutionCourse newInfoFromDomain(ExecutionCourse executionCourse) {
        InfoExecutionCourseWithExecutionPeriodAndExams infoExecutionCourse = null;
        if (executionCourse != null) {
            infoExecutionCourse = new InfoExecutionCourseWithExecutionPeriodAndExams();
            infoExecutionCourse.copyFromDomain(executionCourse);
        }
        return infoExecutionCourse;
    }
}