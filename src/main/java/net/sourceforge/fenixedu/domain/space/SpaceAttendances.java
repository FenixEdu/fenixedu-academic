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
package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.Person;

import org.joda.time.DateTime;

public class SpaceAttendances extends SpaceAttendances_Base {

    public SpaceAttendances(String personIstUsername, String responsibleIstUsername, DateTime entranceTime) {
        this.setPersonIstUsername(personIstUsername);
        this.setResponsibleForEntranceIstUsername(responsibleIstUsername);
        this.setEntranceTime(entranceTime);
    }

    public String getOccupationDesctiption() {
        if (hasOccupiedLibraryPlace() && SpaceUtils.isRoomSubdivision(getOccupiedLibraryPlace())) {
            return getOccupiedLibraryPlace().getName();
        }
        return "-";
    }

    public Person getPerson() {
        return Person.readPersonByUsername(getPersonIstUsername());
    }

    public void exit(String responsibleUsername) {
        if (hasOccupiedLibraryPlace()) {
            setResponsibleForExitIstUsername(responsibleUsername);
            setExitTime(new DateTime());
            setOccupiedLibraryPlace(null);
        }
    }

    public void delete() {
        setOccupiedLibraryPlace(null);
        setVisitedLibraryPlace(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasPersonIstUsername() {
        return getPersonIstUsername() != null;
    }

    @Deprecated
    public boolean hasResponsibleForEntranceIstUsername() {
        return getResponsibleForEntranceIstUsername() != null;
    }

    @Deprecated
    public boolean hasResponsibleForExitIstUsername() {
        return getResponsibleForExitIstUsername() != null;
    }

    @Deprecated
    public boolean hasVisitedLibraryPlace() {
        return getVisitedLibraryPlace() != null;
    }

    @Deprecated
    public boolean hasOccupiedLibraryPlace() {
        return getOccupiedLibraryPlace() != null;
    }

    @Deprecated
    public boolean hasEntranceTime() {
        return getEntranceTime() != null;
    }

    @Deprecated
    public boolean hasExitTime() {
        return getExitTime() != null;
    }

}
