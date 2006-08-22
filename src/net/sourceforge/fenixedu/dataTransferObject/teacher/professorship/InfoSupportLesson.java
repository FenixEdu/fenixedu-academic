package net.sourceforge.fenixedu.dataTransferObject.teacher.professorship;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.util.DiaSemana;

/**
 * @author Fernanda Quitério 17/10/2003
 *  
 */
public class InfoSupportLesson extends InfoObject {
    private DiaSemana weekDay;

    private Date startTime;

    private Date endTime;

    private String place;

    private InfoProfessorship infoProfessorship;

    public InfoSupportLesson() {
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoSupportLesson) {
            InfoSupportLesson infoSupportLessonsTimetable = (InfoSupportLesson) obj;
            if (elementsAreEqual(infoSupportLessonsTimetable.getInfoProfessorship(), this
                    .getInfoProfessorship())
                    && elementsAreEqual(infoSupportLessonsTimetable.getStartTime(), this.getStartTime())
                    && elementsAreEqual(infoSupportLessonsTimetable.getEndTime(), this.getEndTime())
                    && elementsAreEqual(infoSupportLessonsTimetable.getPlace(), this.getPlace())
                    && elementsAreEqual(infoSupportLessonsTimetable.getWeekDay(), this.getWeekDay())) {
                result = true;
            }
        }
        return result;
    }

    private boolean elementsAreEqual(Object element1, Object element2) {
        boolean result = false;
        if ((element1 == null && element2 == null)
                || (element1 != null && element2 != null && element1.equals(element2))) {
            result = true;
        }
        return result;
    }

    /**
     * @return
     */
    public String getPlace() {
        return place;
    }

    /**
     * @param place
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * @return
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * @return
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return
     */
    public DiaSemana getWeekDay() {
        return weekDay;
    }

    /**
     * @param weekDay
     */
    public void setWeekDay(DiaSemana weekDay) {
        this.weekDay = weekDay;
    }

    /**
     * @return Returns the infoProfessorship.
     */
    public InfoProfessorship getInfoProfessorship() {
        return this.infoProfessorship;
    }

    /**
     * @param infoProfessorship
     *            The infoProfessorship to set.
     */
    public void setInfoProfessorship(InfoProfessorship infoProfessorship) {
        this.infoProfessorship = infoProfessorship;
    }

    public static InfoSupportLesson newInfoFromDomain(SupportLesson supportLesson) {
        InfoSupportLesson infoSupportLesson = new InfoSupportLesson();
        InfoProfessorship infoProfessorship = InfoProfessorship.newInfoFromDomain(supportLesson.getProfessorship());

        infoSupportLesson.setEndTime(supportLesson.getEndTime());
        infoSupportLesson.setIdInternal(supportLesson.getIdInternal());
        infoSupportLesson.setPlace(supportLesson.getPlace());
        infoSupportLesson.setStartTime(supportLesson.getStartTime());
        infoSupportLesson.setWeekDay(supportLesson.getWeekDay());

        infoSupportLesson.setInfoProfessorship(infoProfessorship);

        return infoSupportLesson;
    }

}