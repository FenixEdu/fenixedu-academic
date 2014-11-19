/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.dto;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;

public class InfoProfessorship extends InfoObject {

    private final Professorship professorship;

    private InfoProfessorship(final Professorship professorship) {
        this.professorship = professorship;
    }

    public static InfoProfessorship newInfoFromDomain(Professorship professorship) {
        return (professorship != null) ? new InfoProfessorship(professorship) : null;
    }

    public InfoExecutionCourse getInfoExecutionCourse() {
        return InfoExecutionCourse.newInfoFromDomain(this.getProfessorship().getExecutionCourse());
    }

    public InfoTeacher getInfoTeacher() {
        return InfoTeacher.newInfoFromDomain(this.getProfessorship().getTeacher());
    }

    public Boolean getResponsibleFor() {
        return this.getProfessorship().isResponsibleFor();
    }

    @Override
    public String getExternalId() {
        return this.getProfessorship().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    private Professorship getProfessorship() {
        return professorship;
    }

    public Person getPerson() {
        return this.getProfessorship().getPerson();
    }
}