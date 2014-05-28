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
package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

public class StudentContextSelectionBean implements Serializable {

    private AcademicInterval academicInterval;
    private String number;
    private boolean toEdit;

    public StudentContextSelectionBean(AcademicInterval academicInterval) {
        this.academicInterval = academicInterval;
        this.toEdit = false;
    }

    public StudentContextSelectionBean() {
        this(AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER));
        this.toEdit = false;
    }

    public AcademicInterval getAcademicInterval() {
        return academicInterval;
    }

    public void setAcademicInterval(AcademicInterval academicInterval) {
        this.academicInterval = academicInterval;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean getToEdit() {
        return toEdit;
    }

    public void setToEdit(boolean toEdit) {
        this.toEdit = toEdit;
    }

}
