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
/**
 * 
 */
package net.sourceforge.fenixedu.predicates;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

import org.fenixedu.bennu.core.groups.Group;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CompetenceCoursePredicates {

    public static final AccessControlPredicate<CompetenceCourse> readPredicate = new AccessControlPredicate<CompetenceCourse>() {

        @Override
        public boolean evaluate(CompetenceCourse competenceCourse) {

            if (!competenceCourse.isBolonha()) {
                return true;
            }

            Person person = AccessControl.getPerson();
            if (person.hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
                return true;
            }

            boolean isDegreeCurricularPlansMember = false;
            isDegreeCurricularPlansMember = isMemberOfDegreeCurricularPlansGroup(person);

            boolean isCompetenceGroupMember = isMemberOfCompetenceCourseGroup(competenceCourse, person);

            switch (competenceCourse.getCurricularStage()) {
            case DRAFT:
                return isCompetenceGroupMember;
            case PUBLISHED:
                return isCompetenceGroupMember || isDegreeCurricularPlansMember;
            case APPROVED:
                return true;
            default:
                return false;
            }

        }

    };

    public static final AccessControlPredicate<CompetenceCourse> writePredicate = new AccessControlPredicate<CompetenceCourse>() {

        @Override
        public boolean evaluate(CompetenceCourse competenceCourse) {

            if (!competenceCourse.isBolonha()) {
                return true;
            }

            Person person = AccessControl.getPerson();

            if (person.hasRole(RoleType.MANAGER)) {
                return true;
            }

            boolean isDegreeCurricularPlansMember = false;
            isDegreeCurricularPlansMember = isMemberOfDegreeCurricularPlansGroup(person);

            boolean isCompetenceGroupMember = isMemberOfCompetenceCourseGroup(competenceCourse, person);

            switch (competenceCourse.getCurricularStage()) {
            case DRAFT:
                return isCompetenceGroupMember;
            case PUBLISHED:
                return isCompetenceGroupMember || isDegreeCurricularPlansMember;
            case APPROVED:
                return person.hasRole(RoleType.SCIENTIFIC_COUNCIL);
            default:
                return false;
            }

        }
    };

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
                        return isCompetenceGroupMember || person.hasRole(RoleType.SCIENTIFIC_COUNCIL);
                    case APPROVED:
                        return person.hasRole(RoleType.SCIENTIFIC_COUNCIL);
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
