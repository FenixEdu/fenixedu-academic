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
package org.fenixedu.academic.service.services.masterDegree.administrativeOffice.candidate;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.ArrayList;

import org.fenixedu.academic.dto.InfoCandidateRegistration;
import org.fenixedu.academic.dto.InfoEnrolment;
import org.fenixedu.academic.dto.InfoMasterDegreeCandidateWithInfoPerson;
import org.fenixedu.academic.dto.InfoStudentCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.MasterDegreeCandidate;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.predicate.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class GetCandidateRegistrationInformation {

    @Atomic
    public static InfoCandidateRegistration run(String candidateID) {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);

        MasterDegreeCandidate masterDegreeCandidate = FenixFramework.getDomainObject(candidateID);

        Registration registration = masterDegreeCandidate.getPerson().readStudentByDegreeType(DegreeType.MASTER_DEGREE);

        StudentCurricularPlan studentCurricularPlan = null;
        if (registration != null) {
            studentCurricularPlan = registration.getActiveStudentCurricularPlan();
        }

        final InfoCandidateRegistration infoCandidateRegistration = new InfoCandidateRegistration();

        infoCandidateRegistration.setInfoMasterDegreeCandidate(InfoMasterDegreeCandidateWithInfoPerson
                .newInfoFromDomain(masterDegreeCandidate));
        infoCandidateRegistration
                .setInfoStudentCurricularPlan(InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan));

        if (studentCurricularPlan.getEnrolmentsCount() == 0) {
            infoCandidateRegistration.setEnrolments(null);

        } else {
            infoCandidateRegistration.setEnrolments(new ArrayList());
            for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                infoCandidateRegistration.getEnrolments().add(InfoEnrolment.newInfoFromDomain(enrolment));
            }
        }

        return infoCandidateRegistration;
    }
}