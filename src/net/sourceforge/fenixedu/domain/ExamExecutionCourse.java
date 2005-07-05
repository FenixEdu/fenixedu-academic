/*
 * Created on 29/Mar/2003
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ExamExecutionCourse extends ExamExecutionCourse_Base {

    public ExamExecutionCourse() {
    }

    public ExamExecutionCourse(IExam exam, IExecutionCourse executionCourse) {
        this.setExam(exam);
        this.setExecutionCourse(executionCourse);
    }

    public boolean equals(Object obj) {
        if (obj instanceof IExamExecutionCourse) {
            final IExamExecutionCourse examExecutionCourse = (IExamExecutionCourse) obj;
            return this.getIdInternal().equals(examExecutionCourse.getIdInternal());
        }
        return false;
    }

    public String toString() {
        return "[EXAM_EXECUTIONCOURSE:" + " exam= '" + this.getExam() + "'" + " execution_course= '"
                + this.getExecutionCourse() + "'" + "]";
    }

}
