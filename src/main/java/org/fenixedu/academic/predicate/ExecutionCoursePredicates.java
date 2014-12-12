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

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.person.RoleType;

public class ExecutionCoursePredicates {

    public static final AccessControlPredicate<ExecutionCourse> executionCourseLecturingTeacherOrDegreeCoordinator =
            new AccessControlPredicate<ExecutionCourse>() {

                @Override
                public boolean evaluate(ExecutionCourse executionCourse) {
                    Person person = AccessControl.getPerson();

                    Teacher teacher = person.getTeacher();

                    if (executionCourse.teacherLecturesExecutionCourse(teacher)) {
                        return true;
                    }

                    if (RoleType.COORDINATOR.isMember(person.getUser())) {
                        Collection<DegreeCurricularPlan> degreeCurricularPlans =
                                executionCourse.getAssociatedDegreeCurricularPlans();
                        for (DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
                            Collection<ExecutionDegree> coordinatedExecutionDegrees =
                                    person.getCoordinatedExecutionDegrees(degreeCurricularPlan);
                            for (ExecutionDegree executionDegree : coordinatedExecutionDegrees) {
                                if (executionCourse.getExecutionYear().equals(executionCourse.getExecutionYear())) {
                                    return true;
                                }
                            }

                        }
                    }
                    return false;
                }

            };

}
