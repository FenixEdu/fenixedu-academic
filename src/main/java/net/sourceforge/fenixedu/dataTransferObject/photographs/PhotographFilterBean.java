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
package net.sourceforge.fenixedu.dataTransferObject.photographs;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PhotoState;
import net.sourceforge.fenixedu.domain.PhotoType;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.LocalDate;

/**
 * @author Pedro Santos (pmrsa)
 */
public class PhotographFilterBean implements Serializable {
    private static final long serialVersionUID = -6023622162590978369L;

    private PhotoState state;

    private PhotoType type;

    private LocalDate startDate;

    private LocalDate endDate;

    private RoleType personType;

    private String name;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public PhotoState getState() {
        return state;
    }

    public void setState(PhotoState state) {
        this.state = state;
    }

    public PhotoType getType() {
        return type;
    }

    public void setType(PhotoType type) {
        this.type = type;
    }

    public RoleType getPersonType() {
        return personType;
    }

    public void setPersonType(RoleType personType) {
        this.personType = personType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param photograph
     * @return boolean representing failure to accept the photo
     */
    public boolean processDates(Photograph photograph) {
        /*
         * If there is a "start date"
         * If photograph is after the defined "start date"
         * If there is an "end date", "start date" must be before "end date"
         */
        if (getStartDate() != null && getStartDate().isAfter(photograph.getStateChange().toDateMidnight().toLocalDate())
                && (getEndDate() == null ? false : getStartDate().isAfter(getEndDate()))) {
            return true;
        }
        if (getEndDate() != null && getEndDate().isBefore(photograph.getStateChange().toDateMidnight().toLocalDate())
                && (getStartDate() == null ? false : getEndDate().isBefore(getStartDate()))) {
            return true;
        }
        return false;
    }

    public boolean accepts(Photograph photograph) {
        if (getState() != null && photograph.getState() != getState()) {
            return false;
        }
        if (getType() != null && photograph.getPhotoType() != getType()) {
            return false;
        }
        if (processDates(photograph)) {
            return false;
        }
        Person person = photograph.getPerson();
        if (person != null) {
            if (getPersonType() != null && !person.hasRole(getPersonType())) {
                return false;
            }
            if (getName() != null && !person.getPersonName().match(name.toLowerCase().split(" "))) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

}
