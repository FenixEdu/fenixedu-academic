package Dominio;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Util.Season;

/**
 *   18/Mar/2003
 *   @author     Luis Cruz & Sara Ribeiro
 */
public interface IExam {

	public Calendar getBeginning();
	public Date getDay();
	public Calendar getEnd();
	public Season getSeason();	
	public IDisciplinaExecucao getExecutionCourse();
	public List getAssociatedExecutionCourses();

	public void setBeginning(Calendar beginning);
	public void setDay(Date day);
	public void setEnd(Calendar end);
	public void setSeason(Season epoca);
	public void setExecutionCourse(IDisciplinaExecucao executionCourse);
	public void setAssociatedExecutionCourses(List list);

}