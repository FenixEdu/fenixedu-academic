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
	
    /**
     * @return Returns the endTime.
     */
    public abstract Date getEndTime();

	public IExecutionPeriod getExecutionPeriod();
    /**
     * @return Returns the startTime.
     */
    public abstract Date getStartTime();
    /**
     * @return Returns the teacher.
     */
    public abstract ITeacher getTeacher();
    /**
     * @return Returns the weekDay.
     */
    public abstract DiaSemana getWeekDay();
    /**
     * @param endTime
     *                   The endTime to set.
     */
    public abstract void setEndTime(Date endTime);
	public void setExecutionPeriod(IExecutionPeriod executionPeriod);
    /**
     * @param startTime
     *                   The startTime to set.
     */
    public abstract void setStartTime(Date startTime);
    /**
     * @param teacher
     *                   The teacher to set.
     */
    public abstract void setTeacher(ITeacher teacher);
    /**
     * @param weekDay
     *                   The weekDay to set.
     */
    public abstract void setWeekDay(DiaSemana weekDay);
}