/*
 * Created on 29/Mar/2003
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ExamExecutionCourse extends EvaluationExecutionCourse implements IExamExecutionCourse {

    protected IExam exam;

    protected IExecutionCourse executionCourse;

    private Integer keyExam;

    private Integer keyExecutionCourse;

    public ExamExecutionCourse() {
    }

    public ExamExecutionCourse(IExam exam, IExecutionCourse executionCourse) {
        this.setExam(exam);
        this.setExecutionCourse(executionCourse);
    }

    public boolean equals(Object obj) {
        if (obj instanceof ExamExecutionCourse) {
            ExamExecutionCourse examObj = (ExamExecutionCourse) obj;
            return this.getExam().equals(examObj.getExam())
                    && this.getExecutionCourse().equals(examObj.getExecutionCourse());
        }

        return false;
    }

    public String toString() {
        return "[EXAM_EXECUTIONCOURSE:" + " exam= '" + this.getExam() + "'" + " execution_course= '"
                + this.getExecutionCourse() + "'" + "]";
    }

    /**
     * @return
     */
    public IExam getExam() {
        return exam;
    }

    /**
     * @return
     */
    public IExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    /**
     * @return
     */
    public Integer getKeyExam() {
        return keyExam;
    }

    /**
     * @return
     */
    public Integer getKeyExecutionCourse() {
        return keyExecutionCourse;
    }

    /**
     * @param exam
     */
    public void setExam(IExam exam) {
        this.exam = exam;
    }

    /**
     * @param execucao
     */
    public void setExecutionCourse(IExecutionCourse execucao) {
        executionCourse = execucao;
    }

    /**
     * @param integer
     */
    public void setKeyExam(Integer integer) {
        keyExam = integer;
    }

    /**
     * @param integer
     */
    public void setKeyExecutionCourse(Integer integer) {
        keyExecutionCourse = integer;
    }

}