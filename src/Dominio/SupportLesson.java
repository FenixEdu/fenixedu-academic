package Dominio;

import java.util.Date;

import Util.DiaSemana;

/**
 * @author Fernanda Quitério 17/10/2003
 * @author jpvl
 */
public class SupportLesson extends DomainObject implements ISupportLesson
{
    private Date endTime;

    private Integer keyProfessorship;
    private String place;
    private IProfessorship professorship;
    private Date startTime;

    private DiaSemana weekDay;

    public SupportLesson()
    {
    }

    public SupportLesson(Integer idInternal)
    {
        setIdInternal(idInternal);
    }

    private boolean elementsAreEqual(Object element1, Object element2)
    {
        boolean result = false;
        if ((element1 == null && element2 == null)
            || (element1 != null && element2 != null && element1.equals(element2)))
        {
            result = true;
        }
        return result;
    }

    public boolean equals(Object arg0)
    {
        boolean result = false;
        if (arg0 instanceof ISupportLesson)
        {
            ISupportLesson supportLessonsTimetable = (ISupportLesson) arg0;

            if (elementsAreEqual(supportLessonsTimetable.getProfessorship(), this.getProfessorship())
                && elementsAreEqual(supportLessonsTimetable.getStartTime(), this.getStartTime())
                && elementsAreEqual(supportLessonsTimetable.getEndTime(), this.getEndTime())
                && elementsAreEqual(supportLessonsTimetable.getWeekDay(), this.getWeekDay()))
            {
                result = true;
            }
        }
        return result;
    }

    /**
	 * @return
	 */
    public Date getEndTime()
    {
        return endTime;
    }

    /**
	 * @return Returns the keyProfessorship.
	 */
    public Integer getKeyProfessorship()
    {
        return this.keyProfessorship;
    }

    /**
	 * @return
	 */
    public String getPlace()
    {
        return place;
    }

    /**
	 * @return Returns the professorship.
	 */
    public IProfessorship getProfessorship()
    {
        return this.professorship;
    }

    /**
	 * @return
	 */
    public Date getStartTime()
    {
        return startTime;
    }

    /**
	 * @return
	 */
    public DiaSemana getWeekDay()
    {
        return weekDay;
    }

    /**
	 * @param endTime
	 */
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    /**
	 * @param keyProfessorship
	 *                   The keyProfessorship to set.
	 */
    public void setKeyProfessorship(Integer keyProfessorship)
    {
        this.keyProfessorship = keyProfessorship;
    }

    /**
	 * @param place
	 */
    public void setPlace(String place)
    {
        this.place = place;
    }

    /**
	 * @param professorship
	 *                   The professorship to set.
	 */
    public void setProfessorship(IProfessorship professorship)
    {
        this.professorship = professorship;
    }

    /**
	 * @param startTime
	 */
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    /**
	 * @param weekDay
	 */
    public void setWeekDay(DiaSemana weekDay)
    {
        this.weekDay = weekDay;
    }

}
