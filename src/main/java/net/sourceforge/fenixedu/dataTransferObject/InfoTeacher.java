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
/*
 * Created on 12/Mar/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCategory;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
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
        return getTeacher().getPerson().getIstUsername();
    }

    public InfoPerson getInfoPerson() {
        return InfoPerson.newInfoFromDomain(getPerson());
    }

    public InfoCategory getInfoCategory() {
        return InfoCategory.newInfoFromDomain(getTeacher().getCategory());
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