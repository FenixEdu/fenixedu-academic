/**
 * Nov 22, 2005
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher.professorship;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.util.DiaSemana;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class SupportLessonDTO extends InfoObject {

    private Integer professorshipID;

    private DiaSemana weekDay;

    private Date startTime;

    private Date endTime;

    private String place;

    public String getPlace() {
	return place;
    }

    public void setPlace(String place) {
	this.place = place;
    }

    public Integer getProfessorshipID() {
	return professorshipID;
    }

    public void setProfessorshipID(Integer professorshipID) {
	this.professorshipID = professorshipID;
    }

    public Date getEndTime() {
	return endTime;
    }

    public void setEndTime(Date endTime) {
	this.endTime = endTime;
    }

    public Date getStartTime() {
	return startTime;
    }

    public void setStartTime(Date startTime) {
	this.startTime = startTime;
    }

    public DiaSemana getWeekDay() {
	return weekDay;
    }

    public void setWeekDay(DiaSemana weekDay) {
	this.weekDay = weekDay;
    }
}
