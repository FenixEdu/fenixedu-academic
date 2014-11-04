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
/*
 * Created on 12/Mar/2003
 * 
 *  
 */
package org.fenixedu.academic.dto;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Teacher;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Jo�o Mota
 */
public class InfoTeacher extends InfoObject {

    private Person person;

    // This constructor is needed because of CRUDActionByOID. Do not delete it.
    public InfoTeacher() {
    }

    public InfoTeacher(final Person person) {
        this.person = person;
    }

    public InfoTeacher(final Teacher teacher) {
        this.person = teacher.getPerson();
    }

    public List getProfessorShipsExecutionCourses() {
        final List<InfoProfessorship> infoProfessorships = new ArrayList<InfoProfessorship>();
        for (final Professorship professorship : getPerson().getProfessorshipsSet()) {
            infoProfessorships.add(InfoProfessorship.newInfoFromDomain(professorship));
        }
        return infoProfessorships;
    }

    public List getResponsibleForExecutionCourses() {
        final List<InfoProfessorship> infoProfessorships = new ArrayList<InfoProfessorship>();
        for (final Professorship professorship : getPerson().getProfessorshipsSet()) {
            if (professorship.isResponsibleFor()) {
                infoProfessorships.add(InfoProfessorship.newInfoFromDomain(professorship));
            }
        }
        return infoProfessorships;
    }

    public String getTeacherId() {
        return getTeacher().getPerson().getUsername();
    }

    public InfoPerson getInfoPerson() {
        return InfoPerson.newInfoFromDomain(getPerson());
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getPerson() == ((InfoTeacher) obj).getPerson();
    }

    @Override
    public String toString() {
        return getTeacher().toString();
    }

    public static InfoTeacher newInfoFromDomain(final Person person) {
        return person == null ? null : new InfoTeacher(person);
    }

    public static InfoTeacher newInfoFromDomain(final Teacher teacher) {
        return teacher == null ? null : new InfoTeacher(teacher);
    }

    @Override
    public String getExternalId() {
        return getTeacher().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        // This attribution is needed because of CRUDActionByOID. Do not delete
        // it.
        this.person = FenixFramework.<Teacher> getDomainObject(integer).getPerson();
    }

    public Teacher getTeacher() {
        return person.getTeacher();
    }

    public Person getPerson() {
        return this.person;
    }
}