/*
 * Created on 9/Out/2003
 *
 */
package Dominio;

import java.util.Calendar;

import Util.DiaSemana;

/**
 * @author Ana e Ricardo
 *  
 */
public interface IRoomOccupation extends IDomainObject {
    public DiaSemana getDayOfWeek();

    public Calendar getEndTime();

    public Calendar getStartTime();

    public IRoom getRoom();

    public IPeriod getPeriod();

    public void setDayOfWeek(DiaSemana semana);

    public void setEndTime(Calendar calendar);

    public void setStartTime(Calendar calendar);

    public void setRoom(IRoom sala);

    public void setPeriod(IPeriod period);

    public boolean roomOccupationForDateAndTime(IPeriod period, Calendar startTime, Calendar endTime,
            DiaSemana dayOfWeek, Integer frequency, Integer week, IRoom room);

    public boolean roomOccupationForDateAndTime(IPeriod period, Calendar startTime, Calendar endTime,
            DiaSemana dayOfWeek, Integer frequency, Integer week);

    public boolean roomOccupationForDateAndTime(IRoomOccupation roomOccupation);

    public Integer getFrequency();

    public void setFrequency(int frequency);

    public void setFrequency(Integer frequency);

    public Integer getWeekOfQuinzenalStart();

    public void setWeekOfQuinzenalStart(Integer weekOfQuinzenalStart);
}