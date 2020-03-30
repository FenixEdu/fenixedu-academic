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
/**
 * 
 */
package org.fenixedu.academic.service.services.student.administrativeOfficeServices;

import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Qualification;
import org.fenixedu.academic.domain.candidacy.CandidacySituationType;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.contacts.PartyContactType;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.contacts.PhysicalAddressData;
import org.fenixedu.academic.domain.student.PersonalIngressionData;
import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.administrativeOffice.ExecutionDegreeBean;
import org.fenixedu.academic.dto.candidacy.IngressionInformationBean;
import org.fenixedu.academic.dto.candidacy.OriginInformationBean;
import org.fenixedu.academic.dto.candidacy.PrecedentDegreeInformationBean;
import org.fenixedu.academic.dto.person.PersonBean;

import pt.ist.fenixframework.Atomic;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - �ngela Almeida (argelina@ist.utl.pt)
 * 
 */

public class CreateStudent {
    @Atomic
    public static Registration run(PersonBean personBean, ExecutionDegreeBean executionDegreeBean,
            PrecedentDegreeInformationBean precedentDegreeInformationBean, IngressionInformationBean ingressionInformationBean,
            OriginInformationBean originInformationBean) {

        boolean createNewPerson = personBean.getPerson() == null;

        // get or update person
        Person person = getPerson(personBean);

        // Write fiscal information
        if (createNewPerson) {
            // Validate all contacts
            person.getAllPendingPartyContacts().forEach(partyContact -> partyContact.setValid());
        }

        if (createNewPerson && personBean.isUsePhysicalAddress()) {

            person.editSocialSecurityNumber(personBean.getSocialSecurityNumber(), person.getDefaultPhysicalAddress());

        } else if (!createNewPerson && personBean.isUsePhysicalAddress()) {

            person.editSocialSecurityNumber(personBean.getSocialSecurityNumber(),
                    personBean.getFiscalAddressInCreateRegistrationBean().getPhysicalAddress());

        } else if (!personBean.isUsePhysicalAddress()) {

            final Country fiscalAddressCountryOfResidence = personBean.getFiscalAddressCountryOfResidence();
            final String fiscalAddressAddress = personBean.getFiscalAddressAddress();
            final String fiscalAddressAreaCode = personBean.getFiscalAddressAreaCode();
            final String fiscalAddressParishOfResidence = personBean.getFiscalAddressParishOfResidence();

            String fiscalAddressDistrictSubdivisionOfResidence = personBean.getFiscalAddressDistrictSubdivisionOfResidence();
            String fiscalAddressDistrictOfResidence = personBean.getFiscalAddressDistrictOfResidence();
            if (fiscalAddressCountryOfResidence.isDefaultCountry()
                    && personBean.getFiscalAddressDistrictSubdivisionOfResidenceObject() != null) {
                fiscalAddressDistrictSubdivisionOfResidence =
                        personBean.getFiscalAddressDistrictSubdivisionOfResidenceObject().getName();

                fiscalAddressDistrictOfResidence =
                        personBean.getFiscalAddressDistrictSubdivisionOfResidenceObject().getDistrict().getName();
            }

            final PhysicalAddressData fiscalAddressData = new PhysicalAddressData();
            fiscalAddressData.setCountryOfResidence(fiscalAddressCountryOfResidence);
            fiscalAddressData.setAddress(fiscalAddressAddress);
            fiscalAddressData.setAreaCode(fiscalAddressAreaCode);
            fiscalAddressData.setParishOfResidence(fiscalAddressParishOfResidence);
            fiscalAddressData.setDistrictSubdivisionOfResidence(fiscalAddressDistrictSubdivisionOfResidence);
            fiscalAddressData.setDistrictOfResidence(fiscalAddressDistrictOfResidence);

            final PhysicalAddress fiscalAddress =
                    PhysicalAddress.createPhysicalAddress(person, fiscalAddressData, PartyContactType.PERSONAL, false);
            fiscalAddress.setValid();

            person.editSocialSecurityNumber(personBean.getSocialSecurityNumber(), fiscalAddress);
        }

        createStudentIfNeeded(person, personBean.getStudentNumber());

        // create candidacy
        StudentCandidacy studentCandidacy = new StudentCandidacy(person, executionDegreeBean.getExecutionDegree());
        studentCandidacy.setState(CandidacySituationType.REGISTERED);
        studentCandidacy.setIngressionType(ingressionInformationBean.getIngressionType());
        studentCandidacy.setEntryPhase(ingressionInformationBean.getEntryPhase());

        // create registration
        Registration registration =
                Registration.createRegistrationWithCustomStudentNumber(person, executionDegreeBean.getDegreeCurricularPlan(),
                        studentCandidacy, ingressionInformationBean.getRegistrationProtocol(), executionDegreeBean.getCycleType(),
                        executionDegreeBean.getExecutionYear(), personBean.getStudentNumber());
        registration.setHomologationDate(ingressionInformationBean.getHomologationDate());
        registration.setStudiesStartDate(ingressionInformationBean.getStudiesStartDate());

        PersonalIngressionData personalIngressionData = registration.getStudent()
                .getPersonalIngressionDataByExecutionYear(executionDegreeBean.getExecutionDegree().getExecutionYear());

        if (personalIngressionData == null) {
            personalIngressionData = new PersonalIngressionData(originInformationBean, personBean, registration.getStudent(),
                    executionDegreeBean.getExecutionDegree().getExecutionYear());
        } else {
            personalIngressionData.edit(originInformationBean, personBean);
        }
//        PrecedentDegreeInformation precedentDegreeInformation = studentCandidacy.getPrecedentDegreeInformation();
        PrecedentDegreeInformation precedentDegreeInformation = studentCandidacy.getCompletedDegreeInformation();
        precedentDegreeInformation.edit(precedentDegreeInformationBean);
        precedentDegreeInformation.setInstitutionType(personalIngressionData.getHighSchoolType());

        PrecedentDegreeInformation previousDegreeInformation = studentCandidacy.getPreviousDegreeInformation();
        previousDegreeInformation.editPreviousPrecedentInformation(precedentDegreeInformationBean);

        // create qualification
        new Qualification(person, precedentDegreeInformation);

        // add roles
        //	person.addPersonRoleByRoleType(RoleType.STUDENT);
        //	person.addPersonRoleByRoleType(RoleType.PERSON);

        return registration;
    }

    private static Person getPerson(PersonBean personBean) {
        Person person = null;
        if (personBean.getPerson() != null) {
            person = personBean.save();
        } else {
            // create person
            person = new Person(personBean);
        }
        return person;
    }

    // code from legacy RegisteredCandidacySituation
    private static void createStudentIfNeeded(final Person person, Integer studentNumber) {
        if (person.getStudent() == null && studentNumber == null) {
            new Student(person);
        } else if (person.getStudent() == null && studentNumber != null) {
            Student.createStudentWithCustomNumber(person, studentNumber);
        }
    }

}