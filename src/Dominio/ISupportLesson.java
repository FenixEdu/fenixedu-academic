package Dominio;

import java.util.Date;

import Util.DiaSemana;

/**
 * @author Fernanda Quitério
 * @author jpvl
 *  
 */
public interface ISupportLesson extends IDomainObject
{
    public Date getEndTime();
    public String getPlace();

    public IProfessorship getProfessorship();
    public Date getStartTime();
    public DiaSemana getWeekDay();
    public void setEndTime(Date endTime);
    public void setPlace(String place);

    public void setProfessorship(IProfessorship professorship);
    public void setStartTime(Date startTime);
    public void setWeekDay(DiaSemana weekDay);
    public double hours();

}
