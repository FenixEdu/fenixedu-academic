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
package org.fenixedu.academic.domain;

import java.util.Collection;
import java.util.UUID;

import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;

public class AlumniManager {

    public Alumni checkAlumniIdentity(final String documentIdNumber, final String email) {
        Person person = getPerson(documentIdNumber);

        if (person == null) {
            throw new DomainException("error.alumni.person.does.not.exist", documentIdNumber);
        }

        if (person.getStudent() == null) {
            throw new DomainException("error.alumni.person.does.not.have.student.info", documentIdNumber);
        }

        checkRulesToRegisterAlumni(person.getStudent().getNumber(), documentIdNumber);

        final Alumni alumni = getAlumni(person.getStudent().getNumber());

        if (alumni.isRegistered()) {
            throw new DomainException("error.alumni.already.registered");
        }

        alumni.addIfNotExistsEmail(email);
        return alumni;
    }

    private Person getPerson(String documentIdNumber) {
        Collection<Person> personList = Person.readByDocumentIdNumber(documentIdNumber);
        if (personList.size() > 0) {
            return personList.iterator().next();
        }
        return null;
    }

    public Alumni registerAlumni(final Student student) {
        final Alumni alumni = new Alumni(student);
        return alumni;
    }

    public Alumni registerAlumni(final Integer studentNumber, final String documentIdNumber, final String email) {

        checkRulesToRegisterAlumni(studentNumber, documentIdNumber);

        final Alumni alumni = getAlumni(studentNumber);

        if (alumni.isRegistered()) {
            throw new DomainException("error.alumni.already.registered");
        }

        alumni.setUrlRequestToken(UUID.randomUUID());

        alumni.addIfNotExistsEmail(email);

        return alumni;

    }

    private void checkRulesToRegisterAlumni(final Integer studentNumber, final String documentIdNumber) {
        Student student = Student.readStudentByNumber(studentNumber);
        if (student == null) {

            if (Person.findPersonByDocumentID(documentIdNumber).isEmpty()) {
                throw new DomainException("error.person.document.number", Unit.getInstitutionAcronym());
            } else {
                throw new DomainException("error.person.no.student", Unit.getInstitutionAcronym());
            }
        }

        if (!student.getPerson().getDocumentIdNumber().equals(documentIdNumber.trim())) {
            throw new DomainException("error.document.number.student.number.mismatch");
        }

        if (!checkAlumni(student)) {
            if (!student.getRegistrationsSet().isEmpty()) {

                for (Registration registration : student.getRegistrationsSet()) {
                    if (registration.isConcluded() || ProgramConclusion.conclusionsFor(registration).findAny().isPresent()) {
                        return;
                    }
                }

                throw new DomainException("error.no.concluded.registrations");
            } else {
                throw new DomainException("error.no.registrations", Unit.getInstitutionAcronym(), Installation.getInstance()
                        .getInstituitionalEmailAddress("alumni"));
            }
        }
    }

    private boolean checkAlumni(Student alumniStudent) {

        if (alumniStudent.getAlumni() != null || RoleType.ALUMNI.isMember(alumniStudent.getPerson().getUser())) {
            return true;
        }

        return false;
    }

    private Alumni getAlumni(final Integer studentNumber) {
        final Student student = Student.readStudentByNumber(studentNumber);
        return student.getAlumni() != null ? student.getAlumni() : new Alumni(student);
    }

}
