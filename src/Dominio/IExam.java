package Dominio;

import java.util.Calendar;

/**
 *   18/Mar/2003
 *   @author     Luis Cruz & Sara Ribeiro
 */
public interface IExam {

	public Calendar getBeginning();
	public Calendar getDay();
	public Calendar getEnd();
	public IDisciplinaExecucao getExecutionCourse();

	public void setBeginning(Calendar beginning);
	public void setDay(Calendar day);
	public void setEnd(Calendar end);
	public void setExecutionCourse(IDisciplinaExecucao executionCourse);

}