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
package org.fenixedu.academic.domain.candidacyProcess;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.person.PersonBean;

public class IndividualCandidacyExternalPersonDetails extends IndividualCandidacyExternalPersonDetails_Base {
    public IndividualCandidacyExternalPersonDetails(IndividualCandidacy candidacy, IndividualCandidacyProcessBean bean) {
        super();
        setCandidacy(candidacy);
        setInternalized(Boolean.FALSE);

        PersonBean personBean = bean.getPersonBean();
        edit(personBean);
    }

    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public void edit(PersonBean personBean) {
        setNationality(personBean.getNationality());

        setDateOfBirthYearMonthDay(personBean.getDateOfBirth());
        setDocumentIdNumber(personBean.getDocumentIdNumber());
        setExpirationDateOfDocumentIdYearMonthDay(personBean.getDocumentIdExpirationDate());
        setGender(personBean.getGender());
        setIdDocumentType(personBean.getIdDocumentType());
        setGivenNames(personBean.getGivenNames());
        setFamilyNames(personBean.getFamilyNames());

        /*
         * 08/05/2009 - After social security number is the correct property for
         * VAT Number
         */
        setSocialSecurityNumber(personBean.getSocialSecurityNumber());

        setAddress(personBean.getAddress());
        setArea(personBean.getArea());
        setAreaCode(personBean.getAreaCode());

        setTelephoneContact(personBean.getMobile());
        setEmail(personBean.getEmail());
        setCountryOfResidence(personBean.getCountryOfResidence());
    }

    @Override
    public void ensurePersonInternalization() {
        // creates an internal person, this is called just before the
        // Registration is created.
        if (getPerson() == null) {
            Person person = Person.readByDocumentIdNumberAndIdDocumentType(getDocumentIdNumber(), getIdDocumentType());
            if (person != null) {
                setPerson(person);
            } else {
                setPerson(new Person(this, false));
            }
        }
        setInternalized(Boolean.TRUE);
    }

    @Override
    public Student getStudent() {
        return getPerson() != null ? getPerson().getStudent() : null;
    }

    @Override
    public Country getCountry() {
        return this.getNationality();
    }

    @Override
    public void setCountry(Country country) {
        this.setNationality(country);
    }

    @Override
    public String getSocialSecurityNumber() {
        return this.getFiscalCode();
    }

    @Override
    public void setSocialSecurityNumber(String number) {
        this.setFiscalCode(number);
    }

    /**
     * External candidacy submissions dont use PhysicalAddress
     */
    @Override
    public PhysicalAddress getDefaultPhysicalAddress() {
        return null;
    }

    @Override
    public Boolean hasAnyRole() {
        return false;
    }

    @Override
    public void editPublic(PersonBean personBean) {
        this.edit(personBean);
    }

    @Override
    public String getEidentifier() {
        return null;
    }

    @Override
    public String getName() {
        return Stream.of(getGivenNames(), getFamilyNames()).filter(Objects::nonNull).collect(Collectors.joining(" "));
    }

    @Deprecated
    public java.util.Date getDateOfBirth() {
        org.joda.time.YearMonthDay ymd = getDateOfBirthYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setDateOfBirth(java.util.Date date) {
        if (date == null) {
            setDateOfBirthYearMonthDay(null);
        } else {
            setDateOfBirthYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEmissionDateOfDocumentId() {
        org.joda.time.YearMonthDay ymd = getEmissionDateOfDocumentIdYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEmissionDateOfDocumentId(java.util.Date date) {
        if (date == null) {
            setEmissionDateOfDocumentIdYearMonthDay(null);
        } else {
            setEmissionDateOfDocumentIdYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getExpirationDateOfDocumentId() {
        org.joda.time.YearMonthDay ymd = getExpirationDateOfDocumentIdYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setExpirationDateOfDocumentId(java.util.Date date) {
        if (date == null) {
            setExpirationDateOfDocumentIdYearMonthDay(null);
        } else {
            setExpirationDateOfDocumentIdYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

}
