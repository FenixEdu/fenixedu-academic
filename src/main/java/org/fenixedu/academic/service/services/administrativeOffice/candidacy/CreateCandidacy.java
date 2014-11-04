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
package org.fenixedu.academic.service.services.administrativeOffice.candidacy;

import static org.fenixedu.academic.predicate.AccessControl.check;

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.serviceAgreements.DegreeCurricularPlanServiceAgreement;
import org.fenixedu.academic.domain.candidacy.Candidacy;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.person.Gender;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.predicate.RolePredicates;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class CreateCandidacy {

    @Atomic
    public static Candidacy run(ExecutionDegree executionDegree, DegreeType degreeType, String name,
            String identificationDocumentNumber, IDDocumentType identificationDocumentType, String contributorNumber,
            YearMonthDay startDate) {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);

        Person person = Person.readByDocumentIdNumberAndIdDocumentType(identificationDocumentNumber, identificationDocumentType);
        if (person == null) {
            person = new Person(name, identificationDocumentNumber, identificationDocumentType, Gender.MALE);
        }

        person.setSocialSecurityNumber(contributorNumber);

        if (person.getStudent() == null) {
            new Student(person);
        }

        RoleType.grant(RoleType.CANDIDATE, person.getUser());
        RoleType.grant(RoleType.PERSON, person.getUser());

        Candidacy candidacy = CandidacyFactory.newCandidacy(degreeType, person, executionDegree, startDate);

        new DegreeCurricularPlanServiceAgreement(person, executionDegree.getDegreeCurricularPlan().getServiceAgreementTemplate());

        return candidacy;

    }

}