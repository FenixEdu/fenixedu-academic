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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.util.NumberUtils;

public class InfoShiftGroupStatistics extends InfoObject {

    private List<InfoShift> shiftsInGroup;

    public InfoShiftGroupStatistics() {
        this.shiftsInGroup = new ArrayList();
    }

    public List getShiftsInGroup() {
        return shiftsInGroup;
    }

    public Integer getTotalCapacity() {
        Integer totalCapacity = Integer.valueOf(0);

        Iterator iterator = this.shiftsInGroup.iterator();
        while (iterator.hasNext()) {
            InfoShift infoShift = (InfoShift) iterator.next();
            totalCapacity = Integer.valueOf(totalCapacity.intValue() + infoShift.getLotacao().intValue());
        }

        return totalCapacity;
    }

    public Double getTotalPercentage() {
        Integer totalCapacity = Integer.valueOf(0);
        Integer students = Integer.valueOf(0);

        for (InfoShift infoShift : this.shiftsInGroup) {
            students += infoShift.getOcupation();
            totalCapacity += infoShift.getLotacao();
        }

        if (students == 0) {
            // No calculations necessary
            return 0.0;
        }
        return NumberUtils.formatNumber(new Double(students.floatValue() * 100 / totalCapacity.floatValue()), 1);
    }

    public void setShiftsInGroup(List shiftsInGroup) {
        this.shiftsInGroup = shiftsInGroup;
    }

    public Integer getTotalNumberOfStudents() {
        Integer totalNumberOfStudents = Integer.valueOf(0);

        Iterator iterator = this.shiftsInGroup.iterator();
        while (iterator.hasNext()) {
            InfoShift infoShift = (InfoShift) iterator.next();
            totalNumberOfStudents = Integer.valueOf(totalNumberOfStudents.intValue() + infoShift.getOcupation().intValue());
        }

        return totalNumberOfStudents;
    }

}
