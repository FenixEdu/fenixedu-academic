package Dominio;

import java.sql.Time;

import Util.DiaSemana;

/**
 * @author Fernanda Quitério
 * 17/10/2003
 *
 */
public interface ISupportLessonsTimetable extends IDomainObject {
	public Time getStartTime();
	public DiaSemana getWeekDay();
	public Time getEndTime();
	public String getPlace();
	public Integer getKeyTeacher();
	public Integer getKeyExecutionCourse();
	public ITeacher getTeacher();
	public IDisciplinaExecucao getExecutionCourse();
	public void setStartTime(Time startTime);
	public void setWeekDay(DiaSemana weekDay);
	public void setEndTime(Time endTime);
	public void setPlace(String place);
	public void setKeyTeacher(Integer keyTeacher);
	public void setKeyExecutionCourse(Integer keyExecutionCourse);
	public void setTeacher(ITeacher teacher);
	public void setExecutionCourse(IDisciplinaExecucao executionCourse);
}
