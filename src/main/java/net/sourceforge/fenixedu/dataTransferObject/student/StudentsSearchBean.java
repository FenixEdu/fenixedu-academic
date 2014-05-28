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
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.PersonName;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.User;

public class StudentsSearchBean implements Serializable {

    private Integer number;

    private String identificationNumber;

    private IDDocumentType documentType;

    private String name;

    private String username;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public IDDocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(IDDocumentType documentType) {
        this.documentType = documentType;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean hasSearchParameters() {
        return getNumber() != null || (!StringUtils.isEmpty(getIdentificationNumber()) && getDocumentType() != null)
                || !StringUtils.isEmpty(getName()) || !StringUtils.isEmpty(getUsername());
    }

    public Set<Student> search() {
        final Set<Student> students = new HashSet<Student>();

        if (getNumber() != null) {
            for (final Registration registration : Registration.readByNumber(getNumber())) {
                students.add(registration.getStudent());
            }

            final Student student = Student.readStudentByNumber(getNumber());
            if (student != null) {
                students.add(student);
            }

        } else if (!StringUtils.isEmpty(getIdentificationNumber()) && getDocumentType() != null) {
            final Person person = Person.readByDocumentIdNumberAndIdDocumentType(getIdentificationNumber(), getDocumentType());
            if (person != null && person.hasStudent()) {
                students.add(person.getStudent());
            }
        } else if (!StringUtils.isEmpty(getName())) {
            for (final PersonName personName : PersonName.find(getName(), Integer.MAX_VALUE)) {
                if (personName.getPerson().hasStudent()) {
                    students.add(personName.getPerson().getStudent());
                }
            }
        } else if (!StringUtils.isEmpty(getUsername())) {
            User user = User.findByUsername(getUsername());
            if (user != null && user.getPerson().hasStudent()) {
                students.add(user.getPerson().getStudent());
            }
        }

        return students;
    }

    public Set<Student> searchForPrograms(final Set<AcademicProgram> programs) {
        final Set<Student> students = new TreeSet<Student>(Student.NUMBER_COMPARATOR);
        for (Student student : search()) {

            if (student == null) {
                continue;
            }

            if (!student.hasAnyRegistrations()) {
                students.add(student);
            }

            for (Registration registration : student.getRegistrations()) {
                if (programs.contains(registration.getDegree())) {
                    students.add(student);
                }
            }

        }
        return students;
    }

    // Convenience method for invocation as bean.
    public Set<Student> getSearch() {
        return search();
    }

}
