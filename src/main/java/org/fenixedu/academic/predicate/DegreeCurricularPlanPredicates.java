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

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DegreeCurricularPlanPredicates {

    public static final AccessControlPredicate<DegreeCurricularPlan> readPredicate =
            new AccessControlPredicate<DegreeCurricularPlan>() {

                @Override
                public boolean evaluate(DegreeCurricularPlan dcp) {

                    if (!dcp.isBolonhaDegree()) {
                        return true;
                    }

                    Person person = AccessControl.getPerson();

                    if (person.hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
                        return true;
                    }

                    boolean isCurricularPlanMember = dcp.getCurricularPlanMembersGroup().isMember(person.getUser());

                    switch (dcp.getCurricularStage()) {
                    case DRAFT:
                        return isCurricularPlanMember;
                    case PUBLISHED:
                        return isCurricularPlanMember || person.hasRole(RoleType.BOLONHA_MANAGER);
                    case APPROVED:
                        return true;
                    default:
                        return false;
                    }

                }

            };

    public static final AccessControlPredicate<DegreeCurricularPlan> scientificCouncilWritePredicate =
            new AccessControlPredicate<DegreeCurricularPlan>() {

                @Override
                public boolean evaluate(DegreeCurricularPlan dcp) {
                    final Person person = AccessControl.getPerson();
                    return person.hasRole(RoleType.SCIENTIFIC_COUNCIL) || !dcp.isBolonhaDegree();
                }

            };

    public static final AccessControlPredicate<DegreeCurricularPlan> curricularPlanMemberWritePredicate =
            new AccessControlPredicate<DegreeCurricularPlan>() {

                @Override
                public boolean evaluate(DegreeCurricularPlan dcp) {
                    return !dcp.isBolonhaDegree() || dcp.getCurricularPlanMembersGroup().isMember(Authenticate.getUser());
                }

            };

}
