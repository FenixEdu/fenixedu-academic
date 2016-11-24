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
package org.fenixedu.academic.predicate;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accessControl.AcademicAuthorizationGroup;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacy;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyPersonalDetails;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.bennu.core.security.Authenticate;

public class AcademicPredicates {

    public static final AccessControlPredicate<Object> MANAGE_DEGREE_CURRICULAR_PLANS = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object degree) {
            Set<Degree> allowedDegrees = new HashSet<Degree>();
            allowedDegrees.addAll(AcademicAccessRule.getDegreesAccessibleToFunction(
                    AcademicOperationType.MANAGE_DEGREE_CURRICULAR_PLANS, Authenticate.getUser()).collect(Collectors.toSet()));
            return allowedDegrees.contains(degree);
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_EXECUTION_COURSES = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object program) {
            return AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.MANAGE_EXECUTION_COURSES,
                    (AcademicProgram) program, Authenticate.getUser());
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_EXECUTION_COURSES_ADV = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object program) {
            return AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.MANAGE_EXECUTION_COURSES_ADV,
                    (AcademicProgram) program, Authenticate.getUser());
        };
    };

    public static final AccessControlPredicate<Object> CREATE_REGISTRATION = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object unused) {
            return AcademicAuthorizationGroup.get(AcademicOperationType.CREATE_REGISTRATION).isMember(Authenticate.getUser());
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_MARKSHEETS = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object unused) {
            return AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_MARKSHEETS).isMember(Authenticate.getUser());
        };
    };

    @Deprecated
    public static final AccessControlPredicate<Object> MANAGE_PAYMENTS = new AccessControlPredicate<Object>() {

        @Override
        public boolean evaluate(Object c) {
            return DynamicGroup.get("managers").isMember(Authenticate.getUser());
        }

    };

    public static final AccessControlPredicate<AcademicServiceRequest> SERVICE_REQUESTS_REVERT_TO_PROCESSING_STATE =
            new AccessControlPredicate<AcademicServiceRequest>() {
                @Override
                public boolean evaluate(final AcademicServiceRequest request) {
                    return AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.REPEAT_CONCLUSION_PROCESS,
                            request.getAcademicProgram(), Authenticate.getUser());
                };
            };

    public static final AccessControlPredicate<Object> EDIT_STUDENT_PERSONAL_DATA = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object unused) {
            return AcademicAuthorizationGroup.get(AcademicOperationType.EDIT_STUDENT_PERSONAL_DATA).isMember(
                    Authenticate.getUser());
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_ACCOUNTING_EVENTS = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object unused) {
            return AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_ACCOUNTING_EVENTS)
                    .isMember(Authenticate.getUser());
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_STUDENT_PAYMENTS = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object unused) {
            return AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_STUDENT_PAYMENTS).isMember(Authenticate.getUser());
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_STUDENT_PAYMENTS_ADV = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object personToBeViewed) {
            Set<AcademicProgram> allowedPrograms =
                    AcademicAccessRule.getProgramsAccessibleToFunction(AcademicOperationType.MANAGE_STUDENT_PAYMENTS_ADV,
                            Authenticate.getUser()).collect(Collectors.toSet());
            Person person = (Person) personToBeViewed;
            // logic:
            //  if target person is student
            //      1. can be accessed if it has/had at least one of the allowed degrees
            //  if target person has candidacies
            //      2. can be accessed if it has at least one of the allowed degrees
            //      3. or if it has at least one candidacy with no degrees
            //  if target person has phdPrograms
            //      4. can be accessed if it has at least one of the allowed phdPrograms
            //  else, not a student, no candidacies
            //      5. no access
            Student student = person.getStudent();
            if (student != null) {
                Collection<StudentCurricularPlan> curricularPlans = student.getAllStudentCurricularPlans();
                for (StudentCurricularPlan curricularPlan : curricularPlans) {
                    if (allowedPrograms.contains(curricularPlan.getDegree())) {
                        return true; // 1.
                    }
                }
            }

            for (IndividualCandidacyPersonalDetails candidacyDetails : person.getIndividualCandidaciesSet()) {
                IndividualCandidacy candidacy = candidacyDetails.getCandidacy();
                Collection<Degree> candidacyDegrees = candidacy.getAllDegrees();
                if (candidacyDegrees.isEmpty()) {
                    return true; // 3.
                } else {
                    if (!Collections.disjoint(allowedPrograms, candidacyDegrees)) {
                        return true; // 2.
                    }
                }
            }

            for (PhdIndividualProgramProcess programProcess : person.getPhdIndividualProgramProcessesSet()) {
                if (allowedPrograms.contains(programProcess.getPhdProgram())) {
                    return true; // 4.
                }
            }

            return false; // 5.
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_ENROLMENT_PERIODS = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object degree) {
            return AcademicAccessRule
                    .getDegreesAccessibleToFunction(AcademicOperationType.MANAGE_ENROLMENT_PERIODS, Authenticate.getUser())
                    .collect(Collectors.toSet()).contains(degree);
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_PHD_PROCESSES = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object unused) {
            return AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_PHD_PROCESSES).isMember(Authenticate.getUser());
        };
    };

    public static final AccessControlPredicate<Object> VIEW_FULL_STUDENT_CURRICULUM = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object unused) {
            return AcademicAuthorizationGroup.get(AcademicOperationType.VIEW_FULL_STUDENT_CURRICULUM).isMember(
                    Authenticate.getUser());
        };
    };
}
