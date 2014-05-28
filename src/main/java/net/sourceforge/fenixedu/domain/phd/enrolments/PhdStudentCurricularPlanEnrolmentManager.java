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
package net.sourceforge.fenixedu.domain.phd.enrolments;

import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.EnrolmentResultType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurriculum.StudentCurricularPlanEnrolmentManager;

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

        if (!getRegistration().hasPhdIndividualProgramProcess()) {
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
        return person.hasRole(RoleType.STUDENT) && enrolment.getStudent().equals(person.getStudent());
    }

}
