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

import static org.fenixedu.academic.predicate.AccessControl.check;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Qualification;
import org.fenixedu.academic.domain.candidacy.RegisteredCandidacySituation;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.student.PersonalIngressionData;
import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.administrativeOffice.ExecutionDegreeBean;
import org.fenixedu.academic.dto.candidacy.IngressionInformationBean;
import org.fenixedu.academic.dto.candidacy.OriginInformationBean;
import org.fenixedu.academic.dto.candidacy.PrecedentDegreeInformationBean;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.predicate.AcademicPredicates;

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
        check(AcademicPredicates.CREATE_REGISTRATION);

        // get or update person
        Person person = getPerson(personBean);

        // create candidacy
        StudentCandidacy studentCandidacy =
                StudentCandidacy.createStudentCandidacy(executionDegreeBean.getExecutionDegree(), person);

        new RegisteredCandidacySituation(studentCandidacy, ingressionInformationBean.getRegistrationProtocol(),
                executionDegreeBean.getCycleType(), ingressionInformationBean.getIngressionType(),
                ingressionInformationBean.getEntryPhase(), personBean.getStudentNumber());

        // create registration
        Registration registration = studentCandidacy.getRegistration();
        if (registration == null) {
            registration =
                    Registration.createRegistrationWithCustomStudentNumber(person, executionDegreeBean.getDegreeCurricularPlan(),
                            studentCandidacy, ingressionInformationBean.getRegistrationProtocol(),
                            executionDegreeBean.getCycleType(), executionDegreeBean.getExecutionYear(),
                            personBean.getStudentNumber());
        }
        registration.setHomologationDate(ingressionInformationBean.getHomologationDate());
        registration.setStudiesStartDate(ingressionInformationBean.getStudiesStartDate());

        PersonalIngressionData personalIngressionData =
                registration.getStudent().getPersonalIngressionDataByExecutionYear(
                        executionDegreeBean.getExecutionDegree().getExecutionYear());

        if (personalIngressionData == null) {
            personalIngressionData =
                    new PersonalIngressionData(originInformationBean, personBean, registration.getStudent(), executionDegreeBean
                            .getExecutionDegree().getExecutionYear());
        } else {
            personalIngressionData.edit(originInformationBean, personBean);
        }
        PrecedentDegreeInformation precedentDegreeInformation = studentCandidacy.getPrecedentDegreeInformation();
        precedentDegreeInformation.edit(personalIngressionData, registration, precedentDegreeInformationBean, studentCandidacy);

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

}