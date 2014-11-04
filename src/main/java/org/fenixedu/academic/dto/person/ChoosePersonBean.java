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

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyPersonalDetails;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.joda.time.YearMonthDay;

public class ChoosePersonBean implements Serializable {

    private Person person;

    private String name;

    private String identificationNumber;

    private IDDocumentType documentType;

    private YearMonthDay dateOfBirth;

    private boolean firstTimeSearch = true;

    private Integer studentNumber;

    public ChoosePersonBean() {
        super();
    }

    public ChoosePersonBean(IndividualCandidacyPersonalDetails personalDetails) {
        this.name = personalDetails.getName();

        this.identificationNumber = personalDetails.getDocumentIdNumber();
        this.documentType = personalDetails.getIdDocumentType();
        this.dateOfBirth = personalDetails.getDateOfBirthYearMonthDay();
    }

    public ChoosePersonBean(Person person) {
        this();
        setPerson(person);
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public boolean hasPerson() {
        return getPerson() != null;
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

    public YearMonthDay getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(YearMonthDay dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFirstTimeSearch() {
        return firstTimeSearch;
    }

    public boolean getFirstTimeSearch() {
        return this.firstTimeSearch;
    }

    public void setFirstTimeSearch(boolean firstTimeSearch) {
        this.firstTimeSearch = firstTimeSearch;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

}
