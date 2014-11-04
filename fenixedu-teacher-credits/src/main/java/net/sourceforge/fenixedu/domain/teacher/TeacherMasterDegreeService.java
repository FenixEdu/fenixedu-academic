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
package org.fenixedu.academic.domain.teacher;

import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.exceptions.DomainException;

public class TeacherMasterDegreeService extends TeacherMasterDegreeService_Base {

    public TeacherMasterDegreeService(TeacherService teacherService, Professorship professorship) {

        super();
        if (teacherService == null || professorship == null) {
            throw new DomainException("arguments can't be null");
        }
        if (!professorship.getExecutionCourse().isMasterDegreeDFAOrDEAOnly()) {
            throw new DomainException("message.invalid.executionCourse");
        }

        setTeacherService(teacherService);
        setProfessorship(professorship);
    }

    public void updateValues(Double hours, Double credits) {
        if ((hours == null && credits == null) || ((hours == null || hours == 0) && (credits == null || credits == 0))) {
            delete();
        } else {
            setCredits(credits);
            setHours(hours);
        }
    }

    @Override
    public void delete() {
        setProfessorship(null);
        setTeacherService(null);
        super.delete();
    }

    @Override
    public Double getCredits() {
        Double credits = super.getCredits();
        return credits != null ? round(credits) : 0.0;
    }

    private Double round(double n) {
        return Math.round((n * 100.0)) / 100.0;
    }

    @Override
    public void setCredits(Double credits) {
        if (credits != null && credits < 0) {
            throw new DomainException("error.invalid.credits");
        }
        super.setCredits(credits);
    }

    @Override
    public void setHours(Double hours) {
        if (hours != null && hours < 0) {
            throw new DomainException("error.invalid.hours");
        }
        super.setHours(hours);
    }

}
