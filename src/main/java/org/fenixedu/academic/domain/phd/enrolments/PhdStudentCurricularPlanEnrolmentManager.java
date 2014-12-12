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
package org.fenixedu.academic.domain.phd.enrolments;

import java.util.Set;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.EnrolmentResultType;
import org.fenixedu.academic.domain.curriculum.EnrollmentCondition;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.studentCurriculum.StudentCurricularPlanEnrolmentManager;

public class PhdStudentCurricularPlanEnrolmentManager extends StudentCurricularPlanEnrolmentManager {

    public PhdStudentCurricularPlanEnrolmentManager(final EnrolmentContext enrolmentContext) {
        super(enrolmentContext);
    }

    @Override
    protected void assertOtherRolesPreConditions() {
        if (!isResponsiblePersonCoordinator()) {
            super.assertOtherRolesPreConditions();
        }
    }

    @Override
    protected void addRuntimeRules(final Set<ICurricularRule> curricularRules, final CurricularCourse curricularCourse) {
        super.addRuntimeRules(curricularRules, curricularCourse);

        if (mustValidateCurricularCourses()) {
            curricularRules.add(new PhdValidCurricularCoursesRule(curricularCourse));
        }
    }

    private boolean mustValidateCurricularCourses() {

        if (getRegistration().getPhdIndividualProgramProcess() == null) {
            return false;
        }

        return getRegistration().getPhdIndividualProgramProcess().hasCurricularCoursesToEnrol();
    }

    @Override
    protected RuleResult evaluateExtraRules(final RuleResult actualResult) {
        /*
         * Override to avoid running previous years rule, because student must
         * enrol in available courses already defined in study plan
         */
        return actualResult;
    }

    /*
     * This code can be changed in future, but for now student enrolments stay
     * temporary until validation by coordinator or academic admin office
     */
    @Override
    protected EnrollmentCondition getEnrolmentCondition(Enrolment enrolment, EnrolmentResultType resultType) {
        if (enrolment == null) {
            return isResponsiblePersonStudent() ? EnrollmentCondition.TEMPORARY : super.getEnrolmentCondition(enrolment,
                    resultType);
        }
        return wasPerformedByStudent(enrolment) ? EnrollmentCondition.TEMPORARY : super.getEnrolmentCondition(enrolment,
                resultType);
    }

    private boolean wasPerformedByStudent(final Enrolment enrolment) {
        final Person person = Person.readPersonByUsername(enrolment.getCreatedBy());
        return RoleType.STUDENT.isMember(person.getUser()) && enrolment.getStudent().equals(person.getStudent());
    }

}
