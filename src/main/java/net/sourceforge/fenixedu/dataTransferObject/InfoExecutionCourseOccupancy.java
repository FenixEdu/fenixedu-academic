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
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class InfoExecutionCourseOccupancy extends InfoObject {

    private InfoExecutionCourse infoExecutionCourse;

    // Note: This will always be null when putting to request.
    // The ShiftsInGroups will contain all the information arranjed
    private List infoShifts;

    private List shiftsInGroups;

    public InfoExecutionCourseOccupancy() {
    }

    /**
     * @return
     */
    public InfoExecutionCourse getInfoExecutionCourse() {
        return infoExecutionCourse;
    }

    /**
     * @param infoExecutionCourse
     */
    public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
        this.infoExecutionCourse = infoExecutionCourse;
    }

    /**
     * @return
     */
    public List getInfoShifts() {
        return infoShifts;
    }

    /**
     * @param infoShifts
     */
    public void setInfoShifts(List infoShifts) {
        this.infoShifts = infoShifts;
    }

    /**
     * @return
     */
    public List getShiftsInGroups() {
        return shiftsInGroups;
    }

    /**
     * @param shiftsInGroups
     */
    public void setShiftsInGroups(List shiftsInGroups) {
        this.shiftsInGroups = shiftsInGroups;
    }

    @Override
    public String toString() {
        String result = "[InfoExecutionCourseOccupancy ";
        result += "infoExecutionCourse" + this.infoExecutionCourse + ";";
        result += "infoShifts" + this.infoShifts + ";";
        result += "shiftsInGroups" + this.shiftsInGroups + "]";
        return result;
    }

}