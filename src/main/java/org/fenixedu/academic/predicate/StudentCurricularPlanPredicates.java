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

import java.util.stream.Collectors;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.person.RoleType;

public class StudentCurricularPlanPredicates {

    static public final AccessControlPredicate<StudentCurricularPlan> ENROL =
            new AccessControlPredicate<StudentCurricularPlan>() {

                @Override
                public boolean evaluate(StudentCurricularPlan studentCurricularPlan) {
                    final Person person = AccessControl.getPerson();
                    return RoleType.STUDENT.isMember(person.getUser())
                            || hasAuthorization(person, AcademicOperationType.STUDENT_ENROLMENTS,
                                    studentCurricularPlan.getDegree())
                            || RoleType.INTERNATIONAL_RELATION_OFFICE.isMember(person.getUser())
                            /*
                             * used in PhdIndividualProgramProcess enrolments management
                             */
                            || person.isCoordinatorFor(studentCurricularPlan.getDegreeCurricularPlan(),
                                    ExecutionYear.readCurrentExecutionYear());
                }
            };

    static public final AccessControlPredicate<StudentCurricularPlan> ENROL_IN_AFFINITY_CYCLE =
            new AccessControlPredicate<StudentCurricularPlan>() {
                @Override
                public boolean evaluate(StudentCurricularPlan studentCurricularPlan) {
                    final Person person = AccessControl.getPerson();

                    if (!studentCurricularPlan.isConclusionProcessed()) {
                        return RoleType.STUDENT.isMember(person.getUser())
                                || hasAuthorization(person, AcademicOperationType.STUDENT_ENROLMENTS,
                                        studentCurricularPlan.getDegree())
                                || RoleType.INTERNATIONAL_RELATION_OFFICE.isMember(person.getUser());
                    }

                    if (studentCurricularPlan.isEmptyDegree()) {
                        return true;
                    }

                    return hasAuthorization(person, AcademicOperationType.UPDATE_REGISTRATION_AFTER_CONCLUSION,
                            studentCurricularPlan.getDegree());
                }
            };

    static public final AccessControlPredicate<StudentCurricularPlan> MOVE_CURRICULUM_LINES =
            new AccessControlPredicate<StudentCurricularPlan>() {

                @Override
                public boolean evaluate(StudentCurricularPlan studentCurricularPlan) {
                    final Person person = AccessControl.getPerson();
                    return hasAuthorization(person, AcademicOperationType.STUDENT_ENROLMENTS, studentCurricularPlan.getDegree());
                }
            };

    static public AccessControlPredicate<StudentCurricularPlan> MOVE_CURRICULUM_LINES_WITHOUT_RULES =
            new AccessControlPredicate<StudentCurricularPlan>() {

                @Override
                public boolean evaluate(StudentCurricularPlan studentCurricularPlan) {
                    final Person person = AccessControl.getPerson();

                    return hasAuthorization(person, AcademicOperationType.MOVE_CURRICULUM_LINES_WITHOUT_RULES,
                            studentCurricularPlan.getDegree());
                }

            };

    static private boolean hasAuthorization(Person person, AcademicOperationType operation, AcademicProgram program) {
        return AcademicAccessRule.getProgramsAccessibleToFunction(operation, person.getUser()).collect(Collectors.toSet())
                .contains(program);
    }

}
