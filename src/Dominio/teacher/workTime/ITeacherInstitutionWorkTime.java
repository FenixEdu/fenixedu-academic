/*
 * Created on Nov 25, 2003 by jpvl
 *
 */
package Dominio.teacher.workTime;

import java.util.Date;

import Dominio.IDomainObject;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Util.DiaSemana;

/**
 * @author jpvl
 */
public interface ITeacherInstitutionWorkTime extends IDomainObject
{
	
    public abstract Date getEndTime();
	public IExecutionPeriod getExecutionPeriod();
    public abstract Date getStartTime();
    public abstract ITeacher getTeacher();
    public abstract DiaSemana getWeekDay();
    public abstract void setEndTime(Date endTime);
	public void setExecutionPeriod(IExecutionPeriod executionPeriod);
    public abstract void setStartTime(Date startTime);
    public abstract void setTeacher(ITeacher teacher);
    public abstract void setWeekDay(DiaSemana weekDay);
    
    double hours();
}