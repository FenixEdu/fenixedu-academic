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
package org.fenixedu.academic.ui.struts.action.webSiteManager;

import java.io.Serializable;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.organizationalStructure.ResearchContract.ResearchContractType;
import org.fenixedu.academic.domain.organizationalStructure.ResearchUnit;
import org.fenixedu.academic.domain.person.IDDocumentType;

import org.joda.time.YearMonthDay;

public class ResearchContractBean implements Serializable {

    private Boolean externalPerson;

    private Person person;

    private String personNameString;

    private YearMonthDay begin;

    private YearMonthDay end;

    private ResearchUnit unit;

    private String email;

    private IDDocumentType documentType;

    private String documentIDNumber;

    private ResearchContractType contractType;

    public String getDocumentIDNumber() {
        return documentIDNumber;
    }

    public void setDocumentIDNumber(String documentIDNumber) {
        this.documentIDNumber = documentIDNumber;
    }

    public IDDocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(IDDocumentType documentType) {
        this.documentType = documentType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ResearchContractBean() {
        setPerson(null);
        setUnit(null);
        setExternalPerson(Boolean.FALSE);
        setPersonNameString(null);
    }

    public YearMonthDay getBegin() {
        return begin;
    }

    public void setBegin(YearMonthDay begin) {
        this.begin = begin;
    }

    public YearMonthDay getEnd() {
        return end;
    }

    public void setEnd(YearMonthDay end) {
        this.end = end;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public ResearchUnit getUnit() {
        return unit;
    }

    public void setUnit(ResearchUnit unit) {
        this.unit = unit;
    }

    public String getPersonNameString() {
        return personNameString;
    }

    public void setPersonNameString(String personName) {
        this.personNameString = personName;
    }

    public Boolean getExternalPerson() {
        return externalPerson;
    }

    public void setExternalPerson(Boolean externalPerson) {
        this.externalPerson = externalPerson;
    }

    public boolean getShowMessage() {
        return getExternalPerson() && getPerson() == null && getPersonNameString() != null && getPersonNameString().length() > 0;
    }

    public ResearchContractType getContractType() {
        return contractType;
    }

    public void setContractType(ResearchContractType contractType) {
        this.contractType = contractType;
    }
}
