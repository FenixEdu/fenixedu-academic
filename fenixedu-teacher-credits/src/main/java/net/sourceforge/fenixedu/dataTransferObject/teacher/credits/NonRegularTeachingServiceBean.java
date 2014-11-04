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
package org.fenixedu.academic.dto.teacher.credits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.NonRegularTeachingService;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.teacher.DegreeTeachingService;

public class NonRegularTeachingServiceBean implements Serializable {
    Professorship professorship;
    Shift shift;
    Double percentage;
    List<TeachingServicePercentage> teachingServicePercentages = new ArrayList<TeachingServicePercentage>();

    public class TeachingServicePercentage implements Serializable {
        String personName;
        Double percentage;

        public TeachingServicePercentage(String name, Double percentage) {
            setPersonName(name);
            setPercentage(percentage);
        }

        public String getPersonName() {
            return personName;
        }

        public void setPersonName(String personName) {
            this.personName = personName;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }

    }

    public NonRegularTeachingServiceBean(Shift shift, Professorship professorship) {
        setProfessorship(professorship);
        setShift(shift);
        for (NonRegularTeachingService nonRegularTeachingService : shift.getNonRegularTeachingServicesSet()) {
            if (nonRegularTeachingService.getProfessorship().equals(professorship)) {
                setPercentage(nonRegularTeachingService.getPercentage());
            }
            teachingServicePercentages.add(new TeachingServicePercentage(nonRegularTeachingService.getProfessorship().getPerson()
                    .getName(), nonRegularTeachingService.getPercentage()));
        }
        for (DegreeTeachingService degreeTeachingService : shift.getDegreeTeachingServicesSet()) {
            teachingServicePercentages.add(new TeachingServicePercentage(degreeTeachingService.getProfessorship().getPerson()
                    .getName(), degreeTeachingService.getPercentage()));
        }
    }

    public Professorship getProfessorship() {
        return professorship;
    }

    public void setProfessorship(Professorship professorship) {
        this.professorship = professorship;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public List<TeachingServicePercentage> getTeachingServicePercentages() {
        return teachingServicePercentages;
    }

    public void setTeachingServicePercentages(List<TeachingServicePercentage> teachingServicePercentages) {
        this.teachingServicePercentages = teachingServicePercentages;
    }
}