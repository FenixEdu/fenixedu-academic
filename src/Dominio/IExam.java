package Dominio;

import java.util.Calendar;
import java.util.List;

import Util.Season;

/**
 *   18/Mar/2003
 *   @author     Luis Cruz & Sara Ribeiro
 */
public interface IExam {

	public Calendar getBeginning();
	public Calendar getDay();
	public Calendar getEnd();
	public Season getSeason();	
	public List getAssociatedExecutionCourses();

	public void setBeginning(Calendar beginning);
	public void setDay(Calendar day);
	public void setEnd(Calendar end);
	public void setSeason(Season epoca);
	public void setAssociatedExecutionCourses(List list);

}