package Dominio;

import java.util.Calendar;
import java.util.List;

import Util.Season;

/**
 *   18/Mar/2003
 *   @author     Luis Cruz & Sara Ribeiro
 */
public interface IExam extends IDomainObject {

	public Calendar getBeginning();
	public Calendar getDay();
	public Calendar getEnd();
	public Season getSeason();
	public List getAssociatedExecutionCourses();
	public List getAssociatedRooms();
	public List getStudentsEnrolled();
	public Calendar getEnrollmentBeginDay();
	public Calendar getEnrollmentEndDay();
	public Calendar getEnrollmentBeginTime() ;
	public Calendar getEnrollmentEndTime();
	public String getPublishmentMessage();
	

	public void setBeginning(Calendar beginning);
	public void setDay(Calendar day);
	public void setEnd(Calendar end);
	public void setSeason(Season epoca);
	public void setAssociatedExecutionCourses(List list);
	public void setAssociatedRooms(List rooms);
	public void setStudentsEnrolled(List list);
	public void setEnrollmentBeginDay(Calendar calendar);
	public void setEnrollmentEndDay(Calendar calendar);
	public void setEnrollmentBeginTime(Calendar calendar);
	public void setEnrollmentEndTime(Calendar calendar);
	public void setPublishmentMessage(String revisionInformation);

}