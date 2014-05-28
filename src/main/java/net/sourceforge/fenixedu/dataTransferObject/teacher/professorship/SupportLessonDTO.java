/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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

    private String professorshipID;

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

    public String getProfessorshipID() {
        return professorshipID;
    }

    public void setProfessorshipID(String professorshipID) {
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
