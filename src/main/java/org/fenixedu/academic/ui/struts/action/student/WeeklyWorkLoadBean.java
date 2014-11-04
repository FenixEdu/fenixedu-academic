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
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.io.Serializable;

public class WeeklyWorkLoadBean implements Serializable {

    String attendsID;

    Integer contact;
    Integer autonomousStudy;
    Integer other;

    public Integer getAutonomousStudy() {
        return autonomousStudy;
    }

    public void setAutonomousStudy(Integer autonomousStudy) {
        this.autonomousStudy = autonomousStudy;
    }

    public Integer getContact() {
        return contact;
    }

    public void setContact(Integer contact) {
        this.contact = contact;
    }

    public Integer getOther() {
        return other;
    }

    public void setOther(Integer other) {
        this.other = other;
    }

    public String getAttendsID() {
        return attendsID;
    }

    public void setAttendsID(String attendsID) {
        this.attendsID = attendsID;
    }
}
