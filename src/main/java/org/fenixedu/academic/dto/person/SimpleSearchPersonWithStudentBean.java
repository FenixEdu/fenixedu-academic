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
package org.fenixedu.academic.dto.person;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.service.services.person.PersonSearcher;

import com.google.common.base.Strings;

public class SimpleSearchPersonWithStudentBean implements Serializable {
    private String name;

    private String username;

    private String documentIdNumber;

    private IDDocumentType idDocumentType;

    private Integer studentNumber;

    private String paymentCode;

    public SimpleSearchPersonWithStudentBean() {
        super();
    }

    public String getDocumentIdNumber() {
        return documentIdNumber;
    }

    public void setDocumentIdNumber(String documentIdNumber) {
        this.documentIdNumber = documentIdNumber;
    }

    public IDDocumentType getIdDocumentType() {
        return idDocumentType;
    }

    public void setIdDocumentType(IDDocumentType idDocumentType) {
        this.idDocumentType = idDocumentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public Collection<Person> search() {

        if (studentNumber != null) {
            Student student = Student.readStudentByNumber(studentNumber);
            if (student != null) {
                return Stream.of(student.getPerson()).collect(Collectors.toSet());
            }
        }

        Stream<Person> stream =
                new PersonSearcher().name(name).username(username).documentIdNumber(documentIdNumber)
                        .documentIdType(idDocumentType).search();

        if (!Strings.isNullOrEmpty(paymentCode)) {
            stream = stream.filter(p -> p.getPaymentCodeBy(paymentCode) != null);
        }

        return stream.collect(Collectors.toSet());
    }
}
