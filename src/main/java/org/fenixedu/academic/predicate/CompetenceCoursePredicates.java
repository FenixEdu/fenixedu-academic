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
package org.fenixedu.academic.predicate;

import java.util.Collection;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.bennu.core.groups.Group;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CompetenceCoursePredicates {

    public static final AccessControlPredicate<CompetenceCourse> writePredicate = new AccessControlPredicate<CompetenceCourse>() {

        @Override
        public boolean evaluate(CompetenceCourse competenceCourse) {

            if (!competenceCourse.isBolonha()) {
                return true;
            }

            Person person = AccessControl.getPerson();

            boolean isDegreeCurricularPlansMember = false;
            isDegreeCurricularPlansMember = isMemberOfDegreeCurricularPlansGroup(person);

            boolean isCompetenceGroupMember = isMemberOfCompetenceCourseGroup(competenceCourse, person);

            switch (competenceCourse.getCurricularStage()) {
            case DRAFT:
                return isCompetenceGroupMember;
            case PUBLISHED:
                return isCompetenceGroupMember || isDegreeCurricularPlansMember;
            case APPROVED:
                return RoleType.SCIENTIFIC_COUNCIL.isMember(person.getUser());
            default:
                return false;
            }

        }
    };

    @Deprecated
    public static final AccessControlPredicate<CompetenceCourse> editCurricularStagePredicate =
            new AccessControlPredicate<CompetenceCourse>() {

                @Override
                public boolean evaluate(CompetenceCourse competenceCourse) {

                    Person person = AccessControl.getPerson();
                    boolean isCompetenceGroupMember = isMemberOfCompetenceCourseGroup(competenceCourse, person);

                    switch (competenceCourse.getCurricularStage()) {
                    case DRAFT:
                        return isCompetenceGroupMember;
                    case PUBLISHED:
                        return isCompetenceGroupMember || RoleType.SCIENTIFIC_COUNCIL.isMember(person.getUser());
                    case APPROVED:
                        return RoleType.SCIENTIFIC_COUNCIL.isMember(person.getUser());
                    default:
                        return false;
                    }

                }

            };

    private static boolean isMemberOfDegreeCurricularPlansGroup(Person person) {
        Collection<DegreeCurricularPlan> degreeCurricularPlans = DegreeCurricularPlan.readNotEmptyDegreeCurricularPlans();

        for (DegreeCurricularPlan plan : degreeCurricularPlans) {
            Group curricularPlanMembersGroup = plan.getCurricularPlanMembersGroup();
            if (curricularPlanMembersGroup != null) {
                return curricularPlanMembersGroup.isMember(person.getUser());
            }
        }

        return false;
    }

    private static boolean isMemberOfCompetenceCourseGroup(CompetenceCourse competenceCourse, Person person) {
        Group competenceCourseMembersGroup =
                competenceCourse.getDepartmentUnit().getDepartment().getCompetenceCourseMembersGroup();
        if (competenceCourseMembersGroup != null) {
            return competenceCourseMembersGroup.isMember(person.getUser());
        }
        return false;
    }

}
