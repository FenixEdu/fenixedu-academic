/*
 * Created on Nov 25, 2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain.teacher.workTime;

import java.util.Date;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.util.DiaSemana;

/**
 * @author jpvl
 */
public interface ITeacherInstitutionWorkTime extends IDomainObject {

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