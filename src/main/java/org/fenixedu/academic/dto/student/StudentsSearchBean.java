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
package org.fenixedu.academic.dto.student;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.bennu.core.domain.User;

public class StudentsSearchBean implements Serializable {

    private Integer number;

    private String identificationNumber;

    private IDDocumentType documentType;

    private String name;

    private String username;

    private String socialSecurityNumber;

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

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
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
            if (student != null && !student.getRegistrationsSet().isEmpty()) {
                students.add(student);
            }

        } else if (!StringUtils.isEmpty(getIdentificationNumber()) && getDocumentType() != null) {
            final Person person = Person.readByDocumentIdNumberAndIdDocumentType(getIdentificationNumber(), getDocumentType());
            if (person != null && person.getStudent() != null) {
                students.add(person.getStudent());
            }
        } else if (!StringUtils.isEmpty(getName())) {
            students.addAll(Person.findPersonStream(getName(), Integer.MAX_VALUE).map(p -> p.getStudent())
                    .filter(Objects::nonNull).collect(Collectors.toSet()));
        } else if (!StringUtils.isEmpty(getUsername())) {
            User user = User.findByUsername(getUsername());
            if (user != null && user.getPerson().getStudent() != null) {
                students.add(user.getPerson().getStudent());
            }
        } else if (!StringUtils.isEmpty(getSocialSecurityNumber())) {
            Party party = Party.readByContributorNumber(getSocialSecurityNumber());
            if (party != null && party.isPerson() && ((Person) party).getStudent() != null) {
                students.add(((Person) party).getStudent());
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

            if (student.getRegistrationsSet().isEmpty()) {
                students.add(student);
            }

            for (Registration registration : student.getRegistrationsSet()) {
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
