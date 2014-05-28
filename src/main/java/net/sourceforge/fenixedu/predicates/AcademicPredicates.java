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
package net.sourceforge.fenixedu.predicates;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyPersonalDetails;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

import org.fenixedu.bennu.core.security.Authenticate;

public class AcademicPredicates {

    public static final AccessControlPredicate<Object> MANAGE_AUTHORIZATIONS = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object unused) {
            return AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_AUTHORIZATIONS)
                    .isMember(Authenticate.getUser());
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_EQUIVALENCES = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object unused) {
            return AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_EQUIVALENCES)
                    .isMember(Authenticate.getUser());
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_ACADEMIC_CALENDARS = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object unused) {
            return AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_ACADEMIC_CALENDARS)
                    .isMember(Authenticate.getUser());
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_DEGREE_CURRICULAR_PLANS = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object degree) {
            Set<Degree> allowedDegrees = new HashSet<Degree>();
            allowedDegrees.addAll(AcademicAuthorizationGroup.getDegreesForOperation(AccessControl.getPerson(), AcademicOperationType.MANAGE_DEGREE_CURRICULAR_PLANS));
            return allowedDegrees.contains(degree);
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_EXECUTION_COURSES = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object program) {
            Set<AcademicProgram> allowedPrograms = new HashSet<AcademicProgram>();
            allowedPrograms.addAll(AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(), AcademicOperationType.MANAGE_EXECUTION_COURSES));
            return allowedPrograms.contains(program);
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_EXECUTION_COURSES_ADV = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object program) {
            Set<AcademicProgram> allowedPrograms = new HashSet<AcademicProgram>();
            allowedPrograms.addAll(AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(), AcademicOperationType.MANAGE_EXECUTION_COURSES_ADV));
            return allowedPrograms.contains(program);
        };
    };

    public static final AccessControlPredicate<Object> CREATE_REGISTRATION = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object unused) {
            return AcademicAuthorizationGroup.get(AcademicOperationType.CREATE_REGISTRATION)
                    .isMember(Authenticate.getUser());
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_MARKSHEETS = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object unused) {
            return AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_MARKSHEETS).isMember(
                    Authenticate.getUser());
        };
    };

    public static final AccessControlPredicate<Object> DISSERTATION_MARKSHEETS = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object unused) {
            return AcademicAuthorizationGroup.get(AcademicOperationType.DISSERTATION_MARKSHEETS)
                    .isMember(Authenticate.getUser());
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_PAYMENTS = new AccessControlPredicate<Object>() {

        @Override
        public boolean evaluate(Object c) {
            return Authenticate.getUser().getPerson().hasRole(RoleType.MANAGER);
            // return
            // new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_PAYMENTS).isMember(
            // AccessControl.getPerson());
        }

    };

    public static final AccessControlPredicate<Object> DEPOSIT_AMOUNT_ON_PAYMENT_EVENT = new AccessControlPredicate<Object>() {

        @Override
        public boolean evaluate(Object c) {
            return Authenticate.getUser().getPerson().hasRole(RoleType.MANAGER);
            // return new
            // AcademicAuthorizationGroup(AcademicOperationType.DEPOSIT_AMOUNT_ON_PAYMENT_EVENT).isMember(AccessControl
            // .getPerson()) || MANAGE_PAYMENTS.evaluate(c);
        }
    };

    public static final AccessControlPredicate<Object> CREATE_PAYMENT_EVENT = new AccessControlPredicate<Object>() {

        @Override
        public boolean evaluate(Object c) {
            return Authenticate.getUser().getPerson().hasRole(RoleType.MANAGER);
            // return new
            // AcademicAuthorizationGroup(AcademicOperationType.CREATE_PAYMENT_EVENT).isMember(Authenticate.getUser())
            // || MANAGE_PAYMENTS.evaluate(c);
        }

    };

    public static final AccessControlPredicate<Object> SERVICE_REQUESTS = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object unused) {
            return AcademicAuthorizationGroup.get(AcademicOperationType.SERVICE_REQUESTS).isMember(
                    Authenticate.getUser());
        };
    };

    public static final AccessControlPredicate<AcademicServiceRequest> SERVICE_REQUESTS_REVERT_TO_PROCESSING_STATE =
            new AccessControlPredicate<AcademicServiceRequest>() {
                @Override
                public boolean evaluate(final AcademicServiceRequest request) {
                    return AcademicAuthorizationGroup.isAuthorized(AccessControl.getPerson(), request, AcademicOperationType.REPEAT_CONCLUSION_PROCESS);
                };
            };

    public static final AccessControlPredicate<Object> EDIT_STUDENT_PERSONAL_DATA = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object unused) {
            return AcademicAuthorizationGroup.get(AcademicOperationType.EDIT_STUDENT_PERSONAL_DATA)
                    .isMember(Authenticate.getUser());
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
            return AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_STUDENT_PAYMENTS)
                    .isMember(Authenticate.getUser());
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_STUDENT_PAYMENTS_ADV = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object personToBeViewed) {
            Set<AcademicProgram> allowedPrograms = new HashSet<AcademicProgram>();
            allowedPrograms.addAll(AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(), AcademicOperationType.MANAGE_STUDENT_PAYMENTS_ADV));
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

            for (IndividualCandidacyPersonalDetails candidacyDetails : person.getIndividualCandidacies()) {
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

            for (PhdIndividualProgramProcess programProcess : person.getPhdIndividualProgramProcesses()) {
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
            return AcademicAuthorizationGroup.getDegreesForOperation(AccessControl.getPerson(), AcademicOperationType.MANAGE_ENROLMENT_PERIODS).contains(degree);
        };
    };

    public static final AccessControlPredicate<Object> CREATE_SIBS_PAYMENTS_REPORT = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object unused) {
            return AcademicAuthorizationGroup.get(AcademicOperationType.CREATE_SIBS_PAYMENTS_REPORT)
                    .isMember(Authenticate.getUser());
        };
    };

    public static final AccessControlPredicate<Object> MANAGE_PHD_PROCESSES = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(final Object unused) {
            return AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_PHD_PROCESSES)
                    .isMember(Authenticate.getUser());
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
