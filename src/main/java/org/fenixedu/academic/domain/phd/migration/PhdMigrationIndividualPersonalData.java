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
package org.fenixedu.academic.domain.phd.migration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.domain.phd.migration.common.exceptions.BirthdayMismatchException;
import org.fenixedu.academic.domain.phd.migration.common.exceptions.GivenNameMismatchException;
import org.fenixedu.academic.domain.phd.migration.common.exceptions.MultiplePersonFoundByDocumentIdException;
import org.fenixedu.academic.domain.phd.migration.common.exceptions.PersonNotFoundException;
import org.fenixedu.academic.domain.phd.migration.common.exceptions.PersonSearchByNameMismatchException;
import org.fenixedu.academic.domain.phd.migration.common.exceptions.PossiblePersonCandidatesException;
import org.fenixedu.academic.domain.phd.migration.common.exceptions.SocialSecurityNumberMismatchException;
import org.fenixedu.academic.dto.person.PersonBean;
import org.joda.time.YearMonthDay;

public class PhdMigrationIndividualPersonalData extends PhdMigrationIndividualPersonalData_Base {

    protected PhdMigrationIndividualPersonalData(String data) {
        setData(data);
    }

    public PhdMigrationIndividualPersonalDataBean getPersonalBean() {
        return new PhdMigrationIndividualPersonalDataBean(getData());
    }

    public void parse() {
        getPersonalBean();
    }

    public void parseAndSetNumber() {
        final PhdMigrationIndividualPersonalDataBean personalBean = getPersonalBean();
        setNumber(personalBean.getPhdStudentNumber());
    }

    public Person getPerson() {
        if (getPersonalBean().hasChosenPersonManually()) {
            return getPersonalBean().getChosenPersonManually();
        }
        // Get by identification number
        final Collection<Person> personSet = Person.readByDocumentIdNumber(getPersonalBean().getIdentificationNumber());
        final Collection<Person> personNamesSet = Person.findPerson(getPersonalBean().getFullName());

        if (personSet.isEmpty() && personNamesSet.isEmpty()) {
            throw new PersonNotFoundException();
        }

        if (personSet.size() > 1) {
            throw new MultiplePersonFoundByDocumentIdException(getPersonalBean().getIdentificationNumber());
        }

        if (personSet.isEmpty()) {
            checkPossibleCandidates(personNamesSet);
        }

        checkPersonByIdDocument(personSet, personNamesSet);

        Person person = personSet.iterator().next();
        if (!StringUtils.isEmpty(getPersonalBean().getSocialSecurityNumber())
                && !StringUtils.isEmpty(person.getSocialSecurityNumber())
                && getPersonalBean().getSocialSecurityNumber().equals(person.getSocialSecurityNumber())) {
            return person;
        }

        if (!StringUtils.isEmpty(getPersonalBean().getSocialSecurityNumber())
                && !StringUtils.isEmpty(person.getSocialSecurityNumber())) {
            throw new SocialSecurityNumberMismatchException("Original: " + getPersonalBean().getSocialSecurityNumber()
                    + " Differs from: " + person.getSocialSecurityNumber());
        }

        if (person.getDateOfBirthYearMonthDay() == null
                || !person.getDateOfBirthYearMonthDay().isEqual(getPersonalBean().getDateOfBirth())) {
            throw new BirthdayMismatchException("Original: " + getPersonalBean().getDateOfBirth() + " Differs from: "
                    + person.getDateOfBirthYearMonthDay());
        }

        return person;
    }

    private Person checkPersonByIdDocument(final Collection<Person> personSet, final Collection<Person> personNamesSet) {
        Person possiblePerson = personSet.iterator().next();

        for (Person person : personNamesSet) {
            if (person == possiblePerson) {
                return possiblePerson;
            }
        }

        throw new PersonSearchByNameMismatchException(new HashSet<Person>(personNamesSet));
    }

    private void checkPossibleCandidates(final Collection<Person> personNamesSet) {
        Set<Person> possiblePersonSet = new HashSet<Person>();
        for (Person person : personNamesSet) {

            if (StringUtils.isEmpty(person.getSocialSecurityNumber())) {
                continue;
            }

            if (!person.getSocialSecurityNumber().equals(getPersonalBean().getSocialSecurityNumber())) {
                continue;
            }

            if (person.getDateOfBirthYearMonthDay() == null) {
                continue;
            }

            if (!person.getDateOfBirthYearMonthDay().isEqual(getPersonalBean().getDateOfBirth())) {
                continue;
            }

            possiblePersonSet.add(person);
        }

        if (!possiblePersonSet.isEmpty()) {
            throw new PossiblePersonCandidatesException(possiblePersonSet);
        }

        throw new PersonNotFoundException();
    }

    public boolean isPersonRegisteredOnFenix() {
        try {
            return getPerson() != null;
        } catch (PersonNotFoundException e) {
            return false;
        }
    }

    public boolean isSocialSecurityNumberEqual() {
        return getPerson().getSocialSecurityNumber().equals(getPersonalBean().getSocialSecurityNumber());
    }

    private static String readGivenName(String fullName, String familyName) {
        try {
            return fullName.substring(0, fullName.indexOf(familyName)).trim();
        } catch (StringIndexOutOfBoundsException e) {
            throw new GivenNameMismatchException();
        }
    }

    public PersonBean getPersonBean() {
        PersonBean bean = new PersonBean();

        if (getPersonalBean().hasChosenPersonManually()) {
            bean.setPerson(getPersonalBean().getChosenPersonManually());
            return bean;
        }

        if (isPersonRegisteredOnFenix()) {
            bean.setPerson(getPerson());
            return bean;
        }

        final PhdMigrationIndividualPersonalDataBean personalBean = getPersonalBean();

        bean.setAddress(personalBean.getAddress());
        bean.setArea(personalBean.getArea());
        bean.setAreaCode(personalBean.getAreaCode());
        bean.setParishOfResidence(personalBean.getParishOfResidence());
        bean.setDistrictOfResidence(personalBean.getDistrictOfResidence());
        bean.setDistrictSubdivisionOfResidence(personalBean.getDistrictSubdivisionOfResidence());

        bean.setPhone(personalBean.getContactNumber());
        bean.setWorkPhone(personalBean.getOtherContactNumber());
        bean.setProfession(personalBean.getProfession());
        bean.setEmail(personalBean.getEmail());

        bean.setFatherName(personalBean.getFatherName());
        bean.setMotherName(personalBean.getMotherName());
        bean.setIdDocumentType(IDDocumentType.OTHER);
        bean.setDocumentIdNumber(personalBean.getIdentificationNumber());
        bean.setSocialSecurityNumber(personalBean.getSocialSecurityNumber());

        bean.setGivenNames(readGivenName(personalBean.getFullName(), personalBean.getFamilyName()));
        bean.setFamilyNames(personalBean.getFamilyName());

        bean.setDateOfBirth(new YearMonthDay(personalBean.getDateOfBirth().getYear(), personalBean.getDateOfBirth()
                .getMonthOfYear(), personalBean.getDateOfBirth().getDayOfMonth()));
        bean.setGender(personalBean.getGender());
        bean.setNationality(personalBean.getNationality());

        return bean;
    }

}
