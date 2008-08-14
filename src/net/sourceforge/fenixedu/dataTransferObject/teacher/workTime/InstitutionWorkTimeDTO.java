/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher.workTime;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.util.WeekDay;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InstitutionWorkTimeDTO extends InfoObject {

    private WeekDay weekDay;
    private Date startTime;
    private Date endTime;

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

    public WeekDay getWeekDay() {
	return weekDay;
    }

    public void setWeekDay(WeekDay weekDay) {
	this.weekDay = weekDay;
    }
}
