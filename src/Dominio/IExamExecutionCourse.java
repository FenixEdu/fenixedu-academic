package Dominio;


/**
 *   29/Mar/2003
 *   @author     Luis Cruz & Sara Ribeiro
 */
public interface IExamExecutionCourse {

	public IExam getExam();
	public IDisciplinaExecucao getExecutionCourse();

	public void setExam(IExam exam);
	public void setExecutionCourse(IDisciplinaExecucao executionCourse);

}