package net.sourceforge.fenixedu.domain;

/**
 * 29/Mar/2003
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
public interface IExamExecutionCourse extends IEvalutionExecutionCourse {

    public IExam getExam();

    public IExecutionCourse getExecutionCourse();

    public void setExam(IExam exam);

    public void setExecutionCourse(IExecutionCourse executionCourse);

}