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
package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.student.Student;

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
        setName(personBean.getName());

        /*
         * 08/05/2009 - After social security number is the correct property for
         * VAT Number
         */
        setSocialSecurityNumber(personBean.getSocialSecurityNumber());

        setAddress(personBean.getAddress());
        setArea(personBean.getArea());
        setAreaCode(personBean.getAreaCode());

        setTelephoneContact(personBean.getPhone());
        setEmail(personBean.getEmail());
        setCountryOfResidence(personBean.getCountryOfResidence());
    }

    @Override
    public void ensurePersonInternalization() {
        // TODO Auto-generated method stub
        // creates an internal person, this is called just before the
        // Registration is created.
        setInternalized(Boolean.TRUE);
    }

    @Override
    public Student getStudent() {
        return hasPerson() ? getPerson().getStudent() : null;
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
    public Boolean isEmployee() {
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

    @Deprecated
    public boolean hasExpirationDateOfDocumentIdYearMonthDay() {
        return getExpirationDateOfDocumentIdYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasCountryOfResidence() {
        return getCountryOfResidence() != null;
    }

    @Deprecated
    public boolean hasProfession() {
        return getProfession() != null;
    }

    @Deprecated
    public boolean hasTelephoneContact() {
        return getTelephoneContact() != null;
    }

    @Deprecated
    public boolean hasArea() {
        return getArea() != null;
    }

    @Deprecated
    public boolean hasFiscalCode() {
        return getFiscalCode() != null;
    }

    @Deprecated
    public boolean hasNationality() {
        return getNationality() != null;
    }

    @Deprecated
    public boolean hasEmissionLocationOfDocumentId() {
        return getEmissionLocationOfDocumentId() != null;
    }

    @Deprecated
    public boolean hasInternalized() {
        return getInternalized() != null;
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasAreaCode() {
        return getAreaCode() != null;
    }

    @Deprecated
    public boolean hasAreaOfAreaCode() {
        return getAreaOfAreaCode() != null;
    }

    @Deprecated
    public boolean hasEmail() {
        return getEmail() != null;
    }

    @Deprecated
    public boolean hasAddress() {
        return getAddress() != null;
    }

    @Deprecated
    public boolean hasGender() {
        return getGender() != null;
    }

    @Deprecated
    public boolean hasDateOfBirthYearMonthDay() {
        return getDateOfBirthYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasIdDocumentType() {
        return getIdDocumentType() != null;
    }

    @Deprecated
    public boolean hasEmissionDateOfDocumentIdYearMonthDay() {
        return getEmissionDateOfDocumentIdYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasMaritalStatus() {
        return getMaritalStatus() != null;
    }

    @Deprecated
    public boolean hasDocumentIdNumber() {
        return getDocumentIdNumber() != null;
    }

}
