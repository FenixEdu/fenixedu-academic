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
package org.fenixedu.academic.domain.space;

import org.fenixedu.academic.domain.Person;

import org.joda.time.DateTime;

public class SpaceAttendances extends SpaceAttendances_Base {

    public SpaceAttendances(String personUsername, String responsibleUsername, DateTime entranceTime) {
        this.setPersonUsername(personUsername);
        this.setResponsibleForEntranceUsername(responsibleUsername);
        this.setEntranceTime(entranceTime);
    }

    public String getOccupationDesctiption() {
        if (getOccupiedLibraryPlace() != null && SpaceUtils.isRoomSubdivision(getOccupiedLibraryPlace())) {
            return getOccupiedLibraryPlace().getName();
        }
        return "-";
    }

    public Person getPerson() {
        return Person.readPersonByUsername(getPersonUsername());
    }

    public void exit(String responsibleUsername) {
        if (getOccupiedLibraryPlace() != null) {
            setResponsibleForExitUsername(responsibleUsername);
            setExitTime(new DateTime());
            setOccupiedLibraryPlace(null);
        }
    }

    public void delete() {
        setOccupiedLibraryPlace(null);
        setVisitedLibraryPlace(null);
        deleteDomainObject();
    }

}
